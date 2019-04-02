package com.harmazing.test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import redis.clients.jedis.Jedis;

import com.harmazing.Config;
import com.harmazing.redis.RedisContextFactory;

public class TestRedis {
	
	public static void main(String[] args) {
		int c=0;
		long t1=System.currentTimeMillis();
		for(int i=0;i<10000;i++){
//			new RedisThread(i).start();
			c=c+i;			
		}
		long t2=System.currentTimeMillis();
		System.out.println(t2-t1);
	}
}

class RedisThread extends Thread{
	static Config config = Config.getInstance();
	String key="";
	public RedisThread(int key){
		this.key="key"+key;
	}
	public void run(){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		long t1=System.currentTimeMillis();
		Jedis jedis=RedisContextFactory.getInstance().getJedis();
		jedis.set(key,key);
		jedis.expire(key,1000);
		RedisContextFactory.returnJedis(jedis);
		TestCache.set(key,key);
		long t2=System.currentTimeMillis();
		System.out.println(key+" "+(t2-t1));
		
	}
}

class TestCache{
	static ConcurrentMap cm=new ConcurrentHashMap();
	public static void set(String key,String value){
		cm.put(key, value);
	}
	public static String get(String key){
		return (String) cm.get(key);
	}
}
