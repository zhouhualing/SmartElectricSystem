package com.harmazing.cache;

import java.util.HashMap;
import java.util.Map;

public class DeviceArea {
	public static Map<String,String> map=new HashMap<String,String>();
	public synchronized static void  set(String deviceId,String areaId){
		map.put(deviceId, areaId);
	}
	public synchronized static String get(String deviceId){
		return map.get(deviceId);
	}
}
