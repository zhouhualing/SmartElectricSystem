package com.harmazing.mq;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

public class MQProducerUtil {

	
	public static void sendMapMessage(String dest,HashMap<String, String> obj){
		Session session;
		try {
//			System.out.println(">>>>>>>>>>>>>>>>>>send message to mq<<<<<<<<<<<<<<<<<");
			session = MQSessionPool.getMQSession();
			Destination destination=session.createQueue(dest);
			MessageProducer producer = session.createProducer(destination);
			ObjectMessage message=session.createObjectMessage();
			message.setObject(obj);			
			producer.send(message);
			producer.close();
			MQSessionPool.returnMQSession(session);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(String dest, HashMap<String, Object> obj){
		Session session;
		try {
//			System.out.println(">>>>>>>>>>>>>>>>>>send message to mq<<<<<<<<<<<<<<<<<");
			session = MQSessionPool.getMQSession();
			Destination destination=session.createQueue(dest);
			MessageProducer producer = session.createProducer(destination);
			ObjectMessage message=session.createObjectMessage();
			message.setObject(obj);			
			producer.send(message);
			producer.close();
			MQSessionPool.returnMQSession(session);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
