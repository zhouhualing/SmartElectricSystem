package com.harmazing.util;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Config;
import com.harmazing.constant.MessageKey;
import com.harmazing.entity.AirCondition;
import com.harmazing.entity.Device;
import com.harmazing.entity.ZigbeeOO;
import com.harmazing.mq.MQProducerUtil;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceServiceImpl;

public class DeviceLogUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(DeviceLogUtil.class);
	
	public static void deviceInfoHandler(AirCondition ac) {     
		try{
	        MQProducerUtil.sendMapMessage(MessageKey.DEVICE_INFO_PREFIX, ac.toMap());
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
	public static void deviceInfoHandler(Device device) {     
		try{
	        device.setServer(Config.getInstance().SERVER_IP);
	        MQProducerUtil.sendMapMessage(MessageKey.DEVICE_INFO_PREFIX,device.toMap());
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
	/////////////////////////////////////////////////////////////////
	public static void updateAcStatusInfo( DeviceService devSrv, AirCondition ac){
		devSrv.updateACCurrentStatusByACMac(ac.getMac(),
											ac.getMode(), 
											ac.getAcTemp(), 
											ac.getUpDownSwing(),
											ac.getLeftRightSwing(),
											ac.getSpeed(), 
											ac.getOnOff(),
											1);  //operStatus always 1
	}
	/////////////////////////////////////////////////////////////////////////////
	public static Map<String, String> getDeviceInfo(String key){
		Map<String, String> map = null;
		
		RedisContext rc = RedisContextFactory.getInstance().getRedisContext(); 
		
		try{
			map = rc.getDeviceFromRedis(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			RedisContextFactory.getInstance().returnRedisContext(rc);
		}	
		
		return map;
	}
	
	/////////////////////////////////////////////////////////////////////////////
	public static void updateAcInfo( DeviceService devSrv,String mac,String field,String fieldValue){
		devSrv.updateACField(mac,field,fieldValue);
	}
	
	
	/////////////////////////////////////////////////////////////////////////////
	public static void removeDeviceFromRedis(String key){
		RedisContext rc = RedisContextFactory.getInstance().getRedisContext(); 
		try{
			rc.delDeviceFromRedis(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			RedisContextFactory.getInstance().returnRedisContext(rc);
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	public static void updateDeviceInfo(String key, Map<String, String> map){
		RedisContext rc = RedisContextFactory.getInstance().getRedisContext(); 
		try{
			rc.updateDevice2Redis(key, map);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			RedisContextFactory.getInstance().returnRedisContext(rc);
		}
	}
	

	////////////////////////////////////////////////////////////////////////////
	public static void updateAcInfo2Redis( AirCondition ac ){
		String key = RedisContext.DEVICE_CTL_PROP_AC + ac.getMac();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("mac", ac.getMac());
		map.put("onOff", String.valueOf(ac.getOnOff()));
		map.put("operStatus", String.valueOf(ac.getOperStatus()));
		map.put("acTemp", String.valueOf(ac.getAcTemp()));
		map.put("temp", String.valueOf(ac.getTemp()));
		map.put("mode", String.valueOf(ac.getMode()));
		map.put("speed", String.valueOf(ac.getSpeed()));
		map.put("upDownSwing", String.valueOf(ac.getUpDownSwing()));
		map.put("rcuId", String.valueOf(ac.getRcuId()));
		
		updateDeviceInfo(key, map);
	}
	
	/////////////////////////////////////////////////////////////////////////////
	public static void updateAcIntInfo2Redis( String mac,String field, int value){
		String key = RedisContext.DEVICE_CTL_PROP_AC + mac;
		RedisContext rc = RedisContextFactory.getInstance().getRedisContext(); 
		try{
			rc.updateAC2Redis(key, field, String.valueOf(value));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			RedisContextFactory.getInstance().returnRedisContext(rc);
		}
	}	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	public static void updateZigbeeOOIntInfo( DeviceService devSrv,String mac,String field, int value){
		devSrv.updateZigbeeOOStatusByMac( mac,  value,  1); //operStatus always 1
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	public static void updateOOInfo2Redis( ZigbeeOO oo ){
		String key = RedisContext.DEVICE_CTL_PROP_OO + oo.getMac();
		
		 Map<String, String> map = new HashMap<String, String>();
		 map.put("mac", oo.getMac());
		 map.put("onOff", String.valueOf(oo.getOnOff()));
		 map.put("operStatus", "1");
		
		updateDeviceInfo(key, map);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	public static void updateZigbeeOO2Redis( String mac, String field, int value){
		//
		String key = RedisContext.DEVICE_CTL_PROP_OO + mac;		
		RedisContext rc = RedisContextFactory.getInstance().getRedisContext(); 
		try{
			rc.updateZigbeeOO2Redis(key, field, String.valueOf(value));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			RedisContextFactory.getInstance().returnRedisContext(rc);
		}
		
	}
	
	//////////////////////////////////////////////////////////////////
	
	/****************************
	public static void deviceStatusHandler(Device device) {        
        device.setServer(Config.getInstance().SERVER_IP);
        MQProducerUtil.sendMapMessage(MessageKey.DEVICE_STATUS,device.toMap());
    }
	**********************************/
	public static void gwLogHandler(Timestamp timestamp, String deviceId, String userId, Integer on){
		try{
			 HashMap<String, String> map  = new HashMap<String, String>();
		     map.put("timestamp",timestamp.toString().substring(0,19));
		     map.put("deviceId", String.valueOf(deviceId));
		     map.put("userId", String.valueOf(userId));
		     map.put("status", String.valueOf(on));
	         
	//		Config conf = Config.getInstance();
	//		Map m = map;
	//		MongoUtil.insert(conf.MONGO_USER, conf.MONGO_PASSWORD, conf.MONGO_DB, MessageKey.GW_LOG_PREFIX, m);
	
			MQProducerUtil.sendMapMessage(MessageKey.GW_LOG_PREFIX,map);
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	public static void winDoorLogHandler(Timestamp timestamp, String deviceId,boolean on){
		try{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("timestamp", timestamp.toString());
			map.put("deviceId", deviceId);
			map.put("on", String.valueOf(on?1:0));
			
			//insertInto mongodb
	//		Config conf = Config.getInstance();
	//		Map m = map;
	//		MongoUtil.insert(conf.MONGO_USER, conf.MONGO_PASSWORD, conf.MONGO_DB, MessageKey.WD_LOG_PREFIX, m);
			
			MQProducerUtil.sendMapMessage(MessageKey.WD_LOG_PREFIX,map);
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	public static void  AirconLogHandler(Timestamp timestamp, String deviceId,
		Integer currentTemperature, Integer humidity, Integer currentPower,
		Long accumulatePower,Integer reactivePower,Integer reactiveEnergy,
		Integer apparentPower,Integer voltage,Integer current,
		Integer frequency,Integer powerFactor,Timestamp startTime,
		Integer period,Integer activeDemand,Integer reactiveDemand,String eleArea) {
		try{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("timestamp", String.valueOf(timestamp.getTime()));
			map.put("deviceId", deviceId);
			map.put("currentTemperature", String.valueOf(currentTemperature));
			map.put("humidity", String.valueOf(humidity));
			map.put("currentPower", String.valueOf(currentPower));
			map.put("accumulatePower", String.valueOf(accumulatePower));
			map.put("reactivePower", String.valueOf(reactivePower));
			map.put("reactiveEnergy", String.valueOf(reactiveEnergy));
			map.put("apparentPower", String.valueOf(apparentPower));
			map.put("voltage", String.valueOf(voltage));
			map.put("current", String.valueOf(current));
			map.put("frequency", String.valueOf(frequency));
			map.put("powerFactor", String.valueOf(powerFactor));
			map.put("startTime", String.valueOf(startTime.getTime()));
			map.put("period", String.valueOf(period));
			map.put("activeDemand", String.valueOf(activeDemand));
			map.put("reactiveDemand", String.valueOf(reactiveDemand));	
			map.put("eleArea", eleArea);		
			//写入mq
			MQProducerUtil.sendMapMessage(MessageKey.AC_LOG_PREFIX,map);
			
			
			DeviceService dev_srv = new DeviceServiceImpl();
			dev_srv.updateAcTempAndHumidityByAcMac(deviceId, currentTemperature , humidity);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		//insertInto mongodb
//		Config conf = Config.getInstance();
//		Map m = map;
//		m.put("updateTime",timestamp.toString().substring(0,19));
//		MongoUtil.insert(conf.MONGO_USER, conf.MONGO_PASSWORD, conf.MONGO_DB, MessageKey.AC_LOG_PREFIX, m);
		
		/**************************************
		HashMap<String,String> device=new HashMap();
		device.put("id",deviceId);
		device.put("power",String.valueOf(currentPower));
		device.put("startTime",startTime.toString().substring(0,19));
		device.put("accumulatePower",String.valueOf(accumulatePower));	    		
			
		MQProducerUtil.sendMapMessage(MessageKey.DEVICE_INFO_PREFIX,device);
		**************************************************************************/
	}	
	
	public static void gwZigBeeHandler(HashMap<String,String> m){
		try{
			MQProducerUtil.sendMapMessage(MessageKey.GATEWAY_ZIGBEE,m);	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void acZigBeeHandler(HashMap<String,String> m){
		try{
			MQProducerUtil.sendMapMessage(MessageKey.AC_ZIGBEE,m);	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
