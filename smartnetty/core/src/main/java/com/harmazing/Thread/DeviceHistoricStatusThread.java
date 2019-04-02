package com.harmazing.Thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.harmazing.DataSourceSessionFactory;
import com.harmazing.constant.MessageKey;
import com.harmazing.entity.status.ElectricityMeterStatus;
import com.harmazing.entity.status.HumidityTemperatureStatus;
import com.harmazing.entity.status.PirStatus;
import com.harmazing.entity.status.WinDoorStatus;
import com.harmazing.mq.MQProducerUtil;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;

public class DeviceHistoricStatusThread extends Thread{
	
	List<WinDoorStatus> wd_list = null;
	List<HumidityTemperatureStatus> ht_list = null;
	List<PirStatus> pir_list = null;
	List<ElectricityMeterStatus> em_list = null;
	
	
	public DeviceHistoricStatusThread(){
		wd_list  = new ArrayList<WinDoorStatus>();
		ht_list  = new ArrayList<HumidityTemperatureStatus>();
		pir_list = new ArrayList<PirStatus>();
		em_list  = new ArrayList<ElectricityMeterStatus>();
	}
	
	/////////////////////////////////////////////////////
	public synchronized void enqueue(DeviceSpecific.DeviceType dev_type, Object obj){
		HashMap<String, String> ifttt_msg = new HashMap<String, String>();
		
		if( dev_type == DeviceSpecific.DeviceType.DOOR_WINDDOW_SENSOR){
			WinDoorStatus wds = (WinDoorStatus)obj; 
			wd_list.add(wds);
			
			ifttt_msg.put("type", "7");
			ifttt_msg.put("mac", wds.getMac());
			ifttt_msg.put("open", String.valueOf( wds.getOpen()));
			
		}else if(dev_type == DeviceSpecific.DeviceType.TEMPERATURE_HUMIDITY_SENSOR){
			ht_list.add((HumidityTemperatureStatus) obj);
			
			ifttt_msg.put("type", "9");
			
		}else if(dev_type == DeviceSpecific.DeviceType.PIR_SENSOR){
			PirStatus pir = (PirStatus)obj;
			pir_list.add(pir);
			ifttt_msg.put("type", "8");
			
		}else if( dev_type == DeviceSpecific.DeviceType.ZIGBEE_OO_ELECTRICITY_METER){
			em_list.add((ElectricityMeterStatus)obj);
			ifttt_msg.put("type", "11");
		}
		
		// publish message to ActiveMQ for IFTTT
		MQProducerUtil.sendMapMessage(MessageKey.IFTTT_SENSOR, ifttt_msg);		
	}
	
	//////////////////////////////////////////////////////
	public void run(){
		SqlSessionFactory sqlSessionFactory = null;
		SqlSession session = null;
		
		while(true){
			try{
				Thread.sleep(30*1000);
				
				sqlSessionFactory = DataSourceSessionFactory.getInstance().getSqlSessionFactory();
		    	session = sqlSessionFactory.openSession(false);
		    	
		    	if(wd_list != null && wd_list.size() > 0){
		    		session.insert("com.harmazing.mapper.DeviceHistoricStatusMapper.batchInsertWinDoorSensorStatus", wd_list);
		    	}
		    	
		    	if( ht_list!=null && ht_list.size() > 0){
		    		session.insert("com.harmazing.mapper.DeviceHistoricStatusMapper.batchInsertHTSensorStatus", ht_list);
		    	}
		    	
		    	if( pir_list!=null && pir_list.size() > 0){
		    		session.insert("com.harmazing.mapper.DeviceHistoricStatusMapper.batchInsertPirSensorStatus", pir_list);
		    	}
		    	
		    	if( em_list != null && em_list.size() > 0){
		    		session.insert("com.harmazing.mapper.DeviceHistoricStatusMapper.batchInsertElectricMaterStatus", em_list);
		    	}
		    	
	    		session.commit();
	    		
	    		wd_list.clear();
	    		ht_list.clear();
	    		pir_list.clear();
	    		em_list.clear();
	    		
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				session.close();
			}
		}
	}
}
