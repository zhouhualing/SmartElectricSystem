package com.harmazing.service.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Config;
import com.harmazing.cache.ElecResultKeysCache;
import com.harmazing.entity.ResultData;
import com.harmazing.mapper.DeviceMapper;
import com.harmazing.mapper.LogMapper;
import com.harmazing.mapper.ResultDataMapper;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import com.harmazing.service.LogService;
import com.harmazing.util.UUIDGenerator;

/**
 * Created by ming on 14-9-10.
 */
public class LogServiceImpl extends ServiceImpl implements LogService {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);
    
//    @Override
//    public void appendAirconLogs(Stack<Map<String,String>> logs){
//    	SqlSession session = sqlSessionFactory.openSession(false);
//    	LogMapper logMapper = session.getMapper(LogMapper.class);
//		DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
//		Map<String,Map<String,String>> updateDevices = new HashMap<String,Map<String,String>>();
//		List lst=new ArrayList();
//		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	try{
//    		if(logs!=null&&logs.size()>0){
//		    	for(int i = logs.size(); i>0; i--){
//		    		 Map<String, String> log=logs.pop();
//		    		 long timestamp = Long.parseLong(log.get("timestamp"));
//		    		 /*************************************		    		
//		    		 String deviceId = log.get("deviceId");
//		    		 String currentTemperature = log.get("currentTemperature");
//		    		
//		    		 String accumulatePower = log.get("accumulatePower");		    		 
//		    		 Integer reactivePower = Integer.parseInt(log.get("reactivePower"));
//		    		 Integer reactiveEnergy = Integer.parseInt(log.get("reactiveEnergy"));
//		    		 Integer apparentPower = Integer.parseInt(log.get("apparentPower"));
//		    		 Integer voltage = Integer.parseInt(log.get("voltage"));
//		    		 Integer current = Integer.parseInt(log.get("current"));
//		    		 Integer frequency = Integer.parseInt(log.get("frequency"));
//		    		 Integer powerFactor = Integer.parseInt(log.get("powerFactor"));		    		 
//		    		 Integer period = Integer.parseInt(log.get("period"));
//		    		 Integer activeDemand = Integer.parseInt(log.get("activeDemand"));
//		    		 Integer reactiveDemand = Integer.parseInt(log.get("reactiveDemand"));
//		    		 //String areaId = (String)(log.get("elecArea"));
//		    		 *******************************************************/
//		    		 long startTime = Long.parseLong(log.get("startTime"));
//		    		 log.put("currentTime",df.format(new Timestamp(timestamp)));
//		    		 String stString = "";
//		    		 if(startTime==0){
////		    			 period = null;
////		    			 activeDemand = null;
////		    			 reactiveDemand = null;
//		    			 log.put("period",null);
//		    			 log.put("activeDemand",null);
//		    			 log.put("reactiveDemand",null);
//		    		 }else{
//		    			 stString = startTime+"";
//			    		 stString = stString.substring(0,stString.length()-3)+"000";			    		
//		    		 }
//		    		 log.put("startTime",df.format(new Timestamp(startTime)));
//		             int onOff = 1;
//		             Integer currentPower = Integer.parseInt(log.get("currentPower"));
//		             if(currentPower.intValue() <= 0){
//		             	onOff = 0;
//		             		             	
//		             }	
//		             log.put("onOff",onOff+"");	
//		             log.put("id",UUIDGenerator.randomUUID());
//		             lst.add(log);
////		             logMapper.insertAirconShortLog(UUIDGenerator.randomUUID(), deviceId, onOff, Integer.parseInt(currentTemperature), currentPower , Long.parseLong(accumulatePower), new Timestamp(timestamp),
////		            		 reactivePower,reactiveEnergy,apparentPower,voltage,current,frequency,powerFactor, startTime==0?null:new Timestamp(Long.parseLong(stString)) ,
////		            		 period,activeDemand,reactiveDemand);
//		             String deviceId = log.get("deviceId");
//		             if(updateDevices.get(deviceId) == null){
//		            	 updateDevices.put(deviceId, log);
//		             }else{
//		            	 Map<String,String> map = updateDevices.get(deviceId);
//		            	 long lasttime = Long.parseLong(map.get("timestamp"));
//		            	 if(timestamp > lasttime){
//		            		 updateDevices.put(deviceId, log);
//		            	 }
//		             }
//		    	}
//    		}
//    		System.out.println("log size"+lst.size());
//    		//批量插入日志
//    		logMapper.batchInsertACLog(lst);
//    		
//	    	Set<String> keys = updateDevices.keySet();
//	    	for(String key : keys){
//	    		Map<String,String> map = updateDevices.get(key);
//	    		String deviceId = map.get("deviceId");
//	    		Integer power = Integer.parseInt(map.get("currentPower"));
//	    		long time = Long.parseLong(map.get("timestamp"));
//	    		String accumulatePower = map.get("accumulatePower");	    		
//	    		deviceMapper.updateByDevice(null, null, null, null, null, power, null, null, new Timestamp(time), null, null, Long.parseLong(accumulatePower), null, deviceId);
//	    	}
//	    	session.commit();
//    	}catch(Exception e){
//    		LOGGER.info("APPENDAIRCONLOGS", e);
//    		session.rollback(true);
//    	}finally{
//    		session.close();
//    	}
//    	
//    }
    /**
     * 根据区域与时间批量插入数据
     * @param areaMap
     */
    public void appendAirResultData(Stack<Map<String,String>> logs){
    	
    	Map<String, List<ResultData>> areaMap = new HashMap<String, List<ResultData>>();
    	if(logs!=null&&logs.size()>0){
	    	for(int i = logs.size(); i>0; i--){
	    		 Map<String, String> log=logs.pop();
	    		 long timestamp = Long.parseLong(log.get("timestamp"));
	    		 String deviceId = log.get("deviceId");
	    		 String currentTemperature = log.get("currentTemperature");
	    		 Integer currentPower = Integer.parseInt(log.get("currentPower"));
	    		 Integer accumulatePower = Integer.parseInt(log.get("accumulatePower"));
	    		 Integer reactivePower = Integer.parseInt(log.get("reactivePower"));
	    		 Integer reactiveEnergy = Integer.parseInt(log.get("reactiveEnergy"));
	    		 Integer apparentPower = Integer.parseInt(log.get("apparentPower"));
	    		 Integer voltage = Integer.parseInt(log.get("voltage"));
	    		 Integer current = Integer.parseInt(log.get("current"));
	    		 Integer frequency = Integer.parseInt(log.get("frequency"));
	    		 Integer powerFactor = Integer.parseInt(log.get("powerFactor"));
	    		 long startTime = Long.parseLong(log.get("startTime"));
	    		 Integer period = Integer.parseInt(log.get("period"));
	    		 Integer activeDemand = Integer.parseInt(log.get("activeDemand"));
	    		 Integer reactiveDemand = Integer.parseInt(log.get("reactiveDemand"));
//	    		 String areaId = deviceMapper.findEleAreaByDeviceId(deviceId);
	    		 String areaId=log.get("eleArea").toString();
	    		 String stString = "";
	    		 if(startTime==0){
	    			 period = null;
	    			 activeDemand = null;
	    			 reactiveDemand = null;
	    		 }else{
	    			 stString = startTime+"";
		    		 stString = stString.substring(0,stString.length()-3)+"000";
	    		 }
	    		 long timestampSencond = parseSecond(timestamp);
	             if(areaMap.containsKey(areaId+timestampSencond)){
	            	 List<ResultData> resultDataList= areaMap.get(areaId+timestampSencond);
	            	 ResultData rd = new ResultData(areaId, timestampSencond, currentPower, reactivePower, 
	            			 powerFactor, apparentPower, reactiveEnergy, reactiveDemand, 
	            			 activeDemand,startTime,accumulatePower);
	            	 resultDataList.add(rd);
	            	 //areaMap.put(areaId+timestampSencond, resultDataList);
	             }else{
	            	 List<ResultData> resultDataList = new ArrayList<ResultData>();
	            	 ResultData rd = new ResultData(areaId, timestampSencond, currentPower, reactivePower, 
	            			 powerFactor, apparentPower, reactiveEnergy, reactiveDemand, activeDemand,
	            			 startTime,accumulatePower);
	            	 resultDataList.add(rd);
	            	 areaMap.put(areaId+timestampSencond, resultDataList);
	             }
	             
	    	}
    	}
    	updateResultData(areaMap);
    }
    public void updateResultData(Map<String, List<ResultData>> areaMap){
    	SqlSession session =sqlSessionFactory.openSession(false);
//    	DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
    	String ukey=UUIDGenerator.randomUUID();
    	try {
			for(Entry en : areaMap.entrySet()){
				Integer power = 0;
				Integer reactivePower = 0;
				Integer powerFactor = 0;
				Integer apparentPower = 0;
				Integer reactiveEnergy = 0;
				Integer reactiveDemand = 0;
				Integer activeDemand = 0;
				Integer deviceNum = 0;
				List<ResultData> resultDataList = (List<ResultData>)(en.getValue());
				long startTime = resultDataList.get(0).getStartTime();
				long demandTime = resultDataList.get(0).getDemandTime();
				String areaId = resultDataList.get(0).getAreaId();
				String stString = "";
				if(demandTime==0){
					activeDemand = null;
					reactiveDemand = null;
				}else{
					stString = demandTime+"";
					stString = stString.substring(0,stString.length()-3)+"000";
				}				
				for(ResultData rd : resultDataList){
					power = +rd.getPower();
					reactivePower = +rd.getReactivePower();
					powerFactor = + rd.getPowerFactor();
					apparentPower = + rd.getApparentPower();
					reactiveEnergy = + rd.getReactiveEnergy();
					reactiveDemand = + (rd.getReactiveDemand()==null?0:rd.getReactiveDemand());
					activeDemand = + (rd.getActiveDemand()==null?0:rd.getActiveDemand());
				}
				deviceNum = resultDataList.size();				
		    	ResultDataMapper resultDataMapper = session.getMapper(ResultDataMapper.class);
		    	String key=areaId+"-"+startTime+"-"+ukey;
		    	List ulst=new ArrayList();
		    	if(ElecResultKeysCache.get(key)!=null){
		    		String uuid=ElecResultKeysCache.get(key);
		    		Map para=new HashMap();
		    		para.put("id",uuid);
		    		para.put("areaId",areaId);		    		
					para.put("start_time",new Timestamp(startTime));
					para.put("power",power);
					para.put("reactive_power",reactivePower);
					para.put("power_factor",powerFactor/resultDataList.size());
					para.put("apparent_power", apparentPower);
					para.put("reactive_energy", reactiveEnergy);
					para.put("reactive_demand",reactiveDemand);
					para.put("active_demand",activeDemand);
					//para.put("demandTime",demandTime);
					para.put("deviceNum",deviceNum);
//		    		resultDataMapper.updateResutData(UUIDGenerator.randomUUID(), areaId, 
//							new Timestamp(startTime), power, 
//		    				 reactivePower, powerFactor/resultDataList.size(), apparentPower, reactiveEnergy, reactiveDemand, activeDemand,demandTime==0?null:new Timestamp(Long.parseLong(stString)),deviceNum);
					resultDataMapper.updateResutData(para);
//					System.out.println(new Timestamp(startTime).toString()+"******update result data "+deviceNum);
		    	}else{
		    		String uuid=UUIDGenerator.randomUUID();
		     		resultDataMapper.insertResutData(uuid, areaId, 
						new Timestamp(startTime), power, 
			    				 reactivePower, powerFactor/resultDataList.size(), apparentPower, reactiveEnergy, reactiveDemand, activeDemand,demandTime==0?null:new Timestamp(Long.parseLong(stString)),deviceNum);
		     		ElecResultKeysCache.addKey(key,uuid,Config.getInstance().CLIENT_REPORT_INTERVAL*5);
//		     		System.out.println(new Timestamp(startTime).toString()+"******insert result data "+deviceNum);
		    	}							
			}			
			session.commit();
		} catch (NumberFormatException e) {
			session.rollback();
			e.printStackTrace();
			LOGGER.error("computer result data error",e);
		}finally{
			session.close();
		}    	
    }
    /*@Override
    public void appendAirconLog(Timestamp timestamp, String airconId, Integer currentTemperature, Integer currentPower, Long accumulatePower) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            LogMapper logMapper = session.getMapper(LogMapper.class);
            int onOff = 1;
            if(currentPower.intValue() <= 0){
            	onOff = 0;
            }
            logMapper.insertAirconShortLog(UUIDGenerator.randomUUID(), airconId, onOff, currentTemperature, currentPower, accumulatePower, timestamp);
            session.commit();
        } catch (Exception e) {
            LOGGER.info("APPENDAIRCONLOG", e);
        } finally {
            session.close();
        }
    }*/

    /*@Override
    public void appendAirconLog(Timestamp timestamp, String airconId, Integer currentTemperature, Integer targetTemperature, Integer currentPower, Long accumulatePower, Integer mode, Integer speed, Integer direction) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            LogMapper logMapper = session.getMapper(LogMapper.class);
            int onOff = 1;
            if(currentPower.intValue() <= 0){
            	onOff = 0;
            }
            logMapper.insertAirconLongLog(UUIDGenerator.randomUUID(), airconId, onOff, currentTemperature, targetTemperature, currentPower, accumulatePower, mode, speed, direction, timestamp);
            session.commit();
        } catch (Exception e) {
            LOGGER.info("APPENDAIRCONLOG", e);
        } finally {
            session.close();
        }
    }*/
    
    @Override
    public void appendWinDoorLogs(List<Map<String,String>> logs){
    	SqlSession session = sqlSessionFactory.openSession(false);
    	try{
    		LogMapper logMapper = session.getMapper(LogMapper.class);
    		
    		Map<String,Map<String,String>> updateDevices = new HashMap<String,Map<String,String>>();
	    	for(Map<String,String> log : logs){
	    		 long timestamp = Long.parseLong(log.get("timestamp"));
	    		 String deviceId = log.get("deviceId");
	    		 String on = log.get("on");
	             logMapper.insertWinDoorLog(UUIDGenerator.randomUUID(), deviceId, (Boolean.valueOf(on) ? 1: 0), new Timestamp(timestamp));
	    	}	    	
	    	
	    	session.commit();
    	}catch(Exception e){
    		LOGGER.info("APPENDAIRCONLOGS", e);
    		session.rollback(true);
    	}finally{
    		session.close();
    	}
    }

    @Override
    public void appendWinDoorLog(Timestamp timestamp, String deviceId, boolean on) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            LogMapper logMapper = session.getMapper(LogMapper.class);
            logMapper.insertWinDoorLog(UUIDGenerator.randomUUID(), deviceId, (on ? 1: 0), timestamp);
            session.commit();
        } catch (Exception e) {
            LOGGER.info("APPENDWINDOORLOG", e);
        } finally {
            session.close();
        }
    }
    
   @Override
    public void appendGatewayLogs(List<Map<String,String>> logs){
    	SqlSession session = sqlSessionFactory.openSession(false);
    	try{
    		LogMapper logMapper = session.getMapper(LogMapper.class);
    		if(logs!=null&&logs.size()>0){
		    	for(Map<String,String> log : logs){
		    		 String timestamp = log.get("timestamp");
		    		 String deviceId = log.get("deviceId");
		    		 String userId = log.get("userId");
		    		 Integer on = Integer.parseInt(log.get("on"));	    		 
		             logMapper.insertGatewayLog(UUIDGenerator.randomUUID(), deviceId, on, userId, new Timestamp(Long.parseLong(timestamp)));
		    	}
		    	session.commit();
    		}
    	}catch(Exception e){
    		LOGGER.info("APPENDAIRCONLOGS", e);
    		session.rollback(true);
    	}finally{
    		session.close();
    	}
    }
    /*@Override
    public void appendGatewayLogs(List<Map<String,String>> logs){
    	SqlSession session = sqlSessionFactory.openSession(false);
    	批量存入数据库的队列
    	List<Map<String, Object>> dbList = new ArrayList<Map<String, Object>>();
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	try{
    		LogMapper logMapper = session.getMapper(LogMapper.class);
    		if(logs!=null&&logs.size()>0){
		    	for(Map<String,String> log : logs){
		    		 map.put("id", UUIDGenerator.randomUUID());
		    		 map.put("deviceId", log.get("deviceId"));
		    		 map.put("status", Integer.parseInt(log.get("on")));
		    		 map.put("userId", log.get("userId"));
		    		 //map.put("timestamp", new Timestamp(Long.parseLong(log.get("timestamp"))));
		    		 map.put("timestamp", new Timestamp(Long.parseLong(log.get("timestamp"))));
		    		 dbList.add(map);
		    	}
		    	logMapper.batchInsertGWLog(dbList);
		    	session.commit();
    		}
    	}catch(Exception e){
    		LOGGER.info("APPENDAIRCONLOGS", e);
    		session.rollback(true);
    	}finally{
    		session.close();
    	}
    }*/
    
    @Override
    public void appendGatewayStatus(Timestamp timestamp, String deviceId, String userId, Integer on) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            LogMapper logMapper = session.getMapper(LogMapper.class);
            logMapper.insertGatewayLog(UUIDGenerator.randomUUID(), deviceId, on, userId, timestamp);
            session.commit();
        } catch (Exception e) {
            LOGGER.info("APPENDGATEWAYSTATUS", e);
        } finally {
            session.close();
        }
    }
    /**
     * 将毫秒时间转为秒
     * @param time
     * @return
     */
    public long parseSecond(long time){
    	String stString = time+"";
		stString = stString.substring(0,stString.length()-3)+"000";
    	return Long.parseLong(stString);
    }
    @Override
    public void appendAirconLogs(List<Map<String,String>> logs){    	
    	SqlSession session = sqlSessionFactory.openSession(false);
    	//用于存放aclog，将其批量存入数据库中
    	List<Map<String,Object>> logList = new ArrayList<Map<String,Object>>();
    	try{
    		LogMapper logMapper = session.getMapper(LogMapper.class);
    		//ResultDataMapper resultDataMapper = session.getMapper(ResultDataMapper.class);
//    		DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
    		Map<String,Map<String,String>> updateDevices = new HashMap<String,Map<String,String>>();
	    	for(int i=logs.size()-1;i>=0;i--){
	    		Map<String, String> log=logs.remove(i);
	    		if(log==null){
	    			continue;
	    		}
	    		 Map<String, Object> dbMap = new HashMap<String, Object>();
	    		 long timestampLong=0L;
	    		 try{
	    			 timestampLong = Long.parseLong(log.get("timestamp"));	 
	    		 }catch(Exception e){
	    			 e.printStackTrace();
	    			 LOGGER.error("************"+log.get("timestamp")+"********************");
	    		 }
	    		 Timestamp timestamp = new Timestamp(Long.parseLong(log.get("timestamp")));
	    		 String deviceId = log.get("deviceId");
	    		 Integer currentTemperature = Integer.parseInt(log.get("currentTemperature"));
	    		 Integer humidity = Integer.parseInt(log.get("humidity"));
	    		 Integer currentPower = Integer.parseInt(log.get("currentPower"));
	    		 Long accumulatePower = Long.parseLong(log.get("accumulatePower"));
	    		 Integer reactivePower = Integer.parseInt(log.get("reactivePower"));
	    		 Integer reactiveEnergy = Integer.parseInt(log.get("reactiveEnergy"));
	    		 Integer apparentPower = Integer.parseInt(log.get("apparentPower"));
	    		 Integer voltage = Integer.parseInt(log.get("voltage"));
	    		 Integer current = Integer.parseInt(log.get("current"));
	    		 Integer frequency = Integer.parseInt(log.get("frequency"));
	    		 Integer powerFactor = Integer.parseInt(log.get("powerFactor"));
	    		 long startTime = Long.parseLong(log.get("startTime"));
	    		 Integer period = Integer.parseInt(log.get("period"));
	    		 Integer activeDemand = Integer.parseInt(log.get("activeDemand"));
	    		 Integer reactiveDemand = Integer.parseInt(log.get("reactiveDemand"));
//	    		 String areaId = deviceMapper.findEleAreaByDeviceId(deviceId);
	    		 String areaId=log.get("eleArea");
	    		 String stString = "";
	    		 if(startTime==0){
	    			 period = null;
	    			 activeDemand = null;
	    			 reactiveDemand = null;
	    		 }else{
	    			 stString = startTime+"";
		    		 stString = stString.substring(0,stString.length()-3)+"000";
	    		 }
	    		 Timestamp startTimestamp = startTime==0?null:new Timestamp(Long.parseLong(stString));
	             int onOff = 1;
	             if(currentPower.intValue() <= 0){
	             	onOff = 0;
	             }	             
	             dbMap.put("id", UUIDGenerator.randomUUID());
	             dbMap.put("deviceId", deviceId);
	             dbMap.put("onOff", onOff);
	             dbMap.put("currentTemperature", currentTemperature);
	             dbMap.put("humidity", humidity);
	             dbMap.put("power", currentPower);
	             dbMap.put("accumulatePower", accumulatePower);
	             dbMap.put("currentTime", timestamp);
	             dbMap.put("reactivePower", reactivePower);
	             dbMap.put("reactiveEnergy", reactiveEnergy);
	             dbMap.put("apparentPower", apparentPower);
	             dbMap.put("voltage", voltage);
	             dbMap.put("current", current);
	             dbMap.put("frequency", frequency);
	             dbMap.put("powerFactor", powerFactor);
	             dbMap.put("startTime", startTimestamp);
	             dbMap.put("activeDemand", activeDemand);
	             dbMap.put("reactiveDemand", reactiveDemand);
	             logList.add(dbMap);
	          	             
	             //如果设备有更新加入到更新设备列表
	             if(updateDevices.get(deviceId) == null){
	            	 updateDevices.put(deviceId, log);
	             }else{
	            	 Map<String,String> map = updateDevices.get(deviceId);
	            	 long lasttime = Long.parseLong(map.get("timestamp"));
	            	 if(timestampLong > lasttime){
	            		 updateDevices.put(deviceId, log);
	            	 }
	             }
	            
	    	}
	    	
	    	/*********************
	    	Set<String> keys = updateDevices.keySet();
	    	RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();	           
	    	for(String key : keys){
	    		Map<String,String> map = updateDevices.get(key);
	    		Map<String,String> device=new HashMap();
	    		device.put("id",map.get("deviceId"));
	    		device.put("power",map.get("currentPower"));
	    		device.put("startTime",map.get("timestamp"));
	    		device.put("accumulatePower",map.get("accumulatePower"));	    		
	    		//deviceMapper.updateByDevice(null, null, null, null, null, power, null, null, new Timestamp(time), null, null, Long.parseLong(accumulatePower), null, deviceId);
	    		//把更新的设备放入redis缓存由独立的线程统一更新
	    		redisContext.setDeviceInfo(UUID.randomUUID().toString(), device, 60 * 60);		           
	    	}
	    	 redisContext.destroy();
	    	******************************************/
	    	//批量存储日志信息	
	    	logMapper.batchInsertACLog(logList);
	    	LOGGER.info("log size "+logList.size());
	    	session.commit();
    	}catch(Exception e){
    		LOGGER.error("APPENDAIRCONLOGS", e);
    		session.rollback(true);
    	}finally{
    		session.close();
    	}
    }
    public static void main(String[] args) {
    	long l=1111111111111L;
    	l=l/1000*1000;
    	System.out.println(l);
	}


}
