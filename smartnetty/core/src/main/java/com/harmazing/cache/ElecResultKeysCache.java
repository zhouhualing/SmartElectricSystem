package com.harmazing.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;

public class ElecResultKeysCache {
	public static void addKey(String key,String value,int timeout){	
		Jedis jedis=RedisContextFactory.getInstance().getJedis();
		jedis.setex(key,timeout,value);		
		RedisContextFactory.returnJedis(jedis);
	}
	public static String get(String key){
		Jedis jedis=RedisContextFactory.getInstance().getJedis();		
		String val=jedis.get(key);
		RedisContextFactory.returnJedis(jedis);
		return val;
	}
	public static void remove(String key){
		Jedis jedis=RedisContextFactory.getInstance().getJedis();		
		jedis.del(key);
		RedisContextFactory.returnJedis(jedis);
	}
	public static void remove(List<String> keys){
		Jedis jedis=RedisContextFactory.getInstance().getJedis();
		for(int i=0;i<keys.size();i++){
			jedis.del(keys.get(i));
		}
		RedisContextFactory.returnJedis(jedis);
	}
	public static void main(String[] args){
		System.out.println(ElecResultKeysCache.get("aaaaa"));
	}
	
}
