package com.harmazing.cache;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.harmazing.Thread.DeviceInfoThread;
import com.harmazing.Thread.LogResultThread;

public class LogResultThreadCache {
	public static ConcurrentHashMap<String,Thread> threads=new ConcurrentHashMap<String,Thread>();
	
	public  static void put(String key,LogResultThread t){
		threads.put(key, t);
	}
	
	public static Thread get(String key){
		return threads.get(key);
	}
}
