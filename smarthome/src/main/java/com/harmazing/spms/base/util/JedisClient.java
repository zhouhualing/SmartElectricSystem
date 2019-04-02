package com.harmazing.spms.base.util;

import java.util.Iterator;
import java.util.Set;

import com.harmazing.spms.base.util.CommandUtil.JedisFactory;

import redis.clients.jedis.Jedis;

public class JedisClient {

	public static interface KeyType {
		public final static String AUTH = "AUTH_";
		public final static String INNOLINKS_GW = "NT:DEV_CTL_PROP_GW_";
		public final static String INNOLINKS_AC = "NT:DEV_CTL_PROP_AC_";
		public final static String INNOLINKS_PS = "NT:DEV_CTL_PROP_PS_";
		public final static String INNOLINKS_TS = "NT:DEV_CTL_PROP_TS_";
		public final static String INNOLINKS_OO = "NT:DEV_CTL_PROP_OO_";
	}
	
	public static String getRaw(String key){
		JedisFactory jedisFactory = JedisFactory.getInstance();
		Jedis jedis = null;
		try {
			jedis = jedisFactory.getResource();
			if(jedis != null){
			    return jedis.get(key);	
			}
		} catch (Exception e) {
			jedisFactory.returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			jedisFactory.returnResource(jedis);
		}
		return "";
	}
	
	public static String setRaw(String key,String value){
		JedisFactory jedisFactory = JedisFactory.getInstance();
		Jedis jedis = null;
		try {
			jedis = jedisFactory.getResource();
			if(jedis != null){
			    String ret = jedis.set(key, value);	
			    //jedis.expire(key, 12*3600);
			    return ret;
			}
		} catch (Exception e) {
			jedisFactory.returnBrokenResource(jedis);
			e.printStackTrace();
		}finally {
			jedisFactory.returnResource(jedis);
		}
		return "";
	}
	
	
	public static String get(String key,String type){
		return getRaw(ConstantUtil.JEDIS_PREFIX+type+key);
	}
	
	public static String set(String key,String value,String type){		
		return setRaw(ConstantUtil.JEDIS_PREFIX+type+key,value);
	}
	
	public static String getAuth(String key){		
		return get(key,KeyType.AUTH);
	}
	
	public static String setAuth(String key,String value){		
		return set(key,value,KeyType.AUTH);
	}
	
	public static String getGW(String key){		
		return getRaw(KeyType.INNOLINKS_GW+key);
	}
	
	public static String getAC(String key){		
		return getRaw(KeyType.INNOLINKS_AC+key);
	}

	public static String getPS(String key){		
		return getRaw(KeyType.INNOLINKS_PS+key);
	}
	
	public static String getTS(String key){		
		return getRaw(KeyType.INNOLINKS_TS+key);
	}
	
	public static String getOO(String key){		
		return getRaw(KeyType.INNOLINKS_OO+key);
	}
}
