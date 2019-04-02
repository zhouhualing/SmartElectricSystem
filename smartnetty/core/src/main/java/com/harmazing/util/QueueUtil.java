package com.harmazing.util;

import com.harmazing.cache.ActiveCommandQueue;
import com.harmazing.cache.ReflectItem;
import com.harmazing.protobuf.CommandProtos;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;

public class QueueUtil {
	
	//////////////////////////////////////////////////////////////////////////
	public static void addIrControlCmd2Queue(ActiveCommandQueue queue, int seq_num, String channel, String dev_mac, String modsig){
		ReflectItem item = new ReflectItem();
		item.channel = channel;
		
		item.func_type = 1;
		item.cls_name  = "com.harmazing.util.AirconUtil";
		item.func_name = "updateAcByModsig"; 
		item.formal_params = new Class<?>[2];
		item.formal_params[0] = String.class;
		item.formal_params[1] = String.class;
		item.actual_params = new Object[2];
		item.actual_params[0] = dev_mac;
		item.actual_params[1] = modsig;
		
		queue.enqueue(seq_num, item);		
	}
	
	//////////////////////////////////////////////////////////////////////////
	public static void addHALampControlCmd2Queue(ActiveCommandQueue queue, int seq_num, String channel, int illuminance, int red, int green, int blue){
		ReflectItem item = new ReflectItem();
		item.channel = channel;
		
		item.func_type = 0;
		item.cls_name  = "com.harmazing.service.impl.DeviceServiceImpl";
		item.func_name = "updateHALampColor";
		item.formal_params = new Class<?>[4];
		item.formal_params[0] = Integer.class;
		item.formal_params[1] = Integer.class;
		item.formal_params[2] = Integer.class;
		item.formal_params[3] = Integer.class;
		
		item.actual_params = new Object[4];
		item.actual_params[0] = illuminance;
		item.actual_params[1] = red;
		item.actual_params[2] = green;
		item.actual_params[3] = blue;
		
		queue.enqueue(seq_num, item);
	}
	
	//////////////////////////////////////////////////////////////////////////
	public static void addDeviceControlCmd2Queue(ActiveCommandQueue queue, 
			                                     int seq_num, 
			                                     String channel, 
			                                     String sensorId,
			                                     DeviceSpecific.DeviceType devType,
			                                     CommandProtos.AirConditionerControl.CommandType commandType, 
			                                     int value ){
    	ReflectItem item = new ReflectItem();
    	item.channel = channel;
    	
        item.func_type = 0;
        item.cls_name = "com.harmazing.service.impl.DeviceServiceImpl";
        item.func_name = "updateDeviceIntField";
        item.formal_params = new Class<?>[4];
        item.formal_params[0] = DeviceSpecific.DeviceType.class;
        item.formal_params[1] = String.class;
        item.formal_params[2] = String.class;
        item.formal_params[3] = int.class;
        item.actual_params = new Object[4];
        item.actual_params[0] = devType;
        item.actual_params[1] = sensorId;
        if(commandType == CommandProtos.AirConditionerControl.CommandType.TEMP_SET){
        	item.actual_params[2] = "acTemp";
        	item.actual_params[3] = value;
        }else if(commandType == CommandProtos.AirConditionerControl.CommandType.FAN_SET){
        	item.actual_params[2] = "speed";
        	item.actual_params[3] = value;
        }else if(commandType == CommandProtos.AirConditionerControl.CommandType.MODE_SET){
        	item.actual_params[2] = "mode";
        	item.actual_params[3] = value;
        }else if(commandType == CommandProtos.AirConditionerControl.CommandType.OFF){
        	item.actual_params[2] = "onOff";
        	item.actual_params[3] = 0;
        }else if(commandType == CommandProtos.AirConditionerControl.CommandType.ON){
        	item.actual_params[2] = "onOff";
        	item.actual_params[3] = 1;
        }
        queue.enqueue( seq_num, item);
    }
}
