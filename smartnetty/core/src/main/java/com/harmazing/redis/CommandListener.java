package com.harmazing.redis;

import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.harmazing.Config;
import com.harmazing.cache.OpenNetworkCacheElem;
import com.harmazing.cache.ReflectItem;
import com.harmazing.cache.ActiveCommandQueue;
import com.harmazing.cache.OpenNetworkCache;
import com.harmazing.mq.Command;
import com.harmazing.protobuf.CommandProtos;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;
import com.harmazing.server.ActiveChannelGroup;
import com.harmazing.service.DeviceService;
import com.harmazing.service.UserService;
import com.harmazing.service.impl.DeviceServiceImpl;
import com.harmazing.service.impl.UserServiceImpl;
import com.harmazing.util.AirconUtil;
import com.harmazing.util.DeviceLogUtil;
import com.harmazing.util.MessageUtil;
import com.harmazing.util.ProtobufUtil;
import com.harmazing.util.QueueUtil;
import com.harmazing.util.UUIDGenerator;
import com.harmazing.util.UdpLogger;

import io.netty.util.internal.StringUtil;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPubSub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandListener extends JedisPubSub {
	private ActiveCommandQueue activeQueue = null;
	OpenNetworkCache openNetworkCache = null;
	
	public CommandListener(ActiveCommandQueue queue, OpenNetworkCache cache){
		activeQueue = queue;
		openNetworkCache = cache;
	}
	
	public final static class ReturnCodes{
		public final static int    SUCCESS = 0;
		public final static int    ERROR_MODSIG_NOT_FOUND = 2001;	
		public final static int    ERROR_UNKNOWN          = 4000;
	}
	
    public final static class Scope {
        public final static String AREA = "AREA";
        public final static String USER = "USER";
        public final static String DEVICE = "DEVICE";
    }

    public final static class Patterns {
        public final static String COMMAND       = "COMMAND:*";
        public final static String GETGWINFO     = "GETGWINFO:*";
        public final static String IRCONTROL     = "IRCONTROL:*";
        public final static String GETSENSORINFO = "GETSENSORINFO:*";
        public final static String SERVICEUPDATE = "SERVICEUPDATE:*";
        public final static String TIMERCURV     = "TIMERCURV:*";
        public final static String GATEWAYCONTROL= "GATEWAYCONTROL:*";
        public final static String OPEN_NETWORK  = "OPENNETWORK:*";
        public final static String OOCONTROL     = "OOCONTROL:*"; 
        public final static String HALAMPCONTROL = "HALAMPCONTROL:*";
        public final static String OOELECTRICITYMETERCONTROL = "OOELECTRICITYMETERCONTROL:*";
        public final static String LIGHTSWITCHCONTROL = "LIGHTSWITCHCONTROL:*";
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(CommandListener.class);

    /**
     * 取得订阅的消息后的处�?
     *
     * @param channel
     * @param message
     */
    @Override
    public void onMessage(String channel, String message) {
        LOGGER.debug("Channel:" + channel);
        LOGGER.debug("Message:" + message);
    }

    /**
     * 初始化按表达式的方式订阅时候的处理
     *
     * @param pattern
     * @param channel
     * @param message
     */
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        LOGGER.debug("Channel:" + channel);
        UdpLogger.mtInfo("Channel:" + channel);
        LOGGER.debug("Message:" + message);
        UdpLogger.mtInfo("Message:" + message);

        ObjectMapper objectMapper = new ObjectMapper();
        Map map = null;
        try {
            map = objectMapper.readValue(message, Map.class);
        } catch (IOException e) {
            LOGGER.error("MESSAGE PARSE TO MAP ERROR! CHANNEL IS " + channel, e);
            return;
        }

        if (pattern.equals(Patterns.COMMAND)) {
            onReceiveCommandMessage(channel, map);
            
        }else if (pattern.equals(Patterns.IRCONTROL)) {
        	onReceiveIrControlMessage(channel, map);
        	
        }else if (pattern.equals(Patterns.GETGWINFO)) {
            onReceiveGetGWInfoMessage(channel, map);
            
        }else if (pattern.equals(Patterns.GETSENSORINFO)) {
            //onReceiveGetSensorInfoMessage(channel, map);
        	
        }else if(pattern.equals(Patterns.SERVICEUPDATE)){
        	onReceiveServiceUpdateMessage(channel,map);
        	
        }else if(pattern.equals(Patterns.TIMERCURV)){
        	onReceiveTimerCurvMessage(channel,map);
        	
        }else if(pattern.equals(Patterns.GATEWAYCONTROL)){
        	onReceiveGateWayControlMessage(channel,map);
        	
        }else if(pattern.equals(Patterns.HALAMPCONTROL)){
        	onRecevieHALampControlMessage(channel, map);
        	
        }
    }

    ////////////////////////////////////////////////////////////////////
    /**
     * 接收到命令消息处�?
     *
     * @param channel
     * @param message
     */
    private void onReceiveCommandMessage(String channel, Map message) {
    	CommandProtos.AirConditionerControl.CommandType cmd_type = MessageUtil.getCommandType((String) message.get("commandType"));
    	DeviceSpecific.DeviceType dev_type  = MessageUtil.getDeviceType((int)message.get("deviceType"));
    	String scope      = (String) message.get("scope");
        String str_param  = (String) message.get("stringParameter");
        Integer int_param = (Integer)message.get("intParameter");
        
        if (scope == null || cmd_type == null || dev_type == null) {
            returnError(channel, -3, "scope/commandType/dev_type is null.");
            return;
        }

        if (scope.equals(Scope.DEVICE)) {
            String gw_mac    = (String) message.get("id");
            String sensor_id = (String) message.get("sensorId");
            if (gw_mac != null) {
	            int seq_num = ActiveChannelGroup.sendCommandMessageByDeviceId(channel, gw_mac, sensor_id, dev_type, cmd_type, str_param, int_param);
	            QueueUtil.addDeviceControlCmd2Queue( activeQueue, seq_num, channel, sensor_id, dev_type, cmd_type, int_param);
            }
            
        }else if (scope.equals(Scope.AREA)) {
            String area_id = (String) message.get("id");
            if (area_id == null) {
                returnError(channel, -3, "area_id is null!");
                return;
            }
            UserService userService = new UserServiceImpl();
            List<String> userIds = userService.getUserIdByEleAreaId(area_id);
            ActiveChannelGroup.sendCommandMessageByUserId(channel, new ArrayList<String>(userIds), dev_type, cmd_type, str_param, int_param);
            
        }else if (scope.equals(Scope.USER)) {
            String user_id = (String) message.get("id");
            String sensor_id = (String) message.get("sensorId");
            if (user_id == null) {
                returnError(channel, -3, "user_id is null.");
                return;
            }
            ActiveChannelGroup.sendCommandMessageByUserId(channel, user_id, sensor_id, dev_type, cmd_type, str_param, int_param);
            
        } else {
            LOGGER.info("Invalid scope!");
            returnError(channel, -3, "Invalid scope!");
        }        
    }
    
    ////////////////////////////////////////////////////////////////////
	private void onReceiveIrControlMessage(String channel, Map message) {

		String deviceId = (String) message.get("id");
		String sensorId = (String) message.get("sensorId");

		CommandProtos.IrControl.CommandType commandType = MessageUtil.getIrControlCommandType((String) message.get("commandType"));
		String modsig = (String) message.get("modsig");
		String[] temp = modsig.split("&");
		JSONObject json_obj = MessageUtil.getAcCodesById(temp[0], temp[1]);
		
		if( json_obj == null || (int)json_obj.get("status") == 1){
			MessageUtil.publish(channel, ReturnCodes.ERROR_MODSIG_NOT_FOUND);
			
			LOGGER.warn("Could not find matchaced AC codes with modsig=" + modsig);
			return;
		}
				
		String str_main = (String)json_obj.get("main");		
		JSONArray llength = json_obj.getJSONArray("length");
		String length     = MessageUtil.convertJSONArray2String(llength);
				
		ByteString main = null;		
		if( str_main != null && !(str_main.toString().equals(""))){
			main = ProtobufUtil.covertMain2SBytetring( str_main);
		}
								
		int seq_num = ActiveChannelGroup.sendIrControlMessage(channel, deviceId, sensorId, commandType, main, length);		
		QueueUtil.addIrControlCmd2Queue(activeQueue, seq_num, channel, sensorId, modsig.substring(modsig.indexOf('&')+1));
	}
	
	/////////////////////////////////////////////////////////////
	public void onRecevieHALampControlMessage(String channel, Map message){
		String gw_mac    = (String) message.get("id");
        String sensor_id = (String) message.get("sensorId");
        
		int illuminance = (int)message.get("illuminance");
	    int red   = (int)message.get("red");
	    int green = (int)message.get("green");
	    int blue  = (int)message.get("blue");
	    
	    int seq_num = ActiveChannelGroup.sendHALampControlMessage(channel, gw_mac, sensor_id, illuminance, red, green, blue);
	    QueueUtil.addHALampControlCmd2Queue(activeQueue, seq_num, channel, illuminance, red, green, blue);	    
	}
    
	/////////////////////////////////////////////////////////////
    public void onReceiveGateWayControlMessage(String channel,Map message){
    	String gw_mac    = (String)message.get("gwId");
    	String user_code = (String)message.get("userCode");
    	
    	String[] macs = gw_mac.split("_");
    	for( int i=0; i<macs.length; ++i){
    		message.put("gwId", macs[i]);
	    	int seq = ActiveChannelGroup.sendGateWayControlCommand(channel, message);
	    	
	    	// Cache details and waiting GW message to bind user and device together      
	    	long time = System.currentTimeMillis();    	
	    	openNetworkCache.pushInfo( macs[i], user_code, seq, time);
    	}
    }

    /**
     * 接收获取网关消息处理
     *
     * @param channel
     * @param message
     */
    private void onReceiveGetGWInfoMessage(String channel, Map message) {
        String scope = (String) message.get("scope");
        if (scope == null) {
            LOGGER.info("Message not key scope is null");
            returnError(channel, -3, "Message not key scope is null");
            return;
        }

        if (scope.equals(Scope.DEVICE)) {
            String deviceId = (String) message.get("id");
            if (deviceId == null) {
                LOGGER.info("Message not key deviceId is null");
                returnError(channel, -3, "Message not key deviceId is null");
                return;
            }
            ActiveChannelGroup.sendGetGWInfoMessageByDeviceId(channel, deviceId);
        } else if (scope.equals(Scope.USER)) {
            String userId = (String) message.get("id");
            if (userId == null) {
                LOGGER.info("Message not key userId is null");
                returnError(channel, -3, "Message not key userId is null");
                return;
            }
            ActiveChannelGroup.sendGetGWInfoMessageByUserId(channel, userId);
        } else {
            LOGGER.info("Scope is not exist");
            returnError(channel, -3, "Scope is not exist");
        }
    }

    /**
     * 接收获取Sensor Info 消息处理
     *
     * @param channel
     * @param message
     */
    private void onReceiveGetSensorInfoMessage(String channel, Map message) {
        String scope = (String) message.get("scope");
        if (scope == null) {
            LOGGER.info("Message not key scope is null");
            returnError(channel, -3, "Message not key scope is null");
            return;
        }

        if (scope.equals(Scope.DEVICE)) {
            String deviceId = (String) message.get("id");
            if (deviceId == null) {
                LOGGER.info("Message not key deviceId is null");
                returnError(channel, -3, "Message not key deviceId is null");
                return;
            }
            ActiveChannelGroup.sendGetSensorInfoMessageByDeviceId(channel, deviceId);
        } else if (scope.equals(Scope.USER)) {
            String userId = (String) message.get("userId");
            if (userId == null) {
                LOGGER.info("Message not key userId is null");
                returnError(channel, -3, "Message not key userId is null");
                return;
            }
            ActiveChannelGroup.sendGetSensorInfoMessageByUserId(channel, userId);
        } else {
            LOGGER.info("Scope is not exist");
            returnError(channel, -3, "Scope is not exist");
        }
    }

	
    /**
     * 接受服务更新时候的处理
     * @param channel
     * @param message
     */
    public void onReceiveServiceUpdateMessage(String channel,Map message){
//    	String userId=message.get("userId").toString();    	
    	//通知网关
    	ActiveChannelGroup.sendServiceUpdateCommand(channel,message);
    }
    
    public void onReceiveTimerCurvMessage(String channel,Map message){
    	 String deviceId=(String) message.get("deviceId");
    	 if(deviceId==null || deviceId.equals("") || deviceId.equals("all")){
    		 return;
    	 }
    	ActiveChannelGroup.sendTimerCurvCommand(channel,message);
    }
      
    public void returnError(String channel, int errorCode,String message) {
        try {
            Command command = new Command(UUIDGenerator.randomUUID().toString());
            command.setAttribute("channel", channel);
            command.setAttribute("message", message);
            command.setAttribute("result", String.valueOf(errorCode));
            command.complete();
        } catch (Exception e) {
            LOGGER.error("returnError exception:", e);
        }
    }
       
    /**
     * 初始化订阅时候的处理
     *
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {

    }

    /**
     * 取消订阅时候的处理
     *
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {

    }

    /**
     * 取消按表达式的方式订阅时候的处理
     *
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    /**
     * 初始化按表达式的方式订阅时候的处理
     *
     * @param pattern
     * @param subscribedChannels
     */
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }
}
