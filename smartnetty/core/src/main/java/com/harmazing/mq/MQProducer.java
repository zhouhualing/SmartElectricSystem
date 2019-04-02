package com.harmazing.mq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class MQProducer {
	public Session session;
	public Destination destination;
	MessageProducer producer;
	
	public MQProducer(Session session,Destination destination){
		try {
			this.session=session;
			producer = session.createProducer(destination);
		} catch (JMSException e) {			
			e.printStackTrace();
		}
	}
	
}
