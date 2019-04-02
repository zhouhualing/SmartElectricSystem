package com.harmazing.mq;
import java.sql.Timestamp;
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
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Client;
import com.harmazing.Config;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.Thread.LogResultThread;
import com.harmazing.cache.LogResultThreadCache;
import com.harmazing.cache.MessageCache;
import com.harmazing.entity.ResultData;
import com.harmazing.mapper.LogMapper;
import com.harmazing.service.impl.LogServiceImpl;
import com.harmazing.util.UUIDGenerator;


public class ACLogConsumer implements MessageListener {
	private final static Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);
 	public static PooledConnection conn=null;
 	public Session session=null;
 	public Destination destination=null;
 	public final long t=System.currentTimeMillis();
 	public MessageConsumer consumer;
 	public long initTime=0;
 	public long initTime2=0;
 	public Map<String,ResultData> cache;
 	public List logList;
 	public int interval;
 	public static int j=1;
 	{
 		try{
 			conn=MQUtil.getConn();
 			conn.start(); 
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
 	}
	public ACLogConsumer(String dest){				
		try {
			session=conn.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
			destination=session.createQueue(dest+"?consumer.prefetchSize=10");		
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(this);
			initTime=System.currentTimeMillis();
			initTime2=System.currentTimeMillis();
			cache=new HashMap();
			logList=new ArrayList();
			interval=Config.getInstance().CLIENT_REPORT_INTERVAL*1000;
		} catch (JMSException e) {
			e.printStackTrace();
		}		
	}
	@Override
	public void onMessage(Message message) {
//		lognum++;
//		System.out.println("*********"+lognum+"**********************");
		try{
	    	 ObjectMessage objMsg = (ObjectMessage) message;
	    	 Map<String,String> log=(Map) objMsg.getObject();
	    	 long timestamp = Long.parseLong((String)log.get("timestamp"));	
	    	
	    	 String areaId=log.get("eleArea").toString();
	    	 String deviceId = (String)log.get("deviceId");
	   		 String currentTemperature = log.get("currentTemperature");
	   		 String humidity = log.get("humidity");
	   		 Integer currentPower = Integer.parseInt(log.get("currentPower"));
	   		 Long accumulatePower = Long.parseLong(log.get("accumulatePower"));
	   		 Integer reactivePower = Integer.parseInt(log.get("reactivePower"));
	   		 Integer reactiveEnergy = Integer.parseInt(log.get("reactiveEnergy"));
	   		 Integer apparentPower = Integer.parseInt(log.get("apparentPower"));
	   		 Integer voltage = Integer.parseInt(log.get("voltage"));
	   		 Integer current = Integer.parseInt(log.get("current"));
	   		 Integer frequency = Integer.parseInt(log.get("frequency"));
	   		 Integer powerFactor = Integer.parseInt(log.get("powerFactor"));  		 
	   		 Integer period = Integer.parseInt(log.get("period"));
	   		 Integer activeDemand = Integer.parseInt(log.get("activeDemand"));
	   		 Integer reactiveDemand = Integer.parseInt(log.get("reactiveDemand"));
	   		long startTime = Long.parseLong(log.get("startTime"));
	   		
	   		 long timestampSencond = parseSecond(timestamp);	   		 
	   		
	   		 if(startTime==0){
	   			 period = 0;
	   			 activeDemand = 0;
	   			 reactiveDemand = 0;
	   		 }else{
	   			startTime=startTime/1000*1000;
	   		 }
	   		 int onOff = 1;
             if(currentPower.intValue() <= 0){
             	onOff = 0;
             }	         
             Timestamp startTimestamp = startTime==0?null:new Timestamp(startTime);
             Map dbMap=new HashMap();             
             dbMap.put("id", UUIDGenerator.randomUUID());
             dbMap.put("deviceId", deviceId);
             dbMap.put("onOff", onOff);
             dbMap.put("currentTemperature", currentTemperature);
             dbMap.put("humidity", humidity);
             dbMap.put("power", currentPower);
             dbMap.put("accumulatePower", accumulatePower);
             dbMap.put("currentTime",new Timestamp(timestampSencond));
             dbMap.put("reactivePower", reactivePower);
             dbMap.put("reactiveEnergy", reactiveEnergy);
             dbMap.put("apparentPower", apparentPower);
             dbMap.put("voltage", voltage);
             dbMap.put("current", current);
             dbMap.put("frequency", frequency);
             dbMap.put("powerFactor", powerFactor);
             dbMap.put("startTime", startTimestamp);
             dbMap.put("activeDemand", activeDemand);
             dbMap.put("reactiveDemand", reactiveDemand);
             logList.add(dbMap);
             
             long t=System.currentTimeMillis();
             if(t-initTime2>interval || logList.size()==1000){
            	 List logs=new ArrayList();
	    		 logs.addAll(logList);
	    		 logList=new ArrayList();
            	 Client.executorService.execute(new SaveLogThread(logs));
            	 initTime2=t;
             }
             
	   		 ResultData rd =cache.get(areaId+timestampSencond);
//	   		 System.out.println(new Timestamp(timestampSencond).toString()+"--2");
	    	 if(rd==null){
	    		rd = new ResultData(areaId, timestampSencond, currentPower, reactivePower, 
	           			 powerFactor, apparentPower, reactiveEnergy, reactiveDemand, activeDemand,
	           			 startTime,1,accumulatePower);
	    		 cache.put(areaId+timestampSencond,rd);
	    	 }else{
	    			rd.setPower(rd.getPower()+currentPower);
					rd.setReactiveDemand(rd.getReactivePower()+reactivePower);
					rd.setPowerFactor(rd.getPowerFactor()+powerFactor);
					rd.setApparentPower(rd.getApparentPower()+apparentPower);
					rd.setReactiveEnergy(reactiveEnergy+rd.getReactiveEnergy());
					rd.setReactiveDemand(reactiveDemand+(rd.getReactiveDemand()==null?0:rd.getReactiveDemand()));
					rd.setActiveDemand(activeDemand+(rd.getActiveDemand()==null?0:rd.getActiveDemand()));
					rd.setDeviceNum(1+(rd.getDeviceNum()==null?0:rd.getDeviceNum()));
					rd.setAccumulatePower(rd.getAccumulatePower()+accumulatePower);
	    	 }	    	 
	    	
	    	 if(t-initTime>interval || cache.size()>100){
	    		 flush();
	    		 initTime=System.currentTimeMillis();
	    	 }
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	public synchronized void flush(){	
		//System.out.println(interval+"_-_-_-_-_-_-_-_-_-_-"+LogResultThreadCache.threads.size());
		if(cache.size()>0){
			Set<String> keys=cache.keySet();
			for(String key:keys){
				ResultData rd=cache.get(key);
				String area=rd.getAreaId();
				LogResultThread t=(LogResultThread)LogResultThreadCache.get(area);
				if(t==null){
					t=new LogResultThread(area);
					LogResultThreadCache.put(area, t);
					t.start();
				}
				t.addLog(key, rd);
			}
			cache=new HashMap<String,ResultData>();
		}
	}
	 /**
     * 将毫秒时间转为秒
     * @param time
     * @return
     */
    public long parseSecond(long time){
    	String stString = time+"";
		stString = stString.substring(0,stString.length()-3)+"000";
    	return Long.parseLong(stString);
    }
    
    
    class SaveLogThread extends Thread {
    	public List logList;
    	public SaveLogThread(List logLst){
    		this.logList=logLst;
    	}
    	public void run(){
    		DataSourceSessionFactory dataSourceSessionFactory = DataSourceSessionFactory.getInstance();
    		SqlSessionFactory sqlSessionFactory = dataSourceSessionFactory.getSqlSessionFactory();
    		SqlSession session = sqlSessionFactory.openSession(false);        	
        	try{
        		LogMapper logMapper = session.getMapper(LogMapper.class);
        		logMapper.batchInsertACLog(logList);
    	    	session.commit();    	    	
        	}catch(Exception e){
        		LOGGER.error("update ac log error", e);
        		session.rollback(true);
        	}finally{
        		session.close();
        	}
    	}
    }
}
