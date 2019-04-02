package com.harmazing.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.harmazing.Client;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.mapper.LogMapper;
import com.harmazing.mq.GWLogConsumer;
import com.harmazing.service.impl.LogServiceImpl;

public  class SaveGWLogThread extends Thread {
	public List<GWLogConsumer> consumers=new ArrayList<GWLogConsumer>();
	
	public void addConsumer(GWLogConsumer consumer){
		consumers.add(consumer);
	}
	public void run(){
		while(true){
			for(int i=0;i<consumers.size();i++){
				GWLogConsumer consumer=consumers.get(i);
				consumer.addLog(null);
			}
			try {
				Thread.sleep(1000*60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}    	
	
	
}  

