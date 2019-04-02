package com.harmazing.cache;

import java.util.HashMap;

import com.harmazing.Thread.DeviceInfoThread;
import com.harmazing.Thread.DeviceStatusThread;

public class DeviceStatusThreadCache {
	public static HashMap<String,Thread> threads=new HashMap<String,Thread>();
	
	public static void put(String key,DeviceStatusThread t){
		threads.put(key, t);
	}
	
	public static Thread get(String key){
		return threads.get(key);
	}
}
