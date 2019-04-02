package com.harmazing.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.protobuf.ByteString;
import com.harmazing.cache.OpenNetworkCache;
import com.harmazing.cache.OpenNetworkCacheElem;
import com.harmazing.entity.AirCondition;
import com.harmazing.protobuf.CommandProtos;
import com.harmazing.protobuf.MessageProtos;
import com.harmazing.protobuf.SensorProtos;
import com.harmazing.protobuf.SensorProtos.AirConditionerSendorSpecificInfo;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceServiceImpl;

/**
 * Created by ming on 14-9-10.
 */
public class AirconUtil {
    /**
     * 风速
     */
    public final static class Speed {
        public final static int SPEED1      = 1;
        public final static int SPEED2      = 2;
        public final static int SPEED3      = 3;
        public final static int SPEED4      = 4;
        public final static int SPEED_MAX      = 5;
        public final static int SPEED_AUTO      = 6;
        public final static int SILENT      = 7;
    }

    /**
     * 模式
     */
    public final static class Mode {
        public final static int FANONLY     = 0;
        public final static int WARMING     = 1;
        public final static int COOLING     = 2;
        public final static int AUTO        = 3;
        public final static int DEHUMIDITY  = 4;
    }

    /**
     * 获取风速
     * @param fanMode
     * @return
     */
    public final static int getSpeedValue(SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode fanMode) {
        if(fanMode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED_AUTO) {
            return Speed.SPEED_AUTO;
        } else if(fanMode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED1) {
            return Speed.SPEED1;
        } else if(fanMode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED2) {
            return Speed.SPEED2;
        } else if(fanMode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED3) {
            return Speed.SPEED3;
        } else if(fanMode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED4) {
            return Speed.SPEED4;
        } else if(fanMode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED_MAX) {
            return Speed.SPEED_MAX;
        } else if(fanMode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SILENT) {
            return Speed.SILENT;
        } else {
            return Integer.MIN_VALUE;
        }
    }

    /**
     * 获取模式
     * @param mode
     * @return
     */
    public final static int getModeValue(SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode mode) {
        if(mode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.AUTO) {
            return Mode.AUTO;
        } else if(mode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.FANONLY) {
            return Mode.FANONLY;
        } else if(mode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING) {
            return Mode.COOLING;
        } else if(mode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.WARMING) {
            return Mode.WARMING;
        } else if(mode == SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.DEHUMIDITY) {
            return Mode.DEHUMIDITY;
        } else {
            return Integer.MIN_VALUE;
        }
    }
    
    //////////////////////////////////////////////////////////////////////////
    public final static void updateAcByModsig(String dev_mac, String modsig){		
    	AirCondition ac = null;
		DeviceService dev_srv = new DeviceServiceImpl();
				
    	int mode = 0;
		int temp = 0;
		int swing_v = 0;
		int swing_h = 0;
		int fan_speed = 0;
		int onoff = 0;	
		
		Integer value = null;
		
		try{
			mode = Integer.valueOf(modsig.substring(0,1));
		}catch(Exception e){
			if(ac == null) ac = dev_srv.getAcByAcMac(dev_mac);
			value = ac.getMode();
			if(value == null) value = AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING_VALUE;
			else mode = value.intValue();
		}
		
		try{
			temp = Integer.valueOf(modsig.substring(1, 3));
		}catch(Exception e){
			if(ac == null) ac = dev_srv.getAcByAcMac(dev_mac);
			value = ac.getTemp();
			if( value == null) temp = 26;
			else temp = value.intValue();
		}
		
		try{
			swing_v = Integer.valueOf(modsig.substring(3,4));
		}catch(Exception e){
			if(ac == null) ac = dev_srv.getAcByAcMac(dev_mac);
			value = ac.getUpDownSwing();
			if(value == null) swing_v = 0;
			else swing_v = value.intValue(); 
		}
		
		try{
		    swing_h = Integer.valueOf(modsig.substring(4,5));
		}catch(Exception e){
			if(ac == null) ac = dev_srv.getAcByAcMac(dev_mac);
			value = ac.getLeftRightSwing();
			if(value == null) swing_h = 0;
			else swing_h = value.intValue();
		}
		
		try{
			fan_speed = Integer.valueOf(modsig.substring(5,6));
		}catch(Exception e){
			if(ac == null) ac = dev_srv.getAcByAcMac(dev_mac);
			value = ac.getSpeed();
			if(value == null) value = AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED_AUTO_VALUE;
			else fan_speed = value.intValue();
		}
		
		try{
			onoff = Integer.valueOf(modsig.substring(6,7));
		}catch(Exception e){
			if(ac == null) ac = dev_srv.getAcByAcMac(dev_mac);
			value = ac.getOnOff();
			if(value == null) onoff = 0;
			else onoff = value.intValue(); // Off status
		}
		
		dev_srv.updateACCurrentStatusByACMac(dev_mac, mode, temp, swing_v, swing_h, fan_speed, onoff, 1 );
    }
    
    public final static void updateRcuId(String mac, int rcu_id){	
    	DeviceService devSrv = new DeviceServiceImpl();
    	devSrv.updateAcRcuIdByAcMac(mac, rcu_id, 1);
    }
    
}
