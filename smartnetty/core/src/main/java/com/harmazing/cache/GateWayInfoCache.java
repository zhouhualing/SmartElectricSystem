package com.harmazing.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.entity.Device;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;

public class GateWayInfoCache {
	static ConcurrentMap<String,Map> cm=new ConcurrentHashMap();
	public static void set(String key,Map value){
		cm.put(key, value);
	}
	public static Map get(String key){
		return cm.get(key);
	}
	
	
    public static Device getGatewayByMacAndSN(String mac,String sn){
        Device device = new Device();
    	String key=mac + "_" + sn;
        Map gateway=cm.get(key); 
        device.setId((String)gateway.get("gwId"));
        device.setMac((String)gateway.get("mac"));
        device.setSn((String)gateway.get("sn"));
        device.setUserId((String)gateway.get("userId"));
        device.setDisable(Integer.valueOf(gateway.get("disable").toString()));
        device.setEleArea((String)gateway.get("eleArea"));
        device.setAirconServiceList((List<Map>)gateway.get("devices"));
        return device;
    }

}
