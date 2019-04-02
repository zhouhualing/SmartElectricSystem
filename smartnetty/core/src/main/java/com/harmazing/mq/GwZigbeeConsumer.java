package com.harmazing.mq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.jms.pool.PooledConnection;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Config;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.entity.ResultData;
import com.harmazing.mapper.LogMapper;
import com.harmazing.mapper.ZigBeeMapper;
import com.harmazing.service.impl.LogServiceImpl;

public class GwZigbeeConsumer implements MessageListener{
	private final static Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);
 	public static PooledConnection conn=null;
 	public Session session=null;
 	public Destination destination=null;
 	public long t=System.currentTimeMillis();
 	public MessageConsumer consumer;
 	public List DelList;
 	public List InsertList;
 	public String dest;
 	public int interval;
 	{
 		try{
 			conn=MQUtil.getConn();
 			conn.start(); 
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
 	}
	public GwZigbeeConsumer(String dest){
		this.dest=dest;
		t=System.currentTimeMillis();
		interval=Config.getInstance().CLIENT_REPORT_INTERVAL*1000;
		try {
			session=conn.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
			destination=session.createQueue(dest);		
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(this);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onMessage(Message message) {
		 ObjectMessage objMsg = (ObjectMessage) message;
    	 try {
			Map<String,String> log=(Map)objMsg.getObject();
			String id=log.get("id");			
			int channel=Integer.parseInt(log.get("channel"));
			int channelMask=Integer.parseInt(log.get("channelMask"));
			int txPower=Integer.parseInt(log.get("txPower"));
			DataSourceSessionFactory dataSourceSessionFactory = DataSourceSessionFactory.getInstance();
    		SqlSessionFactory sqlSessionFactory = dataSourceSessionFactory.getSqlSessionFactory();
    		SqlSession session = sqlSessionFactory.openSession(false);
        	try{
        		ZigBeeMapper zigbee= session.getMapper(ZigBeeMapper.class);
        		zigbee.delGwZigbee(id);
        		zigbee.insertGwZigbee(id, channel, channelMask, txPower);
    		    session.commit();
        	}catch(Exception e){
        		LOGGER.error("APPENDAGWLOGS", e);
        		session.rollback(true);
        	}finally{
        		session.close();
        	}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
