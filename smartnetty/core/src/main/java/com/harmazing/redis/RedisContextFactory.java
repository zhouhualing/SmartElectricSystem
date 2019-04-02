package com.harmazing.redis;

import com.harmazing.Config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by ming on 14-9-10.
 */
public class RedisContextFactory {

    private static RedisContextFactory instance;
    private static JedisPool jedisPool;
    {
    	 Config config = Config.getInstance();
         JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
         jedisPoolConfig.setMaxIdle(config.REDIS_MAX_IDLE);
         jedisPoolConfig.setMaxTotal(config.REDIS_MAX_TOTAL);
         jedisPoolConfig.setMaxWaitMillis(-1);
         jedisPoolConfig.setTestOnBorrow(config.REDIS_TEST_ON_BORROW);
         jedisPoolConfig.setTestOnReturn(config.REDIS_TEST_ON_RETURN);
         jedisPoolConfig.setTestWhileIdle(config.REDIS_TEST_WHILE_IDLE);
         if(config.REDIS_PASSWORD==null || config.REDIS_PASSWORD.equals("")){
        	 jedisPool = new JedisPool(jedisPoolConfig, config.REDIS_IP, config.REDIS_PORT,config.REDIS_TIMEOUT);
         }else{
        	 jedisPool = new JedisPool(jedisPoolConfig, config.REDIS_IP, config.REDIS_PORT,config.REDIS_TIMEOUT,config.REDIS_PASSWORD);
         }
    }
    private RedisContextFactory() {       
        
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new RedisContextFactory();
        }
    }

    public static synchronized RedisContextFactory getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    public RedisContext getRedisContext() {
    	Jedis resource=null;
    	synchronized(jedisPool){
    		resource=jedisPool.getResource();	
    	}
    	if(resource==null){
    		System.out.println("****************jedis is null********");
    	}
        return new RedisContext(resource);
    }

    public  Jedis getJedis(){
    	Jedis resource=null;
    	//synchronized(jedisPool){
    		resource=jedisPool.getResource();	
    	//}
    	return resource;
    }
    
    public static void returnJedis(Jedis resource){
    	try{	    	
	    	jedisPool.returnResource(resource);
    	}catch(Exception e){
    		resource.close();
    		e.printStackTrace();
    	}
    }
    
    public synchronized void returnRedisContext(RedisContext redisContext) {
    	try{	    	
	    	jedisPool.returnResource(redisContext.jedis);
    	}catch(Exception e){
    		redisContext.jedis.close();
    		e.printStackTrace();
    	}
    }

}
