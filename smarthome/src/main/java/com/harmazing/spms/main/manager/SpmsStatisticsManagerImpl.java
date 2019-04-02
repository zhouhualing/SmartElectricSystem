package com.harmazing.spms.main.manager;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.DateUtil;
import com.harmazing.spms.main.manager.SpmsStatisticsManager;
@Service
public class SpmsStatisticsManagerImpl implements SpmsStatisticsManager{
	
	@Autowired
	private QueryDAO queryDAO;
	
	/**
     * 用户数量按照所属区域进行统计(柱状图)
     *  @param type 1=业务区域
     *  			2=用电区域
     * @return	[{name:'北京',value:10},{name:'天津',value:10}]
     */
	@Override
	public List<Map<String, Object>> userCountByArea(int type) {
		List <Map<String,Object>> result = Lists.newArrayList();
		String sql = "";
		if(type == 1){
			sql = "SELECT areaname, COUNT(su.id) AS value FROM spms_user su, (SELECT getChildsByArea(id, '"+type+"') AS childIds, name AS areaname FROM spms_area WHERE FIND_IN_SET(ID, getChildsByArea('2', '"+type+"')) AND id <> '2' ) temp WHERE FIND_IN_SET(su.biz_area_id, temp.childIds) GROUP BY temp.areaname";
		}else{
			sql = "SELECT areaname, COUNT(su.id) AS value FROM spms_user su, (SELECT getChildsByArea(id, '"+type+"') AS childIds, name AS areaname FROM spms_area WHERE FIND_IN_SET(ID, getChildsByArea('2', '"+type+"')) AND id <> '2' ) temp WHERE FIND_IN_SET(su.biz_area_id, temp.childIds) GROUP BY temp.areaname";
		}
		
		result = queryDAO.getMapObjects(sql);
		List <Map<String,Object>> temp = Lists.newArrayList();
		for(int i = 0 ; i< result.size() ;i++){
			Map map = result.get(i);
			Map<String,Object> t = Maps.newHashMap();
			t.put((String)map.get("areaname"), map.get("value"));
			
			temp.add(t);
		}
		
		return temp;
	}
	
	
	/**
	 * 用户服务类统计(用户数量按照服务的类别)
	 * @return
	 */
	@Override
	public List<Map<String, Object>> userCountByRule() {
		List <Map<String,Object>> result = Lists.newArrayList();
		List <Map<String,Object>> temp = Lists.newArrayList();
		String sql = "SELECT spt. NAMES AS pname, count( surb.user_id) AS value from spms_product_type spt LEFT JOIN spms_product surb ON spt.id = surb.type_id GROUP BY spt.names";
		result = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i< result.size() ;i++){
			Map map = result.get(i);
			Map<String,Object> t = Maps.newHashMap();
			t.put((String)map.get("pname"), map.get("value"));
			temp.add(t);
		}
		return temp;
	}

	/**
	 * 用户数量根据在线状态统计(offline/online/exception)
	 * @return
	 */
	@Override
	public List<Map<String, Object>> userCountByOnlineStatus() {
		List <Map<String,Object>> result = Lists.newArrayList();
		int offlineCount = 0; //离线
		Map<String,Object> offlineMap = Maps.newHashMap();

		int onlineCount = 0; //在线
		Map<String,Object> onlineMap = Maps.newHashMap();
		
		int exceptionCount = 0; //异常
		Map<String,Object> exceptionMap = Maps.newHashMap();
		
		//String sql = "SELECT CASE WHEN sd.operStatus IS NULL THEN 0 ELSE sd.operStatus END AS status FROM (SELECT DISTINCT surb.gwId AS id FROM spms_area sa, spms_user su, spms_user_product_binding surb WHERE (instr(sa.parentIds, ',11,') > 0 OR sa.id = '11') AND su.ele_area_id = sa.id AND surb.user_id = su.id ) decount LEFT JOIN spms_device sd ON decount.id = sd.id";
		String sql="select case when sd.operStatus is null then 0 else sd.operStatus end as status from (select DISTINCT su.gw_id as id from spms_area sa,spms_user su where (instr(sa.parentIds,',"+1+",') > 0 or sa.id = '"+1+"') and su.ele_area_id = sa.id ) decount inner JOIN spms_device sd on decount.id = sd.id";
		List<Object> list = queryDAO.getObjects(sql);
		
		for(int i = 0 ; i < list.size() ; i++){
			int temp = ((BigInteger) list.get(i)).intValue();
			if(temp == 0){
				offlineCount += 1;
			}else if(temp == 1){
				onlineCount += 1;
			}else if(temp == 2){
				exceptionCount += 1;
			}
		}
		
		offlineMap.put("离线", offlineCount);
		
		onlineMap.put("在线", onlineCount);
		
		exceptionMap.put("异常", exceptionCount);
		
		result.add(0, offlineMap);
		result.add(1, onlineMap);
		result.add(2, exceptionMap);
		
		return result;
	}

	/**
	 * 用户开通/退订数量增量根据时间的曲线
	 * @return
	 * @throws ParseException 
	 */
	@Override
	public List<Map<String, Object>> userIncrementByDate() throws ParseException {
		List<Map<String,Object>> result = Lists.newArrayList();
		
		String sql = "SELECT DATE_FORMAT(bind.date, '%Y-%m-%d') date, bind.count, CASE WHEN unbind.count IS NULL THEN 0 ELSE unbind.count END AS uncount FROM ( SELECT DATE_FORMAT( sur.activateDate, '%Y-%m-%d' ) AS date, COUNT(STATUS) AS count FROM spms_product sur WHERE sur. STATUS = 1 GROUP BY DATE_FORMAT( sur.activateDate, '%Y-%m-%d' ) ORDER BY sur.activateDate ) bind LEFT JOIN ( SELECT DATE_FORMAT(t1.END_TIME_, '%Y-%m-%d') AS date, COUNT(1) count FROM act_hi_taskinst t1, tb_workflow_variable t2 WHERE INSTR( t1.PROC_DEF_ID_, 'unsubscribe' ) > 0 AND t1.TASK_DEF_KEY_ = 'usertask3' AND t1.END_TIME_ IS NOT NULL AND t1.PROC_INST_ID_ = t2.processId AND t2.iKey = 'orderProduct' GROUP BY DATE_FORMAT(t1.END_TIME_, '%Y-%m-%d')) unbind ON bind.date = unbind.date";
		
		List list = queryDAO.getMapObjects(sql);
		
		for (int i = 0; i <list.size(); i++) {
			Map<String,Object> data = Maps.newHashMap();
			Map map = (Map) list.get(i);
			String date = (String)map.get("date");
			Integer count = Integer.parseInt(map.get("count").toString());
			Integer uncount = Integer.parseInt(map.get("uncount").toString());
			data.put("date", new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime());
			data.put("count", count);
			data.put("uncount", uncount);
			result.add(data);
			
		}
		
		return result;
	}

	/**
	 * 设备库存统计(按型号/按仓库位置（各类别设备）)
	 * @return
	 */
	@Override
	public List<Map<String, Object>> deviceByModel() {
		List<Map<String,Object>> result = Lists.newArrayList();
		String sql = "select type,count(sd.id) as value from spms_device sd where sd.status = 1 GROUP BY sd.type";
		List devices = queryDAO.getMapObjects(sql);
		
		for (int i = 0; i < devices.size(); i++) {
			Map<String,Object> childMap = Maps.newHashMap();
			Map item = (Map)devices.get(i);
			Integer type = (Integer)item.get("type");
			Integer value = Integer.parseInt(item.get("value").toString());
			childMap.put("type", type==1?"网关":type==2?"空调":type==3?"门传感":"窗传感");
			childMap.put("value", value);
            result.add(childMap);
		}
		
		return result;
	}


	/**
	 * 设备运营状态统计(设备数目按照 - 库存/运营/售后维修/报废 （个类别设备）)
	 * @return
	 */
	@Override
	public List<Map<String, Object>> deviceByOptStatus() {
		List<Map<String,Object>> result = Lists.newArrayList();
		String sql = "select type,sum(case when status=1 then 1 else 0 end) as stock,sum(case when status=2 then 1 else 0 end) as operate,sum(case when status=3 then 1 else 0 end) as repair,sum(case when status=4 then 1 else 0 end) as scrap from spms_device sd group by sd.type";
		
		List list = queryDAO.getMapObjects(sql);
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> item = Maps.newHashMap();
			Map data = (Map) list.get(i);
			Integer type = (Integer) data.get("type");
			Integer stock = Integer.parseInt(data.get("stock").toString());
			Integer operate = Integer.parseInt(data.get("operate").toString());
			Integer repair = Integer.parseInt(data.get("repair").toString());
			Integer scrap = Integer.parseInt(data.get("scrap").toString());
			item.put("name", type == 1?"网关":type ==2?"空调":type==3?"门传感":"窗传感");
			item.put("stock", stock);
			item.put("operate", operate);
			item.put("repair", repair);
			item.put("scrap", scrap);
			result.add(item);
		}
		
		return result;
	}


	/**
	 * 用户服务期限统计（已到期，1月内，1-3月内，3月以上,半年以上）
	 * @throws ParseException 
	 */
	@Override
	public List<Map<String, Object>> userCountByProductRemaining() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//Date nowDate  = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH)+1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		
		List<Map<String,Object>> result = Lists.newArrayList();
		double mo = 2592000000.0;
		int t1 = 0;//已到期
		int t2 = 0;//一月内
		int t3 = 0;//1-3月内
		int t4 = 0;//3月以上
		int t5 = 0;//半年以上
		Map<String,Object> data1 = Maps.newHashMap();
		Map<String,Object> data2 = Maps.newHashMap();
		Map<String,Object> data3 = Maps.newHashMap();
		Map<String,Object> data4 = Maps.newHashMap();
		Map<String,Object> data5 = Maps.newHashMap();
		String sql = "SELECT DATE_FORMAT( expireDate, '%Y-%m-%d' ) FROM spms_product WHERE expireDate IS NOT NULL and status = 1 ";
		List list = queryDAO.getObjects(sql);
		for(int i = 0 ; i < list.size() ; i ++){
			String dateTemp = (String)list.get(i);
			Date tempDate = sdf.parse(dateTemp);
			Calendar td = Calendar.getInstance();
			td.setTime(tempDate);
			
			int tyear = td.get(Calendar.YEAR);
			int tmonth = td.get(Calendar.MONTH)+1;
			int tday = td.get(Calendar.DAY_OF_MONTH);
			
			if(tyear >= year){
				if( tyear== year){
					if(tmonth<month){
						t1++;
						continue;
					}else if(tmonth == month){
						if(tday>day){
							t2++;
							continue;
						}else{
							t1++;
							continue;
						}
					}else{
						if((tmonth-month)>6){
							t5++;
							continue;
						}else if((tmonth-month)<=6 && (tmonth-month)>3){
							t4++;
							continue;
						}else if((tmonth-month)<=3 && (tmonth-month)>=2){
							t3++;
							continue;
						}else{
							if((tmonth - month)==1  && tday >= day){
								t3++;
								continue;
							}else if((tmonth - month)==1 && tday < day){
								t2++;
								continue;
							}
						}
					}
				}else{
					t5++;
					continue;
				}
			}else{
				t1++;
				continue;
			}
		}
		
		data1.put("已到期", t1);
		data2.put("1月内", t2);
		data3.put("1-3月内", t3);
		data4.put("3-6各月", t4);
		data5.put("半年以上", t5);

		result.add(0, data1);
		result.add(1, data2);
		result.add(2, data3);
		result.add(3, data4);
		result.add(4, data5);
		
		
		return result;
	}

/**
 * 软件版本统计信息
 */
	@Override
	public Map<String, Object> getSoftwareVersionStatisticalData() {
		// TODO Auto-generated method stub
		//SELECT software,count(1) from spms_device WHERE type=1  GROUP BY software 
		
		List<Map<String, Object>> l = queryDAO.getMapObjects("SELECT software,count(1) as count from spms_device WHERE type=1  GROUP BY software");
		List<Map<String, Object>> rl = new ArrayList<Map<String, Object>>();
		Map<String,Object> rm = new HashMap<String,Object>();
		List<String> l1 = new ArrayList<String>();
		List<Long> l2 = new ArrayList<Long>();
		for(Map<String, Object> m : l){
			l1.add(		m.get("software")+""	);
			l2.add(		Long.valueOf(	(m.get("count")+"")	)	);
		}
		rm.put("software", l1);
		rm.put("count", l2);
		return rm;
	}
}
