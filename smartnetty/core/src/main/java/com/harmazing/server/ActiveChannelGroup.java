package com.harmazing.server;

import com.google.protobuf.ByteString;
import com.harmazing.Config;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.mapper.SysConfigMapper;
import com.harmazing.protobuf.CommandProtos;
import com.harmazing.protobuf.MessageProtos;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceServiceImpl;
import com.harmazing.util.AttributeKeyUtil;
import com.harmazing.util.DeviceLogUtil;
import com.harmazing.util.MessageUtil;

import io.netty.channel.Channel;
import io.netty.channel.group.*;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by ming on 14-9-11.
 */
public class ActiveChannelGroup {
    private final static ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("SERVER_CHANNEL", GlobalEventExecutor.INSTANCE);
    private final static Logger LOGGER = LoggerFactory.getLogger(ActiveChannelGroup.class);
    private final static AttributeKey<Session> sessionAttributeKey = AttributeKeyUtil.getSessionAttributeKey();
    private final static String sessionUserIdAttributeKey    = "userId";
    private final static String sessionGatewayMacAtrributeKey = "gw_mac";

    /**
     * 添加Channel
     *
     * @param channel
     * @return
     */
    public synchronized final static boolean addChannel(Channel channel) {
        boolean result = false;
        try {
            CHANNEL_GROUP.add(channel);
            return true;
        } catch (Exception e) {
            LOGGER.error("ADD CHANNEL ERROR", e);
        } finally {
            return result;
        }
    }

    /**
     * 移除Channel
     *
     * @param channel
     * @return
     */
    public synchronized final static boolean removeChannel(Channel channel) {
        boolean result = false;
        try {
        	Session session = channel.attr(sessionAttributeKey).get();
            //String gw_mac = (String)session.getAttribute(sessionGatewayMacAtrributeKey);            	
        	//DeviceService devSrv = new DeviceServiceImpl();
        	//devSrv.updateGWOprStatusByMac(gw_mac, 0, 0);
        	
            CHANNEL_GROUP.remove(channel);          
            
            return true;
        } catch (Exception e) {
            LOGGER.error("REMOVE CHANNEL ERROR", e);
        } finally {
            return result;
        }
    }

    /**
     * 根据Session Id 匹配 Channel
     *
     * @param sessionIds
     * @return
     */
    public synchronized final static ChannelMatcher getSessionChannelMatcher(final ArrayList<String> sessionIds) {
        return new ChannelMatcher() {
            @Override
            public boolean matches(Channel channel) {
                Session session = channel.attr(sessionAttributeKey).get();
                if (sessionIds.contains(session.getId())) {
//                    System.out.println("ChannelMatcher session:" + session.getId());
                    return true;
                }
                return false;
            }
        };
    }
    /**
     *匹配所有Channel
     */
    public synchronized final static ChannelMatcher getAllChannelMatcher() {
    	 return new ChannelMatcher() {
             @Override
             public boolean matches(Channel channel) {
                return true;
             }
         };
    }
    /**
     * 根据用户Id 匹配 Channel
     *
     * @param userIds
     * @return
     */
    public synchronized final static ChannelMatcher getUserChannelMatcher(final ArrayList<String> userIds) {
        return new ChannelMatcher() {
            @Override
            public boolean matches(Channel channel) {
                Session session = channel.attr(sessionAttributeKey).get();
                if (userIds.contains(session.getAttribute(sessionUserIdAttributeKey))) {
//                    System.out.println("ChannelMatcher user:" + session.getAttribute(sessionUserIdAttributeKey));
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * 根据Device Id 匹配 Channel
     *
     * @param deviceIds
     * @return
     */
    public synchronized final static ChannelMatcher getDeviceChannelMatcher(final ArrayList<String> deviceIds) {
        return new ChannelMatcher() {
            @Override
            public boolean matches(Channel channel) {
                Session session = channel.attr(sessionAttributeKey).get();
                String dev_id = session.getAttribute(sessionGatewayMacAtrributeKey).toString();
                if (deviceIds.contains(dev_id)) {
                	LOGGER.debug("#### ActiveChannelGroup.ChannelMatcher.maches, found matched channel with gw_mac(" + dev_id + "), do sending.");
                    return true;
                }
                return false;
            }
        };
    }

    public synchronized final static ChannelMatcher getDeviceChannelMatcher(final String gw_mac) {
        return new ChannelMatcher() {
            @Override
            public boolean matches(Channel channel) {
                Session session = channel.attr(sessionAttributeKey).get();
                String mac = session.getAttribute(sessionGatewayMacAtrributeKey).toString();
                if (gw_mac.toUpperCase().equals(mac.toUpperCase())) {
                	LOGGER.debug("**** ActiveChannelGroup.ChannelMatcher.maches, found matched channel with gw_mac(" + gw_mac + ") , do sending.");
                    return true;
                }
                
                return false;
            }
        };
    }

    /**
     * 发送消�?
     *
     * @param message
     * @param channelMatcher
     */
    public synchronized final static void writeAndFlush(Object message, ChannelMatcher channelMatcher) {
        ChannelGroupFuture channelFutures = CHANNEL_GROUP.writeAndFlush(message, channelMatcher);

        channelFutures.addListener(new ChannelGroupFutureListener() {
            @Override
            public void operationComplete(ChannelGroupFuture future) throws Exception {
                if (future.isPartialFailure()) {
                    LOGGER.info("FUTURE ISPARTIALFAILURE");
                }

                if (future.isDone()) {
                    LOGGER.info("FUTURE ISDONE");
                }
            }
        });
    }

    /**
     * 给设备发送Command
     *
     * @param sessionIds
     * @param commandType
     * @param stringParameter
     * @param intParameter
     * @return
     */
    public synchronized final static void sendCommandMessageBySession(final String channel,
                                                                      final ArrayList<String> sessionIds,
                                                                      DeviceSpecific.DeviceType devType,
                                                                      CommandProtos.AirConditionerControl.CommandType commandType,
                                                                      String stringParameter,
                                                                      Integer intParameter) {

        MessageProtos.SpmMessage spmMessage = MessageUtil.buildCommandMessage(null, devType, commandType, stringParameter, intParameter);
        ChannelMatcher channelMatcher = ActiveChannelGroup.getSessionChannelMatcher(sessionIds);

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给某个设备连接发送控制命�?
     *
     * @param sessionId
     * @param sensorId
     * @param commandType
     * @param stringParameter
     * @param intParameter
     */
    public synchronized final static void sendCommandMessageBySession(final String channel,
                                                                      final String sessionId,
                                                                      final String sensorId,
                                                                      final DeviceSpecific.DeviceType devType,
                                                                      CommandProtos.AirConditionerControl.CommandType commandType,
                                                                      String stringParameter,
                                                                      Integer intParameter) {
        ChannelMatcher channelMatcher = ActiveChannelGroup.getSessionChannelMatcher(new ArrayList<String>() {{
            add(sessionId);
        }});
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildCommandMessage(sensorId, devType, commandType, stringParameter, intParameter);

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给用户设备发送Command
     *
     * @param userIds
     * @param commandType
     * @param stringParameter
     * @param intParameter
     * @return
     */
    public synchronized final static void sendCommandMessageByUserId(final String channel,
                                                                     final ArrayList<String> userIds,
                                                                     final DeviceSpecific.DeviceType devType,
                                                                     CommandProtos.AirConditionerControl.CommandType commandType,
                                                                     String stringParameter,
                                                                     Integer intParameter) {
        ChannelMatcher channelMatcher = ActiveChannelGroup.getUserChannelMatcher(userIds);
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildCommandMessage(null, devType, commandType, stringParameter, intParameter);

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给用户设备发送Command
     *
     * @param userId
     * @param sensorId
     * @param commandType
     * @param stringParameter
     * @param intParameter
     * @return
     */
    public synchronized final static void sendCommandMessageByUserId(final String channel,
                                                                     final String userId,
                                                                     final String sensorId,
                                                                     DeviceSpecific.DeviceType devType,
                                                                     CommandProtos.AirConditionerControl.CommandType commandType,
                                                                     String stringParameter,
                                                                     Integer intParameter) {
        ChannelMatcher channelMatcher = ActiveChannelGroup.getUserChannelMatcher(new ArrayList<String>() {{
            add(userId);
        }});
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildCommandMessage(sensorId, devType, commandType, stringParameter, intParameter);

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给设备发送Command
     *
     * @param deviceIds
     * @param commandType
     * @param stringParameter
     * @param intParameter
     * @return
     */
    public synchronized final static void sendCommandMessageByDeviceId(final String channel,
                                                                       final ArrayList<String> deviceIds,
                                                                       final DeviceSpecific.DeviceType devType,
                                                                       final CommandProtos.AirConditionerControl.CommandType commandType,
                                                                       final String stringParameter,
                                                                       final Integer intParameter) {
        ChannelMatcher channelMatcher = ActiveChannelGroup.getDeviceChannelMatcher(deviceIds);
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildCommandMessage(null, devType, commandType, stringParameter, intParameter);

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 根据设备发送Command
     *
     * @param deviceId
     * @param sensorId
     * @param commandType
     * @param stringParameter
     * @param intParameter
     */
    public synchronized final static int sendCommandMessageByDeviceId(final String channel,
                                                                      final String gw_mac,
                                                                      final String sensor_id,
                                                                      DeviceSpecific.DeviceType dev_type,
                                                                      CommandProtos.AirConditionerControl.CommandType cmd_type,
                                                                      String str_param,
                                                                      Integer int_param) {
    	ChannelMatcher channelMatcher = ActiveChannelGroup.getDeviceChannelMatcher( gw_mac ); 
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildCommandMessage(sensor_id, dev_type, cmd_type, str_param, int_param);
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
        
        return spmMessage.getHeader().getSeqnum();
    }   
    
    
    
    ///////////////////////////////////////////////////////////
    public synchronized final static int sendIrControlMessage(final String channel,
                                                                     final String deviceId,
                                                                     final String sensorId,
                                                                     CommandProtos.IrControl.CommandType commandType,
                                                                     ByteString main,
                                                                     String length) {
        ChannelMatcher channelMatcher = ActiveChannelGroup.getDeviceChannelMatcher(new ArrayList<String>() {{
            add(deviceId);
        }});
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildIrControlMessage(sensorId, commandType, main, length);

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
        
        return spmMessage.getHeader().getSeqnum();
    }

    /////////////////////////////////////////////////////////////
    public synchronized final static int sendHALampControlMessage(final String channel,
    		                                                      final String gw_mac,
    													          final String sensorId,
    													          int illuminance,
    													          int red,
    													          int green,
    													          int blue){
    	ChannelMatcher channelMatcher = ActiveChannelGroup.getDeviceChannelMatcher( gw_mac ); 
    	MessageProtos.SpmMessage spmMessage = MessageUtil.buildHALampControlMessage(sensorId, illuminance, red, green, blue);
    	ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    	
    	return spmMessage.getHeader().getSeqnum();    	
    }
    
    
    ////////////////////////////////////////////////////////////
    public synchronized final static void forwardDebugMessage( final String gw_mac,
    														   final MessageProtos.SpmMessage spm_msg){
    	ChannelMatcher channelMatcher = ActiveChannelGroup.getDeviceChannelMatcher( gw_mac ); 
    	ActiveChannelGroup.writeAndFlush(new MiddleMessage(spm_msg, "debug_channel"), channelMatcher);   	
    }

    /**
     * 给多个连接发�?GET_GWINFO
     *
     * @param sessionIds
     */
    public synchronized final static void sendGetGWInfoMessageBySession(final String channel,
                                                                        final ArrayList<String> sessionIds) {
        final ChannelMatcher channelMatcher = ActiveChannelGroup.getSessionChannelMatcher(sessionIds);
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildGWInfoMessage();

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给一个连接发�?GET_GWINFO
     *
     * @param sessionId
     */
    public synchronized final static void sendGetGWInfoMessageBySession(final String channel,
                                                                        final String sessionId) {
        final ChannelMatcher channelMatcher = ActiveChannelGroup.getSessionChannelMatcher(new ArrayList<String>() {{
            add(sessionId);
        }});
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildGWInfoMessage();

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给用户发�?GET_GWINFO
     *
     * @param userIds
     */
    public synchronized final static void sendGetGWInfoMessageByUserId(final String channel,
                                                                       final ArrayList<String> userIds) {
        final ChannelMatcher channelMatcher = ActiveChannelGroup.getUserChannelMatcher(userIds);
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildGWInfoMessage();

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给用户发�?GET_GWINFO
     *
     * @param userId
     */
    public synchronized final static void sendGetGWInfoMessageByUserId(final String channel,
                                                                       final String userId) {
        final ChannelMatcher channelMatcher = ActiveChannelGroup.getUserChannelMatcher(new ArrayList<String>() {{
            add(userId);
        }});
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildGWInfoMessage();

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给设备发�?GET_GWINFO
     *
     * @param deviceIds
     */
    public synchronized final static void sendGetGWInfoMessageByDeviceId(final String channel,
                                                                         final ArrayList<String> deviceIds) {
        final ChannelMatcher channelMatcher = ActiveChannelGroup.getDeviceChannelMatcher(deviceIds);
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildGWInfoMessage();

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给设备发�?GET_GWINFO
     *
     * @param deviceId
     */
    public synchronized final static void sendGetGWInfoMessageByDeviceId(final String channel,
                                                                         final String deviceId) {
        final ChannelMatcher channelMatcher = ActiveChannelGroup.getDeviceChannelMatcher(new ArrayList<String>() {{
            add(deviceId);
        }});
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildGWInfoMessage();

        //write and flush
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }


    /**
     * 给多个连接发�?GET_SENSORINFO
     *
     * @param sessionIds
     */
    public synchronized final static void sendGetSensorInfoMessageBySession(final String channel,
                                                                            final ArrayList<String> sessionIds) {
        final ChannelMatcher channelMatcher = ActiveChannelGroup.getSessionChannelMatcher(sessionIds);
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildGetSensorInfoMessage();
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给一个连接发�?GET_SENSORINFO
     *
     * @param sessionId
     */
    public synchronized final static void sendGetSensorInfoMessageBySession(final String channel,
                                                                            final String sessionId) {
        sendGetSensorInfoMessageBySession(channel, new ArrayList<String>() {{
            add(sessionId);
        }});
    }

    /**
     * 给多个连接发�?GET_SENSORINFO
     *
     * @param userIds
     */
    public synchronized final static void sendGetSensorInfoMessageByUserId(final String channel,
                                                                           final ArrayList<String> userIds) {
        final ChannelMatcher channelMatcher = ActiveChannelGroup.getUserChannelMatcher(userIds);
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildGetSensorInfoMessage();
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给一个连接发�?GET_SENSORINFO
     *
     * @param userId
     */
    public synchronized final static void sendGetSensorInfoMessageByUserId(final String channel,
                                                                           final String userId) {
        ActiveChannelGroup.sendGetSensorInfoMessageByUserId(channel,new ArrayList<String>() {{
            add(userId);
        }});
    }

    /**
     * 给多个连接发�?GET_SENSORINFO
     * @param deviceIds
     */
    public synchronized final static void sendGetSensorInfoMessageByDeviceId(final String channel,
                                                                             final ArrayList<String> deviceIds) {
        final ChannelMatcher channelMatcher = ActiveChannelGroup.getDeviceChannelMatcher(deviceIds);
        MessageProtos.SpmMessage spmMessage = MessageUtil.buildGetSensorInfoMessage();
        ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }

    /**
     * 给一个连接发�?GET_SENSORINFO
     *
     * @param deviceId
     */
    public synchronized final static void sendGetSensorInfoMessageByDeviceId(final String channel,
                                                                             final String deviceId) {
        ActiveChannelGroup.sendGetSensorInfoMessageByDeviceId(channel, new ArrayList<String>() {{
            add(deviceId);
        }});
    }
    /**
     * 根据用户给网关发送服务更新信�?
     * @param channel
     * @param map
     */
    public synchronized final static void sendServiceUpdateCommand(final String channel,final Map parameter){
    	
    	int type=Integer.parseInt(parameter.get("commandType").toString());
    	CommandProtos.ServiceUpdate.CommandType commandType=MessageUtil.getServiceUpdateCommandType(type);
    	
    	
    	
    	MessageProtos.SpmMessage spmMessage=MessageUtil.buildServiceUpdateMessage(parameter);		
		if(commandType.equals(CommandProtos.ServiceUpdate.CommandType.LOG_REPORT_INTERVAL_CHG)
				|| commandType.equals(CommandProtos.ServiceUpdate.CommandType.LOG_INTERVAL_CHG)){
			int interval=Integer.parseInt(parameter.get("interval").toString());
	    	Config.getInstance().setSysConfig();
			ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), ActiveChannelGroup.getAllChannelMatcher());
		}else if(commandType.equals(CommandProtos.ServiceUpdate.CommandType.FIRMWARE_AVAILABLE)){
			ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), ActiveChannelGroup.getAllChannelMatcher());
		}else if(commandType.equals(CommandProtos.ServiceUpdate.CommandType.DSM_REQUEST)){
			
			/****************
			String areaId=parameter.get("areaId").toString();
			Config.getInstance().setDsmTempByAreaId(areaId);
	    	DeviceServiceImpl ds=new DeviceServiceImpl();
	    	ArrayList<String> sessions=null;
	    	if(!commandType.equals(CommandProtos.ServiceUpdate.CommandType.DEVICE_BINDING_CHG)){
	    		sessions=(ArrayList<String>) ds.getSessionsByArea(areaId);
	    	}	    	
			ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), ActiveChannelGroup.getSessionChannelMatcher(sessions));
			****************/			
			String deviceId=parameter.get("deviceId").toString();
			String lowTemp=parameter.get("lowTemp").toString();
			String upperTemp=parameter.get("upperTemp").toString();
			
			//更新到Reids
			Jedis jedis=RedisContextFactory.getInstance().getJedis();
			jedis.set(RedisContext.Device_DSM_PREFIX+deviceId,lowTemp+"-"+upperTemp);
			RedisContextFactory.returnJedis(jedis);
			
			ArrayList<String> deviceIds=new ArrayList<String>();
			deviceIds.add(deviceId);
			ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), ActiveChannelGroup.getDeviceChannelMatcher(deviceIds));
			
			
		}		
		else if(commandType.equals(CommandProtos.ServiceUpdate.CommandType.DEVICE_BINDING_CHG)){
			String userId=parameter.get("userId").toString();
			DeviceServiceImpl ds=new DeviceServiceImpl();
	    	//更新设备到redis缓存
	    	ds.updateGWInfoToRedis(userId);
//			ds.updateGWInfoToCache(userId);
	    	ArrayList userIds=new ArrayList();
	    	userIds.add(userId);	    	
	    	final ChannelMatcher channelMatcher = ActiveChannelGroup.getUserChannelMatcher(userIds);
	    	/*****如果是设备有变化，需要先打开网络***
	    	MessageProtos.SpmMessage networkMessage=MessageUtil.buildGateWayControlMessage(parameter);
	    	ActiveChannelGroup.writeAndFlush(new MiddleMessage(networkMessage,channel), channelMatcher);
	    	*****
	    	/********发送打开命令之后，在发送服务更�?*******/
			ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
		}
    }
    /**
     * 设置用户定时信息
     * @param channel
     * @param parameter
     */
    public synchronized final static void sendTimerCurvCommand(final String channel,final Map parameter){
//    	String deviceId=parameter.get("deviceId").toString();
//    	ArrayList deviceIds=new ArrayList();
//    	deviceIds.add(deviceId);
//    	final ChannelMatcher channelMatcher = ActiveChannelGroup.getDeviceChannelMatcher(deviceIds);
    	String userId=parameter.get("userId").toString();
    	ArrayList userIds=new ArrayList();
    	userIds.add(userId);
    	final ChannelMatcher channelMatcher = ActiveChannelGroup.getUserChannelMatcher(userIds);
    	MessageProtos.SpmMessage spmMessage=MessageUtil.buildTimeCurvMessage(parameter);
    	ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    }
    
    /**
     * 发送网关控制信�?
     * @param channel
     * @param parameter
     */
    public synchronized final static int sendGateWayControlCommand(final String channel,final Map parameter){
    	String devId = parameter.get("gwId").toString();
    	ArrayList<String> devIds = new ArrayList<String>();
    	devIds.add(devId);
    	final ChannelMatcher channelMatcher = ActiveChannelGroup.getDeviceChannelMatcher(devIds);
    	MessageProtos.SpmMessage spmMessage=MessageUtil.buildGateWayControlMessage(parameter);
    	ActiveChannelGroup.writeAndFlush(new MiddleMessage(spmMessage, channel), channelMatcher);
    	
    	return spmMessage.getHeader().getSeqnum();
    }
}
