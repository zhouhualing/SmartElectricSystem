package com.harmazing.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class TimerCache {
	public ConcurrentMap<String,String> kv=new ConcurrentHashMap<String,String>();
	public ConcurrentMap<String,Long> kt=new ConcurrentHashMap<String,Long>();
	public long ct=0;
	public long interval=0;
	public TimerCache(long t){
		this.interval=t;
	}	
	public void set(String key,String value,long t){
		long tt=System.currentTimeMillis();
		kt.put(key,tt+t);
		kv.put(key, value);
		System.out.println("timercache size:"+kt.size());
		if(tt-ct>interval){
			new CleanThread(this).start();
		}
	}
	
	public String get(String key){
		return kv.get(key);
	}
	public void remove(String key){		
		kt.remove(key);
		kv.remove(key);
	}
}
class CleanThread extends Thread{
	private TimerCache cache=null;
	public CleanThread(TimerCache cache){
		this.cache=cache;
	}
	public void run(){
		Set<String> keys=cache.kt.keySet(); 
		Iterator iter=keys.iterator();
		while(iter.hasNext()){
			try{
				String key=(String) iter.next();	
				long t=cache.kt.get(key);
				cache.ct=System.currentTimeMillis();
				if(t>cache.ct){
					cache.kv.remove(key);
					cache.kt.remove(key);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	
	}
}