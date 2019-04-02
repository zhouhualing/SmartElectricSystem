package com.harmazing.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.DataSourceSessionFactory;
import com.harmazing.mapper.LogMapper;

public  class SaveGwRun implements Runnable{
	private final static Logger LOGGER = LoggerFactory.getLogger(SaveGwRun.class);
	public List<Map<String,Object>> logs=new ArrayList<Map<String,Object>>();
	public SaveGwRun(List logs){
		this.logs=logs;
	}
	@Override
	public void run() {
		if(logs==null || logs.size()==0){
			return;
		}
		DataSourceSessionFactory dataSourceSessionFactory = DataSourceSessionFactory.getInstance();
		SqlSessionFactory sqlSessionFactory = dataSourceSessionFactory.getSqlSessionFactory();
		SqlSession session = sqlSessionFactory.openSession(false);
    	try{
    		LogMapper logMapper = session.getMapper(LogMapper.class);        		
    		logMapper.batchInsertGWLog(logs);
		    session.commit();
//		    System.out.println("insert gw success:"+logs.size());
    	}catch(Exception e){
    		LOGGER.error("APPENDAGWLOGS", e);
    		session.rollback(true);
    	}finally{
    		session.close();
    	}
		
	}
}
