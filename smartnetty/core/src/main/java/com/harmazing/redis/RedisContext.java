package com.harmazing.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.jms.JMSException;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.constant.MessageKey;
import com.harmazing.mq.MQProducerUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;


public class RedisContext {
    public Jedis jedis;

    public final static Logger LOGGER = LoggerFactory.getLogger(RedisContext.class);

    public final static String USER_GATEWAY_KEY_PREFIX     = "USER_GATEWAY:";
    public final static String USER_KEY_PREFIX             = "USER:";
    public final static String GATEWAY_KEY_PREFIX          = "GATEWAY:";
    public static final String USER_SERVICE_KEY_PREFIX     = "USER_SERVICE:";
    public static final String MAC_SN_GATEWAY_PREFIX       = "MAC_SN_GATEWAY_PREFIX";
    public static final String MAC_SN_SENSOR_PREFIX        = "MAC_SN_SENSOR_PREFIX";
    public static final String MESSAGE_KEY_PREFIX          = "MESSAGE:";
    public static final String AC_LOG_PREFIX               = "AC_LOG:";
    public static final String WD_LOG_PREFIX               = "WD_LOG:";
    public static final String GW_LOG_PREFIX               = "GW_LOG:";
    public static final String DEVICE_INFO_PREFIX          = "DEVICE_INFO:";
    public static final String Device_DSM_PREFIX		   = "DSM:";
    public static final String OPEN_NETWORK_STATUS_PREFIX  = "OPEN_NETWORK:";

    /**
     * 基础信息前缀
     */
    public static final String BASE_INFO_PREFIX            = "BASE:";
    
    /**
     * Device
     */
    public static final String DEVICE_CTL_PROP             = "NT:DEV_CTL_PROP_";
    public static final String DEVICE_CTL_PROP_AC          = "NT:DEV_CTL_PROP_AC_";
    public static final String DEVICE_CTL_PROP_SOCKET      = "NT:DEV_CTL_PROP_SOCKET_";
    public static final String DEVICE_CTL_PROP_TS          = "NT:DEV_CTL_PROP_TS_";
    public static final String DEVICE_CTL_PROP_OO		   = "NT:DEV_CTL_PROP_OO_";
    
    /**
     * 基础信息 GW前缀
     */
    public static final String BASE_INFO_GATEWAY           = "GW:";

    public RedisContext(Jedis resource) {
        this.jedis = resource;
    }

    /**
     * 连接Jedis
     */
    public void connect() {
        if(!this.jedis.isConnected()) {
            this.jedis.connect();
        }
    }

    //////////////////////////////////////////////////////////
    public Set<String> keys(final String pattern) {
    	return this.jedis.keys(pattern);
    }
    
	//////////////////////////////////////////////////////////
	public void delDeviceFromRedis(String key){
		this.jedis.del(key);
	}
	
    //////////////////////////////////////////////////////////
    public void addDevice2Redis(String[] keysvalues){
    	this.jedis.mset(keysvalues);
    }
    
    //////////////////////////////////////////////////////////
    public void addDevice2Redis(String key, Map<String, String> value){
    	this.jedis.hmset(key,  value);
    }
    
	//////////////////////////////////////////////////////////
	public void updateDevice2Redis(String key, Map<String, String> value){
		this.jedis.hmset(key,  value);
	}


	//////////////////////////////////////////////////////////
	public void updateAC2Redis(String key, String field, String value){
		this.jedis.hset(key, field, value);
		/*
		String result = this.jedis.get(key);
		try{
			JSONObject json_obj = new JSONObject(result);
			Map<String, String> map = new HashMap<String,String>();			
			
			map.put("mac", json_obj.getString("mac"));
			map.put("onOff", json_obj.getString("onOff"));
			map.put("operStatus", json_obj.getString("operStatus"));
			map.put("acTemp", json_obj.getString("acTemp"));
			map.put("temp", json_obj.getString("temp"));
			map.put("mode", json_obj.getString("mode"));
			map.put("speed", json_obj.getString("speed"));
			map.put("upDownSwing", json_obj.getString("upDownSwing"));
			map.put("rcuId", json_obj.getString("rcuId"));
			map.put(field, value);
			
			json_obj = new JSONObject(map);
			this.jedis.set(key, json_obj.toString());			
		}catch(Exception e){
			e.printStackTrace();
		}*/
	}
	
	//////////////////////////////////////////////////////////
	public void updateZigbeeOO2Redis(String key, String field, String value){
		this.jedis.hset(key, field, value);
		/*
		String result = this.jedis.get(key);
		try{
			JSONObject json_obj = new JSONObject(result);
			Map<String, String> map = new HashMap<String,String>();			
			
			map.put("mac", json_obj.getString("mac"));
			map.put("onOff", value);
			map.put("operStatus", value);
						
			json_obj = new JSONObject(map);
			this.jedis.set(key, json_obj.toString());			
		}catch(Exception e){
			e.printStackTrace();
		}*/
	}
	
	//////////////////////////////////////////////////////////
	public Map<String, String> getDeviceFromRedis(String key){
		return this.jedis.hgetAll(key);
	}
	
	//////////////////////////////////////////////////////////
	public Map<String, String> getIftttStrategyFromRedis(String key){
		return this.jedis.hgetAll(key);
	}
	
	//////////////////////////////////////////////////////////
	public void delDeviceFromRedisServer(){
		String part = DEVICE_CTL_PROP + "*";
		Set<String> keys = this.jedis.keys(part);
		if(keys.size() >0){
			long count = jedis.del(keys.toArray(new String[keys.size()]));
			LOGGER.debug("Clear Redis server, count = " + count);
		}
	}
    /**
     * Jedis Publish
     * @param channel
     * @param message
     */
    public void publish(String channel, String message) {
        connect();
        LOGGER.info("publish channel:" + channel + ",\t message:" + message);
        jedis.publish(channel, message);
    }

    public void setMessage(String key, Map<String, String> message, int timeout) {
        key = MESSAGE_KEY_PREFIX + key;
        this.jedis.hmset(key, message);
        this.jedis.expire(key, timeout);
    }

    public void setAcLog(String key, Map<String, String> log, int timeout) {
        key = AC_LOG_PREFIX + key;
        this.jedis.hmset(key, log);
        this.jedis.expire(key, timeout);
    }
   
    public void setBaseGW(List<String> keys, List<Map> message, int timeout) {
        Pipeline pipeline = jedis.pipelined();
        if(keys.size()>0&&message.size()>0){
	        for(int i=0; i<keys.size(); i++) {
	        	if(message.get(i)==null){
	        		LOGGER.info("*************************"+message.get(i-1).get("gwId")+"***********************");
	        	}
	        	try {
	        		pipeline.hmset(BASE_INFO_PREFIX +BASE_INFO_GATEWAY+keys.get(i), message.get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
	            
	        }
        }
        pipeline.sync();
    }
    
    /*
     * 删除网关在redis中的信息
     */
    public void delBaseGW() {    	
        String part=BASE_INFO_PREFIX +BASE_INFO_GATEWAY+"*";
        Set<String> keys=this.jedis.keys(part);
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            this.jedis.del(key);
            
        }        
     }
    
    
    //////////////////////////////////////////////////////////
     /**
      * 删除dsm在redis中的信息
      */
    public void delDsmInfo(){
    	String part=this.Device_DSM_PREFIX+"*";
    	Set<String> keys=this.jedis.keys(part);
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            this.jedis.del(key);
        }        
    }
    
    /**
     * 更新网关信息
     * @param key
     * @return
     */
    public void updateBaseGW(String key,Map message) {
//        Pipeline pipeline = jedis.pipelined();        
    	try {
//    		pipeline.hmset(BASE_INFO_PREFIX +BASE_INFO_GATEWAY+key, message);
    		this.jedis.hmset(BASE_INFO_PREFIX +BASE_INFO_GATEWAY+key, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
//        pipeline.sync();
    }

    /*public Map<String,String> getBaseGW(String key) {
        key = BASE_INFO_PREFIX +BASE_INFO_GATEWAY+key;
        try {
        	return this.jedis.hgetAll(key);
		} catch (Exception e) {
			return null;
		}
        
    }*/
    
    public List<Map<String,String>> getBaseGW(String key) {
    	key = BASE_INFO_PREFIX +BASE_INFO_GATEWAY+key+"*";
    	Set<String> keys = this.jedis.keys(key);
    	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key0 = iterator.next();
            list.add(this.jedis.hgetAll(key0));
        }
        return list;
    }
    
    public Map<String, String> getDevicesByGw(String gw_mac){
    	return this.jedis.hgetAll( BASE_INFO_PREFIX +BASE_INFO_GATEWAY +gw_mac);
    }
        
    public void getAcLog(String key) {
        this.jedis.hgetAll(AC_LOG_PREFIX + key);
    }

    public  List<Map<String, String>> getAcLogList() {
	        Set<String> keys = this.jedis.keys(AC_LOG_PREFIX +"*");
	        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	        Iterator<String> iterator = keys.iterator();
	        int i=0;
	        while (iterator.hasNext()) {
	            String key = iterator.next();
	            Map<String,String> m=this.jedis.hgetAll(key);
	            this.jedis.del(key);
	            if(m.keySet().size()!=0){
	            	i++;
	            	if(i>1000){
	            		break;
	            	}
	            	list.add(m);
	            }            
	        }
	        return list;
    }
  
    
    public void setWdLog(String key, Map<String, String> log, int timeout) {
        key = WD_LOG_PREFIX + key;
        this.jedis.hmset(key, log);
        this.jedis.expire(key, timeout);
    }

    public void getWdLog(String key) {
        this.jedis.hgetAll(WD_LOG_PREFIX + key);
    }

    public List<Map<String, String>> getWdLogList() {
        Set<String> keys = this.jedis.keys(WD_LOG_PREFIX + "*");
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            list.add(this.jedis.hgetAll(key));
            this.jedis.del(key);
        }
        return list;
    }
    
    public void setGwLog(String key, Map<String, String> log, int timeout) {
        key = GW_LOG_PREFIX + key;
        this.jedis.hmset(key, log);
        this.jedis.expire(key, timeout);
    }
	
    
    public void getGwLog(String key) {
        this.jedis.hgetAll(GW_LOG_PREFIX + key);
    }

    public List<Map<String, String>> getGwLogList() {
        Set<String> keys = this.jedis.keys(GW_LOG_PREFIX + "*");
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            list.add(this.jedis.hgetAll(key));
            this.jedis.del(key);
        }
        return list;
    }

    public void setDeviceInfo(String key, Map<String, String> deviceInfo, int timeout) {
        key = DEVICE_INFO_PREFIX + key;
        this.jedis.hmset(key, deviceInfo);
        this.jedis.expire(key, timeout);
    }

    public void getDeviceInfo(String key) {
        this.jedis.hgetAll(DEVICE_INFO_PREFIX + key);
    }

    public List<Map<String, String>> getDeviceInfoList() {
        Set<String> keys = this.jedis.keys(DEVICE_INFO_PREFIX + "*");
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            list.add(this.jedis.hgetAll(key));
            this.jedis.del(key);
        }
        return list;
    }

    public Map<String, String> getMessage(String key) {
        return this.jedis.hgetAll(MESSAGE_KEY_PREFIX + key);
    }

    public void delMessage(String key) {
        this.jedis.del(MESSAGE_KEY_PREFIX + key);
    }


    /**
     * 增加用户
     * @param context
     * @param map
     */
    public static void setUser(RedisContext context, Map<String, String> map) {
        String uid = map.get("id");
        if(uid == null || "".equals(uid)) {
            throw new IllegalArgumentException("id is not found!");
        }
        String key = USER_KEY_PREFIX + uid;
        context.jedis.hmset(key, map);
    }

    /**
     * 获取用户
     * @param context
     * @param uid
     * @return
     */
    public static Map<String, String> getUser(RedisContext context, String uid) {
        String key = USER_KEY_PREFIX + uid;
        return context.jedis.hgetAll(key);
    }

    /**
     * 获取用户的网关
     * @param context
     * @param uid
     * @return
     */
    public static List<String> getUserGateway(RedisContext context, String uid) {
        String key = USER_GATEWAY_KEY_PREFIX + uid;
        long len = context.jedis.llen(key);
        return context.jedis.lrange(key, 0, len - 1);
    }

    /**
     * 获取用户的网关
     * @param context
     * @param uid
     * @param start
     * @param end
     * @return
     */
    public static List<String> getUserGateway(RedisContext context, String uid, long start, long end) {
        String key = USER_GATEWAY_KEY_PREFIX + uid;
        return context.jedis.lrange(key, start, end);
    }

    /**
     * 给用户添加Gateway
     * @param context
     * @param uid
     * @param gatewayIds
     * @return
     */
    public static Long pushUserGateway(RedisContext context, String uid, final String... gatewayIds) {
        String key = USER_GATEWAY_KEY_PREFIX + uid;
        return context.jedis.lpush(key, gatewayIds);
    }

    /**
     * SET GATEWAY
     * @param context
     * @param map
     */
    public static String setGateway(RedisContext context, Map<String, String> map) {
        String id = map.get("id");
        if(id == null || "".equals(id)) {
            throw new IllegalArgumentException("id is not found!");
        }
        String key = GATEWAY_KEY_PREFIX + id;
        return context.jedis.hmset(key, map);
    }

    /**
     * 获取网关
     * @param context
     * @param id
     * @return
     */
    public static Map<String, String> getGateway(RedisContext context, String id) {
        String key = GATEWAY_KEY_PREFIX + id;
        return context.jedis.hgetAll(key);
    }

    /**
     * 删除网关
     * @param context
     * @param id
     * @return
     */
    public static Long delGateway(RedisContext context, String id) {
        String key = GATEWAY_KEY_PREFIX + id;
        return context.jedis.del(key);
    }

    /**
     * 获取用户的服务
     * @param context
     * @param uid
     * @return
     */
    public static List<String> getUserService(RedisContext context, String uid) {
        String key = USER_KEY_PREFIX + uid;
        long len = context.jedis.llen(key);
        return context.jedis.lrange(key, 0, len - 1);
    }

    /**
     * 获取网关Session
     * @param context
     * @param gwid
     * @return
     */
    public static String getGatewaySession(RedisContext context, String gwid) {
        String key = GATEWAY_KEY_PREFIX + gwid;
        return context.jedis.hget(key, "session");
    }

    /**
     * 设置网关Session
     * @param context
     * @param gwid
     * @param session
     * @return
     */
    public static long setGatewaySession(RedisContext context, String gwid, String session) {
        String key = GATEWAY_KEY_PREFIX + gwid;
        return context.jedis.hset(key, "session", session);
    }
    
    ///////////////////////////////////////////////////////////////
    public void setOpenNetworkStatus(String key, String value, int timeout){
    	key = OPEN_NETWORK_STATUS_PREFIX + key;
    	this.jedis.set(key, value);
        this.jedis.expire(key, timeout);
    }
    
	///////////////////////////////////////////////////////////////
	public String getOpenNetworkStatus(String key){
		key = OPEN_NETWORK_STATUS_PREFIX + key;
		return this.jedis.get(key);		
	}

    /**
     * 关闭Redis客户端
     */
    public void destroy() {
//        jedis.close();
        RedisContextFactory.getInstance().returnRedisContext(this);
    }

   

}
