package com.harmazing.Thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.App;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.mapper.DeviceMapper;

public class DeviceStatusThread extends Thread{
	public static Logger LOGGER = LoggerFactory.getLogger(DeviceStatusThread.class);
	public String key;
	public Map<String,Map<String,String>> cache=new HashMap<String,Map<String,String>>();
	public DeviceStatusThread(String key){
		this.key=key;
	}
	public void run(){
		while(true){
			Map<String,Map<String,String>> tc=new HashMap<String,Map<String,String>>();
			synchronized (cache) {
				tc.putAll(cache);
				cache.clear();
			}
			List lst=new ArrayList();
			if(tc.size()>0){
				Set<String> keys=tc.keySet();
				int i=0;
				for(String key:keys){
					lst.add(tc.get(key));
					i=i+1;
					if(i==1000){
						updateDevices(lst);
						lst=new ArrayList();
						i=0;
					}
				}
				if(lst.size()>0){
					updateDevices(lst);
					lst=new ArrayList();
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
  public void addDevice(Map device){
	 synchronized (cache) {
		if(cache.get(device.get("id"))!=null){
			cache.get(device.get("id")).putAll(device);
		}else{
			cache.put((String) device.get("id"),device);
		}
	 }
  }
  public void updateDevices(List devices){	
	  	SqlSessionFactory sqlSessionFactory = DataSourceSessionFactory.getInstance().getSqlSessionFactory();
    	SqlSession session = sqlSessionFactory.openSession(false);
    	DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);	    	
    	try{		    	
	    	session.update("com.harmazing.mapper.DeviceMapper.batchUpdateDevices", devices);
	    	session.commit();
	    	System.out.println(key+"update device status success:"+devices.size());
    	}catch(Exception e){
    		LOGGER.error("update devices",e);
    		session.rollback(true);
    		System.out.println(key+"update device status error:"+devices.size());
    	}finally{
    		session.close();
    	}
    }
  
  public static void main(String[] args){
	  Map m1=new HashMap();
	  Map m2=new HashMap();
	  m1.put("test","test1");
	  m2.put("test","test2");
	  m2.putAll(m1);
	  System.out.println(m2.get("test"));
  }
	
}
