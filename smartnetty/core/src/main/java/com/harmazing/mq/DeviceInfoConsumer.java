package com.harmazing.mq;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.harmazing.Thread.DeviceInfoThread;
import com.harmazing.cache.DeviceArea;
import com.harmazing.cache.DeviceThreadCache;
import com.harmazing.cache.MessageCache;
import com.harmazing.entity.ResultData;


public class DeviceInfoConsumer implements MessageListener {
 	public static PooledConnection conn=null;
 	public Session session=null;
 	public Destination destination=null;	 
 	public static int i=1;
 	public final long t=System.currentTimeMillis();
 	public MessageConsumer consumer;
 	public long initTime=0;
 	public Map<String,ResultData> cache; 
 	public static int j=1;
 	{
 		try{
 			conn=MQUtil.getConn();
 			conn.start(); 
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
 	}
	public DeviceInfoConsumer(String dest){
		i++;		
		try {
			session=conn.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
			destination=session.createQueue(dest);		
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(this);
			initTime=System.currentTimeMillis();
			cache=new HashMap();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void onMessage(Message message) {
		try{
	    	 ObjectMessage objMsg = (ObjectMessage) message;
	    	 Map<String,String> device=(Map) objMsg.getObject();
	    	 String id=device.get("id");	
	    	 String area=DeviceArea.get(id);
	    	 if(area==null){
	    		 area=device.get("eleArea");
	    		 if(area!=null){
	    			 DeviceArea.set(id, area);
	    		 }else{
	    			 area="noarea";
	    		 }
	    	 }
	    	 DeviceInfoThread t=(DeviceInfoThread) DeviceThreadCache.get(area);
    		 if(t==null){	
    			 t=new DeviceInfoThread(area);
    			 DeviceThreadCache.put(area, t);
    			 t.start();    			 
    		 }
    		 t.addDevice(device);
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
}
