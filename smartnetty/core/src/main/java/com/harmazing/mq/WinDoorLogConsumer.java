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
import com.harmazing.cache.LogResultThreadCache;
import com.harmazing.cache.MessageCache;
import com.harmazing.entity.ResultData;
import com.harmazing.mapper.LogMapper;
import com.harmazing.service.impl.LogServiceImpl;
import com.harmazing.util.UUIDGenerator;


public class WinDoorLogConsumer implements MessageListener {
	private static Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);
 	public static PooledConnection conn=null;
 	public Session session=null;
 	public Destination destination=null;
 	public MessageConsumer consumer;
 	public long initTime=0;
 	public List logList;
 	public static int j=1;
 	{
 		try{
 			conn=MQUtil.getConn();
 			conn.start(); 
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
 	}
	public WinDoorLogConsumer(String dest){				
		try {
			session=conn.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
			destination=session.createQueue(dest);		
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(this);
			initTime=System.currentTimeMillis();
			logList=new ArrayList();
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
			e.printStackTrace();
		}
	}	
	
	public synchronized void addLog(Map<String,String> log){
		boolean bs=false;
		if(log==null){
			bs=true;
		}else{		
			 logList.add(log);
		   	 long t=System.currentTimeMillis();
		   	 if(logList.size()==1000){
		   		 bs=true;		   		
		   	 }
		}
		if(bs){
			if(logList.size()==0){
				return;
			}
			 List logs=new ArrayList();
	   		 logs.addAll(logList);
	   		 logList=new ArrayList();
	   		 Client.executorService.execute(new SaveWDLogThread(logs));	   		
	   		 initTime=System.currentTimeMillis();
		}
	}
	
    
    class SaveWDLogThread extends Thread {
    	public List logList;
    	public SaveWDLogThread(List logLst){
    		this.logList=logLst;
    	}
    	public void run(){
    		if(logList==null || logList.size()==0){
    			return;
    		}
    		DataSourceSessionFactory dataSourceSessionFactory = DataSourceSessionFactory.getInstance();
    		SqlSessionFactory sqlSessionFactory = dataSourceSessionFactory.getSqlSessionFactory();
    		 SqlSession session = sqlSessionFactory.openSession(true);
    	        try {
    	            LogMapper logMapper = session.getMapper(LogMapper.class);
    	            logMapper.batchInsertWDLog(logList);
    	            session.commit();
    	        } catch (Exception e) {
    	            LOGGER.error("APPENDWINDOORLOG", e);
    	            session.rollback();
    	        } finally {
    	            session.close();
    	        }
    	}
    }
}
