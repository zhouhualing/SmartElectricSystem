package com.harmazing.mq;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnection;
import org.apache.activemq.pool.PooledConnectionFactory;

import com.harmazing.Config;

public class MQUtil {
	public static PooledConnectionFactory poolFactory;
    public static void init() {  
    	Config conf=Config.getInstance();
        String url =conf.ACTIVEMQ_BROKERURL; 
        ActiveMQConnectionFactory factory=null;
        if(conf.MQ_USER!=null && !conf.MQ_USER.equals("")){
        	factory= new ActiveMQConnectionFactory(conf.MQ_USER,conf.MQ_PASSWORD,url);
        	
        }else{
        	 factory= new ActiveMQConnectionFactory(url); 
        }
        try {  
            poolFactory = new PooledConnectionFactory(factory);             
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
      
    public static void destroy(PooledConnection conn){  
        try {  
            if(conn != null){  
                conn.close();  
            }  
        } catch (JMSException e) {  
            e.printStackTrace();  
        }  
    }    
      
    public static PooledConnection getConn(){     	
    	 PooledConnection conn;
		try {
			conn = (PooledConnection) poolFactory.createConnection();
			 conn.start(); 
		} catch (JMSException e) {
			conn=null;
			e.printStackTrace();
		}           
         return conn;
    }  
    
}
