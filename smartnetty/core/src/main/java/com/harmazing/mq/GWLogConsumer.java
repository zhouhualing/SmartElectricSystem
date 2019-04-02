package com.harmazing.mq;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnection;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Client;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.Thread.LogResultThread;
import com.harmazing.Thread.SaveGWLogThread;
import com.harmazing.Thread.SaveGwRun;
import com.harmazing.cache.LogResultThreadCache;
import com.harmazing.cache.MessageCache;
import com.harmazing.entity.ResultData;
import com.harmazing.mapper.LogMapper;
import com.harmazing.service.impl.LogServiceImpl;
import com.harmazing.util.UUIDGenerator;


public class GWLogConsumer implements MessageListener {
	private final static Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);
 	public static PooledConnection conn=null;
 	public Session session=null;
 	public Destination destination=null;
 	public MessageConsumer consumer;
 	public List<Map<String,String>> logList;
 	public long initTime=0;
 	{
 		try{ 			
 			conn=MQUtil.getConn();
 			conn.start(); 
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
 	}
	public GWLogConsumer(String dest){				
		try {
			session=conn.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
			destination=session.createQueue(dest);		
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(this);
			initTime=System.currentTimeMillis();
			logList=new ArrayList<Map<String,String>>();
		} catch (JMSException e) {
			e.printStackTrace();
		}		
	}
	@Override
	public void onMessage(Message message) {
		try{
	    	 ObjectMessage objMsg = (ObjectMessage) message;
	    	 Map<String,String> log=(Map) objMsg.getObject();
	    	 log.put("id",UUID.randomUUID().toString());  	
	    	 addLog(log);
		}catch(Exception e){
			LOGGER.error("接受GWLOG消息失败",e);
			//e.printStackTrace();
		}
	}	
    
	public synchronized  void addLog(Map<String,String> log){
		Boolean bs=false;
		if(log==null){
			bs=true;
		}else{
			logList.add(log); 
			if(logList.size()==1000){
//				System.out.println("******add gw*********");
				bs=true;			
			}	
		}
		if(bs){
			if(logList.size()==0){
				return;
			}
//			System.out.println("******save gw*********");
			List logs=new ArrayList();
			logs.addAll(logList);
			logList=new ArrayList();
			Client.executorService.execute(new SaveGwRun(logs));
			initTime=System.currentTimeMillis();
		}
	}  
}
