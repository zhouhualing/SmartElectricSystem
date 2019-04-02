package com.harmazing.ifttt;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.harmazing.cache.ActiveCommandQueue;
import com.harmazing.entity.AirCondition;
import com.harmazing.entity.Gateway;
import com.harmazing.entity.ZigbeeOO;
import com.harmazing.ifttt.CommandUtil.Scope;
import com.harmazing.protobuf.CommandProtos;
import com.harmazing.protobuf.CommandProtos.AirConditionerControl.CommandType;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific.DeviceType;
import com.harmazing.server.ActiveChannelGroup;
import com.harmazing.service.DeviceService;
import com.harmazing.util.MessageUtil;
import com.harmazing.util.ProtobufUtil;
import com.harmazing.util.QueueUtil;

public class IftttAction {
public final static Logger LOGGER = LoggerFactory.getLogger(IftttAction.class);
	
	private final static String channel = "ifttt_channel";
	private ActiveCommandQueue activeQueue = null;
	
	DeviceService dev_srv = null;
	
	/////////////////////////////////////////////////////
	public IftttAction(DeviceService srv, ActiveCommandQueue queue ){
		dev_srv = srv;
		activeQueue = queue;
	}
	
	/////////////////////////////////////////////////////
	public void execute(String act) throws Exception{
		JSONObject obj = new JSONObject(act);
		JSONArray array = obj.getJSONArray("action");
		
		for(int i=0; i<array.length(); ++i){
			JSONObject action = (JSONObject)array.get(i);
			int dev_type = (int)action.get("type");
			if(dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC_VALUE ){
				executeAC(action);
			
			}else if(dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET_VALUE){
				executeIR(action);
			
			}else if(dev_type == DeviceSpecific.DeviceType.ZIGBEE_OO_VALUE){
				executeOO(action);
			}
		}	
	}
	
	///////////////////////////////////////////////////////
	private void executeAC(JSONObject obj){
		String op  = (String)obj.get("op");
		String var = (String)obj.get("var");
		int val    = Integer.parseInt((String)obj.get("val"));
		
		String mac = (String)obj.get("mac");
		AirCondition ac = dev_srv.getAcByAcMac(mac);
		/*
		long current_time = System.currentTimeMillis();
		long update_time = this.convertTimeStr2Long(ac.getUpdate_time());
		if( (current_time - update_time) < regulation_interval)
		return;
		*/
		if( ac == null || (ac.getOperStatus() == 0)|| (ac.getOnOff() == 0)) {
			LOGGER.info("IftttThread.executeAC: AC("+mac+") is OFFLINE, do nothing.");
			return;
		}
		
		Gateway gw = dev_srv.getGWByGwId(ac.getGw_id());
		if( gw.getOperStatus() == 0) {
			LOGGER.info("IftttThread.executeAC: GW("+mac+") is OFFLINE, do nothing.");
			return;
		}
		
		
		String gw_mac = gw.getMac();
		String ac_mac = ac.getMac();
		DeviceType dev_type  = DeviceType.INNOLINKS_AC;
		
		publish("COMMAND", "", gw_mac, ac_mac, var, String.valueOf(val));
		//execution(var, val, channel, gw_mac, ac_mac, dev_type,"", val);
		
	}
	
	///////////////////////////////////////////////////////
	private void executeIR(JSONObject obj){
		String modsig = "";
		int rcu_id = 1000;
		
		String ac_mac = (String)obj.get("mac");
		String var = (String)obj.get("var");
		int val = Integer.parseInt((String)obj.get("val"));
		
		AirCondition ac = dev_srv.getAcByAcMac(ac_mac);
		if( ac == null || (ac.getOperStatus() == 0) || (ac.getOnOff() == 0)) {
			LOGGER.info("IftttThread.executeAC: AC("+ac_mac+") is OFFLINE, do nothing.");
			return;
		}
		
		Gateway gw = dev_srv.getGWByGwId(ac.getGw_id());
		String gw_mac = gw.getMac();
		if( gw.getOperStatus() == 0) {
			LOGGER.info("IftttThread.executeAC: GW("+gw_mac+") is OFFLINE, do nothing.");
			return;
		}		
		
		rcu_id  = ac.getRcuId();
		
		int mode    = ac.getMode();
		int acTemp  = ac.getAcTemp();
		int udSwing = ac.getUpDownSwing();
		int lrSwing = ac.getLeftRightSwing();
		int speed   = ac.getSpeed();
		int onOff   = ac.getOnOff();
		
		if(var.equals("mode")){
			mode = val;
		}else if(var.equals("acTemp")){
			acTemp = val;
		}else if(var.equals("onOff")){
			onOff = val;
		}else if(var.equals("speed")){
			speed = val;
		}else if(var.equals("upDownSwing")){
			udSwing = val;
		}
		modsig += mode;
		if(acTemp < 10) modsig += "0";
		modsig += acTemp;
		modsig += udSwing;
		modsig += lrSwing;
		modsig += speed;
		modsig += onOff;
		
				
		publish(CommandUtil.IrControlCommandType.IR_TX_CODE, "IRCONTROL",  gw_mac, ac_mac, "modsig", ""+rcu_id+"&"+modsig);
		/*
		JSONObject ac_code_obj = MessageUtil.getAcCodesById(String.valueOf(rcu_id), modsig);
		if(ac_code_obj == null || (int)ac_code_obj.get("status") == 1){
			LOGGER.warn("IfTTT.executeIR: Could not find matchaced AC codes with modsig(" + modsig + "), do nothing.");
			return;
		}
		
		String str_main = (String)ac_code_obj.get("main");
		JSONArray llength = ac_code_obj.getJSONArray("length");
		String length     = MessageUtil.convertJSONArray2String(llength);
		
		ByteString main = null;		
		if( str_main != null && !(str_main.toString().equals(""))){
			main = ProtobufUtil.covertMain2SBytetring( str_main);
		}
		
		
		CommandProtos.IrControl.CommandType cmd_type = CommandProtos.IrControl.CommandType.IR_TX_CODE;
		int seq_num = ActiveChannelGroup.sendIrControlMessage(channel, gw_mac, ac_mac, cmd_type, main, length);		
		QueueUtil.addIrControlCmd2Queue(activeQueue, seq_num, channel, ac_mac, modsig); 
		*/
	}
	
	///////////////////////////////////////////////////////
	private void executeOO(JSONObject obj){
		String op  = (String)obj.get("op");
		String var = (String)obj.get("var");
		int val    = Integer.parseInt((String)obj.get("val"));
		
		String mac = (String)obj.get("mac");
		ZigbeeOO oo = dev_srv.getZiebeeOOByMac(mac);
		Gateway gw = dev_srv.getGWByGwId(oo.getGw_id());
		
		String gw_mac = gw.getMac();
		String ac_mac = oo.getMac();
		DeviceType dev_type  = DeviceType.ZIGBEE_OO;
		
		//execution(var, val, channel, gw_mac, ac_mac, dev_type,"", val);
		publish("COMMAND", "", gw_mac, ac_mac, var, String.valueOf(val));
	}
	
	//////////////////////////////////////////////////////
	private void execution(String var, int val,
		String channel, String gw_mac, String dev_mac, DeviceType dev_type, String str_param, Integer int_param){
		CommandType cmd_type = CommandType.ON;
		if( var.equals("onOff")){
			if( val == 0){
				cmd_type = CommandType.OFF;
			}else if( val == 1){
				cmd_type = CommandType.ON;
			}
		}else if( var.equals("acTemp")){
			cmd_type = CommandType.TEMP_SET;
		}else if( var.equals("speed")){
			cmd_type = CommandType.FAN_SET;
		}
		
		int seq_num = ActiveChannelGroup.sendCommandMessageByDeviceId(channel, gw_mac, dev_mac, dev_type, cmd_type, str_param, int_param);
		QueueUtil.addDeviceControlCmd2Queue( activeQueue, seq_num, channel, dev_mac, dev_type, cmd_type, int_param);
	}
	
	////////////////////////////////////////////////////////
	private void publish( String cmd_type, String msg_type, String gw_mac, String dev_mac, String var, String value){
		try{
			Map<String, String> command = null;
		
			if(msg_type.equals("IRCONTROL")){
				command =CommandUtil.buildIrControlMessageMap(gw_mac, dev_mac, cmd_type, value);
				
			}else if( msg_type.equals("COMMAND")){
				command =CommandUtil.buildCommandMessageMap( Scope.DEVICE, gw_mac, dev_mac, cmd_type, var, Integer.parseInt(value));
			}
			
			int onStatus = CommandUtil.syncSendMessage(command); 
			if( onStatus == 0){
				LOGGER.debug("Regulation and adjustment succesfully.");
			}else{
				LOGGER.debug("Regulation and adjustment failed.");
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
}
