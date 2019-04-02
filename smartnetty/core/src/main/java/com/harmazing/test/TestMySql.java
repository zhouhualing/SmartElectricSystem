package com.harmazing.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.harmazing.Config;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.constant.MessageKey;
import com.harmazing.entity.Device;
import com.harmazing.mapper.DeviceMapper;
import com.harmazing.util.MongoUtil;

public class TestMySql {
	public static void main(String[] args) {
		SqlSessionFactory sqlSessionFactory = DataSourceSessionFactory.getInstance().getSqlSessionFactory();
    	SqlSession session = sqlSessionFactory.openSession(false);
    	DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);    	
    	List devices=new ArrayList();
    	long t1=System.currentTimeMillis();
    	Config conf = Config.getInstance();
    	
    	for(int i=0;i<1000;i++){
    		Device device=new Device();
    		device.setId("test"+i);
    		device.setMac("mac"+i);
    		device.setSn("sn"+i);
    		device.setVender("test");
    		device.setMode(1);
    		device.setHardware("test");
    		device.setSoftware("test");
    		device.setCurrentSoftware("test");
    		device.setOnOff(0);
    		device.setSession("");
    		device.setStorage("1");
    		device.setType("1");
    		devices.add(device);
    		
//    		deviceMapper.insertDevice("test"+i,"test","test","test","test",null,0,0,null,"mac"+i,"100","sn"+i,"1");
//    		MongoUtil.insert(conf.MONGO_USER, conf.MONGO_PASSWORD, conf.MONGO_DB, "zjhtest",(HashMap)device.toMap());
    	}
    
    	try{	
    		t1=System.currentTimeMillis();
    		session.insert("com.harmazing.mapper.DeviceMapper.batchInsertDevice", devices);
    		long t2=System.currentTimeMillis();
    		System.out.println("insert:"+(t2-t1));
    		for(int i=0;i<10;i++){
	    		t1=System.currentTimeMillis();
		    	session.update("com.harmazing.mapper.DeviceMapper.batchUpdateDevices", devices);
		    	t2=System.currentTimeMillis();
		    	System.out.println("update:"+(t2-t1));	
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		session.rollback();
    		session.close();
    	}
    }
}
