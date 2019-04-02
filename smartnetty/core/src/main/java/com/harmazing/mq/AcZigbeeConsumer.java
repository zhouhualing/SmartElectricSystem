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
import com.harmazing.protobuf.SensorProtos.RadioInfo;
import com.harmazing.service.impl.LogServiceImpl;

public class AcZigbeeConsumer implements MessageListener{
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
	public AcZigbeeConsumer(String dest){
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
		 DataSourceSessionFactory dataSourceSessionFactory = DataSourceSessionFactory.getInstance();
		 SqlSessionFactory sqlSessionFactory = dataSourceSessionFactory.getSqlSessionFactory();
		 SqlSession session = sqlSessionFactory.openSession(false);
    	 try {
			Map<String,String> log=(Map)objMsg.getObject();
			String id=(String)log.get("id");			
			int rxLqi=Integer.parseInt(log.get("rxLqi"));
			int rxRssi=Integer.parseInt(log.get("rxRssi"));
			int txlqi=Integer.parseInt(log.get("txlqi"));
			int txRssi=Integer.parseInt(log.get("txRssi"));        
    		ZigBeeMapper zigbee= session.getMapper(ZigBeeMapper.class);
    		zigbee.delAcZigbee(id);
    		zigbee.insertAcZigbee(id, rxRssi, rxLqi, txRssi, txlqi);
		    session.commit();
    	}catch(Exception e){
    		LOGGER.error("save aczigbee error", e);
    		session.rollback(true);
    	}finally{
    		session.close();
    	}
	}
}
