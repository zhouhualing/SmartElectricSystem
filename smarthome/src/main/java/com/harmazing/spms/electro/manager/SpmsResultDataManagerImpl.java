package com.harmazing.spms.electro.manager;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.area.dao.AreaDAO;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.electro.manager.SpmsResultDataManager;
@Service("spmsResultDataManager")
public class SpmsResultDataManagerImpl implements SpmsResultDataManager{

	@Autowired
	private QueryDAO queryDAO;
	@Autowired
	private AreaDAO areaDAO;
	
	@Override
	public List<List<Object>> getAllPowerByArea(String areaId,boolean isUpdate)
			throws ParseException {
		List<List<Object>> result = Lists.newArrayList();
		String sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') as date, case when sum(power) " +
				"is null then 0 else truncate(sum(power),2) end as power from spms_electro_result_data "  +"serd, spms_area sa "+
				"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id "
				+" group by " +
								"DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') "; 
		if(isUpdate){
			sql = sql + "ORDER BY date desc LIMIT 2,1";
		}else{
			sql = sql + "ORDER BY date asc";
		}
		List list = queryDAO.getMapObjects(sql);
		return handleData(result, list);
	}


	@Override
	public List<List<Object>> getAllReactivePowerByArea(String areaId ,boolean isUpdate)
			throws ParseException {
		List<List<Object>> result = Lists.newArrayList();
		List<Map> temp = Lists.newArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String d = sdf.format(new Date(System.currentTimeMillis()));
		String sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') as date,case when sum(power) is null then 0 else truncate(sum(power),2) end as power,case when sum(apparent_power) is null then 0 else truncate(sum(apparent_power),2) end as apparentpower from spms_electro_result_data " +"serd, spms_area sa "+
				"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id  and DATE_FORMAT(start_time,'%Y-%m-%d') = '" + d + "' "+
				" group by DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') ";
		if(isUpdate){
			sql = sql + "ORDER BY date desc LIMIT 2,1";
		}else{
			sql = sql + "ORDER BY date asc";
		}
		List list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size() ; i++){
			Map m1 = (Map) list.get(i);
			Map<String,Object> m2 = Maps.newHashMap();
			float temp1 = Float.valueOf(m1.get("power").toString()).longValue();
			float temp2 = Float.valueOf(m1.get("apparentpower").toString()).longValue();
			double temp3 = (temp2*temp2)-(temp1*temp1);
			double t = Math.sqrt(temp3);
			m2.put("date", m1.get("date"));
			m2.put("power", t);
			temp.add(m2);
		}
		return handleData(result, temp);
	}

	@Override
	public List<List<Object>> getAllPowerFactorByArea(String areaId,boolean isUpdate)
			throws ParseException {
		List<List<Object>> result = Lists.newArrayList();
		String sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') as date,"
				+ "case when sum(apparent_power) is null then 0 when sum(apparent_power) = 0 then 0 else truncate((sum(power)*100/sum(apparent_power)),2) end as power "
				+ "from spms_electro_result_data " +"serd, spms_area sa "+
				"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id "+
				" group by DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') ";
		if(isUpdate){
			sql = sql + "ORDER BY date desc LIMIT 2,1";
		}else{
			sql = sql + "ORDER BY date asc";
		}
		List list = queryDAO.getMapObjects(sql);
		return handleData(result, list);
	}

	@Override
	public List<List<Object>> getAllApparentPowerByArea(String areaId,boolean isUpdate)
			throws ParseException {
		List<List<Object>> result = Lists.newArrayList();
		
		String sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') as date,case when sum(apparent_power) is null then 0 else truncate(sum(apparent_power),2) end as power from spms_electro_result_data "  +"serd, spms_area sa "+
				"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id "+
				" group by DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') ";
		if(isUpdate){
			sql = sql + "ORDER BY date desc LIMIT 2,1";
		}else{
			sql = sql + "ORDER BY date asc";
		}
		List list = queryDAO.getMapObjects(sql);
		return handleData(result, list);
	}

	@Override
	public List<List<Object>> getAllEpByArea(String areaId,boolean isUpdate, String rbtn) throws ParseException {
		String sql = "";
		String formatStr = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date(System.currentTimeMillis()));
		if(rbtn.equals("1")){
			sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') as date,case when sum(accumulatePower) is null then 0 else truncate(sum(accumulatePower)/1000,3) end as power from spms_electro_result_data serd, spms_area sa "+
					"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y-%m-%d') = '" + date + "' " +
					" group by DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') ";
		}else if(rbtn.equals("2")){
			formatStr = "yyyy-MM-dd HH";
			sql = "select t.date1 as date,sum(t.power1) as power from (select sa.id as id,DATE_FORMAT(start_time,'%Y-%m-%d %H') as date1,case when max(accumulatePower) is null then 0 else truncate(max(accumulatePower)/1000,3) end as power1 from spms_electro_result_data serd, spms_area sa "+
					"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y-%m-%d') = '" + date + "' " +
					" group by DATE_FORMAT(start_time,'%Y-%m-%d %H'),sa.id) t group by t.date1 ";
		}else if(rbtn.equals("3")){
			date = date.substring(0,7);
//			formatStr = "yyyy-MM-dd";
			sql = " select date,truncate(accumulatePower/1000,3) as power from spms_electro_result_data_month where area_id = '" + areaId + "' and substr(date,1,7) ='" + date + "' ";
//			sql = "select t.date1 as date,sum(t.power1) as power from (select sa.id as id,DATE_FORMAT(start_time,'%Y-%m-%d') as date1,case when max(accumulatePower) is null then 0 else max(accumulatePower) end as power1 from spms_electro_result_data serd, spms_area sa "+
//					"where (instr(sa.parentIds,'"+areaId+"') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y-%m') = '" + date + "' " +
//					" group by DATE_FORMAT(start_time,'%Y-%m-%d'),sa.id) t group by t.date1 ";
		}else if(rbtn.equals("4")){
			date = date.substring(0,4);
//			formatStr = "yyyy-MM";
			sql = " select date,truncate(accumulatePower/1000,3) as power from spms_electro_result_data_year where area_id = '" + areaId + "' and substr(date,1,4) ='" + date + "' ";
//			sql = "select t.date1 as date,sum(t.power1) as power from (select sa.id as id,DATE_FORMAT(start_time,'%Y-%m') as date1,case when max(accumulatePower) is null then 0 else max(accumulatePower) end as power1 from spms_electro_result_data serd, spms_area sa "+
//					"where (instr(sa.parentIds,'"+areaId+"') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y') = '" + date + "' " +
//					" group by DATE_FORMAT(start_time,'%Y-%m'),sa.id) t group by t.date1 ";
		}
		sql = sql + " ORDER BY date asc";
		List<Map<String, Object>> list = queryDAO.getMapObjects(sql);
		List<List<Object>> result = new ArrayList<List<Object>>();
		return handleData(result, list);
	}

	@Override
	public List<List<Object>> getAllReactiveEnergyByArea(String areaId,boolean isUpdate, String rbtn)
			throws ParseException {
		String sql = "";
		String formatStr = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date(System.currentTimeMillis()));
		if(rbtn.equals("1")){
			 sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') as date,case when sum(reactive_energy) is null then 0 else truncate(sum(reactive_energy)/1000,2) end as power from spms_electro_result_data serd, spms_area sa "+
					"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y-%m-%d') = '" + date + "' " +
					" group by DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s')  ";
		}else if(rbtn.equals("2")){
			formatStr = "yyyy-MM-dd HH";
			sql = "select t.date1 as date,sum(t.power1) as power from (select sa.id as id,DATE_FORMAT(start_time,'%Y-%m-%d %H') as date1,case when max(reactive_energy) is null then 0 else truncate(max(reactive_energy)/1000,2) end as power1 from spms_electro_result_data serd, spms_area sa "+
					"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y-%m-%d') = '" + date + "' " +
					" group by DATE_FORMAT(start_time,'%Y-%m-%d %H'),sa.id) t group by t.date1 ";
		}else if(rbtn.equals("3")){
//			date = date.substring(0,7);
//			formatStr = "yyyy-MM-dd";
			sql = " select date,case when reactive_energy is null then 0 else truncate(reactive_energy,2) end as power from spms_electro_result_data_month where area_id = '" + areaId + "' and substr(date,1,7) ='" + date + "' ";
//			sql = "select t.date1 as date,sum(t.power1) as power from (select DATE_FORMAT(start_time,'%Y-%m-%d') as date1,case when sum(reactive_energy) is null then 0 else sum(reactive_energy) end as power1 from spms_electro_result_data serd, spms_area sa "+
//					"where (instr(sa.parentIds,'"+areaId+"') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y-%m') = '" + date + "' " +
//					" group by DATE_FORMAT(start_time,'%Y-%m-%d'),sa.id) t group by t.date1 ";
		}else if(rbtn.equals("4")){
			date = date.substring(0,4);
//			formatStr = "yyyy-MM";
			sql = " select date,case when reactive_energy is null then 0 else truncate(reactive_energy,2) end as power from spms_electro_result_data_year where area_id = '" + areaId + "' and substr(date,1,4) ='" + date + "' ";
//			sql = "select t.date1 as date,sum(t.power1) as power from (select DATE_FORMAT(start_time,'%Y-%m') as date1,case when sum(reactive_energy) is null then 0 else sum(reactive_energy) end as power1 from spms_electro_result_data serd, spms_area sa "+
//					"where (instr(sa.parentIds,'"+areaId+"') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y') = '" + date + "' " +
//					" group by DATE_FORMAT(start_time,'%Y-%m'),sa.id) t group by t.date1 ";
		}
		sql = sql + "ORDER BY date asc";
		List<Map<String, Object>> list = queryDAO.getMapObjects(sql);
		List<List<Object>> result = new ArrayList<List<Object>>();
		return handleData(result, list);
	}

	@Override
	public List<List<Object>> getAllReactiveDemandByArea(String areaId,boolean isUpdate)
			throws ParseException {
		List<List<Object>> result = Lists.newArrayList();
		
		String sql = "select DATE_FORMAT(demand_time,'%Y-%m-%d %H:%i:%s') as date,case when sum(reactive_demand) is null then 0 else truncate(sum(reactive_demand),2) end as power from spms_electro_result_data "  +"serd, spms_area sa "+
				"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id "+
				" group by DATE_FORMAT(demand_time,'%Y-%m-%d %H:%i:%s') ";
		if(isUpdate){
			sql = sql + "ORDER BY date desc LIMIT 2,1";
		}else{
			sql = sql + "ORDER BY date asc";
		}
		List list = queryDAO.getMapObjects(sql);
		return handleData(result, list);
	}

	@Override
	public List<List<Object>> getAllActiveDemandByArea(String areaId,boolean isUpdate)
			throws ParseException {
		List<List<Object>> result = Lists.newArrayList();
		String sql = "select DATE_FORMAT(demand_time,'%Y-%m-%d %H:%i:%s') as date,case when sum(active_demand) is null then 0 else truncate(sum(active_demand),2) end as power from spms_electro_result_data " +"serd, spms_area sa "+
				"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id "+
				" group by DATE_FORMAT(demand_time,'%Y-%m-%d %H:%i:%s') ";
		if(isUpdate){
			sql = sql + "ORDER BY date desc LIMIT 2,1";
		}else{
			sql = sql + "ORDER BY date asc";
		}
		List list = queryDAO.getMapObjects(sql);
		return handleData(result, list);
	}
	
	@Override
	public List<List<Object>> getAllDeviceNumByArea(String areaId, boolean isUpdate) throws ParseException {
		List<List<Object>> result = Lists.newArrayList();
		String sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') as date, case when sum(device_num) is null then 0 else sum(device_num) end as power  from spms_electro_result_data "  +"serd, spms_area sa "+
				"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id "
				+" group by DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') ";
		if(isUpdate){
			sql = sql + "ORDER BY date desc LIMIT 2,1";
		}else{
			sql = sql + "ORDER BY date asc";
		}
		List list = queryDAO.getMapObjects(sql);
		return handleData(result, list);
	}
	
	private List<List<Object>> handleData(List<List<Object>> result, List list)
			throws ParseException {
		for (int i = 0; i < list.size(); i++) {
			List<Object> item = Lists.newArrayList();
			Map map = (Map)list.get(i);
			if(map.get("date") == null){
				continue;
			}
			String date = map.get("date").toString();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH");
			SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM");
			Double power = Double.valueOf(map.get("power").toString());
			if(date.length() == 19){
				item.add(0,sdf1.parse(date).getTime());
			}else if(date.length() == 16){
				item.add(0,sdf2.parse(date).getTime());
			}else if(date.length() == 13){
				item.add(0,sdf3.parse(date).getTime());
			}else if(date.length() == 10){
				item.add(0,sdf4.parse(date).getTime());
			}else if(date.length() == 7){
				item.add(0,sdf5.parse(date).getTime());
			}
			item.add(1,power);
			result.add(item);
		}
//		if(result.size()>2){
//			result.remove(result.size()-1);
//			result.remove(result.size()-1);
//		}
		return result;
	}


	@Override
	public Map<String,Object> getAllDataByArea(String areaId, boolean isUpdate,String rbtn)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = sdf.format(new Date(System.currentTimeMillis()));
		String sql = "";
		if(rbtn.equals("1")){
			sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') as date, case when sum(power) " +
					"is null then 0 else truncate(sum(power),2) end as power," +
					"case when sum(apparent_power) is null then 0 when sum(apparent_power) = 0 then 0 else truncate((sum(power)*100/sum(apparent_power)),2) end as apparent_power1," +
					"case when sum(apparent_power) is null then 0 else truncate(sum(apparent_power),2) end as apparent_power2," +
					"case when sum(reactive_demand) is null then 0 else truncate(sum(reactive_demand),2) end as reactive_demand," +
					"case when sum(active_demand) is null then 0 else truncate(sum(active_demand),2) end as active_demand," +
					"case when sum(device_num) is null then 0 else sum(device_num) end as device_num " +
					"from spms_electro_result_data serd, spms_area sa "+
					"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y-%m-%d') = '" + d + "' "+
					" group by DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') ";
			sql = sql + "ORDER BY date asc";
		}else if(rbtn.equals("2")){
			sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') as date, case when sum(power) " +
					"is null then 0 else truncate(sum(power),2) end as power," +
					"case when sum(apparent_power) is null then 0 when sum(apparent_power) = 0 then 0 else truncate((sum(power)*100/sum(apparent_power)),2) end as apparent_power1," +
					"case when sum(apparent_power) is null then 0 else truncate(sum(apparent_power),2) end as apparent_power2," +
					"case when sum(reactive_demand) is null then 0 else truncate(sum(reactive_demand),2) end as reactive_demand," +
					"case when sum(active_demand) is null then 0 else truncate(sum(active_demand),2) end as active_demand," +
					"case when sum(device_num) is null then 0 else sum(device_num) end as device_num " +
					"from spms_electro_result_data serd, spms_area sa "+
					"where (instr(sa.parentIds,',"+areaId+",') > 0 or sa.id = '"+areaId+"') AND serd.area_id = sa.id and DATE_FORMAT(start_time,'%Y-%m-%d') = '" + d + "' "+
					" group by DATE_FORMAT(start_time,'%Y-%m-%d %H:%i:%s') ";
			sql = sql + "ORDER BY date asc";
		}else if(rbtn.equals("3")){
			sdf1 = new SimpleDateFormat("yyyy-MM-dd HH");
			d = d.substring(0,7);
			sql = "select date, case when sum(power) " +
					"is null then 0 else truncate(sum(power),2) end as power," +
					"case when sum(apparent_power) is null then 0 when sum(apparent_power) = 0 then 0 else truncate((sum(power)*100/sum(apparent_power)),2) end as apparent_power1," +
					"case when sum(apparent_power) is null then 0 else truncate(sum(apparent_power),2) end as apparent_power2," +
					"case when sum(reactive_demand) is null then 0 else truncate(sum(reactive_demand),2) end as reactive_demand," +
					"case when sum(active_demand) is null then 0 else truncate(sum(active_demand),2) end as active_demand," +
					"case when sum(device_num) is null then 0 else sum(device_num) end as device_num " +
					"from spms_electro_result_data_hour serd, spms_area sa "+
					"where sa.id = '"+areaId+"' AND serd.area_id = sa.id and substring(date,1,7) = '" + d + "' "+
					" group by date ";
			sql = sql + "ORDER BY date asc";
		}else if(rbtn.equals("4")){
			sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			d = d.substring(0,4);
			sql = "select t.date as date,sum(t.power) as power," +
					"sum(t.apparent_power1) as apparent_power1," +
					"sum(t.apparent_power2) as apparent_power2," +
					"sum(t.reactive_demand) as reactive_demand," +
					"sum(t.active_demand) as active_demand," +
					"sum(t.device_num) as device_num " +
					"from " +
					"(select sa.id,substring(serd.date,1,10) as date, case when avg(power) " +
					"is null then 0 else truncate(avg(power),2) end as power," +
					"case when avg(apparent_power) is null then 0 when avg(apparent_power) = 0 then 0 else truncate((avg(power)*100/avg(apparent_power)),2) end as apparent_power1," +
					"case when avg(apparent_power) is null then 0 else truncate(avg(apparent_power),2) end as apparent_power2," +
					"case when avg(reactive_demand) is null then 0 else truncate(avg(reactive_demand),2) end as reactive_demand," +
					"case when avg(active_demand) is null then 0 else truncate(avg(active_demand),2) end as active_demand," +
					"case when avg(device_num) is null then 0 else round(avg(device_num),0) end as device_num " +
					"from spms_electro_result_data_hour serd, spms_area sa "+
					"where sa.id = '"+areaId+"' AND serd.area_id = sa.id and substring(date,1,4) = '" + d + "' "+
					" group by substring(serd.date,1,10)) t group by t.date order by t.date asc";
		}
		
		List list = queryDAO.getMapObjects(sql);
		List<List<Long>> power = new ArrayList<List<Long>>();
		List<List<Long>> powerFactor = new ArrayList<List<Long>>();
		List<List<Long>> apparentPower = new ArrayList<List<Long>>();
		List<List<Long>> reactiveDemand = new ArrayList<List<Long>>();
		List<List<Long>> activeDemand = new ArrayList<List<Long>>();
		List<List<Long>> deviceNum = new ArrayList<List<Long>>();
		List<List<Long>> reactivePower = new ArrayList<List<Long>>();
		for (int i = 0; i < list.size(); i++) {
			List<Long> p = Lists.newArrayList();
			List<Long> pf = Lists.newArrayList();
			List<Long> ap = Lists.newArrayList();
			List<Long> rd = Lists.newArrayList();
			List<Long> ad = Lists.newArrayList();
			List<Long> dn = Lists.newArrayList();
			List<Long> rp = Lists.newArrayList();
			
			Map map = (Map)list.get(i);
			if(map.get("date") == null){
				continue;
			}
			String date = map.get("date").toString();
			
			p.add(0,sdf1.parse(date).getTime());
			p.add(1,Double.valueOf(map.get("power").toString()).longValue());
			power.add(p);
			
			pf.add(0,sdf1.parse(date).getTime());
			pf.add(1,Double.valueOf(map.get("apparent_power1").toString()).longValue());
			powerFactor.add(pf);

			ap.add(0,sdf1.parse(date).getTime());
			ap.add(1,Double.valueOf(map.get("apparent_power2").toString()).longValue());
			apparentPower.add(ap);

			rd.add(0,sdf1.parse(date).getTime());
			rd.add(1,Double.valueOf(map.get("reactive_demand").toString()).longValue());
			reactiveDemand.add(rd);
			
			ad.add(0,sdf1.parse(date).getTime());
			ad.add(1,Double.valueOf(map.get("active_demand").toString()).longValue());
			activeDemand.add(ad);
			
			dn.add(0,sdf1.parse(date).getTime());
			dn.add(1,Double.valueOf(map.get("device_num").toString()).longValue());
			deviceNum.add(dn);
			
			double temp1 = Double.valueOf(map.get("power").toString()).longValue();
			double temp2 = Double.valueOf(map.get("apparent_power2").toString()).longValue();
			double temp3 = (temp2*temp2)-(temp1*temp1);
			double t = Math.sqrt(temp3);
			rp.add(0,sdf1.parse(date).getTime());
			rp.add(1,Double.valueOf(t).longValue());
			reactivePower.add(rp);
		}
		Map<String,Object> result = new HashMap<String, Object>();
		if(isUpdate){
			result.put("newpowers", power);
			result.put("newreactivePowers", reactivePower);
			result.put("neweps", getAllEpByArea(areaId, false, rbtn));
			result.put("newreactiveEnergys", getAllReactiveEnergyByArea(areaId, false, rbtn));
			result.put("newpowerFactors", powerFactor);
			result.put("newapparentPowers", apparentPower);
			result.put("newactiveDemands", activeDemand);
			result.put("newreactiveDemands", reactiveDemand);
			result.put("newdeviceNum", deviceNum);
		}else{
			result.put("powers", power);
			result.put("reactivePowers", reactivePower);
			result.put("eps", getAllEpByArea(areaId, false, rbtn));
			result.put("reactiveEnergys", getAllReactiveEnergyByArea(areaId, false, rbtn));
			result.put("powerFactors", powerFactor);
			result.put("apparentPowers", apparentPower);
			result.put("activeDemands", activeDemand);
			result.put("reactiveDemands", reactiveDemand);
			result.put("deviceNum", deviceNum);
		}
		return result;
	}
}
