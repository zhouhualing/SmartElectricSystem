package com.harmazing.mq;

import javax.jms.Session;

import org.apache.commons.pool.impl.GenericObjectPool;

import com.harmazing.Config;

public class MQSessionPool {
	
	public static GenericObjectPool pool =null;
	
	static{
		 Config config=Config.getInstance();
		 MQSessionFactory sf=new MQSessionFactory();
		 pool= new GenericObjectPool(sf);		
		 pool.setMaxActive(config.ACTIVEMQ_MAX); // 能从池中借出的对象的最大数目  
		 pool.setMaxIdle(config.ACTIVEMQ_IDLE); // 池中可以空闲对象的最大数目  
		 pool.setMaxWait(config.ACTIVEMQ_WAIT); // 对象池空时调用borrowObject方法，最多等待多少毫秒  
		 pool.setTimeBetweenEvictionRunsMillis(600000);// 间隔每过多少毫秒进行一次后台对象清理的行动  
		 pool.setNumTestsPerEvictionRun(-1);// －1表示清理时检查所有线程  
		 pool.setMinEvictableIdleTimeMillis(3000);// 设定在进行后台对象清理时，休眠时间超过了3000毫秒		
	}
	
	public static Session getMQSession(){
		try {			
			return (Session)pool.borrowObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void returnMQSession(Session s){
		try {
			pool.returnObject(s);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
