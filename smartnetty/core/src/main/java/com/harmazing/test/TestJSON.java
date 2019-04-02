package com.harmazing.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.json.JSONObject;

import com.harmazing.Config;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.constant.MessageKey;
import com.harmazing.entity.Device;
import com.harmazing.entity.status.HumidityTemperatureStatus;
import com.harmazing.entity.status.WinDoorStatus;
import com.harmazing.mapper.DeviceMapper;
import com.harmazing.util.MongoUtil;

public class TestJSON {
	public static void main(String[] args) {
		SqlSessionFactory sqlSessionFactory = DataSourceSessionFactory.getInstance().getSqlSessionFactory();
    	SqlSession session = sqlSessionFactory.openSession(false);
    	    	
    	List wds=new ArrayList();
    	   	
    	for(int i=0;i<1000;i++){
    		WinDoorStatus wd = new WinDoorStatus();
    		wd.setId(UUID.randomUUID().toString());
    		wd.setMac("0001AFED" + i);
    		wd.setOpen(i%2==0 ? 1 : 0);
    		
    		wds.add(wd);
    	}
    	
    	List hts=new ArrayList();
    	for( int i=0; i<1200; i++){
    		HumidityTemperatureStatus ht = new HumidityTemperatureStatus();
    		ht.setId(UUID.randomUUID().toString());
    		ht.setMac("0001AFEDFFFF" + i);
    		ht.setHumidity(i );
    		ht.setTemp(i/100);
    		
    		hts.add(ht);
    	}
    
    	try{	
    		
    		session.insert("com.harmazing.mapper.DeviceHistoricStatusMapper.batchInsertWinDoorSensorStatus", wds);
    		session.insert("com.harmazing.mapper.DeviceHistoricStatusMapper.batchInsertHTSensorStatus", hts);
    		session.commit();
    	}catch(Exception e){
    		session.rollback();
    		e.printStackTrace();
    	}finally{
    		session.close();
    	}
    		
    }
}
