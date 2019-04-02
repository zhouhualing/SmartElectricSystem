package com.harmazing.spms.spmsuc.Manager;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.base.util.DateUtil;
import com.harmazing.spms.base.util.ErrorCodeConsts;
import com.harmazing.spms.base.util.MongoUtil;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.device.dao.*;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.device.entity.SpmsDeviceData;
import com.harmazing.spms.product.entity.SpmsProductType;
import com.harmazing.spms.spmsuc.Manager.SpmsAppManager;
import com.harmazing.spms.user.dao.SpmsUserProductBindingDAO;
import com.harmazing.spms.usersRairconSetting.dto.DeviceErrorDTO;
import com.harmazing.spms.usersRairconSetting.manager.DeviceErrorManager;
import com.harmazing.spms.device.manager.SpmsDeviceManager;
import com.harmazing.spms.helper.EnumTypesConsts;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Service
public class SpmsAppManager implements IManager{
	@Autowired
	private QueryDAO queryDAO;
	@Autowired
	private SpmsUserProductBindingDAO spmsUserProductBindingDAO;
	
	@Autowired
	private SpmsGatewayDAO spmsGwDAO;
	
	@Autowired
	private SpmsAirConditionDAO spmsDeviceDAO;
	@Autowired
	private DeviceErrorManager deviceErrorManager;
	
	@Autowired
	private SpmsAirConditionDAO spmsAcDAO;
	
	@Autowired
	private SpmsDeviceManager spmsDeviceManager;
	
	public List<Map<String, Object>> getDeviceStatusByUser(String userId) {
		List<Map<String,Object>> result = Lists.newArrayList();
		String sql = "select sd.id,sd.type,sd.operStatus as status,sd.onOff as onOff,sd.temp,"
				+ "sd.acTemp as acTemp,sd.mode,"
				+ "sd.speed,sd.remain,su.gw_id as gwId,surb.customName as name,sd.sn "
				+ "from spms_device sd ,"
				+ " spms_user_product_binding surb,spms_user su where surb.user_id = su.id and sd.id=surb.device_id and surb.user_id='"+userId+"'";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		
		for(int i = 0 ; i < list.size() ; i++){
			Map<String,Object> data = Maps.newHashMap();
			data = list.get(i);
			String id = data.get("id").toString();
			Map<String,Object> ruleInfo = getDeviceRlueInfo(id);
			int type = (int) data.get("type");
			String name = (String) data.get("name");
			if(StringUtil.isNUll(name)){
				data.put("name", spmsDeviceDAO.findOne(id).getSn());
			}
			data.put("type",String.valueOf(type));
			data.put("id", String.valueOf(id));
			data.put("maxTemp",(Integer)ruleInfo.get("maxTemp"));
        	data.put("minTemp",(Integer)ruleInfo.get("minTemp"));
        	data.put("allowHeat",(Integer)ruleInfo.get("allowHeat"));
        	if((Integer)data.get("status") == 0) {
                data.put("state", 2);
            }else if((Integer)data.get("status") ==1){
                data.put("state",(Integer)data.get("onOff"));
            }else{
            	data.put("state", (Integer)data.get("status"));
            }
        	DeviceErrorDTO deviceErrorDTO=deviceErrorManager.queryDeviceError(id);
        	if(deviceErrorDTO!=null){
        		if(deviceErrorDTO.getErrorCode()!=null && !deviceErrorDTO.getErrorCode().toString().equals("E0")){
	        		data.put("errorCode",deviceErrorDTO.getErrorCode());
	        		data.put("errorDetail",deviceErrorDTO.getErrorExplain());
        		}
        	}
        	//获取定时和舒睡曲线状态
        	/*sql="select sum(bz) clock from ("
			+ "	(select 1 as bz from rairconcurve_clocksetting where alone=1  "
			+ " and spmsDevice_id='"+id+"' LIMIT 0,1)"
			+ "	union all "
			+ " (select 2  as bz from spms_device_curve c"
			+ "	where  c.spmsDevice_id='"+id+"' LIMIT 0,1)"
			+ ") t";*/
	
			sql = "  select count(1) as clock from spms_device_curve t3,spms_raircon_setting t5 where t5.id = t3.curve_id and t3.spmsDevice_id = '"+id+"' " +
					"and ( ( t3.monday<>0) or"+
					"(t3.tuesday<>0) or "+
					"(t3.wednesday<>0) or "+
					"(t3.thursday<>0) or "+
					"(t3.friday<>0) or "+
					"(t3.saturday<>0) or "+
					"(t3.sunday<>0) ) and "+
					"( "+																													
					"	(select count(1) from rairconcurve_clocksetting t2 where  t2.raircon_Setting_id = t3.curve_id  and t2.temp <= "+data.get("maxTemp")+" and  t2.temp >= "+data.get("minTemp")+" ) "+
					"		= (select count(1) from rairconcurve_clocksetting t4 where  t4.raircon_Setting_id = t3.curve_id) "+
					"	and  "+
					"	((select count(1) from rairconcurve_clocksetting t4 where  t4.raircon_Setting_id = t3.curve_id) <> 0) "+
					")";
			List<Map<String,Object>> lst = queryDAO.getMapObjects(sql);
			int clock=0;
			if(lst!=null && lst.size()>0){
				Map<String,Object> m=lst.get(0);
				if(m.get("clock")!=null){
					try{
						clock=Integer.parseInt(m.get("clock").toString());
					}catch(Exception e){
						
					}
				}
			}
			data.put("clock",clock);
			/*
			 *加载空调设备定时数量
			 */
			sql = " select count(*) as alert from RairconCurve_Clocksetting where spmsDevice_id = '"+id+"' ";
			lst = queryDAO.getMapObjects(sql);
			clock = 0;
			if(lst!=null && lst.size()>0){
				Map<String,Object> m=lst.get(0);
				if(m.get("alert")!=null){
					try{
		    				clock=Integer.parseInt(m.get("alert").toString());
		    			}catch(Exception e){}
				}
			}
			
			data.put("alert",clock);
			result.add(data);
		}
		return result;
	}

	
	public Map<String, Object> getDeviceRlueInfo(String deviceid) {
		Map<String,Object> result = new HashMap<String,Object>();
//		List<SpmsUserProductBinding> temp = spmsUserProductBindingDAO.findByDevice(deviceid);
//		if(temp == null || temp.size() ==0){
//			return null;
//		}
//		SpmsUserProductBinding surb = temp.get(0);
//		if(surb!=null){
//			SpmsProductType sr = surb.getSpmsProduct().getSpmsProductType();
//			
//			result.put("allowHeat", sr.getConfigurationInformation());
//			result.put("maxTemp", sr.getZhiReMax());
//			result.put("minTemp", sr.getZhiLengMix());
//			return result;
//		}else{
//			System.out.println("用deviceid没有找到对应的rule");
//			return null;
//		}
		
		return result;
	}

	
	public List<Map<String, Object>> getDevicesCurrentStatusByApp(String gwId, String[] deviceIds) {
		List<Map<String,Object>> result = Lists.newArrayList();
		
		Integer gwStatus = getGwJoinStatus(gwId);
		
		 for(String deviceId : deviceIds){
			 Map<String,Object> data = Maps.newHashMap();
			 try {
				 SpmsDeviceData spmsDeviceData = loadDeviceNewest(deviceId);
				 if(spmsDeviceData.getType() == 2){
					 Map<String,Object> ruleInfo = getDeviceRlueInfo(spmsDeviceData.getDeviceId());
					 
					 data.put("maxTemp",(Integer)ruleInfo.get("maxTemp"));
			        data.put("minTemp",(Integer)ruleInfo.get("minTemp"));
			        data.put("allowHeat",(Integer)ruleInfo.get("allowHeat"));
                     int status = spmsDeviceData.getStatus();
                     int onOff = spmsDeviceData.getOnOff();
                     int temp = spmsDeviceData.getTemp();
                     int acTemp = spmsDeviceData.getAcTemp();
                     int mode = spmsDeviceData.getMode();
                     int speed = spmsDeviceData.getSpeed();
                     data.put("success", true);
                     data.put("status",gwStatus.intValue()==1?status:gwStatus);//0离线，1在线，2异常
                     data.put("onOff",onOff);
                     data.put("temp",temp);
                     data.put("acTemp",acTemp);
                     data.put("mode",mode);
                     data.put("speed",speed);
                     data.put("deviceId", deviceId);
                     data.put("type", spmsDeviceData.getType());
				 }else if(spmsDeviceData.getType() == 3 || spmsDeviceData.getType() == 4) {
					 int status = spmsDeviceData.getStatus();
                     int onOff = spmsDeviceData.getOnOff();
                     int remain = spmsDeviceData.getRemain();
                     data.put("success", true);
                     data.put("status",gwStatus.intValue()==1?status:gwStatus);//0离线，1在线，2异常
                     data.put("onOff", onOff);
                     data.put("remain", remain);
                     data.put("deviceId", deviceId);
                     data.put("type", spmsDeviceData.getType());
				 }
			 }catch (Exception e) {
	                data.put("success", false);
	                data.put("deviceId",deviceId);
	                e.printStackTrace();
	         }
			 result.add(data);
		 }
		
		return result;
	}
	
	public Integer getGwJoinStatus(String gwid) {
		
		SpmsDeviceBase sd = spmsDeviceManager.getDeviceByMac(gwid);
		if(sd==null)
			sd = spmsDeviceManager.getDeviceById(gwid);
		if(sd == null || sd.getId() == null){
			return null;
		}
		return sd.getOperStatus();
	}
	
	public SpmsDeviceData loadDeviceNewest(String deviceIdOrMac) {
		return loadDeviceNewest(deviceIdOrMac,EnumTypesConsts.DeviceType.Dev_Type_AC);
	}
	public SpmsDeviceData loadDeviceNewest(String deviceIdOrMac, Integer type) {
		//both is null or empty 
		if (StringUtil.isNUllOrEmpty(deviceIdOrMac))
			return null;
		
		SpmsDeviceDTO deviceDTO = spmsDeviceManager.getDeviceDTO(deviceIdOrMac, type);
		
		if(deviceDTO!=null && deviceDTO.getId()!=null){
			
			SpmsDeviceData data = new SpmsDeviceData();
			data.setAcTemp(deviceDTO.getAcTemp());
			data.setTemp(deviceDTO.getTemp());
			data.setDeviceId(deviceDTO.getId());
			data.setMode(deviceDTO.getMode());
			data.setOnOff(deviceDTO.getOnOff());
			data.setSpeed(deviceDTO.getSpeed());
			data.setStatus(deviceDTO.getOperStatus());
			data.setType(deviceDTO.getType());
			//data.setRemain(sd.getRemain());
			data.setMac(deviceDTO.getMac());
			data.setGwMac(deviceDTO.getGwMac());
			return data;
		}	
		
		return null;
	}	
	
	public List<Object[]> aceEleRecord(String deviceId)  throws ParseException {
		List<Object[]> result = Lists.newArrayList();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		
		String sql = "select start_time as time,case when on_off = 0 then 0 else power end as power from spms_ac_status where device_id='"+deviceId+"'  AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '" + date + "' order by start_time asc";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size() ; i++){
			Map<String,Object> map = list.get(i);
			float power = Float.valueOf(map.get("power").toString());
			//long time = DateUtil.parseStringToDate(map.get("time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			long time = ((Date)map.get("time")).getTime();
			result.add(new Object[]{time,power});
		}
		return result;
//		MongoDatabase db = MongoUtil.getDB("spms");
//		MongoCollection<Document> coll = db.getCollection("AC_LOG:");
//		Document d = new Document();
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.DATE, -1);
//		d.put("updateTime", new Document("$gt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime())));
//		d.put("deviceId", deviceId);
//		long t1 = System.currentTimeMillis();
//		FindIterable<Document> ite = coll.find(d);
//		System.out.println("find end");
//		long t2 = System.currentTimeMillis();
//		Iterator<Document> i = ite.iterator();
//		List<Object[]> list = new ArrayList<Object[]>();
//		while(i.hasNext()){
//			Document doc = i.next();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			Date startTime = sdf.parse(doc.getString("updateTime"));
//			int power = doc.getString("currentPower")!= null?Integer.parseInt(doc.getString("currentPower")):0;
//			list.add(new Object[]{startTime,power});
//		}
//		return list;
	}

	
	public List<Object[]> acNewElRecord(String deviceId, Long endTime) throws ParseException {
		List<Object[]> result = Lists.newArrayList();
    	Timestamp last = new Timestamp(endTime);
    	String sql = "select start_time as time,case when on_off = 0 then 0 else power end as power from spms_ac_status where device_id='"+deviceId+"' and start_time > '"+endTime+"'  order by start_time asc";
    	List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
    	for(int i = 0 ; i < list.size() ; i++){
    		Map<String,Object> map = list.get(i);
    		float power = Float.valueOf(map.get("power").toString());
			//long time = DateUtil.parseStringToDate(map.get("time").toString(),"yyyy-MM-dd hh:mm:ss").getTime();
			long time = ((Date)map.get("time")).getTime();
			result.add(new Object[]{time,power});
    	}
		return result;
	}
	
	
	public List<Object[]> aceEleRecord1(String deviceId, String rbtn)  throws ParseException {
		// 根据统计类型 查询不同表
		String sql = "";
		// 统计类型
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		String formatStr = "";
		if (rbtn.equals("1")) {
			formatStr = "yyyy-MM-dd HH:mm:ss";
			sql = "select start_time as date ,truncate(accumulatePower/1000,3) as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					+ "AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"
					+ date
					+ "' ORDER BY start_time asc";

		} else if (rbtn.equals("2")) {
			formatStr = "yyyy-MM-dd HH";
			sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H') as date,truncate(max(accumulatePower)/1000,3) as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					+ "AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"
					+ date
					+ "' group by DATE_FORMAT(start_time,'%Y-%m-%d %H') ORDER BY start_time asc";
		} else if (rbtn.equals("3")) {
			date = date.substring(0, 7);
			formatStr = "yyyy-MM-dd";
			sql = " select date,truncate(accumulatePower/1000,3) as power from spms_ac_status_month where device_id = '"
					+ deviceId
					+ "' and substr(date,1,7) ='"
					+ date
					+ "' order by date";
		} else if (rbtn.equals("4")) {
			date = date.substring(0, 4);
			formatStr = "yyyy-MM";
			sql = " select date,truncate(accumulatePower/1000,3) as power from spms_ac_status_year where device_id = '"
					+ deviceId
					+ "' and substr(date,1,4) ='"
					+ date
					+ "' order by date";
		}

		List<Map<String, Object>> d = queryDAO.getMapObjects(sql);
		// 如果该用户没有绑定网关，则就没有对应的设备。
		List<Object[]> datas = new ArrayList<Object[]>();
		for (Map<String, Object> map : d) {
			Double power = (Double.parseDouble( map.get("power").toString()));
			long time = new SimpleDateFormat(formatStr).parse(
					map.get("date").toString()).getTime();
			Object[] data = new Object[] { time, power };
			datas.add(data);
		}
		return datas;
	}
	
	
	public List<Object[]> acNewElRecord1(String deviceId, Long endTime,String rbtn) throws ParseException {
		List<Object[]> result = Lists.newArrayList();
    	Timestamp last = new Timestamp(endTime);
    	
    	Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(last);
		String formatStr = "";
		String sql = "";
		if (rbtn.equals("1")) {
			formatStr = "yyyy-MM-dd HH:mm:ss";
			sql = "select start_time as date ,truncate(accumulatePower/1000,3) as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					+ "AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"
					+ date
					+ "' ORDER BY start_time asc";

		} else if (rbtn.equals("2")) {
			formatStr = "yyyy-MM-dd HH";
			sql = "select DATE_FORMAT(start_time,'%Y-%m-%d %H') as date,truncate(max(accumulatePower)/1000,3) as power from spms_ac_status where device_id = '"
					+ deviceId
					+ "' "
					+ "AND DATE_FORMAT(start_time,'%Y-%m-%d') >= '"
					+ date
					+ "' group by DATE_FORMAT(start_time,'%Y-%m-%d %H') ORDER BY start_time asc";
		} else if (rbtn.equals("3")) {
			date = date.substring(0, 7);
			formatStr = "yyyy-MM-dd";
			sql = " select date,truncate(accumulatePower/1000,3) as power from spms_ac_status_month where device_id = '"
					+ deviceId
					+ "' and substr(date,1,7) ='"
					+ date
					+ "' order by date";
		} else if (rbtn.equals("4")) {
			date = date.substring(0, 4);
			formatStr = "yyyy-MM";
			sql = " select date,truncate(accumulatePower/1000,3) as power from spms_ac_status_year where device_id = '"
					+ deviceId
					+ "' and substr(date,1,4) ='"
					+ date
					+ "' order by date";
		}
    	List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
    	for(int i = 0 ; i < list.size() ; i++){
    		Map<String,Object> map = list.get(i);
    		Double power = Double.valueOf(map.get("power").toString());
    		long time = new SimpleDateFormat(formatStr).parse(
					map.get("date").toString()).getTime();
			result.add(new Object[]{time,power});
    	}
		return result;
//		MongoDatabase db = MongoUtil.getDB("spms");
//		MongoCollection<Document> coll = db.getCollection("AC_LOG:");
//		Document d = new Document();
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.DATE, -1);
//		d.put("updateTime", new Document("$gt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime())));
//		d.put("deviceId", deviceId);
//		long t1 = System.currentTimeMillis();
//		FindIterable<Document> ite = coll.find(d);
//		System.out.println("find end");
//		long t2 = System.currentTimeMillis();
//		Iterator<Document> i = ite.iterator();
//		List<Object[]> list = new ArrayList<Object[]>();
//		while(i.hasNext()){
//			Document doc = i.next();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			Date startTime = sdf.parse(doc.getString("updateTime"));
//			int power = doc.getString("accumulatePower")!= null?Integer.parseInt(doc.getString("accumulatePower")):0;
//			list.add(new Object[]{startTime,power});
//		}
//		return list;
	}

	
	public List<Object[]> getSensorOnOffState(String deviceId) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		
		List<Object[]> result = Lists.newArrayList();
        String sql = "select operate_time as time,operate_type as onOff from spms_win_door_status where device_id='"+deviceId+"' and DATE_FORMAT(operate_time,'%Y-%m-%d %H') >= '" + date + "' order by time asc";
        List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
        for(int i = 0 ; i < list.size() ; i++){
        	Map<String,Object> map = list.get(i);
        	int power = (int) map.get("onOff");
			//long time = DateUtil.parseStringToDate(map.get("time").toString()).getTime();
			long time = ((Date)map.get("time")).getTime();
			result.add(new Object[]{time,power});
        }
        
		return result;
	}

	
	public Object[] getNewSensorOnOffState(String deviceid) throws ParseException {
		List<Object[]> result = Lists.newArrayList();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		String sql = "select operate_time as time, operate_type from spms_win_door_status where device_id='"+deviceid+"' and DATE_FORMAT(operate_time,'%Y-%m-%d %H') >= '" + date + "' order by operate_time desc ";
		List<Map<String,Object>> list = queryDAO.getMapObjects(sql);
		for(int i = 0 ; i < list.size() ; i++){
			Map<String,Object> map = list.get(i);
			//long time = DateUtil.parseStringToDate(map.get("time").toString()).getTime();
			long time = ((Date)map.get("time")).getTime();
			float openclose = Float.valueOf(map.get("operate_type").toString());
			result.add(new Object[]{time,openclose});
		}
		if(result!=null && result.size()>0){
			return result.get(0);
		}
		return null;
	}

	
	public Map<String, Object> changeAcStatus(String gwId, String deviceId, String commandType, int value) {
		return changeAcStatus(gwId,deviceId,EnumTypesConsts.DeviceType.Dev_Type_AC,commandType,value);
	}
	
	public Map<String, Object> changeAcStatus(String gwId, String deviceId, Integer deviceType,String commandType, int value) {
		Map<String,Object> result = Maps.newHashMap();
		SpmsDeviceData spmsDeviceData = loadDeviceNewest(deviceId,deviceType);
		if(spmsDeviceData ==null){
			result.put("status",ErrorCodeConsts.DeviceNotExist);
			return result;
		}
		gwId = spmsDeviceData.getGwMac();

		Integer gwStatus = getGwJoinStatus(gwId);
		if(gwStatus.intValue() != 1){
            result.put("status",ErrorCodeConsts.GatewayNotOnline);
            return  result;
        }
		Map command = Maps.newHashMap();
		int status = spmsDeviceData.getStatus();
		if (status == 0) {
			result.put("status", ErrorCodeConsts.DeviceNotOnline);
			return result;
		}
		try {
			command = CommandUtil.buildCommandMessageMap(CommandUtil.Scope.DEVICE, gwId, spmsDeviceData.getMac(),
					deviceType, commandType, null, value);
			int onStatus = CommandUtil.syncSendMessage(command);
			if (onStatus != 0) {
				try {
					String strStatus = new Integer(onStatus).toString();
					result.put("status", strStatus);
					result.put("msg", ErrorCodeConsts.getValue(strStatus));
				} catch (Exception e) {
					result.put("status", ErrorCodeConsts.UnknowError);
					result.put("msg", ErrorCodeConsts.getValue(ErrorCodeConsts.UnknowError));
					e.printStackTrace();
				}
				return result;
			}

		} catch (Exception e) {
			result.put("status", ErrorCodeConsts.OverTime);
			e.printStackTrace();
			return result;
		}
		result.put("status", ErrorCodeConsts.Success);
		return result;
	}
	
	public Map<String, Object> changeLampStatus(String gwId, String deviceId, Integer[] irgb) {
		Map<String,Object> result = Maps.newHashMap();
		SpmsDeviceData spmsDeviceData = loadDeviceNewest(deviceId,EnumTypesConsts.DeviceType.Dev_Type_Lamp);
		if(spmsDeviceData ==null){
			result.put("status",ErrorCodeConsts.DeviceNotExist);
			return result;
		}
		gwId = spmsDeviceData.getGwMac();

		Integer gwStatus = getGwJoinStatus(gwId);
		if(gwStatus.intValue() != 1){
            result.put("status",ErrorCodeConsts.GatewayNotOnline);
            return  result;
        }
		Map command = Maps.newHashMap();
		int status = spmsDeviceData.getStatus();
		if (status == 0) {
			result.put("status", ErrorCodeConsts.DeviceNotOnline);
			return result;
		}
		try {
			command = CommandUtil.buildHALampControlMessageMap(CommandUtil.Scope.DEVICE, gwId, spmsDeviceData.getMac(),irgb);
			int onStatus = CommandUtil.syncSendMessage(command);
			if (onStatus != 0) {
				try {
					String strStatus = new Integer(onStatus).toString();
					result.put("status", strStatus);
					result.put("msg", ErrorCodeConsts.getValue(strStatus));
				} catch (Exception e) {
					result.put("status", ErrorCodeConsts.UnknowError);
					result.put("msg", ErrorCodeConsts.getValue(ErrorCodeConsts.UnknowError));
					e.printStackTrace();
				}
				return result;
			}

		} catch (Exception e) {
			result.put("status", ErrorCodeConsts.OverTime);
			e.printStackTrace();
			return result;
		}
		result.put("status", ErrorCodeConsts.Success);
		return result;
	}

	public Map<String, Object> changeAcPlugStatus(String gwId, String deviceId, String commandType, String modsig) {
		Map<String, Object> result = Maps.newHashMap();

		Integer gwStatus = getGwJoinStatus(gwId);
		if (gwStatus.intValue() != 1) {
			result.put("success", false);
			result.put("msg", "网关连接失败");
			result.put("status", ErrorCodeConsts.GatewayNotOnline);
			return result;
		}

		SpmsDeviceData spmsDeviceData = loadDeviceNewest(deviceId,EnumTypesConsts.DeviceType.Dev_Type_AC);
		int status = spmsDeviceData.getStatus();
		if (status == 0) {
			result.put("success", false);
			result.put("msg", "空调连接异常");
			result.put("status", ErrorCodeConsts.DeviceNotOnline);
			return result;
		}
		try {
			Map command = CommandUtil.buildIrControlMessageMap(gwId, spmsDeviceData.getMac(),commandType, modsig);			
        	int onStatus = CommandUtil.syncSendMessage(command);        	
			if (onStatus != 0) {				
				try {
					String strStatus = new Integer(onStatus).toString();
					result.put("status", strStatus);
					result.put("msg", ErrorCodeConsts.getValue(strStatus));
				} catch (Exception e) {
					result.put("status", ErrorCodeConsts.UnknowError);
					result.put("msg", ErrorCodeConsts.getValue(ErrorCodeConsts.UnknowError));
					e.printStackTrace();
				}
				return result;
			}
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "连接超时");
			result.put("status", ErrorCodeConsts.OverTime);
			e.printStackTrace();
			
			return result;
		}
		result.put("status", ErrorCodeConsts.Success);
		return result;
	}
	
	public Map<String, Object> updateRcuId(String gwMac,String devMac,String rcuId){
		Map<String, Object> result = Maps.newHashMap();

		Integer gwStatus = getGwJoinStatus(gwMac);
		if (gwStatus.intValue() != 1) {
			result.put("success", false);
			result.put("msg", "网关连接失败");
			result.put("status", ErrorCodeConsts.GatewayNotOnline);
			return result;
		}

		SpmsDeviceData spmsDeviceData = loadDeviceNewest(devMac,EnumTypesConsts.DeviceType.Dev_Type_AC);
		int status = spmsDeviceData.getStatus();
		if (status == 0) {
			result.put("success", false);
			result.put("msg", "空调连接异常");
			result.put("status", ErrorCodeConsts.DeviceNotOnline);
			return result;
		}
		try {
			Map command = CommandUtil.buildRcUpdateMessageMap(gwMac, spmsDeviceData.getMac(),rcuId);			
        	int onStatus = CommandUtil.syncSendMessage(command);
			if (onStatus != 0) {				
				try {
					String strStatus = new Integer(onStatus).toString();
					result.put("status", strStatus);
					result.put("msg", ErrorCodeConsts.getValue(strStatus));
				} catch (Exception e) {
					result.put("status", ErrorCodeConsts.UnknowError);
					result.put("msg", ErrorCodeConsts.getValue(ErrorCodeConsts.UnknowError));
					e.printStackTrace();
				}
				return result;
			}
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "连接超时");
			result.put("status", ErrorCodeConsts.OverTime);
			e.printStackTrace();
			
			return result;
		}
		result.put("status", ErrorCodeConsts.Success);
		return result;
	}
	
	private boolean getSensorInfo(String gwId,String deviceId) {
        boolean flag = true;

        Map command = Maps.newHashMap();
        try {
            command = CommandUtil.buildGetSensorInfoMessageMap(CommandUtil.Scope.DEVICE, gwId, deviceId);
            int status = CommandUtil.syncSendMessage(command);
            if(status == 1){
                flag = true;
            }else{
                flag = false;
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
	
}
