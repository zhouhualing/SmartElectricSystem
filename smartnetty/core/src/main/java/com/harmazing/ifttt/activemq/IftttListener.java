package com.harmazing.ifttt.activemq;

import java.util.Map;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.jms.pool.PooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.entity.status.PirStatus;
import com.harmazing.entity.status.WinDoorStatus;
import com.harmazing.mq.MQUtil;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;

public class IftttListener implements MessageListener {
	private final static Logger LOGGER = LoggerFactory.getLogger(IftttListener.class);

	
	/////////////////////////////////////////////////////////
	@Override
	public void onMessage(Message msg) {
		try{
			ObjectMessage obj_msg = (ObjectMessage)msg;
			Map<String, String> obj_map = (Map)obj_msg.getObject();
			int dev_type = Integer.parseInt(obj_map.get("type"));
			Object sensor = (Object)obj_map.get("obj");
			
			if(dev_type == DeviceSpecific.DeviceType.DOOR_WINDDOW_SENSOR_VALUE){
				WinDoorStatus wds = (WinDoorStatus)sensor;
				String mac = wds.getMac();
				int open = wds.getOpen();
				
				System.out.println("***************** recevied WIN_DOOR open = " + open);
			}else if(dev_type == DeviceSpecific.DeviceType.PIR_SENSOR_VALUE){
				PirStatus pirs = (PirStatus)sensor;
				String mac = pirs.getMac();
				
				System.out.println("***************** recevied PIR Alarmed = 1");
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
