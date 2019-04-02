package com.harmazing.spms.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.MongoUtil;
import com.harmazing.spms.base.util.PropertyUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component("spmsDeviceLogTask")
@Lazy(false)
public class SpmsDeviceLogTask {
	private final static Logger LOGGER = LoggerFactory.getLogger(SpmsDeviceLogTask.class);
	
	@Autowired
	private QueryDAO queryDao;
	
	@Scheduled(cron="0 0 2 * * ?")
	public void statisticsAreaEPRE(){
		try {
			String areaSql = "select * from spms_area where instr(parentIds,',1,') and classification = '2'";
			List<Map<String,Object>> list = queryDao.getMapObjects(areaSql);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			Date d = c.getTime();
			String formatStr = "yyyy-MM-dd";
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			String date = sdf.format(d);

			for (int i = 0; i < list.size(); i++) {
				String areaId = list.get(i).get("id").toString();
				statisticsAreaByHour(areaId,c);
				//获取前一天数据
				String daySql = "select t.date1 as date,sum(t.accumulatePower1) as accumulatePower,"
						+ "sum(t.reactive_energy1) as reactive_energy from "
						+ "(select sa.id as id,DATE_FORMAT(start_time,'%Y-%m-%d') as date1,"
						+ "case when max(accumulatePower) is null then 0 else max(accumulatePower) end as accumulatePower1,"
						+ "case when max(reactive_energy) is null then 0 else max(reactive_energy) end as reactive_energy1"
						+ " from spms_electro_result_data serd, spms_area sa "+
						"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"')"
								+ " AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y-%m-%d') = '" + date + "' " +
						" group by DATE_FORMAT(start_time,'%Y-%m-%d'),sa.id) t group by t.date1 ";
				List<Map<String,Object>> dayList = queryDao.getMapObjects(daySql);
				if(null != dayList && dayList.size() > 0){
					Map<String,Object> dayResult = dayList.get(0);
					//将结果插入数据库
					String dayInsert = "insert into spms_electro_result_data_month(area_id,date,accumulatePower,reactive_energy) values('" + areaId + "','" + date + "'," + dayResult.get("accumulatePower") + "," + dayResult.get("reactive_energy") + ")";
					queryDao.doExecuteSql(dayInsert);
				}
				
				//获取前一天所在月的数据
				sdf = new SimpleDateFormat("yyyy-MM");
				String month = sdf.format(d);
				String monthSql = "select t.date1 as date,sum(t.accumulatePower1) as accumulatePower,sum(t.reactive_energy1) as reactive_energy from (select sa.id as id,substr(serd.date,1,7) as date1,case when max(accumulatePower) is null then 0 else max(accumulatePower) end as accumulatePower1,case when max(reactive_energy) is null then 0 else max(reactive_energy) end as reactive_energy1 from spms_electro_result_data_month serd, spms_area sa "+
						"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and substr(serd.date,1,7) = '" + month + "' " +
						" group by substr(serd.date,1,7),sa.id) t group by t.date1 ";
				List<Map<String,Object>> monthList = queryDao.getMapObjects(monthSql);
				//如果有数据
				if(null != monthList && monthList.size() > 0){
					Map<String,Object> monthResult = monthList.get(0);
					//判断是否存在该月份的数据
					monthSql = "select * from spms_electro_result_data_year where date = '" + month + "'";
					List<Map<String,Object>> flag = queryDao.getMapObjects(monthSql);
					if(null == flag || flag.size() == 0){
						//insert
						String monthInsert = "insert into spms_electro_result_data_year(area_id,date,accumulatePower,reactive_energy) values('" + areaId + "','" + month + "'," + monthResult.get("accumulatePower") + "," + monthResult.get("reactive_energy") + ")";
						queryDao.doExecuteSql(monthInsert);
					}else{
						//update
						String monthInsert = "update spms_electro_result_data_year "
								+ "set accumulatePower = " +Long.parseLong(monthResult.get("accumulatePower").toString()) 
										//+ Long.parseLong(flag.get(0).get("accumulatePower").toString()))
										+ ",reactive_energy = " 
										+Long.parseLong(monthResult.get("reactive_energy").toString()) 
										//+ Long.parseLong(flag.get(0).get("reactive_energy").toString())) 
										+ " where area_id = '" + areaId + "' and date = '" + month + "'";
						queryDao.doExecuteSql(monthInsert);
					}
				}
			}
			new Thread(new insertER2MongoRunnable()).start();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * 统计和存储每小时数据
	 * @param areaId
	 * @param c
	 */
	private void statisticsAreaByHour(String areaId, Calendar c) {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		for (int s = 0; s < 24; s++) {
			String sql = "select t.date," +
					"sum(t.power) power," +
					"sum(t.apparent_power) apparent_power," +
					"sum(t.reactive_demand) reactive_demand," +
					"sum(t.active_demand) active_demand," +
					"sum(t.device_num) device_num " +
					" from " +
					"(select sa.id," +
					"DATE_FORMAT(start_time,'%Y-%m-%d %H') as date, " +
					"case when avg(power) is null then 0 else truncate(avg(power),2) end as power," +
					"case when avg(apparent_power) is null then 0 else truncate(avg(apparent_power),2) end as apparent_power," +
					"case when avg(reactive_demand) is null then 0 else truncate(avg(reactive_demand),2) end as reactive_demand," +
					"case when avg(active_demand) is null then 0 else truncate(avg(active_demand),2) end as active_demand," +
					"case when avg(device_num) is null then 0 else round(avg(device_num),0) end as device_num " +
					"from spms_electro_result_data "  +"serd, spms_area sa "+
					"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y-%m-%d %H') = '" + (date + " " + (s>=10?s:"0"+s) )+ "' "+
					" group by DATE_FORMAT(start_time,'%Y-%m-%d %H'),sa.id) t group by t.date";
			List<Map<String,Object>> dayList = queryDao.getMapObjects(sql);
			if(null != dayList && dayList.size() > 0){
				Map<String,Object> dayResult = dayList.get(0);
				//将结果插入数据库
				String dayInsert = "insert into spms_electro_result_data_hour(area_id,date,power,apparent_power,reactive_demand,active_demand,device_num)" +
						" values('" + areaId + "','" + (date + " " + (s>=10?s:"0"+s) ) + "'," + dayResult.get("power") + "," + dayResult.get("apparent_power") + ","
						+ dayResult.get("reactive_demand")+ ","+ dayResult.get("active_demand") +"," + dayResult.get("device_num") + ")";
				queryDao.doExecuteSql(dayInsert);
			}
		}
	}

	private class insertER2MongoRunnable implements Runnable{

		@Override
		public void run() {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			Date d = c.getTime();
			String formatStr = "yyyy-MM-dd";
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			String date = sdf.format(d);
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(
						PropertyUtil.getPropertyInfo("jdbc.url"),
						PropertyUtil.getPropertyInfo("jdbc.username"),
						PropertyUtil.getPropertyInfo("jdbc.password"));
				stmt = conn.createStatement();
				// 插入将数据存储进mongodb
				String sql = "select * from spms_electro_result_data where DATE_FORMAT(start_time,'%Y-%m-%d') = '"
						+ date + "'";
				rs = stmt.executeQuery(sql);
				
				MongoDatabase db = MongoUtil.getDB("spms");
				MongoCollection<Document> coll = db
						.getCollection("spms_electro_result_data");
				while (rs.next()) {
					Document doc = new Document();
					ResultSetMetaData md = rs.getMetaData();
					int columnCount = md.getColumnCount();
					for (int j = 1; j <= columnCount; j++) {
						doc.put(md.getColumnName(j),(rs.getObject(j) == null?"":rs.getObject(j)).toString());
					}
					coll.insertOne(doc);
				}
				// 插入完成，清除数据
				sql = "delete from spms_electro_result_data where DATE_FORMAT(start_time,'%Y-%m-%d') = '" + date + "'";
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			} finally {
				if(null != conn ){
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(null != stmt ){
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(null != rs){
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
	}
	
	
	@Scheduled(cron="0 0 2 * * ?")
	public void statisticsDeviceEP(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		Date d = c.getTime();
		String formatStr = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		String date = sdf.format(d);

		// 获取前一天数据
		String daySql = "select device_id,DATE_FORMAT(start_time,'%Y-%m-%d') as date,case when max(sas.accumulatePower) is null then 0 else max(sas.accumulatePower) end as power,case when max(sas.reactiveEnergy) is null then 0 else max(sas.reactiveEnergy) end as power1 from spms_ac_status sas "
				+ "where DATE_FORMAT(sas.start_time,'%Y-%m-%d') = '"
				+ date
				+ "' " + " group by DATE_FORMAT(sas.start_time,'%Y-%m-%d'),device_id ";
		List<Map<String, Object>> dayList = queryDao.getMapObjects(daySql);
		for (int i = 0; i < dayList.size(); i++) {
			Map<String, Object> dayResult = dayList.get(i);
			// 将结果插入数据库
			String dayInsert = "insert into spms_ac_status_month(device_id,date,accumulatePower,reactiveEnergy) values('"
					+ dayResult.get("device_id")
					+ "','"
					+ date
					+ "',"
					+ dayResult.get("power")
					+ ","
					+ dayResult.get("power1")
					+ ")";
			queryDao.doExecuteSql(dayInsert);
		}

		// 获取前一天所在月的数据
		sdf = new SimpleDateFormat("yyyy-MM");
		String month = sdf.format(d);
		String monthSql = "select device_id,substr(date,1,7),case when max(sas.accumulatePower) is null then 0 else max(sas.accumulatePower) end as power,case when max(sas.reactiveEnergy) is null then 0 else max(sas.reactiveEnergy) end as power1 from spms_ac_status_month sas "
				+ "where substr(date,1,7) = '"
				+ month
				+ "' "
				+ " group by substr(date,1,7),device_id ";
		List<Map<String, Object>> monthList = queryDao.getMapObjects(monthSql);
		for (int i = 0; i < monthList.size(); i++) {
			Map<String, Object> monthResult = monthList.get(i);
			// 判断是否存在该月份的数据
			monthSql = "select * from spms_ac_status_year where date = '"
					+ month + "' and device_id = '" + monthResult.get("device_id") + "'";
			List<Map<String, Object>> flag = queryDao.getMapObjects(monthSql);
			if (null == flag || flag.size() == 0) {
				// insert
				String monthInsert = "insert into spms_ac_status_year(device_id,date,accumulatePower,reactiveEnergy) values('"
						+ monthResult.get("device_id")
						+ "','"
						+ month
						+ "',"
						+ monthResult.get("power") + 
						"," +
						monthResult.get("power1") + 
						")";
				queryDao.doExecuteSql(monthInsert);
			} else {
				// update
				String monthInsert = "update spms_ac_status_year set accumulatePower = "
						+ (Long.parseLong(monthResult.get("power").toString())/* + Long.parseLong(flag
								.get(0).get("accumulatePower").toString())*/)
						+ ",reactiveEnergy = " 
						+ (Long.parseLong(monthResult.get("power1").toString()) + Long.parseLong(flag
								.get(0).get("reactiveEnergy").toString()))
						
						+ " where device_id = '"
						+ monthResult.get("device_id")
						+ "' and date = '"
						+ month + "'";
				queryDao.doExecuteSql(monthInsert);
			}
		}
		
		new Thread(new insertAC2MongoRunnable()).start();
	}
	
	private class insertAC2MongoRunnable implements Runnable{
		@Override
		public void run() {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			Date d = c.getTime();
			String formatStr = "yyyy-MM-dd";
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
			String date = sdf.format(d);
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(
						PropertyUtil.getPropertyInfo("jdbc.url"),
						PropertyUtil.getPropertyInfo("jdbc.username"),
						PropertyUtil.getPropertyInfo("jdbc.password"));
				String sql = "select count(1) as count from spms_ac_status where DATE_FORMAT(start_time,'%Y-%m-%d') = '"
						+ date + "'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				rs.next();
				int count = rs.getInt("count");
				rs.close();
				
				int i = 0;
				while (true) {
					if (i >= count) {
						break;
					}

					// 插入将数据存储进mongodb
					sql = "select * from spms_ac_status where DATE_FORMAT(start_time,'%Y-%m-%d') = '"
							+ date + "' limit " + i + "," + 1000;
					rs = stmt.executeQuery(sql);

					MongoDatabase db = MongoUtil.getDB("spms");
					MongoCollection<Document> coll = db
							.getCollection("spms_ac_status");
					while(rs.next()){
						Document doc = new Document();
						ResultSetMetaData md = rs.getMetaData();
						int columnCount = md.getColumnCount();
						for (int j = 1; j <= columnCount; j++) {
							doc.put(md.getColumnName(j),(rs.getObject(j) == null?"":rs.getObject(j)).toString());
						}
						coll.insertOne(doc);
					}
					rs.close();
					i = i + 1000;
				}
				// 插入完成，清除数据
				for (int s = 0; s < 24; s++) {
					sql = "delete from spms_ac_status where DATE_FORMAT(start_time,'%Y-%m-%d %H') = '"
							+ date + " " + (s < 10? ("0" + s) :s) + "'";
					stmt.executeUpdate(sql);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(null != conn ){
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(null != stmt ){
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(null != rs){
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
}
