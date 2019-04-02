package com.harmazing.util;

import com.google.protobuf.ByteString;
import com.harmazing.Config;
import com.harmazing.entity.ACClock;
import com.harmazing.entity.AirCondition;
import com.harmazing.entity.Device;
import com.harmazing.entity.DeviceType;
import com.harmazing.entity.Gateway;
import com.harmazing.entity.ZigbeeOO;
import com.harmazing.protobuf.CommandProtos;
import com.harmazing.protobuf.DebugProtos;
import com.harmazing.protobuf.CommandProtos.DsmTemperatureRange;
import com.harmazing.protobuf.DebugProtos.Disconnect;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import com.harmazing.protobuf.LoginProtos;
import com.harmazing.protobuf.MessageProtos;
import com.harmazing.protobuf.SensorProtos;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.ACClockServiceImpl;
import com.harmazing.service.impl.DeviceServiceImpl;
import com.sun.research.ws.wadl.Response;

import io.netty.util.internal.StringUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.python.antlr.PythonParser.return_stmt_return;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ming on 14-9-9.
 */
public class MessageUtil {
	private static int seqnum = 0; 
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageUtil.class);
    private static PythonInterpreter interpreter = null;


    /////////////////////////////////////////////////
    public static LoginProtos.LoginResponse.Builder createLoginResonseBuilder(MessageProtos.SpmMessage req, int ret_code, String description){
    	Config config = Config.getInstance();

    	LoginProtos.LoginResponse.Builder login_response = LoginProtos.LoginResponse.newBuilder();
    	login_response.setReturnCode(ret_code);
    	login_response.setFaultString(description);
    	
    	LoginProtos.LogCfg.Builder log_cfg = LoginProtos.LogCfg.newBuilder();
    	log_cfg.setLoginterval(config.CLIENT_LOG_INTERVAL);
    	log_cfg.setReportinterval( config.CLIENT_REPORT_INTERVAL);
    	login_response.setLogCfg( log_cfg);
    	
    	LoginProtos.DeviceSet.Builder dev_set = createDeviceSet(req.getLogin().getGatewayMac());    	
    	login_response.setDeviceSet( dev_set);
    	
    	LoginProtos.GlobalDeviceConfig.Builder global_dev_config = LoginProtos.GlobalDeviceConfig.newBuilder();
    	CommandProtos.DsmTemperatureRange.Builder dsm = CommandProtos.DsmTemperatureRange.newBuilder();
    	dsm.setUpperTemperature(-1);
    	dsm.setLowerTemperature(-1);
    	
    	global_dev_config.setDsmTemperatureRange( dsm);
    	login_response.setGlobalConfig(global_dev_config);    	
    	
    	return login_response;
    	
    }
    
    ////////////////////////////////////////////////
    public static LoginProtos.DeviceSet.Builder createDeviceSet(String gw_mac){
    	LoginProtos.DeviceSet.Builder dev_set = LoginProtos.DeviceSet.newBuilder();
    	
    	DeviceService dev_srv = new DeviceServiceImpl();
    
    	////////////////////////////////////////////
    	// handle AC
    	List<AirCondition> acs = dev_srv.getAcsByGwMac(gw_mac); 
    	if( acs != null){
	    	Iterator<AirCondition> ac_it = acs.iterator();
	    	while(ac_it.hasNext()){
	    		AirCondition ac = (AirCondition) ac_it.next();
	    	    		
	    		// response to loing    		
	    		LoginProtos.Device.Builder dev = LoginProtos.Device.newBuilder();
				dev.setType( LoginProtos.Device.DeviceType.AIRCONDITIONER);
				dev.setDeviceId( ac.getMac());
				/*
				LoginProtos.AirConditionerAttribute.Builder ac_attr = LoginProtos.AirConditionerAttribute.newBuilder();
				LoginProtos.AirConditionerAttribute.TemperatureRange.Builder temp_range = LoginProtos.AirConditionerAttribute.TemperatureRange.newBuilder();
				temp_range.setCoolerStart(15); //TODO: Update the date by checking database
				temp_range.setCoolerEnd(28);
				temp_range.setHeaterStart(20);
				temp_range.setHeaterEnd(28);
				ac_attr.setTemperatureRange( temp_range);
				ac_attr.setWinDoorSensorEnabled( false); // TODO: update in the future with reality
				ac_attr.addBindingSensorsId(""); // TODO: relevant to WinDoorSensor
				
				int ac_mode = ac.getMode();
				if(ac_mode == AirconMode.COOLERONLY) {
					ac_attr.addMode(SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING );
	            } else if(ac_mode == AirconMode.COOLERANDHEATER) {
	            	ac_attr.addMode( SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING );
	            	ac_attr.addMode( SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.WARMING );
	            } else {
	            	ac_attr.addMode( SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING );
	            }    			
				dev.setAcAttr( ac_attr);
				dev_set.addDevices( dev);
				*/
	    	}
    	}
    	
    	/*
		////////////////////////////////////////////
    	// handle ZigbeeOO
    	List<ZigbeeOO> oos = dev_srv.getOOsByGwMac(gw_mac); 
    	if( oos != null){
	    	Iterator<ZigbeeOO> oo_it = oos.iterator();
	    	while(oo_it.hasNext()){
	    		ZigbeeOO oo = (ZigbeeOO) oo_it.next();
	    		
	    		// response to login    		
	    		LoginProtos.Device.Builder dev = LoginProtos.Device.newBuilder();
				dev.setType( LoginProtos.Device.DeviceType.);
				dev.setDeviceId( oo.getMac());
				
				LoginProtos.AirConditionerAttribute.Builder ac_attr = LoginProtos.AirConditionerAttribute.newBuilder();
				LoginProtos.AirConditionerAttribute.TemperatureRange.Builder temp_range = LoginProtos.AirConditionerAttribute.TemperatureRange.newBuilder();
				temp_range.setCoolerStart(15); //TODO: Update the date by checking database
				temp_range.setCoolerEnd(28);
				temp_range.setHeaterStart(20);
				temp_range.setHeaterEnd(28);
				ac_attr.setTemperatureRange( temp_range);
				ac_attr.setWinDoorSensorEnabled( false); // TODO: update in the future with reality
				ac_attr.addBindingSensorsId(""); // TODO: relevant to WinDoorSensor
				
				int ac_mode = ac.getMode();
				if(ac_mode == AirconMode.COOLERONLY) {
					ac_attr.addMode(SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING );
	            } else if(ac_mode == AirconMode.COOLERANDHEATER) {
	            	ac_attr.addMode( SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING );
	            	ac_attr.addMode( SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.WARMING );
	            } else {
	            	ac_attr.addMode( SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING );
	            }    			
				dev.setAcAttr( ac_attr);
				dev_set.addDevices( dev);
	    	}
    	}
    	*/
    	
    	/*
    	RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();
    	Map<String, String> dev_list = redisContext.getDevicesByGw(gw_mac);
    	    	
    	String ac_json = dev_list.get("AC");
    	if(ac_json != null && ac_json.length()>0){
    		JSONObject jsonObj = new JSONObject(ac_json);
    		JSONArray jsonArr  = ((JSONArray) jsonObj.get("AC")); 
    		for(int i=0; i<jsonArr.length(); ++i){
    			JSONObject obj = (JSONObject) jsonArr.get(i);
    			
    			LoginProtos.Device.Builder dev = LoginProtos.Device.newBuilder();
    			dev.setType( LoginProtos.Device.DeviceType.AIRCONDITIONER);
    			dev.setDeviceId( obj.getString("mac"));
    			
    			LoginProtos.AirConditionerAttribute.Builder ac_attr = LoginProtos.AirConditionerAttribute.newBuilder();
    			LoginProtos.AirConditionerAttribute.TemperatureRange.Builder temp_range = LoginProtos.AirConditionerAttribute.TemperatureRange.newBuilder();
    			temp_range.setCoolerStart(15); //TODO: Update the date by checking database
    			temp_range.setCoolerEnd(28);
    			temp_range.setHeaterStart(20);
    			temp_range.setHeaterEnd(28);
    			ac_attr.setTemperatureRange( temp_range);
    			ac_attr.setWinDoorSensorEnabled( false); // TODO: update in the future with reality
    			ac_attr.addBindingSensorsId(""); // TODO: relevant to WinDoorSensor
    			
    			int ac_mode = Integer.valueOf(obj.get("mode").toString());
    			if(ac_mode == AirconMode.COOLERONLY) {
    				ac_attr.addMode(SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING );
                } else if(ac_mode == AirconMode.COOLERANDHEATER) {
                	ac_attr.addMode( SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING );
                	ac_attr.addMode( SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.WARMING );
                } else {
                	ac_attr.addMode( SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING );
                }    			
    			dev.setAcAttr( ac_attr);
    			dev_set.addDevices( dev);
    			
    			//TODO: time Curv
    		}    		
    	}    
    	*/	
    	
    	return dev_set;
    }
    
	//////////////////////////////////////////////////////////////////
	public static CommandProtos.RcuUpdateCommand.Builder createRcuUpdateCmdBuilder(
			int device_id,
			int frequency,
			int duty_cycle_numerator,
			int duty_cycle_denominator,
			JSONArray pulse,
			JSONArray length){
		LOGGER.debug("Enter CommandProtos.RcuUpdateCommand.Builder createRcuUpdateCmdBuilder.");
		CommandProtos.RcuUpdateCommand.Builder builder = null;
		try{
			
			builder = CommandProtos.RcuUpdateCommand.newBuilder();
			builder.setRcuId( device_id);
			builder.setFrequency( frequency);
			builder.setDutyCycleNumerator(duty_cycle_numerator);
			builder.setDutyCycleDenominator(duty_cycle_denominator);
			
			LOGGER.debug(" *** CommandProtos.RcuUpdateCommand.Builder, duty("+ duty_cycle_numerator + "), dyty2(" + duty_cycle_denominator + "), pulse("
					+ pulse + "), length(" + length +")" );
			for(int i=0; i<pulse.length(); ++i){
				builder.addNumberOfPulses(pulse.getInt(i));
			}
			
			for(int i=0; i<length.length(); ++i){
				builder.addLength(length.getInt(i));
			}
			
			LOGGER.debug("Exit CommandProtos.RcuUpdateCommand.Builder createRcuUpdateCmdBuilder.");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return builder;
		
	}
    
    //////////////////////////////////////////////////////////////////
	public static CommandProtos.IrPacket.Builder createIrPacketBuilder( 
			int device_id,
			String length,
			ByteString main){
		LOGGER.debug("Enter CommandProtos.IrPacket.Builder createIrPacketBuilder.");
		
		CommandProtos.IrPacket.Builder builder = CommandProtos.IrPacket.newBuilder();
		builder.setRcuId(device_id);
		builder.setMain( main);
		
		String []tmp = length.split(" ");
		for(int i=0; i<tmp.length; ++i){
			builder.addLength(Integer.valueOf(tmp[i]));
		}
		
		LOGGER.debug("End CommandProtos.IrPacket.Builder createIrPacketBuilder.");
		
		return builder;
	}
    
    //////////////////////////////////////////////////////////////////
    public static CommandProtos.IrControl.Builder createIrControlMessage(String sensor_id, 
    		CommandProtos.IrControl.CommandType type,
    		CommandProtos.RcuUpdateCommand.Builder rcu_builder,
    		CommandProtos.IrPacket.Builder irPacket_builder){
    	
    	LOGGER.debug("Enter CommandProtos.IrControl.Builder createIrControlMessage.");
    	
    	CommandProtos.IrControl.Builder irCtrl_builder = CommandProtos.IrControl.newBuilder();
    	
    	irCtrl_builder.setSensorid( sensor_id);
		irCtrl_builder.setType( type);    	
		if( type == CommandProtos.IrControl.CommandType.IR_RCU_UPDATE){
    		irCtrl_builder.setRcuUpdateCommand( rcu_builder);
    	}else if( type == CommandProtos.IrControl.CommandType.IR_TX_CODE){
    		irCtrl_builder.setIrPacket( irPacket_builder);
    	}    	
    	
		LOGGER.debug("Exit CommandProtos.IrControl.Builder createIrControlMessage.");
		
    	return irCtrl_builder;
    }
    
    //////////////////////////////////////////////////////////////////
    public static MessageProtos.SpmMessage createIrControlMessage(
    		int seq_num,
    		String session,
    		CommandProtos.IrControl.Builder irControl_builder){
    	
    	LOGGER.debug("Enter MessageProtos.SpmMessage createIrControlMessage.");
    	
    	MessageProtos.SpmMessage.Builder msg_builder = MessageProtos.SpmMessage.newBuilder();
    	
    	MessageProtos.SpmMessage.Header.Builder header_builder = MessageProtos.SpmMessage.Header.newBuilder();
    	header_builder.setType( MessageProtos.SpmMessage.MsgType.COMMAND);
    	header_builder.setSeqnum(seq_num);
    	header_builder.setSession(session);    	
    	msg_builder.setHeader(header_builder);
    	
    	CommandProtos.Command.Builder cmd_builder = CommandProtos.Command.newBuilder();
    	cmd_builder.setIrControl( irControl_builder);
    	msg_builder.setControlCommand(cmd_builder);
    	
    	LOGGER.debug("Exit MessageProtos.SpmMessage createIrControlMessage.");
    	
    	return msg_builder.build();
    }
    
    //////////////////////////////////////////////////////////////////
    public static String analysisModSig(int dev_id, String main) {
    	PythonInterpreter interp = getPythonInterpreter();
    	if( interp == null ) return "";
	    
    	try{
    		interp.execfile(new FileInputStream("./scripts/ir.py"));
		    PyFunction func = (PyFunction)interpreter.get("innolinks_demodsig",PyFunction.class); 
	        PyObject pyobj = func.__call__(new PyString("./scripts/data"), new PyInteger(dev_id), new PyString(main));
	        return pyobj.toString();    
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        
    	return "";
    }
    
	//////////////////////////////////////////////////////////////////
	public static int analysisDeviceId(String length, String main) {
		PythonInterpreter interp = getPythonInterpreter();
		if( interp == null ) return 0;
		    	
		interp.execfile("./scripts/ir.py");
		PyFunction func = (PyFunction)interpreter.get("innolinks_pair"); 
		PyObject pyobj = func.__call__(new PyString("./scripts/data/clusters.json"), new PyString(length), new PyString(main));
	
		return pyobj.asInt();    		
	}
	
	//////////////////////////////////////////////////////////////////
	public static JSONObject getAcCodesById( String rcu_id, String modsig){		
		String uri = Config.getInstance().ACCODE_WEBRUL + "?rcu_id=" + rcu_id + "&modsig=" + modsig ;		
		return runHttpGetCommand(uri);
	}
		
	/////////////////////////////////////////////////////////////////
	public static String getModsig(int rcu_id, String length, String main){
		String uri = Config.getInstance().ACCODE_WEBRUL + "/modsig/";
	
		JSONObject obj = runHttpPostcommand(uri, rcu_id, length, main);
		if( obj == null ||  (int)obj.get("status") == 1){
			return "";
		}
		
		return (String)obj.get("modsig");
	}
	
	/////////////////////////////////////////////////////////////////
	//public static Map<String, Object> getRcuInfo(int rcu_id, String length, String main){
	public static JSONObject getRcuInfo(int rcu_id, String length, String main){
		String uri = Config.getInstance().ACCODE_WEBRUL + "/pairing/";
		
		return runHttpPostcommand(uri, rcu_id, length, main);
	}
	
	/////////////////////////////////////////////////////////////////
	//public static Map<String, Object> runHttpPostcommand(String uri, int rcu_id, String length, String main){
	public static JSONObject runHttpPostcommand(String uri, int rcu_id, String length, String main){
		HttpURLConnection con = null;
		DataOutputStream out = null;
				
		Map<String, Object> map = null;
		try{
			URL obj = new URL(uri);		
			con= (HttpURLConnection) obj.openConnection();
			
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestMethod("POST");
			con.setUseCaches(false);
			con.setInstanceFollowRedirects(false);
			
    		out = new DataOutputStream(con.getOutputStream());
            JSONObject params = new JSONObject();
            if(rcu_id != -1 ){
            	params.put("rcu_id", rcu_id);
            }
            params.put("length", length );
            params.put("main", main);
            String str = params.toString();
            out.write(str.getBytes("UTF-8"));
            out.flush();
            out.close();

            
            ObjectMapper mapper = new ObjectMapper();   
            map = mapper.readValue(con.getInputStream(), Map.class);          
            
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.disconnect();
		}
		
		JSONObject obj = new JSONObject(map);
		LOGGER.debug("Enter MessageProtos.runHttpPostcommand get AC codes = " + obj.toString() + ", uri = " + uri);
		return obj;
	}
	
	/////////////////////////////////////////////////////////////////
	public static JSONObject runHttpGetCommand(String uri){
		String response = "";
		Map<String, Object> map = null;
		
		try{
			URL obj = new URL(uri);		
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");			
			int responseCode = con.getResponseCode();
			
			ObjectMapper mapper = new ObjectMapper();   
            map = mapper.readValue(con.getInputStream(), Map.class);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		JSONObject obj = new JSONObject(map);
		LOGGER.debug("Enter MessageProtos.runHttpGetCommand get AC details = " + obj.toString() + ", uri = " + uri);
		
		return obj;
	}
    //////////////////////////////////////////////////////////////////
    public static LoginProtos.DeviceSet toDeviceSet(List<Map> airconServiceList) {
        LoginProtos.DeviceSet.Builder deviceSetBuilder = LoginProtos.DeviceSet.newBuilder();

        Iterator<Map> iterator = airconServiceList.iterator();
        while (iterator.hasNext()) {
            Map service = iterator.next();
            //模式
            int airconMode = Integer.valueOf(service.get("mode").toString());
            //服务温度控制
            Integer coolerEnd = Integer.valueOf(service.get("coolerEnd").toString());
            Integer coolerStart = Integer.valueOf(service.get("coolerStart").toString());
            Integer heaterEnd = Integer.valueOf(service.get("heaterEnd").toString());
            Integer heaterStart = Integer.valueOf(service.get("heaterStart").toString());

            String mac = (String)service.get("mac");
            String type = service.get("type").toString();
            /*将type强制转换*/
            if(type!=null){
                if(type.equals("1")){
                    type = DeviceType.GATEWAY;
                }else if(type.equals("2")){
                    type = DeviceType.AIRCON_SENSOR;
                }else if(type.equals("3")){
                    type = DeviceType.DOOR_SENSOR;
                }else {
                    type = DeviceType.WINDOW_SENSOR;
                }
            }
            //List<Device> deviceList = (List<Device>) service.get("devices");
            //Iterator<Device> deviceIterator = deviceList.iterator();

            //while (deviceIterator.hasNext()) {
            //Device device = deviceIterator.next();

            LoginProtos.Device.Builder deviceBuilder = LoginProtos.Device.newBuilder();

            //设备Mac
            deviceBuilder.setDeviceId(mac);

            //设置设备类型
            if(DeviceType.AIRCON_SENSOR.equals(type)) {
                deviceBuilder.setType(LoginProtos.Device.DeviceType.AIRCONDITIONER);
                LoginProtos.AirConditionerAttribute.Builder airConditionerAttributeBuilder
                        = LoginProtos.AirConditionerAttribute.newBuilder();
                //门窗绑定
                airConditionerAttributeBuilder.setWinDoorSensorEnabled(false);

                //温度变化
                LoginProtos.AirConditionerAttribute.TemperatureRange.Builder temperatureRangeBuilder
                        = LoginProtos.AirConditionerAttribute.TemperatureRange.newBuilder();
                temperatureRangeBuilder.setCoolerEnd(coolerEnd);
                temperatureRangeBuilder.setCoolerStart(coolerStart);
                temperatureRangeBuilder.setHeaterEnd(heaterEnd);
                temperatureRangeBuilder.setHeaterStart(heaterStart);
                airConditionerAttributeBuilder.setTemperatureRange(temperatureRangeBuilder);
                String deviceId=(String) service.get("id");
                /****************设置定时曲线信息********************/           
                ACClockServiceImpl cs=new ACClockServiceImpl();
                List<ACClock> lst=cs.getClockSettingByDevice(deviceId);
                for(int i=0;i<lst.size();i++){
        			 ACClock clock=lst.get(i);        			 
	       	    	 CommandProtos.TimerCurv.Builder timerCurvBuilder = CommandProtos.TimerCurv.newBuilder();
	       	         timerCurvBuilder.setRecurrentDate(clock.getAppDate());	       	         
	       	         CommandProtos.TimerInfo.Builder timerInfoBuilder = CommandProtos.TimerInfo.newBuilder();
	       	         timerInfoBuilder.setStartTime(clock.getStartTime());	       	         
	       	         int acState=clock.getAcState();	         
	       	         if(acState==1){
	       	        	 timerInfoBuilder.setState(CommandProtos.TimerInfo.ACState.ON);
	       	         }else{
	       	        	 timerInfoBuilder.setState(CommandProtos.TimerInfo.ACState.OFF);
	       	         }
	       	         timerInfoBuilder.setTemperature(clock.getTemperature());
	       	         timerInfoBuilder.setFanMode(getFanMode(clock.getFanMode()));
	       	         timerInfoBuilder.setOperationMode(getOperMode(clock.getOperMode()));        
	       	         
	       	         timerCurvBuilder.addTimer(timerInfoBuilder);
	       	         airConditionerAttributeBuilder.addCurves(i, timerCurvBuilder);
        		 }         
                 /****************************************/
                if(airconMode == AirconMode.COOLERONLY) {
                    airConditionerAttributeBuilder.addMode(
                            SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING
                    );
                } else if(airconMode == AirconMode.COOLERANDHEATER) {
                    airConditionerAttributeBuilder.addMode(
                            SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING
                    );
                    airConditionerAttributeBuilder.addMode(
                            SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.WARMING
                    );
                } else {
                    airConditionerAttributeBuilder.addMode(
                            SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING
                    );
                }
                deviceBuilder.setAcAttr(airConditionerAttributeBuilder);
            } else if(DeviceType.DOOR_SENSOR.equals(type) ||
                    DeviceType.WINDOW_SENSOR.equals(type)) {
                deviceBuilder.setType(LoginProtos.Device.DeviceType.WINDOOR);
                LoginProtos.WinDoorAttribute.Builder winDoorAttributeBuilder =
                        LoginProtos.WinDoorAttribute.newBuilder();
                deviceBuilder.setWdAttr(winDoorAttributeBuilder);
            } else {
                LOGGER.debug("not support device type : ", type);
                continue;
            }
            deviceSetBuilder.addDevices(deviceBuilder);
            //}
        }
        return deviceSetBuilder.build();
    }


    public static interface AirconMode {
        public final static int COOLERANDHEATER = 1;
        public final static int COOLERONLY = 2;
    }

    public static interface CommandType {
        public final static String ON = "ON";
        public final static String OFF = "OFF";
        public final static String MODE_SET = "MODE_SET";
        public final static String FAN_SET = "FAN_SET";
        public final static String TEMP_SET = "TEMP_SET";
    }
    
	public static interface IrControlCommandType {
		public final static String IR_RCU_UPDATE = "IR_RCU_UPDATE";
		public final static String IR_TX_CODE = "IR_TX_CODE";
	}
    
    public static interface ServiceUpdateCommandType{
    	public final static int DEVICE_CHG=0;
    	public final static int LOG_CHG=1;
    	public final static int LOG_REPORT_CHG=2;
    	public final static int FireWare=3;
    	public final static int DSM=4;
    }
    
    
    /**
     * 将空调服务转换成消息
     * @param map
     * @return
     */
//    public final static LoginProtos.AirConditionerService toAirconService(Map map) {
//        LoginProtos.AirConditionerService.Builder airConditionerServiceBuilder = LoginProtos.AirConditionerService.newBuilder();
//
//        //服务模式
//        int airconMode = (Integer) map.get("airconMode");
//        if(airconMode == AirconMode.COOLERANDHEATER) {
//            airConditionerServiceBuilder.addMode(SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING);
//            airConditionerServiceBuilder.addMode(SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.WARMING);
//        } else if(airconMode == AirconMode.COOLERONLY) {
//            airConditionerServiceBuilder.addMode(SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING);
//        } else {
//            airConditionerServiceBuilder.addMode(SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING);
//        }
//
//
//
//        //服务温度控制
//        LoginProtos.AirConditionerService.TemperatureRange.Builder temperatureRangeBuilder = LoginProtos.AirConditionerService.TemperatureRange.newBuilder();
//        temperatureRangeBuilder.setCoolerEnd((Integer) map.get("coolerEnd"));
//        temperatureRangeBuilder.setCoolerStart((Integer) map.get("coolerStart"));
//        temperatureRangeBuilder.setHeaterEnd((Integer) map.get("heaterEnd"));
//        temperatureRangeBuilder.setHeaterStart((Integer) map.get("heaterStart"));
//        airConditionerServiceBuilder.setTemperatureRange(temperatureRangeBuilder);
//
//        //门窗传感器是否允�?
//        Integer winDoorSensorEnabled = (Integer) map.get("winDoorSensorEnabled");
//        if(winDoorSensorEnabled != null && winDoorSensorEnabled > 1) {
//            airConditionerServiceBuilder.setWinDoorSensorEnabled(true);
//        } else {
//            airConditionerServiceBuilder.setWinDoorSensorEnabled(false);
//        }
//
//        //绑定的传感器Id
//        List<String> sensorIdList = (List<String>) map.get("deviceIds");
//        Iterator iterator = sensorIdList.iterator();
//        while (iterator.hasNext()) {
//            String sensorId = (String) iterator.next();
//            if(sensorId == null) {
//                LOGGER.info("sensorId is NULL");
//                continue;
//            }
//            airConditionerServiceBuilder.addBindingSensorsId(sensorId);
//        }
//
//        return airConditionerServiceBuilder.build();
//    }

    /**
     * 生成命令消息
     * @param sensorId 传感器Id即Mac地址
     * @param commandType
     * @param stringParameter
     * @param intParameter
     * @return
     */
    public static MessageProtos.SpmMessage buildCommandMessage(String sensorId,
    														   DeviceSpecific.DeviceType devType,
                                                               CommandProtos.AirConditionerControl.CommandType commandType,
                                                               String stringParameter, Integer intParameter) {
        MessageProtos.SpmMessage.Builder spmMessageBuilder = MessageProtos.SpmMessage.newBuilder();

        //Add Head
        MessageProtos.SpmMessage.Header.Builder headerBuilder = MessageProtos.SpmMessage.Header.newBuilder();
        headerBuilder.setSeqnum(buildSeqnum());
        headerBuilder.setSession("");
        headerBuilder.setType(MessageProtos.SpmMessage.MsgType.COMMAND);
        spmMessageBuilder.setHeader(headerBuilder);

        //Add Command
        CommandProtos.Command.Builder commandBuilder = CommandProtos.Command.newBuilder();
        CommandProtos.AirConditionerControl.Builder accBuilder = null;
        CommandProtos.OoControl.Builder ooBuilder = null;
        CommandProtos.OoElectricityMeterControl.Builder ooEleMeterBuilder = null;
        CommandProtos.LightSwitchControl.Builder lsBuilder = null;
                
        if(devType == DeviceSpecific.DeviceType.INNOLINKS_AC ||
           devType == DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET ||
           devType == DeviceSpecific.DeviceType.INNOLINKS_THERMOSTAT){
        	accBuilder = CommandProtos.AirConditionerControl.newBuilder();
        	if(stringParameter != null) {
                accBuilder.setStringParameter(stringParameter);
            }
            if(intParameter != null) {
                accBuilder.setIntParameters(intParameter);
            }
            
            accBuilder.setSensorid(sensorId == null ? "" : sensorId);
            accBuilder.setType(commandType);
            commandBuilder.setAcControl(accBuilder);
            
        }else if(devType == DeviceSpecific.DeviceType.ZIGBEE_OO){
        	ooBuilder = CommandProtos.OoControl.newBuilder();
        	ooBuilder.setSensorid(sensorId == null ? "" : sensorId);
        	ooBuilder.setOnOff(commandType == CommandProtos.AirConditionerControl.CommandType.ON ? true : false);
        	commandBuilder.setOoControl(ooBuilder);        	
        	
        }else if(devType == DeviceSpecific.DeviceType.ZIGBEE_OO_ELECTRICITY_METER){
        	ooEleMeterBuilder = CommandProtos.OoElectricityMeterControl.newBuilder();
        	ooEleMeterBuilder.setSensorid(sensorId == null ? "" : sensorId);
        	ooEleMeterBuilder.setOnOff(commandType == CommandProtos.AirConditionerControl.CommandType.ON ? true : false);
        	commandBuilder.setOoemControl(ooEleMeterBuilder);    	
        	
        }else if(devType == DeviceSpecific.DeviceType.LIGHT_SWITCH){
        	lsBuilder = CommandProtos.LightSwitchControl.newBuilder();
        	lsBuilder.setSensorid(sensorId == null ? "" : sensorId);
        	lsBuilder.setOnOff(commandType == CommandProtos.AirConditionerControl.CommandType.ON ? true : false);
        	commandBuilder.setLsControl(lsBuilder);    
        }
        
        spmMessageBuilder.setControlCommand(commandBuilder);

        return spmMessageBuilder.build();
    }
    
    //////////////////////////////////////////////////////////////////////
    public static MessageProtos.SpmMessage buildHALampControlMessage(String sensorid, 
    		                                                         int illuminance,
    		                                                         int red,
    		                                                         int green,
    		                                                         int blue){
    	MessageProtos.SpmMessage.Builder msgBuilder = MessageProtos.SpmMessage.newBuilder();
    	MessageProtos.SpmMessage.Header.Builder headerBuilder = MessageProtos.SpmMessage.Header.newBuilder();
        headerBuilder.setSeqnum(buildSeqnum());
        headerBuilder.setSession("");
        headerBuilder.setType(MessageProtos.SpmMessage.MsgType.COMMAND);
        msgBuilder.setHeader(headerBuilder);
        
        CommandProtos.Command.Builder commandBuilder = CommandProtos.Command.newBuilder();
        CommandProtos.HALampControl.Builder lampCtlBuilder = CommandProtos.HALampControl.newBuilder();
        CommandProtos.LampColor.Builder colorBuilder = CommandProtos.LampColor.newBuilder();
        colorBuilder.setIlluminance(illuminance);
        colorBuilder.setRed(red);
        colorBuilder.setGreen(green);
        colorBuilder.setBlue(blue);   	
        
        lampCtlBuilder.setSensorid(sensorid);
        lampCtlBuilder.setColor(colorBuilder);      
        commandBuilder.setLpControl(lampCtlBuilder);
        msgBuilder.setControlCommand(commandBuilder);
        
        return msgBuilder.build();    	
    }
    
    /**
     * 生成命令消息
     * @param sensorId 传感器Id即Mac地址
     * @param commandType
     * @param stringParameter
     * @param intParameter
     * @return
     */
    public static MessageProtos.SpmMessage buildIrControlMessage(String sensorId,
                                                               CommandProtos.IrControl.CommandType commandType,
                                                               ByteString main, String length) {
        MessageProtos.SpmMessage.Builder spmMessageBuilder = MessageProtos.SpmMessage.newBuilder();

        //Add Head
        MessageProtos.SpmMessage.Header.Builder headerBuilder = MessageProtos.SpmMessage.Header.newBuilder();
        headerBuilder.setSeqnum(buildSeqnum());
        headerBuilder.setSession("");
        headerBuilder.setType(MessageProtos.SpmMessage.MsgType.COMMAND);
        spmMessageBuilder.setHeader(headerBuilder);

        //Add Command
        CommandProtos.Command.Builder commandBuilder = CommandProtos.Command.newBuilder();
        CommandProtos.IrControl.Builder irBuilder = CommandProtos.IrControl.newBuilder();
        CommandProtos.IrPacket.Builder irPacketBuilder = CommandProtos.IrPacket.newBuilder();

        String[] lens = StringUtil.split(length,' ');
        for(int i = 0; i<lens.length; i++){
            irPacketBuilder.addLength(Integer.parseInt(lens[i]));
        }       
        
        //irPacketBuilder.setMain(ByteString.copyFrom(main.getBytes()));
        irPacketBuilder.setMain(main);
        if(sensorId == null) {
        	irBuilder.setSensorid("");
        } else {
        	irBuilder.setSensorid(sensorId);
        }
        irBuilder.setIrPacket(irPacketBuilder);
        irBuilder.setType(commandType);
        commandBuilder.setIrControl(irBuilder);
        spmMessageBuilder.setControlCommand(commandBuilder);

        return spmMessageBuilder.build();
    }
    
    /////////////////////////////////////////////////////////////////////
    public static MessageProtos.SpmMessage buildDebugDisconnectMsg( String type){
    	MessageProtos.SpmMessage.Builder spm_msg_builder = MessageProtos.SpmMessage.newBuilder();
    	MessageProtos.SpmMessage.Header.Builder header = MessageProtos.SpmMessage.Header.newBuilder();
    	header.setSeqnum(buildSeqnum());
    	header.setSession("");
    	header.setType(MessageProtos.SpmMessage.MsgType.DEBUG);
    	spm_msg_builder.setHeader( header);
    	
    	DebugProtos.Disconnect.Builder dis_conn = DebugProtos.Disconnect.newBuilder();
    	dis_conn.setType(type);
    	
    	DebugProtos.Debug.Builder debug = DebugProtos.Debug.newBuilder();
    	debug.setDisconnect(dis_conn);
    	
    	spm_msg_builder.setDebug(debug);
    	
    	return spm_msg_builder.build();   	
    	
    }
    
    /**
     * 生成 GET_GWINFO
     * @return
     */
    public static MessageProtos.SpmMessage buildGWInfoMessage() {
        MessageProtos.SpmMessage.Builder spmMessageBuilder = MessageProtos.SpmMessage.newBuilder();

        //Add Head
        MessageProtos.SpmMessage.Header.Builder headerBuilder = MessageProtos.SpmMessage.Header.newBuilder();
        headerBuilder.setSeqnum(buildSeqnum());
        headerBuilder.setSession("");
        headerBuilder.setType(MessageProtos.SpmMessage.MsgType.GET_GWINFO);
        spmMessageBuilder.setHeader(headerBuilder);
        return spmMessageBuilder.build();
    }

    public static MessageProtos.SpmMessage buildGetSensorInfoMessage() {
        MessageProtos.SpmMessage.Builder spmMessageBuilder = MessageProtos.SpmMessage.newBuilder();

        //Add Head
        MessageProtos.SpmMessage.Header.Builder headerBuilder = MessageProtos.SpmMessage.Header.newBuilder();
        headerBuilder.setSeqnum(buildSeqnum());
        headerBuilder.setSession("");
        headerBuilder.setType(MessageProtos.SpmMessage.MsgType.GET_SENSORINFO);
        spmMessageBuilder.setHeader(headerBuilder);
        return spmMessageBuilder.build();
    }

    public static CommandProtos.AirConditionerControl.CommandType getCommandType(String commandType) {
        if(CommandType.FAN_SET.equals(commandType)) {
            return CommandProtos.AirConditionerControl.CommandType.FAN_SET;
        } else if(CommandType.MODE_SET.equals(commandType)) {
            return CommandProtos.AirConditionerControl.CommandType.MODE_SET;
        } else if(CommandType.TEMP_SET.equals(commandType)) {
            return CommandProtos.AirConditionerControl.CommandType.TEMP_SET;
        } else if(CommandType.ON.equals(commandType)) {
            return CommandProtos.AirConditionerControl.CommandType.ON;
        } else if(CommandType.OFF.equals(commandType)) {
            return CommandProtos.AirConditionerControl.CommandType.OFF;
        } else {
            return null;
        }
    }
    
    public static DeviceSpecific.DeviceType getDeviceType(int dev_type_value){
    	if(dev_type_value == DeviceSpecific.DeviceType.INNOLINKS_GATEWAY_VALUE){
    		return DeviceSpecific.DeviceType.INNOLINKS_GATEWAY;
    	}else if(dev_type_value == DeviceSpecific.DeviceType.INNOLINKS_AC_VALUE){
    		return DeviceSpecific.DeviceType.INNOLINKS_AC;
    	}else if(dev_type_value == DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET_VALUE){
    		return DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET;
    	}else if(dev_type_value == DeviceSpecific.DeviceType.INNOLINKS_THERMOSTAT_VALUE){
    		return DeviceSpecific.DeviceType.INNOLINKS_THERMOSTAT;
    	}else if(dev_type_value == DeviceSpecific.DeviceType.ZIGBEE_OO_VALUE){
    		return DeviceSpecific.DeviceType.ZIGBEE_OO;
    	}else if( dev_type_value == DeviceSpecific.DeviceType.ZIGBEE_OO_ELECTRICITY_METER_VALUE){
    		return DeviceSpecific.DeviceType.ZIGBEE_OO_ELECTRICITY_METER;
    	}else if(dev_type_value == DeviceSpecific.DeviceType.DOOR_WINDDOW_SENSOR_VALUE){
    		return DeviceSpecific.DeviceType.DOOR_WINDDOW_SENSOR;
    	}else if( dev_type_value == DeviceSpecific.DeviceType.PIR_SENSOR_VALUE){
    		return DeviceSpecific.DeviceType.PIR_SENSOR;
    	}else if( dev_type_value == DeviceSpecific.DeviceType.TEMPERATURE_HUMIDITY_SENSOR_VALUE){
    		return DeviceSpecific.DeviceType.TEMPERATURE_HUMIDITY_SENSOR;
    	}else if( dev_type_value == DeviceSpecific.DeviceType.LIGHT_SWITCH_VALUE){
    		return DeviceSpecific.DeviceType.LIGHT_SWITCH;
    	}else if( dev_type_value == DeviceSpecific.DeviceType.ZIGBEE_HA_LAMP_VALUE){
    		return DeviceSpecific.DeviceType.ZIGBEE_HA_LAMP;
    	}else{
    		return DeviceSpecific.DeviceType.RESERVED;
    	}
    }
    
	public static CommandProtos.IrControl.CommandType getIrControlCommandType(String commandType) {
		switch (commandType) {
		case IrControlCommandType.IR_TX_CODE:
			return CommandProtos.IrControl.CommandType.IR_TX_CODE;
		case IrControlCommandType.IR_RCU_UPDATE:
			return CommandProtos.IrControl.CommandType.IR_RCU_UPDATE;
		default:
			return null;
		}
	}
    
    /**
     * 获取空调风速模�?
     */
  
    public static SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode getFanMode(int mode){
    	if(mode==1){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED1;
    	}else if(mode==2){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED2;
    	}
    	else if(mode==3){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED3;
    	}
    	else if(mode==4){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED4;
    	}
    	else if(mode==5){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED_MAX;
    	}
    	else if(mode==6){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SPEED_AUTO;
    	}
    	else if(mode==7){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerFanMode.SILENT;
    	}
    	else{
    		return null;
    	}
    }
    /**
     * 
     */
    public static SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode getOperMode(int mode){
    	if(mode==0){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.AUTO;
    	}else if(mode==1){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.FANONLY;
    	}else if(mode==2){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.COOLING;
    	}else if(mode==3){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.WARMING;
    	}else if(mode==4){
    		return SensorProtos.AirConditionerSendorSpecificInfo.AirConditionerOperatironMode.DEHUMIDITY;
    	}else{
    		return null;
    	}
    }
    
    /**
     * 
     */
//    public static int convertACCodeMode2OriginDesign(int mode){
//    	if( mode == 0){
//    		return AirconUtil.Mode.FANONLY;
//    	}else if( mode == 1){
//    		return AirconUtil.Mode.WARMING;
//    	}else if( mode == 2){
//    		return AirconUtil.Mode.COOLING;
//    	}else if( mode == 3) {
//    		return AirconUtil.Mode.AUTO;
//    	}else if( mode == 4) {
//    		return AirconUtil.Mode.DEHUMIDITY;
//    	}else {
//    		return -1;
//    	}
//    	
//    }
    
    /**
     * 获取服务更新的命�?
     */
    public static CommandProtos.ServiceUpdate.CommandType getServiceUpdateCommandType(int commandType){
    	if(commandType==ServiceUpdateCommandType.DEVICE_CHG){
    		return CommandProtos.ServiceUpdate.CommandType.DEVICE_BINDING_CHG;
    	}else if(commandType==ServiceUpdateCommandType.LOG_REPORT_CHG){
    		return CommandProtos.ServiceUpdate.CommandType.LOG_REPORT_INTERVAL_CHG;
    	}else if(commandType==ServiceUpdateCommandType.LOG_CHG){
    		return CommandProtos.ServiceUpdate.CommandType.LOG_INTERVAL_CHG;
    	}else if(commandType==ServiceUpdateCommandType.FireWare){
    		return CommandProtos.ServiceUpdate.CommandType.FIRMWARE_AVAILABLE;
    	}else if(commandType==ServiceUpdateCommandType.DSM){
    		return CommandProtos.ServiceUpdate.CommandType.DSM_REQUEST;
    	}else{
    		return null;
    	}
    }
    
    /**
     * 创建服务更新消息
     */
    public static MessageProtos.SpmMessage buildServiceUpdateMessage(Map parameter) {
		MessageProtos.SpmMessage.Builder spmMessageBuilder = MessageProtos.SpmMessage.newBuilder();		
		//Add Head
		MessageProtos.SpmMessage.Header.Builder headerBuilder = MessageProtos.SpmMessage.Header.newBuilder();
		headerBuilder.setSeqnum(buildSeqnum());
		headerBuilder.setSession("");
		headerBuilder.setType(MessageProtos.SpmMessage.MsgType.SERVICE_UPDATE);
		spmMessageBuilder.setHeader(headerBuilder);		
		//Add Command		
		CommandProtos.ServiceUpdate.Builder updateBuilder = CommandProtos.ServiceUpdate.newBuilder();		
		int type=Integer.parseInt(parameter.get("commandType").toString());
		CommandProtos.ServiceUpdate.CommandType commandType=getServiceUpdateCommandType(type);
		updateBuilder.setType(commandType);
		if(commandType.equals(CommandProtos.ServiceUpdate.CommandType.LOG_REPORT_INTERVAL_CHG)
				|| commandType.equals(CommandProtos.ServiceUpdate.CommandType.LOG_INTERVAL_CHG)){
			int interval=Integer.parseInt(parameter.get("interval").toString());
			updateBuilder.setInterval(interval);
		}else if(commandType.equals(CommandProtos.ServiceUpdate.CommandType.FIRMWARE_AVAILABLE)){			
			updateBuilder.setDescription((String)parameter.get("updateLink"));
		}else if(commandType.equals(CommandProtos.ServiceUpdate.CommandType.DSM_REQUEST)){
			CommandProtos.DsmTemperatureRange.Builder dsmBuilder=CommandProtos.DsmTemperatureRange.newBuilder();
			int lowTemp=-1;
			int upperTemp=-1;
			try{
				lowTemp=Integer.parseInt(parameter.get("lowTemp").toString());
				upperTemp=Integer.parseInt(parameter.get("upperTemp").toString());
			}catch(Exception e){
				LOGGER.error((String)parameter.get("lowTemp"));
				LOGGER.error((String)parameter.get("upperTemp"));
				LOGGER.error("dsm温度转换异常",e);
			}
			if(lowTemp!=-1 && upperTemp!=-1){
				dsmBuilder.setLowerTemperature(lowTemp);
	//			}
	//			if(upperTemp!=-1){
				dsmBuilder.setUpperTemperature(upperTemp);
					//dsmBuilder.setUpperTemperature(upperTemp);
//				}
				updateBuilder.setDsmTemperatureRange(dsmBuilder);
			}	
		}
		spmMessageBuilder.setServiceUpdate(updateBuilder);	
		return spmMessageBuilder.build();
	}
    /**
     * 生产定时设置消息
     */
    /*************/
    public static MessageProtos.SpmMessage buildTimeCurvMessage(Map parameter){    	
    	 MessageProtos.SpmMessage.Builder spmMessageBuilder = MessageProtos.SpmMessage.newBuilder();
    	 MessageProtos.SpmMessage.Header.Builder headerBuilder = MessageProtos.SpmMessage.Header.newBuilder();
 		 headerBuilder.setSeqnum(buildSeqnum());
 		 headerBuilder.setSession("");
 		 headerBuilder.setType(MessageProtos.SpmMessage.MsgType.COMMAND);
 		 spmMessageBuilder.setHeader(headerBuilder);
 		 
 		 String deviceId=(String) parameter.get("deviceId");
 		 ACClockServiceImpl cs=new ACClockServiceImpl();
 		 List<ACClock> lst=cs.getClockSettingByDevice(deviceId);
 		 String mac=""; 
		 CommandProtos.AirConditionerControl.Builder airBuilder=CommandProtos.AirConditionerControl.newBuilder();
	     airBuilder.setType(CommandProtos.AirConditionerControl.CommandType.TIMER_SET);	    
 		 for(int i=0;i<lst.size();i++){
 			 ACClock clock=lst.get(i);
 			 if(i==0){
 				 mac=clock.getDeviceMac();
 				 airBuilder.setSensorid(mac);
 			 }
	    	 CommandProtos.TimerCurv.Builder timerCurvBuilder = CommandProtos.TimerCurv.newBuilder();	    	 
	    	 //int appDate=Integer.parseInt(parameter.get("date").toString());
	         timerCurvBuilder.setRecurrentDate(clock.getAppDate());
	         
	         CommandProtos.TimerInfo.Builder timerInfoBuilder = CommandProtos.TimerInfo.newBuilder();   
	         //long startTime=Long.parseLong(parameter.get("startTime").toString());
	         timerInfoBuilder.setStartTime(clock.getStartTime());
	         
	         int acState=clock.getAcState();	         
	         if(acState==1){
	        	 timerInfoBuilder.setState(CommandProtos.TimerInfo.ACState.ON);
	         }else{
	        	 timerInfoBuilder.setState(CommandProtos.TimerInfo.ACState.OFF);
	         }
	         
	         //int tem=Integer.parseInt(parameter.get("temperature").toString());
	         timerInfoBuilder.setTemperature(clock.getTemperature());
	                  
	         //int fanMode=Integer.parseInt(parameter.get("fanMode").toString());
	         timerInfoBuilder.setFanMode(getFanMode(clock.getFanMode()));
	         
	         //int operMode=Integer.parseInt(parameter.get("operMode").toString());
	         timerInfoBuilder.setOperationMode(getOperMode(clock.getOperMode()));        
	         
	         timerCurvBuilder.addTimer(timerInfoBuilder);
	        // airBuilder.addCurves(timerCurvBuilder);
	         airBuilder.addCurves(i, timerCurvBuilder);
 		 }         
 		 if(mac.equals("")){
 			DeviceServiceImpl ds=new DeviceServiceImpl();
 			Device d=ds.getDeviceById(deviceId);
 			airBuilder.setSensorid(d.getMac());
 		 }
         CommandProtos.Command.Builder commandBuilder = CommandProtos.Command.newBuilder();
         commandBuilder.setAcControl(airBuilder);
         spmMessageBuilder.setControlCommand(commandBuilder);
        
         return spmMessageBuilder.build();         
         
    }
    /******************************/
    /**
     * 生产定时设置消息
     */
    /*************/
    public static MessageProtos.SpmMessage buildGateWayControlMessage(Map parameter){    	
    	 MessageProtos.SpmMessage.Builder spmMessageBuilder = MessageProtos.SpmMessage.newBuilder();
    	 MessageProtos.SpmMessage.Header.Builder headerBuilder = MessageProtos.SpmMessage.Header.newBuilder();
 		 headerBuilder.setSeqnum(buildSeqnum());
 		 headerBuilder.setSession("");
 		 headerBuilder.setType(MessageProtos.SpmMessage.MsgType.COMMAND);
 		 spmMessageBuilder.setHeader(headerBuilder);
 		 
 		 
 		CommandProtos.GatewayControl.Builder gateWayControlBuilder=CommandProtos.GatewayControl.newBuilder();
 		gateWayControlBuilder.setType(CommandProtos.GatewayControl.CommandType.GW_ZB_OPENNETWORK);
 		gateWayControlBuilder.setOpenDuration(Config.getInstance().NETWORK_OPENTIME); 		
         
       
         CommandProtos.Command.Builder commandBuilder = CommandProtos.Command.newBuilder();
         commandBuilder.setGwControl(gateWayControlBuilder);
         spmMessageBuilder.setControlCommand(commandBuilder);
        
         return spmMessageBuilder.build();         
         
    }
    
    ///////////////////////////////////////////////////////////////////////
    public static int buildSeqnum(){
    	if( seqnum < 210000000) 
    		seqnum++;
    	else
    		seqnum = 0;
    	
    	return seqnum;
    }
    
    ///////////////////////////////////////////////////////////////////////
    public static String AnalysisAcCode(String _raw){
    	return "";
    }
    
    ///////////////////////////////////////////////////////////////////////
    public synchronized static PythonInterpreter getPythonInterpreter(){
    	if( interpreter == null){
    		interpreter = new PythonInterpreter();
    	}
    	return interpreter;
    }
    
    ///////////////////////////////////////////////////////////////////////
    public static String convertJSONArray2String(JSONArray array){
		String result = "";
		for(int i=0; i<array.length(); ++i){
			result += array.getInt(i);
			result += " ";
		}
		
		return result.trim();
	}
    
    ///////////////////////////////////////////////////////////////////////	
    public static void publish(String channel, int result){
		RedisContext jedis = RedisContextFactory.getInstance().getRedisContext();
		
		Map map = new HashMap();
	    map.put("result", result);
	    ObjectMapper objectMapper = new ObjectMapper();
	    	
		try {
			jedis.publish("RESPONSE:" + channel, objectMapper.writeValueAsString(map));
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			jedis.destroy();
		}
	}
    
    ///////////////////////////////////////////////////////////////////////
    
    public static void main(String[] args){
    	int  dev_id = MessageUtil.analysisDeviceId("540 1620 4200 8440", "320001010001000100000001010001010000000000000000000000000000000000000100000100000000000000000001010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000101000100000");
    	String modSig = MessageUtil.analysisModSig(1000, "320001010001000100000001010001010000000000000000000000000000000000000100000100000000000000000001010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000101000100000");
    	System.out.println(dev_id + ",  " + modSig);
    }
}
