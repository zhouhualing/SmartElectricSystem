package com.harmazing.server;

import com.google.protobuf.TextFormat;
import com.harmazing.cache.OpenNetworkCacheElem;
import com.harmazing.Thread.DeviceHistoricStatusThread;
import com.harmazing.Thread.IrPacketHandlerThread;
import com.harmazing.cache.ActiveCommandQueue;
import com.harmazing.cache.OpenNetworkCache;
import com.harmazing.entity.AirCondition;
import com.harmazing.entity.Device;
import com.harmazing.entity.ZigbeeOO;
import com.harmazing.entity.status.HumidityTemperatureStatus;
import com.harmazing.entity.status.PirStatus;
import com.harmazing.entity.status.WinDoorStatus;
import com.harmazing.mq.Command;
import com.harmazing.protobuf.*;
import com.harmazing.protobuf.CommandProtos.LampColor;
import com.harmazing.protobuf.DebugProtos.Connect;
import com.harmazing.protobuf.DebugProtos.Debug;
import com.harmazing.protobuf.DebugProtos.Text;
import com.harmazing.protobuf.DeviceProtos.DeviceAnnouncement;
import com.harmazing.protobuf.DeviceProtos.DeviceOnline;
import com.harmazing.protobuf.DeviceProtos.DeviceOnline.DeviceVersion;
import com.harmazing.protobuf.DeviceProtos.DeviceOnline.DoorWindowSensorOnline;
import com.harmazing.protobuf.DeviceProtos.DeviceOnline.InnolinksAcOnline;
import com.harmazing.protobuf.DeviceProtos.DeviceOnline.InnolinksAcPowerSocketOnline;
import com.harmazing.protobuf.DeviceProtos.DeviceOnline.InnolinksThermostatOnline;
import com.harmazing.protobuf.DeviceProtos.DeviceOnline.PirSensorOnline;
import com.harmazing.protobuf.DeviceProtos.DeviceOnline.TemperatureHumiditySensorOnline;
import com.harmazing.protobuf.DeviceProtos.DeviceOnline.ZigbeeHALampOnline;
import com.harmazing.protobuf.DeviceProtos.DeviceOnline.ZigbeeOoElectricityMeterOnline;
import com.harmazing.protobuf.DeviceProtos.DeviceOnline.ZigbeeOoOnline;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific.DeviceType;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;
import com.harmazing.protobuf.SensorProtos.RadioInfo;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceExceptionServiceImpl;
import com.harmazing.service.impl.DeviceServiceImpl;
import com.harmazing.util.AirconUtil;
import com.harmazing.util.AttributeKeyUtil;
import com.harmazing.util.DeviceLogUtil;
import com.harmazing.util.UUIDGenerator;
import com.harmazing.util.UdpLogger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import org.apache.commons.lang.NullArgumentException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DefaultServerHandler extends ChannelInboundHandlerAdapter {
	public final static Logger LOGGER = LoggerFactory.getLogger(DefaultServerHandler.class);
	public static long connections = 0;
	
	private ExecutorService irFixedThreadPool = null;
	private ActiveCommandQueue queue = null;
	private OpenNetworkCache openNetworkCache = null;
	private DeviceHistoricStatusThread devHistoricsStatus = null;
	
	
	
	public DefaultServerHandler(ActiveCommandQueue queue, OpenNetworkCache cache){
		this.queue = queue;
		this.openNetworkCache = cache;
		this.devHistoricsStatus =  new DeviceHistoricStatusThread();
		this.devHistoricsStatus.start();
				
		irFixedThreadPool = Executors.newFixedThreadPool(20);
	}
	

	/////////////////////////////////////////////////////////////////////////
	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		connections++;
		LOGGER.info("DefaultServerHandler.channelActive.connections = " + connections);
	}

    ///////////////////////////////////////////////////////////////////////
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		connections--;
		connections = connections<0 ? 0 : connections;	
		
		if(ctx != null)
			ctx.close();
		
		LOGGER.info("DefaultServerHandler.channelInactive.connections = " + connections);
	}

	///////////////////////////////////////////////////////////////////////
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			MessageProtos.SpmMessage message = (MessageProtos.SpmMessage) msg;
			MessageProtos.SpmMessage.Header header = message.getHeader();

			if (header.getType() == MessageProtos.SpmMessage.MsgType.GWINFO_RESPONSE) {
				//Deprecated message in v2.0
				//handleGWInfoRespone(ctx, message);
			} else if (header.getType() == MessageProtos.SpmMessage.MsgType.SENSORINFO_RESPONSE) {
				// Deprecated message in v2.0
				//handleSensorInfoRespone(ctx, message);
			} else if (header.getType() == MessageProtos.SpmMessage.MsgType.LOGREPORT) {
				handleLogReport(ctx, message);
			} else if (header.getType() == MessageProtos.SpmMessage.MsgType.EVENTREPORT) {
				handleEventReport(ctx, message);
			} else if (header.getType() == MessageProtos.SpmMessage.MsgType.HEARTBEAT) {
				handleHeartbeat(ctx, message);
			} else if (header.getType() == MessageProtos.SpmMessage.MsgType.COMMAND_RESPONSE) {
				handleCommandResponse(ctx, message);
			} else if(header.getType() == MessageProtos.SpmMessage.MsgType.DEVICE_SPECIFIC){
				handleDeviceSpecific(ctx, message);
			} else {
				handleUnsupportedMessage(ctx, message);
			}

		} catch (Exception e) {
			LOGGER.error("channelRead error, with exception:\n", e);
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	//////////////////////////////////////////////////////////////
	private void handleDeviceSpecific(ChannelHandlerContext ctx, MessageProtos.SpmMessage message) {
		LOGGER.debug("Enter DefaultServerHandler.handleDeviceSpecific, message=" + TextFormat.printToString(message));
		
		if(!message.hasDeviceSpecific()){
			LOGGER.debug("No DeviceSpecific included, return.");
			return;
		}
		
		MessageProtos.SpmMessage.Header header = message.getHeader();
		int seqnum = header.getSeqnum();
		
		DeviceSpecific dev_spec = message.getDeviceSpecific();
		DeviceSpecific.DeviceType type = dev_spec.getDeviceType();		
		if(type == DeviceSpecific.DeviceType.INNOLINKS_AC ||
				type == DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET || 
				type == DeviceSpecific.DeviceType.INNOLINKS_THERMOSTAT ||
				type == DeviceSpecific.DeviceType.ZIGBEE_OO ||
				type == DeviceSpecific.DeviceType.DOOR_WINDDOW_SENSOR ||
				type == DeviceSpecific.DeviceType.PIR_SENSOR ||
				type == DeviceSpecific.DeviceType.TEMPERATURE_HUMIDITY_SENSOR ||
				type == DeviceSpecific.DeviceType.ZIGBEE_HA_LAMP ||
				type == DeviceSpecific.DeviceType.ZIGBEE_OO_ELECTRICITY_METER ){
		
			handleAcSpecific(ctx, seqnum, dev_spec);
		}else{
			LOGGER.error("RESERVED or Invalid device type, do nothing.");
		}
		
		LOGGER.debug("Exit DefaultServerHandler.handleDeviceSpecific");
	}
	
	
	//////////////////////////////////////////////////////////////
	private void handleAcSpecific(ChannelHandlerContext ctx, int seqnum, DeviceSpecific spec){
		LOGGER.debug("Enter DefaultServerHandler.handleAcSpecific");
		
		DeviceService dev_srv = new DeviceServiceImpl();		
		String dev_mac    = spec.getEui64();
		
		DeviceSpecific.DeviceType dev_type  = spec.getDeviceType();
		DeviceSpecific.MessageType msg_type = spec.getMessageType();
		if(msg_type == DeviceSpecific.MessageType.DEVICE_ANNOUNCEMENT){
			if( spec.hasDeviceAnnouncement()){	
				
				Session session = ctx.channel().attr(AttributeKeyUtil.getSessionAttributeKey()).get();
				String gw_mac = (String) session.getAttribute("gw_mac");
				OpenNetworkCacheElem elem = openNetworkCache.get(gw_mac);
				if( elem == null){
					LOGGER.warn("No matched gateway, try to run OpenNetwork again.");
					return;
				}
				
				dev_srv.deleteUserDevice(dev_mac);	
				//String key = RedisContext.DEVICE_CTL_PROP_AC + dev_mac;
				//DeviceLogUtil.removeDeviceFromRedis(key);
				
				DeviceAnnouncement anno = spec.getDeviceAnnouncement();	
				String sn     = "";
				String dev_id = "";
				String software  = anno.getSoftwareVersion();
				String hardware  = anno.getHardwareVersion();
				String vendor = anno.getManufacturerName();
				String model  = anno.getModelName();
				
				DeviceSpecific.DeviceType type = spec.getDeviceType();		
				dev_srv.createDevice(type,
						          UUID.randomUUID().toString(), 
						 	      sn, 
					              dev_mac, 
					              software, 
					              hardware, 
					              model, 
					              vendor, 
					              spec.getDeviceType().getNumber(),  // INNOLINKS_AC_POWER_SOCKET
					              0,
					              1); // onOff=0 means OFF status	
		
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", UUID.randomUUID().toString());
				params.put("device_mac", dev_mac);
				params.put("device_type", spec.getDeviceType().getNumber());
				params.put("is_primary", 1);
				params.put("user_code",  elem.userCode);	
				params.put("gw_mac", gw_mac);
				
				dev_srv.handleDeviceSpecific(params, spec.getDeviceType());	
						
	
				LOGGER.debug("DefaultServerHandler.handleAcSpecific.Device_Annousement.");
			}			
		}else if(msg_type == DeviceSpecific.MessageType.DEVICE_ONLINE){			
			DeviceOnline ol = spec.getDeviceOnline();
			if( ol.hasInnolinksAcOnline()){
				InnolinksAcOnline ac_ol = ol.getInnolinksAcOnline();
				int onOff = ac_ol.getOnOff() ? 1 : 0;
				int operStatus = 1;
				int mode       = ac_ol.getOperationMode();
				int acTemp     = ac_ol.getTemperature();
				int speed      = ac_ol.getFanSpeed();
				int energy     = ac_ol.getCurrentEnergy();
				
				dev_srv.updateAcStatusByAcMac(dev_mac, onOff, operStatus, mode, acTemp, speed, energy);
				LOGGER.debug("DefaultServerHandler.handleAcSpecific.InnolinksAcOnline");
			}
			
			if(ol.hasInnolinksAcPowerSocketOnline()){
				InnolinksAcPowerSocketOnline socket_ol = ol.getInnolinksAcPowerSocketOnline();
				
				AirCondition ac = dev_srv.getAcByAcMac(dev_mac);
				if(ac == null || ac.getOnOff() == null){
					LOGGER.error("DefaultServerHandler.handleAcSpecific.InnolinksAcPowerSocketOnline ac does not exists or onOff status=null");
					return;
				}
				
				dev_srv.updateOpstatus(dev_mac, ac.getOnOff(), 1 );	
				LOGGER.debug("DefaultServerHandler.handleAcSpecific.InnolinksAcPowerSocketOnline");
			}
			
			if( ol.hasInnolinksThermostatOnline()){
				InnolinksThermostatOnline ts_ol = ol.getInnolinksThermostatOnline();
				int onOff = ts_ol.getOnOff() ? 1 : 0;
				int operStatus = 1;
				int mode       = ts_ol.getOperationMode();
				int temp       = ts_ol.getTemperature();
				int speed      = ts_ol.getFanSpeed();
				int energy     = 0;
				dev_srv.updateAcStatusByAcMac(dev_mac, onOff, operStatus, mode, temp, speed, energy);
				LOGGER.debug("DefaultServerHandler.handleAcSpecific.InnolinksThermostatOnline");
			}
			
			if( ol.hasZigbeeOoOnline()){
				ZigbeeOoOnline oo_ol = ol.getZigbeeOoOnline();
				int onOff = oo_ol.getOnOff() ? 1 : 0;
				int operStatus = 1;
				dev_srv.updateZigbeeOOStatusByMac(dev_mac, onOff, operStatus);
				LOGGER.debug("DefaultServerHandler.handleAcSpecific.ZigbeeOoOnline");
			}
			
			if(ol.hasDoorWindowSensorOnline()){
				DoorWindowSensorOnline dw_ol = ol.getDoorWindowSensorOnline();
				int onOff = dw_ol.getOpen() ? 1 : 0;
				int operStatus = 1;
				dev_srv.updateWinDoorStatusByMac(dev_mac, onOff, operStatus);
				
				WinDoorStatus wds = new WinDoorStatus();
				wds.setId(UUID.randomUUID().toString());
				wds.setMac(dev_mac);
				wds.setOpen(onOff);
				devHistoricsStatus.enqueue(DeviceType.DOOR_WINDDOW_SENSOR, wds);
					
				LOGGER.debug("DefaultServerHandler.handleAcSpecific.WindowSensorOnline");
			}
			
			if( ol.hasPirSensorOnline()){
				PirSensorOnline pir_ol = ol.getPirSensorOnline();
				int onOff = pir_ol.getAlarmed() ? 1 : 0;
				int operStatus = 1;
				dev_srv.updateWinDoorStatusByMac(dev_mac, onOff, operStatus);
				
				PirStatus ps = new PirStatus();
				ps.setId(UUID.randomUUID().toString());
				ps.setMac(dev_mac);
				ps.setAlarmed(onOff);
				devHistoricsStatus.enqueue(DeviceType.PIR_SENSOR, ps);
				
				LOGGER.debug("DefaultServerHandler.handleAcSpecific.PirSensorOnline");
			}
			
			if( ol.hasTemperatureHumiditySensorOnline()){
				TemperatureHumiditySensorOnline th_ol = ol.getTemperatureHumiditySensorOnline();
				int temp = th_ol.getTemperature();
				int humidity = th_ol.getHumidity();				
				dev_srv.updateTempAndHumidityByMac(dev_mac, temp, humidity);
				
				HumidityTemperatureStatus hts = new HumidityTemperatureStatus();
				hts.setId(UUID.randomUUID().toString());
				hts.setMac(dev_mac);
				hts.setTemp(temp);
				hts.setHumidity(humidity);
				devHistoricsStatus.enqueue(DeviceType.TEMPERATURE_HUMIDITY_SENSOR, hts);
				
				LOGGER.debug("DefaultServerHandler.handleAcSpecific.TemperatureHumiditySensorOnline");
			}
			
			if( ol.hasZigbeeOoElectrocityMeterOnline()){
				ZigbeeOoElectricityMeterOnline em_ol = ol.getZigbeeOoElectrocityMeterOnline();
				int on_off = (em_ol.getOnOff() ? 1 : 0);
				dev_srv.updateElecMaterIntField(dev_mac, "onOff", on_off);
				
				
			}
			
			if(ol.hasZigbeeHaLampOnline()){
				ZigbeeHALampOnline ha_ol = ol.getZigbeeHaLampOnline();
				
				int onOff = ha_ol.getOnOff() ? 1 : 0;
				int operStatus = 1;
				int illuminance = ha_ol.getIlluminance();
				int red = ha_ol.getRed();
				int green = ha_ol.getGreen();
				int blue = ha_ol.getBlue();
				
				dev_srv.updateHALampAll(dev_mac, onOff, operStatus, illuminance, red, green, blue);
				LOGGER.debug("DefaultServerHandler.handleAcSpecific.ZigbeeHALampOnline");
			}
			
			if( ol.hasDeviceVersion()){
				DeviceVersion ver = ol.getDeviceVersion();
				
				String sw_ver = ver.getSoftwareVersion();
				String hd_ver = ver.getHardwareVersion();
				
				dev_srv.updateDeviceVersion( dev_type, dev_mac, sw_ver, hd_ver);
			}
			
		}else if(msg_type == DeviceSpecific.MessageType.DEVICE_OFFLINE){
			if(dev_type == DeviceSpecific.DeviceType.INNOLINKS_GATEWAY ){
				String ip_address = ctx.channel().remoteAddress().toString();
				ip_address = ip_address.substring(1, ip_address.lastIndexOf(":"));
				
				dev_srv.updateGWOprStatusByMac(dev_mac, 0, 0, ip_address);
				
			}else if(dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC || 
					dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET ||
					dev_type == DeviceSpecific.DeviceType.INNOLINKS_THERMOSTAT ){	
				dev_srv.updateACIntField(dev_mac, "operStatus", 0);
				
				
			}else if(dev_type == DeviceSpecific.DeviceType.ZIGBEE_OO){
				dev_srv.updateOODevIntField(dev_mac, "operStatus", 0);
				
			}else if(dev_type == DeviceSpecific.DeviceType.ZIGBEE_OO_ELECTRICITY_METER){
				// TODO
				
			}else if(dev_type == DeviceSpecific.DeviceType.PIR_SENSOR){
				dev_srv.updatePirtatusByMac(dev_mac, 0, 0);
				
			}else if(dev_type == DeviceSpecific.DeviceType.DOOR_WINDDOW_SENSOR){
				dev_srv.updateWinDoorStatusByMac(dev_mac, 0, 0);
				
			}else if(dev_type == DeviceSpecific.DeviceType.TEMPERATURE_HUMIDITY_SENSOR){
				// Nothing
				
			}else if(dev_type == DeviceSpecific.DeviceType.LIGHT_SWITCH){
				// TODO
				
			}else if(dev_type == DeviceSpecific.DeviceType.TEMPERATURE_HUMIDITY_SENSOR){
				// TODO
				
			}
			LOGGER.debug("DefaultServerHandler.handleAcSpecific.DEVICE_OFFLINE, MAC(" + dev_mac + ")");
		}else{
			LOGGER.error("Invalid message type, do nothing.");
		}
		
		LOGGER.debug("Exit DefaultServerHandler.handleAcSpecific");
	}
	
   
	//////////////////////////////////////////////////////////////
	private void handleCommandResponse(ChannelHandlerContext ctx, MessageProtos.SpmMessage message) {
		if (!message.hasControlCommandResponse()) {
			LOGGER.error("Invalid ControlCommandResponse, do nothing.");
			return;
		}
		
		try {
			MessageProtos.SpmMessage.Header header = message.getHeader();
			int seq_num = header.getSeqnum();
			CommandProtos.CommandResponse cmd_resp = message.getControlCommandResponse();
			int ret_code = cmd_resp.getReturnCode();
			queue.call(seq_num, ret_code);
			
		} catch (Exception e) {
			LOGGER.error("translate error!", e);
		}
	}

	/////////////////////////////////////////////////////////////////////
	private void translate(ChannelHandlerContext ctx, int seqnum, String returnCode) {
		Session session = this.getSession(ctx);

		Command command = new Command(session.getId(), seqnum);
		command.setAttribute("result", returnCode);
		try {
			command.complete();
		} catch (IOException e) {
			LOGGER.error("handleCommandResponse Exception :", e);
		}
	}

	/////////////////////////////////////////////////////////////////////
	private void handleHeartbeat(ChannelHandlerContext ctx, MessageProtos.SpmMessage message) {
		MessageProtos.SpmMessage.Builder spm_builder = MessageProtos.SpmMessage.newBuilder();
		MessageProtos.SpmMessage.Header.Builder head_builder = MessageProtos.SpmMessage.Header.newBuilder();
		head_builder.setType( MessageProtos.SpmMessage.MsgType.HEARTBEAT_RESPONSE);
		head_builder.setSeqnum(message.getHeader().getSeqnum());
		head_builder.setSession(message.getHeader().getSession());
		
		spm_builder.setHeader( head_builder);										
		ctx.writeAndFlush(spm_builder.build());	
		
		LOGGER.debug("BEATHEART");			
	}

	////////////////////////////////////////////////////////////////////
	private void handleEventReport(ChannelHandlerContext ctx, MessageProtos.SpmMessage message) {
		LOGGER.debug("Enter DefaultServerHandler.handleEventReport");
		if (!message.hasEventReport()) {
			LOGGER.error("Invalid event report, do nothing.");			
			return;
		}
		
		try {
			Session session = ctx.channel().attr(AttributeKeyUtil.getSessionAttributeKey()).get();
			String gw_mac = (String) session.getAttribute("gw_mac");
			if (gw_mac == null) {
				return;
			}	
			
			EventProtos.EventReport eventReport = message.getEventReport();
			List<EventProtos.Event> eventsList = eventReport.getEventsList();
			Iterator<EventProtos.Event> iterator = eventsList.iterator();
			while (iterator.hasNext()) {
				EventProtos.Event event = iterator.next();
				EventProtos.Event.EventType eventType = event.getType();
				String dev_mac = event.getSource();
				
				DeviceService devSrv = new DeviceServiceImpl();							

				if (eventType == EventProtos.Event.EventType.SENSOR_JOIN) {
					LOGGER.debug("Event SENSOR_JOIN, no handle currently.");
					
				} else if (eventType == EventProtos.Event.EventType.SENSOR_LEAVE) {
					LOGGER.debug("Event SENSOR_LEAVE, no handle currently");
					
				} else if (eventType == EventProtos.Event.EventType.SENSOR_PAIR) {
					LOGGER.debug("Event SENSOR_PAIR, no handle currently.");
					
				} else if (eventType == EventProtos.Event.EventType.SENSOR_UNPAIR) {
					LOGGER.debug("Event SENSOR_UNPAIR, no handle currently");
					
				} else if (eventType == EventProtos.Event.EventType.SENSOR_PAIR_REQUEST) {
					LOGGER.debug("Event SENSOR_PAIR_REQUEST, no handle currently");
					
				} else if (eventType == EventProtos.Event.EventType.SENSOR_AC_ON) {
					LOGGER.debug("Event SENSOR_AC_ON");
					//DeviceLogUtil.updateAcIntInfo2Redis(dev_mac, "onOff", 1);
					devSrv.updateACIntField(dev_mac, "onOff", 1);
					
				} else if (eventType == EventProtos.Event.EventType.SENSOR_AC_OFF) {
					LOGGER.debug("Event SENSOR_AC_OFF");
					//DeviceLogUtil.updateAcIntInfo2Redis(dev_mac, "onOff", 0);
					devSrv.updateACIntField(dev_mac, "onOff", 0);
			
				} else if (eventType == EventProtos.Event.EventType.SENSOR_AC_MODE_CHANGED) {
					LOGGER.debug("Event SENSOR_AC_MODE_CHANGED");
					if (event.hasIntParameter()) {
						//DeviceLogUtil.updateAcIntInfo2Redis( dev_mac, "mode", event.getIntParameter());
						devSrv.updateACIntField(dev_mac, "mode", event.getIntParameter());
					}
					
				} else if (eventType == EventProtos.Event.EventType.SENSOR_AC_WIND_CHANGED) {
					LOGGER.debug("Event SENSOR_AC_WIND_CHANGED");
					if (event.hasIntParameter()) {
						//DeviceLogUtil.updateAcIntInfo2Redis( dev_mac, "speed", event.getIntParameter());
						devSrv.updateACIntField(dev_mac, "speed", event.getIntParameter());
					}
					
				} else if (eventType == EventProtos.Event.EventType.SENSOR_AC_LOCAL_TEMP_CHANGED) {
					LOGGER.debug("Event SENSOR_AC_LOCAL_TEMP_CHANGED");
					if (event.hasIntParameter()) {
						//DeviceLogUtil.updateAcIntInfo2Redis( dev_mac,"temp", event.getIntParameter());
						devSrv.updateACIntField(dev_mac, "temp", event.getIntParameter());
					}
					
				} else if (eventType == EventProtos.Event.EventType.SENSOR_AC_TARGET_TEMP_CHANGED) {
					LOGGER.debug("Event SENSOR_AC_TARGET_TEMP_CHANGED");
					if (event.hasIntParameter()) {
						//DeviceLogUtil.updateAcIntInfo2Redis(dev_mac,"acTemp", event.getIntParameter());
						devSrv.updateACIntField(dev_mac, "acTemp", event.getIntParameter());
					}
					
				} else if (eventType == EventProtos.Event.EventType.SENSOR_OPEN) {
					LOGGER.debug("Event SENSOR_WINDOOR_OPEN");
					devSrv.updateWinDoorFieldByMac(dev_mac, "onOff", 1);
										
					WinDoorStatus wds = new WinDoorStatus();
					wds.setId(UUID.randomUUID().toString());
					wds.setMac(dev_mac);
					wds.setOpen(1);
					devHistoricsStatus.enqueue(DeviceType.DOOR_WINDDOW_SENSOR, wds);
					
				} else if (eventType == EventProtos.Event.EventType.SENSOR_CLOSE) {
					LOGGER.debug("Event SENSOR_WINDOOR_CLOSE");		
					devSrv.updateWinDoorFieldByMac(dev_mac, "onOff", 0);
					
					WinDoorStatus wds = new WinDoorStatus();
					wds.setId(UUID.randomUUID().toString());
					wds.setMac(dev_mac);
					wds.setOpen(0);
					devHistoricsStatus.enqueue(DeviceType.DOOR_WINDDOW_SENSOR, wds);
					
				}else if(eventType==EventProtos.Event.EventType.SENSOR_AC_EXCEPTION){					
					LOGGER.debug("Event SENSOR_AC_EXCEPTION");
					if(event.hasAcException()){
						byte[] bytes=event.getAcException().toByteArray();
						String exceptionCode="";
						if(bytes[0]==0){
							exceptionCode="E0";
						}else{
							exceptionCode=new String(bytes,1,bytes.length-1);
						}

						String deviceMac=event.getSource();						
						DeviceExceptionServiceImpl eptnService=new DeviceExceptionServiceImpl();
						String id=UUIDGenerator.randomUUID();
						eptnService.saveorupdateException(id,deviceMac,exceptionCode);						
					}
					
				} else if (eventType == EventProtos.Event.EventType.IR_CODE_RECEIVED){
					CommandProtos.IrPacket irPacket = event.getIrPacket();

					MessageProtos.SpmMessage.Header header = message.getHeader();
					IrPacketHandlerThread ir_thread = new IrPacketHandlerThread(ctx, irPacket, header.getSeqnum(), header.getSession(), dev_mac);
					irFixedThreadPool.execute( ir_thread);
					
				}else if(eventType == EventProtos.Event.EventType.SENSOR_OO_ON){					
					devSrv.updateZigbeeOOStatusByMac(dev_mac, 1, 1);
					LOGGER.debug("Event SENSOR_OO_ON");
					
				}else if(eventType == EventProtos.Event.EventType.SENSOR_OO_OFF){
					devSrv.updateZigbeeOOStatusByMac(dev_mac, 0, 1);
					LOGGER.debug("Event SENSOR_OO_OFF");
					
				}else if( eventType == EventProtos.Event.EventType.SENSOR_TEMPERATURE){
					if(event.hasIntParameter()){
						int temp = event.getIntParameter();
						devSrv.updateTHIntField(dev_mac,  "temp", temp);
						
						HumidityTemperatureStatus hts = new HumidityTemperatureStatus();
						hts.setId(UUID.randomUUID().toString());
						hts.setMac(dev_mac);
						hts.setTemp(temp);
						hts.setHumidity(0);
						devHistoricsStatus.enqueue(DeviceType.TEMPERATURE_HUMIDITY_SENSOR, hts);
					}
					LOGGER.debug("Event.SENSOR_TEMPERATURE.");
					
				}else if( eventType == EventProtos.Event.EventType.SENSOR_HUMIDITY){
					if(event.hasIntParameter()){
						int humidity = event.getIntParameter();
						devSrv.updateTHIntField(dev_mac,  "humidity", humidity);
						
						HumidityTemperatureStatus hts = new HumidityTemperatureStatus();
						hts.setId(UUID.randomUUID().toString());
						hts.setMac(dev_mac);
						hts.setTemp(0);
						hts.setHumidity(humidity);
						devHistoricsStatus.enqueue(DeviceType.TEMPERATURE_HUMIDITY_SENSOR, hts);
						
					}
					LOGGER.debug("Event.SENSOR_TEMPERATURE.");
					
				}else if( eventType == EventProtos.Event.EventType.SENSOR_LAMP_COLOR){
					if(event.hasColor()){
						LampColor lc = event.getColor();
						int illuminance = lc.getIlluminance();
						int red = lc.getRed();
						int green = lc.getGreen();
						int blue = lc.getBlue();
						devSrv.updateHALampColor(dev_mac, illuminance, red, green, blue);
					}
					LOGGER.debug("Event.SENSOR_LAMP_COLOR.");
					
				}else if(eventType == EventProtos.Event.EventType.SENSOR_ALARMED) {
					devSrv.updatePirIntField(dev_mac, "onOff", 1);
					
					PirStatus ps = new PirStatus();
					ps.setId(UUID.randomUUID().toString());
					ps.setMac(dev_mac);
					ps.setAlarmed(1);
					devHistoricsStatus.enqueue(DeviceType.PIR_SENSOR, ps);
					
					
				}else {
					LOGGER.debug("this event type is not support");
				}
			} 
		} catch (Exception e) {
			LOGGER.error("UNKNOW EXCEPTION", e);
		}
		
		LOGGER.debug("Enter DefaultServerHandler.handleEventReport");
	}

	////////////////////////////////////////////////////////////////////////
	private void handleLogReport(ChannelHandlerContext ctx, MessageProtos.SpmMessage message) {
		if (!message.hasLogReport()) {
			return;
		}
		
		Session session = ctx.channel().attr(AttributeKeyUtil.getSessionAttributeKey()).get();
		String gw_id = (String) session.getAttribute("gw_mac");
		
		LogProtos.Log log = message.getLogReport();
		List<LogProtos.Record> records = log.getRecordsList();
		if( null == records) return;
		
		Iterator<LogProtos.Record> it = records.iterator();
		while(it.hasNext()){
			LogProtos.Record record = (LogProtos.Record) it.next();
			if(!record.hasType() || !record.hasSensorid()) continue;
			
			if( record.hasAcRecord()){
				//List<LogProtos.AirConditionerRecord> ac_logs = record.getAcRecordList();
				LogProtos.AirConditionerRecord ac_log = record.getAcRecord();								
				LogProtos.AirConditionerRecord.ExtendedParameters ep = ac_log.getExtParameters();
				LogProtos.AirConditionerRecord.ExtendedParameters.DemandType dt = ep.getDemand();
				
				int react_power  = ep.getReactivePower();
				int react_energy = ep.getReactiveEnergy();
				int apparent_power = ep.getApparentPower();
				int voltage        = ep.getVoltage();
				int current        = ep.getCurrent();
				int frequency      = ep.getFrequency();
				int power_factor   = ep.getPowerFactor();
				long start_time    = dt.getStartTime();
				int period         = dt.getPeriod();
				int active_cmd     = dt.getActiveDemand();
				int reactive_cmd   = dt.getReactiveDemand();
				
				long t1 = record.getTimestamp();
				long current_time = System.currentTimeMillis();
		   		
		   		if( Math.abs(current_time - t1) > 3600 * 1000){
		   			LOGGER.error("****** LOGREPORT start_time is NOT correct, current_time("+current_time+"), LOGREPORT timestamp(" + t1 + ")");
		   			t1 = current_time;	   			
		   		}
		   		 
				
				DeviceLogUtil.AirconLogHandler(new Timestamp(t1),//record.getTimestamp()), 
												record.getSensorid(),
												ac_log.getCurTemperature(),
												ac_log.getCurHumidity(),
												ac_log.getCurPower(),
												ac_log.getAccumulatePower(),
												react_power, 
												react_energy, 
												apparent_power,
												voltage, 
												current, 
												frequency, 
												power_factor,
												new Timestamp(start_time), 
												period, 
												active_cmd,
												reactive_cmd,
												"");//device.getEleArea());	
			}else if(record.getType() == SensorProtos.Sensor.SensorType.WINDOWDOORSENSOR && record.hasWindoorRecord()){
				LogProtos.WinDoorRecord wd_log = record.getWindoorRecord();
				boolean onOff = wd_log.getOn();
				
				
			}
		}
		
		/*
		//List<Map> services = (List<Map>) session.getAttribute("services");
		String gwid=(String) session.getAttribute("gw_mac");
		//检测网关是否在�?如果数据库不在线，断开连接
		
		LogProtos.Log log = message.getLogReport();
		List<LogProtos.Record> recordsList = log.getRecordsList();
		if (recordsList == null) {
			return;
		}
		Map tmp=new HashMap();
		// LogService logService = new LogServiceImpl();
		// DeviceService deviceService = new DeviceServiceImpl();
		Iterator iterator = recordsList.iterator();
		while (iterator.hasNext()) {
			LogProtos.Record record = (LogProtos.Record) iterator.next();			
			if (!record.hasType() || !record.hasSensorid()) {
				continue;
			}

			// 空调日志
			if (record.getType() == SensorProtos.Sensor.SensorType.AIRCONDITIONER
					&& record.getAcRecordCount() > 0) {
				// List<Device> devices = new ArrayList<Device>();
				List<LogProtos.AirConditionerRecord> acRecordList = record.getAcRecordList();
				Iterator acIterator = acRecordList.iterator();				
				while (acIterator.hasNext()) {					
					LogProtos.AirConditionerRecord airConditionerRecord = (LogProtos.AirConditionerRecord) acIterator.next();
					LogProtos.AirConditionerRecord.ExtendedParameters extParam = airConditionerRecord.getExtParameters();
					LogProtos.AirConditionerRecord.ExtendedParameters.DemandType demandType = extParam.getDemand();
					
					int reactivePower = extParam.getReactivePower();
					int reactiveEnergy = extParam.getReactiveEnergy();
					int apparentPower = extParam.getApparentPower();
					int voltage = extParam.getVoltage();
					int current = extParam.getCurrent();
					int frequency = extParam.getFrequency();
					int powerFactor = extParam.getPowerFactor();
					long startTime = demandType.getStartTime();
					int period = demandType.getPeriod();
					int activeDemand = demandType.getActiveDemand();
					int reactiveDemand = demandType.getReactiveDemand();
					//检测是否存在重复的log
					String sensorid=record.getSensorid();
					if(tmp.get(sensorid+record.getTimestamp())!=null){
						System.out.println("存在重复的空调log");
						continue;
					}else{
						tmp.put(sensorid+record.getTimestamp(),"1");
					}
	
					DeviceLogUtil.AirconLogHandler(new Timestamp(record.getTimestamp()), 
							record.getSensorid(),
							airConditionerRecord.getCurTemperature(),
							airConditionerRecord.getCurPower(),
							airConditionerRecord.getAccumulatePower(),
							reactivePower, 
							reactiveEnergy, 
							apparentPower,
							voltage, 
							current, 
							frequency, 
							powerFactor,
							new Timestamp(startTime), period, activeDemand,
							reactiveDemand,
							"");//device.getEleArea());
				}
			}

			// 门窗日志
			if (record.getType() == SensorProtos.Sensor.SensorType.WINDOWDOORSENSOR
					&& record.getWindoorRecordCount() > 0) {
				List<LogProtos.WinDoorRecord> windoorRecordList = record
						.getWindoorRecordList();
				Iterator windoorIterator = windoorRecordList.iterator();
				while (windoorIterator.hasNext()) {
					LogProtos.WinDoorRecord winDoorRecord = (LogProtos.WinDoorRecord) windoorIterator
							.next();
					Device device = getSensorDeviceFromServicesByMac(services,
							record.getSensorid());
					
					// logService.appendWinDoorLog(new
					// Timestamp(record.getTimestamp()), device.getId(),
					// winDoorRecord.getOn());
//					Runnable runable = new WinDoorLogRunable(new Timestamp(
//							record.getTimestamp()), device.getId(),
//							winDoorRecord.getOn());
//					App.executorService.execute(runable);
					
					DeviceLogUtil.winDoorLogHandler(new Timestamp(record.getTimestamp()), device.getId(),
							winDoorRecord.getOn());
				}
				continue;
			}
			
		}*/
	}
	

	/**
	 * 处理不支持的请求
	 * 
	 * @param ctx
	 * @param message
	 */
	private void handleUnsupportedMessage(ChannelHandlerContext ctx, MessageProtos.SpmMessage message) {
		LOGGER.debug("UnsupportedMessage", message);
	}

	/**
	 * 处理SENSORINFORESPONE
	 * 
	 * @param ctx
	 * @param message
	 */
	private void handleSensorInfoRespone(ChannelHandlerContext ctx, MessageProtos.SpmMessage message) {
		if (!message.hasSensorInfoResponse()) {
			return;
		}

		SensorProtos.SensorInfoResponse sensorInfoResponse = message.getSensorInfoResponse();		
		if (!sensorInfoResponse.hasReturnCode() || sensorInfoResponse.getReturnCode() != 0) {
			if (sensorInfoResponse.hasFaultString()) {
				LOGGER.info("fault message= " + sensorInfoResponse.hasFaultString());
			}
		} else {
			if (sensorInfoResponse.getSensorsCount() == 0) {
				return;
			}
			updateSensorInfo(ctx, sensorInfoResponse.getSensorsList());
		}
		
		try {
			MessageProtos.SpmMessage.Header header = message.getHeader();
			int seqnum = header.getSeqnum();
			if (sensorInfoResponse.getReturnCode() == 0) {
				translate(ctx, seqnum, "1");
			} else {
				translate(ctx, seqnum, "0");
			}
		} catch (Exception e) {
			LOGGER.error("translate error!", e);
		}
	}

	/**
	 * 处理GWINFO_RESPONE
	 * 
	 * @param ctx
	 * @param message
	 */
	private void handleGWInfoRespone(ChannelHandlerContext ctx, MessageProtos.SpmMessage message) {
		if (!message.hasGwinforesponse()) {
			return;
		}

		GatewayInfoProtos.GatewayInfoResponse gatewayInfoResponse = message.getGwinforesponse();

		/*
		if (!gatewayInfoResponse.hasReturnCode()
				|| gatewayInfoResponse.getReturnCode() != 0) {
			LOGGER.debug("return code is "
					+ gatewayInfoResponse.getReturnCode());
			if (gatewayInfoResponse.hasFaultString()) {
				LOGGER.debug("fault message : "
						+ gatewayInfoResponse.hasFaultString());
			}
		

		} else */{

			/**
			 * 更新网关信息 暂未考虑 userAddress�?zigbeeChannel�?zigbeePanId�?zigbeeKey
			 */
			DeviceService deviceService = new DeviceServiceImpl();

			Session session = ctx.channel().attr(AttributeKeyUtil.getSessionAttributeKey()).get();
			Device gateway = deviceService.getDeviceById((String) session.getAttribute("gw_mac"));

			if (gateway == null) {
				LOGGER.info("Device is NULL!");
			}

			gateway.setCurrentSoftware(String.valueOf(gatewayInfoResponse.getSoftwareVersion()));
			
			HashMap<String,String> gwzig=new HashMap();
			gwzig.put("id",gateway.getId());
			if(gatewayInfoResponse.hasZigbeeChannel()){
				gwzig.put("channel",gatewayInfoResponse.getZigbeeChannel()+"");
			}
			if(gatewayInfoResponse.hasZigbeeChannelMask()){
				gwzig.put("channelMask",gatewayInfoResponse.getZigbeeChannelMask()+"");
			}
			if(gatewayInfoResponse.hasZigbeeTxPower()){
				gwzig.put("txPower",gatewayInfoResponse.getZigbeeTxPower()+"");
			}
			DeviceLogUtil.gwZigBeeHandler(gwzig);
			// deviceService.updateDevice(gateway);
//			App.executorService
//					.execute(new AuthenticateHandler.UpdateDeviceRunable(
//							gateway));
			DeviceLogUtil.deviceInfoHandler(gateway);

			if (gatewayInfoResponse.getSensorsCount() == 0) {
				return;
			}

			updateSensorInfo(ctx, gatewayInfoResponse.getSensorsList());

		}

		try {
			MessageProtos.SpmMessage.Header header = message.getHeader();
			int seqnum = header.getSeqnum();
			translate(ctx, seqnum, "0");
		} catch (Exception e) {
			LOGGER.error("translate error!", e);
		}

	}

	//////////////////////////////////////////////////////////////////
	private Device getSensorDeviceFromServicesByMac(List<Map> services, String mac) {
		if (mac == null || services == null) {
			LOGGER.debug("Sensor Mac is NULL!");
			throw new NullArgumentException("mac");
		}

		Device result = null;
		Iterator<Map> iterator = services.iterator();
		while (iterator.hasNext()) {
			/*
			 * if (result != null) { break; }
			 */
			Map service = iterator.next();
			String deviceMac = (String) service.get("mac");
			/*
			 * List<Device> devices = (List<Device>) service.get("devices");
			 * Iterator<Device> iterator1 = devices.iterator(); while
			 * (iterator1.hasNext()) { Device device = iterator1.next();
			 */
			if (mac.equals(deviceMac)) {
				// LOGGER.info(mac + " Sensor Mac is Matched!");
				result = new Device();
				result.setId(service.get("id").toString());
				result.setMac(deviceMac);
				result.setEleArea(service.get("eleArea").toString());
				return result;
			}
			// }
		}
		return result;
	}

	public static List<Device> getAllSensorDeviceFromServices(List<Map> services) {
		List<Device> list = new ArrayList<Device>();
		Iterator<Map> iterator = services.iterator();
		while (iterator.hasNext()) {
			Map service = iterator.next();
			String id = (String) service.get("id");

			Device device = new Device();
			device.setId(id);
			list.add(device);
			/*
			 * List<Device> devices = (List<Device>) service.get("devices");
			 * if(devices != null) { list.addAll(devices); }
			 */
		}
		return list;
	}

	private void updateSensorInfo(ChannelHandlerContext ctx, List<SensorProtos.Sensor> sensorsList) {
		Iterator iterator = sensorsList.iterator();
		//Session session = ctx.channel().attr(AttributeKeyUtil.getSessionAttributeKey()).get();
		
		while (iterator.hasNext()) {
			SensorProtos.Sensor sensor = (SensorProtos.Sensor) iterator.next();			
			DeviceService devSrv = new DeviceServiceImpl();
						
			String db_id = "";
			SensorProtos.Sensor.SensorType dev_type = sensor.getType();
			if (dev_type == SensorProtos.Sensor.SensorType.AIRCONDITIONER) {
				AirCondition ac = devSrv.getAcByAcMac(sensor.getEUID());
				db_id = ac.getId();
				
				if (sensor.hasAcSpecificInfo()) {
					SensorProtos.AirConditionerSendorSpecificInfo acSpecificInfo = sensor.getAcSpecificInfo();									
					ac.setAcTemp(acSpecificInfo.getLocalTemperature());
					ac.setTemp(acSpecificInfo.getTargetTemperature());
					ac.setPower(acSpecificInfo.getInstantCapacity());
					ac.setAccumulatedPower(acSpecificInfo.getAccumulatedKwh());
					
					//TODO
					Integer val1 = acSpecificInfo.getOperationMode().getNumber();
					Integer val2 = acSpecificInfo.getFanMode().getNumber();
					ac.setMode(AirconUtil.getModeValue(acSpecificInfo.getOperationMode()));
					ac.setSpeed(AirconUtil.getSpeedValue(acSpecificInfo.getFanMode()));
					ac.setOperStatus(1);
				}

				ac.setStartTime(new Timestamp(sensor.getLocalTime()));	
				if (sensor.hasPoweredOn()) {
					if (sensor.getPoweredOn() == 0 || sensor.getPoweredOn() == 1) {
						ac.setOnOff(sensor.getPoweredOn());
						ac.setOperStatus(1);
					} else {
						ac.setOnOff(0);
						ac.setOperStatus(0);
					}
				} 
				
				if (sensor.hasSoftwareVersion()) {
					ac.setSoftwareVer(String.valueOf(sensor.getSoftwareVersion()));
				}
				if (sensor.hasHardwareVersion()) {
					ac.setHardwareVer(String.valueOf(sensor.getHardwareVersion()));
				}				

				UdpLogger.mtImportant("mac: " + ac.getMac() + " operStatus:"+ac.getOperStatus().toString()+" mode:" + ac.getMode().toString()+ " acTemp:" + ac.getAcTemp());
				DeviceLogUtil.deviceInfoHandler(ac);				
			}

	/*
			if (sensor.getType() == SensorProtos.Sensor.SensorType.WINDOWDOORSENSOR) {
				if (sensor.hasWdSpecificInfo()) {
					SensorProtos.WinDoorSensorSpecificInfo wdSpecificInfo = sensor.getWdSpecificInfo();
					
					boolean state = WindoorUtil.getStateValue(wdSpecificInfo.getState());
					device.setOnOff(WindoorUtil.getStateValue(wdSpecificInfo.getState()) ? 1 : 0);
					if (sensor.hasPoweredOn()) {
						if (sensor.getPoweredOn() == 0 || sensor.getPoweredOn() == 1) {
							device.setOperationStatus(1);
						} else {
							device.setOperationStatus(0);
						}
					} else {
						device.setOperationStatus(0);
					}
					
					DeviceLogUtil.winDoorLogHandler(new Timestamp(System.currentTimeMillis()), device.getId(), state);
				}
			}
*/
			HashMap<String,String> radio=new HashMap<String, String>();			
			if(sensor.hasRadioInfo()){
				RadioInfo radioInfo=sensor.getRadioInfo();
				radio.put("rxLqi",radioInfo.getAverageRxLQI()+"");
				radio.put("rxRssi",radioInfo.getAverageRxRssi()+"");
				radio.put("txlqi",radioInfo.getAverageTxLQI()+"");
				radio.put("txRssi",radioInfo.getAverageTxRssi()+"");
				radio.put("id", db_id);
				
				DeviceLogUtil.acZigBeeHandler(radio);
			}
		}
	}

	/**
	 * 获取Session
	 * 
	 * @param ctx
	 * @return
	 */
	public Session getSession(ChannelHandlerContext ctx) {
		return ctx.channel().attr(AttributeKeyUtil.getSessionAttributeKey()).get();
	}

	///////////////////////////////////////////////////////////////////////////////
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		super.userEventTriggered(ctx, evt);

		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				LOGGER.info("READER_IDLE read timeout.");
				ctx.disconnect();
				
			} else if (event.state() == IdleState.WRITER_IDLE) {
				LOGGER.info("WRITER_IDLE write timeout.");
				
			} else if (event.state() == IdleState.ALL_IDLE) {
				LOGGER.info("ALL_IDLE all timeout");
			}
		}
	}

	/////////////////////////////////////////////////////////////////////
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
	
}