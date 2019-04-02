package com.harmazing.cache;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.App;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import com.harmazing.util.MessageUtil;

public class ActiveCommandQueue {
	public final static Logger LOGGER = LoggerFactory.getLogger(ActiveCommandQueue.class);
	
	ConcurrentHashMap<Integer, ReflectItem> queue = null;
	
	public ActiveCommandQueue(){
		queue = new ConcurrentHashMap<Integer, ReflectItem>();
	}
	
	public void enqueue(int seq_num, ReflectItem item){
		LOGGER.debug("ActiveCommandQueue.enqueue, seq_num =" + seq_num);
		queue.put(seq_num,  item);
		LOGGER.debug("ActiveCommandQueue.enqueue, size=" + queue.size());
	}
		
	public void call(int seq_num, int ret_code){
		LOGGER.debug("ActiveCommandQueue.call, seq_num =" + seq_num);
		
		ReflectItem item = queue.remove(seq_num);
		if( item == null) return;
		
		try {
			if( ret_code == 0){
				Class<?> cls = Class.forName(item.cls_name);		
				Method method = cls.getMethod( item.func_name, item.formal_params);
				method.invoke(item.func_type==0?cls.newInstance():null, item.actual_params);
			}			
			MessageUtil.publish(item.channel, ret_code);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		LOGGER.debug("ActiveCommandQueue.enqueue, size=" + queue.size());
	}	
	
	//////////////////////////////////////////////////////////////
	
}
