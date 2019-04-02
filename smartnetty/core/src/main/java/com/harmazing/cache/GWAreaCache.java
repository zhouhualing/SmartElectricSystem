package com.harmazing.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GWAreaCache {
	public static ConcurrentMap<String,String> map=new ConcurrentHashMap<String,String>();
	public static void  set(String deviceId,String areaId){
		map.put(deviceId, areaId);
	}
	public static String get(String deviceId){
		return map.get(deviceId);
	}
}
