package com.harmazing.ifttt;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Config;
import com.harmazing.entity.AirCondition;
import com.harmazing.entity.DoorWindowSensor;
import com.harmazing.entity.ZigbeeOO;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;
import com.harmazing.server.DefaultServerHandler;
import com.harmazing.service.DeviceService;

public class IftttCondition {
	public final static Logger LOGGER = LoggerFactory.getLogger(IftttCondition.class);
	
	DeviceService dev_srv = null;
	
	////////////////////////////////////////////////////////
	public IftttCondition( DeviceService srv){
		dev_srv = srv;
	}
	
	
	////////////////////////////////////////////////////////
	public boolean check(String condition, boolean need_rt_data, Map<String, String> rt_data) throws Exception{
		boolean result = false;
		JSONObject obj = new JSONObject(condition);
		JSONArray array = obj.getJSONArray("condition");
		
		if( array == null) return false;
		
		for(int i=0; i<array.length(); ++i){
			JSONObject con = (JSONObject)array.get(i);
			int dev_type = (int)con.get("type");
			if(dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC_VALUE ||
			  dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET_VALUE){
				result = checkAC(con, dev_type);
				if( !result) return false;
				
			}else if(dev_type == DeviceSpecific.DeviceType.ZIGBEE_OO_VALUE){
				result = checkOO(con);
				if( !result) return false;
				
			}else if( dev_type == DeviceSpecific.DeviceType.DOOR_WINDDOW_SENSOR_VALUE){
				if( need_rt_data){					
					result = checkWinDoor(con, rt_data);
				}else{
					result = checkWinDoor(con);
				}
				
				if( !result ) return false;
				
			}else if( dev_type == 200){ // Defined for Time
					result = checkTime(con);
				if(!result) return false;
			}
		}
	
	
	return result;
	}

	///////////////////////////////////////////////////////////
	public boolean checkAC(JSONObject obj, int dev_type) throws Exception{
		boolean result = false;
		
		String mac = (String)obj.get("mac");
		
		AirCondition ac = dev_srv.getAcByAcMac(mac);
		if( ac == null) return false;
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp ts = ac.getStartTime();
		long last_reg_time = ts.getTime();
		long cur_time = System.currentTimeMillis();
		/*
		if( (cur_time-last_reg_time) < Config.getInstance().IFTTT_TIME_REGULATION_INTERVAL * 60000 )
			return false;
		*/
		
		if( dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET_VALUE){
			if( ac.getRcuId() == null || ac.getRcuId() == 65535){
				LOGGER.info("AC Plug("+mac+")Unpaired, do nothing.");
				return false;
			}else if( ac.getOnOff() == 0 || ac.getOperStatus() == 0){
				LOGGER.info("AC is OFF or OFFLINE, do nothing.");
				return false;
			}
		}

		JSONArray attrs = obj.getJSONArray("attrs");
		for( int i=0; i<attrs.length(); ++i){
		
			JSONObject attr = attrs.getJSONObject(i);
			
			String op  = (String)attr.get("op");
			int val    = Integer.parseInt((String)attr.get("val"));			
			String var = (String)attr.get("var");
			
			if (var.equals("mode")){			
			int mode = ac.getMode();
			result =  check(mode, val, op);
			
			}else if( var.equals("onOff")){
				int onOff = ac.getOnOff();			
				result = check(onOff, val, op);
			
			}else if(var.equals("operStatus")){
				int operStatus = ac.getOperStatus();
				result =  check(operStatus, val, op);
				
			}else if( var.equals("temp")){
				int temp = ac.getTemp();
				//temp += temp_correction;
				result =  check(temp, val, op);
				
			}else if(var.equals("acTemp")){
				int acTemp = ac.getAcTemp();
				result = check(acTemp, val, op);	
				
			}else if(var.equals("humidity")){
				int humidity = ac.getHumidity();
				//humidity += humidity_correction;
				result = check(humidity, val, op);
			
			}else if(var.equals("speed")){
				int speed = ac.getSpeed();
				result = check(speed, val, op);
			
			}else{
				LOGGER.debug("Unsupported AirCondition attribute!");
			}
			
			if( !result ) 
				return false;
			
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////
	public  boolean checkOO(JSONObject obj) throws Exception{		
		String mac = (String)obj.get("mac");		
		ZigbeeOO oo = dev_srv.getZiebeeOOByMac(mac);
		
		if(oo.getOperStatus() == 0 ) return false;
		
		JSONArray attrs = obj.getJSONArray("attrs");
		for( int i=0; i<attrs.length(); ++i){
			JSONObject attr = attrs.getJSONObject(i);
			
		    String op  = (String)attr.get("op");
			int val    = Integer.parseInt((String)obj.get("val"));
			
			String var = (String)obj.get("var");
			if( var.equals("onOff")){
				int onOff = oo.getOnOff();			
				return check(onOff, val, op);
			
			}else if(var.equals("operStatus")){
				int operStatus = oo.getOperStatus();
				return check(operStatus, val, op);			
			}else{
				LOGGER.debug("Unsupported ZigBee OO attribute!");
			}
		}
		
		return false;
	
	}
	
	//////////////////////////////////////////////////////
	public boolean checkWinDoor(JSONObject obj, Map<String, String> rt_data) throws Exception{
		boolean result = false;
		
		String mac = (String) obj.get("mac");				
		JSONArray attrs = obj.getJSONArray("attrs");
		for( int i=0; i<attrs.length(); ++i){
			
			JSONObject attr = attrs.getJSONObject(i);
			
			String op  = (String)attr.get("op");
			int val    = Integer.parseInt((String)attr.get("val"));			
			String var = (String)attr.get("var");
			
			if( var.equals("onOff")){
				int rt_val = Integer.valueOf((String) rt_data.get("onOff"));	
				result = check(rt_val, val, op);			
			}else{
				LOGGER.debug("Unsupported AirCondition attribute!");
		}
		
		if( !result ) 
		return false;
	
	}
	
	return result;
	}
	
	
	
	//////////////////////////////////////////////////////
	public boolean checkWinDoor(JSONObject obj) throws Exception{
		boolean result = false;
		
		String mac = (String) obj.get("mac");		
		DoorWindowSensor wd = dev_srv.getWinDoorSensorByMac(mac);
		if( wd == null) return false;
		
		
		JSONArray attrs = obj.getJSONArray("attrs");
		for( int i=0; i<attrs.length(); ++i){
		
			JSONObject attr = attrs.getJSONObject(i);
			
			String op  = (String)attr.get("op");
			int val    = Integer.parseInt((String)attr.get("val"));			
			String var = (String)attr.get("var");
			
			if( var.equals("onOff")){
				int onOff = wd.getOnOff();			
				result = check(onOff, val, op);			
			}else{
				LOGGER.debug("Unsupported AirCondition attribute!");
			}
			
			if( !result ) 
				return false;
			
		}
		
		return result;
	}
	
	///////////////////////////////////////////////////////
	public boolean checkTime(JSONObject obj){		
		boolean result = false;
		
		JSONArray attrs = obj.getJSONArray("attrs");
		for( int i=0; i<attrs.length(); ++i){
			JSONObject attr = attrs.getJSONObject(i);
			
			String op  = (String)attr.get("op");
			String val = (String)attr.get("val");
			
			
			String str_exp_time = replaceDayWithToday(val);
			int  exp_time = (int)(convertTimeStr2Long(str_exp_time)/1000);
			
			Calendar cal = Calendar.getInstance();
			int cur_time = (int)(cal.getTimeInMillis()/1000);
			
			int period = (int)obj.get("period");
			if( period == 0){			
				result =  check(cur_time, exp_time, op);
			
			}else{
				int today = cal.get(Calendar.DAY_OF_WEEK)-1;
				if( (period & today) == 0) return false;
				
				result =  check(cur_time, exp_time, op);
			}	
			
			if( !result)
			return false;
		}	
		
		return result;
	}
	
	//////////////////////////////////////////////////////
	public long convertTimeStr2Long(String t){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = null;
		try{
			dt = sdf.parse(t);
		}catch(Exception e){
			e.printStackTrace();
		}
		return dt.getTime();
	}
	
	//////////////////////////////////////////////////////
	public String replaceDayWithToday( String t){
		String date = "";
		Calendar cal = Calendar.getInstance();
		date += cal.get(Calendar.YEAR);
		date += "-";
		date += cal.get(Calendar.MONDAY)+1;
		date += "-";
		date += cal.get(Calendar.DAY_OF_MONTH);
		date += t.substring(t.indexOf(" "), t.length());
		
		return date;
	}
	
	
	////////////////////////////////////////////////////
	public boolean check(int cur_value, int exp_value, String op){
		boolean result = false;
		
		if(op.equals("==")){
			return (cur_value == exp_value ? true : false);
		
		}else if( op.equals("!=")){
			return (cur_value != exp_value ? true : false);
		
		}else if( op.equals(">")){
			return (cur_value > exp_value ? true : false);
		
		}else if( op.equals("<")){
			return (cur_value < exp_value ? true : false);
		
		}else if( op.equals(">=")){
			return (cur_value >= exp_value ? true : false);
		
		}else if( op.equals("<=")){
			return (cur_value <= exp_value ? true : false);
		}
		
		return result;
	}
}
