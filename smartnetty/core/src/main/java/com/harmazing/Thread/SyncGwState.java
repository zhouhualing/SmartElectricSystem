package com.harmazing.Thread;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;



import com.harmazing.Config;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.mapper.DeviceMapper;
import com.harmazing.mapper.GatewayMapper;

public class SyncGwState extends Thread{
	public static HashMap<String,String> offgw=new HashMap<String,String>(); 
	public void run(){
		int sleeptime=Config.getInstance().thread_syncgw_sleeptime;
		while(true){		
	        try {
	        	DataSourceSessionFactory dataSourceSessionFactory = DataSourceSessionFactory.getInstance();
	        	SqlSessionFactory sqlSessionFactory = dataSourceSessionFactory.getSqlSessionFactory();
	        	SqlSession session = sqlSessionFactory.openSession();
	        	GatewayMapper gwMapper = session.getMapper(GatewayMapper.class);
	        	List<String> gws = gwMapper.getGwState();	            
	            offgw=new HashMap<String,String>();
	            for(int i=0;i<gws.size();i++){
	            	offgw.put(gws.get(i),"");
	            }
	            Thread.sleep(sleeptime);
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
		}
	}
}
 