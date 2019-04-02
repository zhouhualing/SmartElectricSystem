package com.harmazing.cache;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Config;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.mapper.ResultDataMapper;
import com.harmazing.util.MongoUtil;

public class LogCountErrorCache {
	 public static ConcurrentLinkedQueue insertQ=new ConcurrentLinkedQueue();
	 public static ConcurrentLinkedQueue updateQ=new ConcurrentLinkedQueue();
	 
	 public static void addErrorCache(List insertCache,List updateCache){
		 insertQ.add(insertCache);
		 updateQ.add(updateCache);
	 }	

	public static class ErrorHandlerThread extends Thread{	
		public Logger LOGGER = LoggerFactory.getLogger(ErrorHandlerThread.class);
		Config conf = Config.getInstance();
		public void run(){
			while(true){
				List insertCache=(List) LogCountErrorCache.insertQ.poll();
				List updateCache=(List) LogCountErrorCache.updateQ.poll();
				
				 if((insertCache==null || insertCache.size()==0) &&
					 (updateCache==null || updateCache.size()==0) ){
					   return;
				   }
		//		   System.out.println(this.insertCache.size()+"----"+this.updateCache.size());
				   SqlSessionFactory  sqlSessionFactory =DataSourceSessionFactory.getInstance().getSqlSessionFactory();
				   SqlSession session=null;
				   try{
					   session =sqlSessionFactory.openSession(false);
					   ResultDataMapper resultDataMapper = session.getMapper(ResultDataMapper.class);			   
					   if(insertCache.size()!=0){				  
						   resultDataMapper.batchInsertResult(insertCache);				   
					   }			   
					   if(updateCache.size()!=0){
						   resultDataMapper.batchUpdateResult(updateCache);				   
					   }
					   session.commit();
					   System.out.println("handler error data success:"+insertCache.size()+"--"+updateCache.size());
				   }catch(Exception e){
					  System.out.println("handler error data error:"+insertCache.size()+"--"+updateCache.size());
					  session.rollback();
					  LOGGER.error("",e);
//					  LogCountErrorCache.addErrorCache(insertCache, updateCache);					
					  MongoUtil.insert(conf.MONGO_USER,conf.MONGO_PASSWORD,"logerror","insertError",insertCache);
					  MongoUtil.insert(conf.MONGO_USER,conf.MONGO_PASSWORD,"logerror","updateError",insertCache);
				   }finally{
					   if(session!=null){
						   session.close();
					   }
				   }		
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	}

}
