package com.harmazing.test;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Config;
import com.harmazing.constant.MessageKey;
import com.harmazing.entity.Device;
import com.harmazing.mq.MQSessionPool;
import com.harmazing.mq.MQUtil;
import com.harmazing.redis.RedisContext;
import com.harmazing.util.DeviceLogUtil;
import com.harmazing.util.MongoUtil;



public class test {
	public static ConcurrentHashMap map =new ConcurrentHashMap();
	public static void main(String[] args) {	
		MQUtil.init();		
		for(int i=0;i<100;i++){
			new TestThread().start();
		}
//		for(int i=0;i<300;i++){
//			new Consumer();
//		}
	}
}
	class TestThread extends Thread{
		public static volatile int i=0;
		public static volatile long t;
		{
			t=System.currentTimeMillis();
			System.out.println(t);
		}
		public TestThread(){
			
		}
		public void run(){
			/******************/
			while(true){
//				try {
//					Thread.sleep(10);
//					Session session=MQSessionPool.getMQSession();
//					Destination destination=session.createQueue("zjhtest");
//					MessageProducer producer = session.createProducer(destination);
//					TextMessage message = session.createTextMessage();
//					message.setText(UUID.randomUUID().toString());					
//					producer.send(message);						
//					producer.close();				
//					MQSessionPool.returnMQSession(session);
//					i=i+1;
//					System.out.println("*********"+i+"****"+(System.currentTimeMillis()-t));
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}	
				String r=UUID.randomUUID().toString();
				if(test.map.get(r)!=null){
					System.out.println("*******************");
				}
				test.map.put(r, "aaa");
//				try {
//					Thread.sleep(1);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			/***************/
		}
	}
	class Consumer implements MessageListener{
		private final static Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
		public Consumer(){
			Session session=MQSessionPool.getMQSession();
			Destination destination;
			try {
				destination = session.createQueue("zjhtest");			
				MessageConsumer consumer = session.createConsumer(destination);
				consumer.setMessageListener(this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		
		@Override
		public void onMessage(Message message) {
			TextMessage mesg = (TextMessage) message;	    	 
			try {
				logError(mesg.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		
		public synchronized static void logError(String message){
			try {
				LOGGER.error(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	