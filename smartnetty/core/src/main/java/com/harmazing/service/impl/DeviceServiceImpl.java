package com.harmazing.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.Config;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.cache.GateWayInfoCache;
import com.harmazing.entity.AirCondition;
import com.harmazing.entity.Device;
import com.harmazing.entity.DoorWindowSensor;
import com.harmazing.entity.Gateway;
import com.harmazing.entity.PirSensor;
import com.harmazing.entity.TemperatureHumiditySensor;
import com.harmazing.entity.User;
import com.harmazing.entity.ZigbeeHALamp;
import com.harmazing.entity.ZigbeeOO;
import com.harmazing.entity.ZigbeeOOElectricityMeter;
import com.harmazing.mapper.AirconditionMapper;
import com.harmazing.mapper.DeviceHistoricStatusMapper;
import com.harmazing.mapper.DeviceMapper;
import com.harmazing.mapper.DoorWindowSensorMapper;
import com.harmazing.mapper.GatewayMapper;
import com.harmazing.mapper.PirSensorMapper;
import com.harmazing.mapper.SysConfigMapper;
import com.harmazing.mapper.TemperatureHumiditySensorMapper;
import com.harmazing.mapper.UserMapper;
import com.harmazing.mapper.ZigBeeMapper;
import com.harmazing.mapper.ZigbeeHALampMapper;
import com.harmazing.mapper.ZigbeeOOElectricityMeterMapper;
import com.harmazing.mapper.ZigbeeOOMapper;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import com.harmazing.service.DeviceService;
import com.harmazing.util.DeviceLogUtil;
import com.harmazing.util.UdpLogger;

/**
 * Created by ming on 14-9-2.
 */
public class DeviceServiceImpl extends ServiceImpl implements DeviceService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceServiceImpl.class);

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    /////////////////////////////////////////////////////////////
    public Gateway getGWByGwId(String id){
    	SqlSession session = sqlSessionFactory.openSession();
		try{
			GatewayMapper mapper = session.getMapper(GatewayMapper.class);
			return mapper.getGWByGwId(id);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}

		return null;
    }
    
	/////////////////////////////////////////////////////////////////////
	@Override
    public void createDevice(DeviceSpecific.DeviceType dev_type, String id, String sn, String mac, String software, String hardware, String model, String vendor, int type, int onOff, int operStatus){
		if(dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC ||
				 dev_type == DeviceSpecific.DeviceType.INNOLINKS_THERMOSTAT ||
				 dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET){
			createAC(id, sn, mac, software, hardware, model, vendor, type, onOff, 1 );	
			
		}else if(dev_type == DeviceSpecific.DeviceType.ZIGBEE_OO){
			createZigbeeOO(id, sn, mac, software, hardware, model, vendor, type, onOff, 1 );
			
		}else if(dev_type == DeviceSpecific.DeviceType.DOOR_WINDDOW_SENSOR){
			this.createDoowWindowSensor(id, sn, mac, software, hardware, model, vendor, type, onOff, 1);
			
		}else if( dev_type == DeviceSpecific.DeviceType.PIR_SENSOR){
			this.createPirSensor(id, sn, mac, software, hardware, model, vendor, type, onOff, 1);
			
		}else if( dev_type == DeviceSpecific.DeviceType.TEMPERATURE_HUMIDITY_SENSOR){
			this.createTempAndHumiditySensor(id, sn, mac, software, hardware, model, vendor, type, 1, 1);
			
		}else if( dev_type == DeviceSpecific.DeviceType.ZIGBEE_HA_LAMP){
			this.createHALamp(id, sn, mac, software, hardware, model, vendor, type, onOff, 1, 0, 0, 0, 0);
			
		}else if( dev_type == DeviceSpecific.DeviceType.ZIGBEE_OO_ELECTRICITY_METER){
			this.createElectricityMeter(id, sn, mac, software, hardware, model, vendor, type, onOff, 1);
		}
	}
	
	public void testUserMapper(){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			UserMapper userMapper = session.getMapper(UserMapper.class);	
			
			String user_id = userMapper.getTbUserUserIdByUserCode("1454399567464");
			User user = userMapper.getTbUserUserByUserCode("1454399567464");
			if(user == null){
				LOGGER.error("Unknow user, do nothing.");
				
			session.commit();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
    /////////////////////////////////////////////////////////////////////
    @Override
    public void handleDeviceSpecific(Map<String, Object> params, DeviceSpecific.DeviceType dev_type){
    	SqlSession session = sqlSessionFactory.openSession(false);
    	try{
			String gw_mac = params.get("gw_mac").toString();
			String dev_mac= params.get("device_mac").toString();
						
			GatewayMapper gwMapper = session.getMapper(GatewayMapper.class);
			String gw_id = gwMapper.getGWIdByGwMac(gw_mac);			
			
			String dev_id = "";
			if( dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC ||
				dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET ||
				dev_type == DeviceSpecific.DeviceType.INNOLINKS_THERMOSTAT){
				AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
				AirCondition ac = acMapper.getAcByAcMac(dev_mac);
				if(ac == null) return;
				
				dev_id = ac.getId();
				acMapper.updateGwAndACBinding(gw_id, ac.getId());				
			}else if(dev_type == DeviceSpecific.DeviceType.ZIGBEE_OO){
				ZigbeeOOMapper zigbeeOOMapper = session.getMapper(ZigbeeOOMapper.class);
				ZigbeeOO zigbeeOO = zigbeeOOMapper.getZiebeeOOByAcMac(dev_mac);
				if( zigbeeOO == null) return;
				
				dev_id = zigbeeOO.getId();
				zigbeeOOMapper.updateGwAndDeviceBinding(gw_id, dev_id);	
			}else if(dev_type == DeviceSpecific.DeviceType.DOOR_WINDDOW_SENSOR){
				DoorWindowSensorMapper mapper = session.getMapper(DoorWindowSensorMapper.class);
				DoorWindowSensor sensor = mapper.getWinDoorSensorByMac(dev_mac);
				if( sensor == null) return;
				
				dev_id = sensor.getId();
				mapper.updateGwAndDeviceBinding(gw_id, dev_id);
				
			}else if( dev_type == DeviceSpecific.DeviceType.TEMPERATURE_HUMIDITY_SENSOR){
				TemperatureHumiditySensorMapper mapper = session.getMapper(TemperatureHumiditySensorMapper.class);
				TemperatureHumiditySensor sensor = mapper.getTempAndHumiditySensorByMac(dev_mac);
				if(sensor == null ) return;
				
				dev_id = sensor.getId();
				mapper.updateGwAndDeviceBinding(gw_id, dev_id);
				
			}else if( dev_type == DeviceSpecific.DeviceType.PIR_SENSOR){
				PirSensorMapper mapper = session.getMapper(PirSensorMapper.class);
				PirSensor sensor = mapper.getPirSensorByMac(dev_mac);
				if(sensor == null) return;
				
				dev_id = sensor.getId();
				mapper.updateGwAndDeviceBinding(gw_id, dev_id);
			}else if( dev_type == DeviceSpecific.DeviceType.ZIGBEE_OO_ELECTRICITY_METER){
				ZigbeeOOElectricityMeterMapper mapper = session.getMapper(ZigbeeOOElectricityMeterMapper.class);
				ZigbeeOOElectricityMeter sensor = mapper.getElecMaterByMac(dev_mac);
				if(sensor == null) return;
				
				dev_id = sensor.getId();
				mapper.updateGwAndDeviceBinding(gw_id, dev_id);
			}
						
			UserMapper userMapper = session.getMapper(UserMapper.class);	
			
			String userCode = params.get("user_code").toString();
			User user = userMapper.getTbUserUserByUserCode(userCode);
			if(user == null){
				LOGGER.error("Unknow user, do nothing.");
				return;
			}
			String user_id = user.getId();								
			String id = params.get("id").toString();
			int type = (int)params.get("device_type");
			int is_primary  = (int)params.get("is_primary");
			params.put("device_id", dev_id);
			params.put("user_id", user_id);			
		 
			userMapper.insertAcAndUserBinding(id, user_id, user.getMobile(), dev_id, dev_mac, type, is_primary);
						
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}    	
    }
    

    /////////////////////////////////////////////////////////////////////
    @Override
    public String getTbUserDeviceIdByUserIdAndDeviceId(String dev_id, String user_id){
    	String id = "";
    	
    	
    	
    	return id;
    }
    
	/////////////////////////////////////////////////////////////////////
	public AirCondition createAC(String id, String sn, String mac, String software, String hardware, String model, String vendor, int type, int onOff, int operStatus){
		
		SqlSession session = sqlSessionFactory.openSession();
		
		AirconditionMapper mapper = null;
		AirCondition ac = null;		
		try{
			mapper = session.getMapper(AirconditionMapper.class);
			ac = mapper.getAcByAcMac(mac);
			if(ac != null){
				mapper.deleteACByMac(mac);
			}
			mapper.createAC(id, sn, mac, software, hardware, model, vendor, type, onOff, operStatus, "new"); // Set tag = 0 means new AC
			session.commit();
			
			//ac = mapper.getAcByAcMac(mac);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			session.close();
		}
		
		return ac;
	}
	
    ////////////////////////////////////////////////////////////////////
	@Override
	public List<AirCondition> getAcsByGwMac(String gw_mac) {
		// TODO Auto-generated method stub
		SqlSession session = sqlSessionFactory.openSession();
		List<AirCondition> acList = null;
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
			acList = acMapper.getAcsByGwMac(gw_mac);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}

		return acList;
	}
	
	////////////////////////////////////////////////////////////////////
	@Override
	public List<AirCondition> getAllAirConditions(){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
			return acMapper.getAllAirConditions();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}

		return null;
	}
	
	////////////////////////////////////////////////////////////////////
	public void updateOpstatus(String ac_mac, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
			acMapper.updateOpstatus(ac_mac, onOff, operStatus); 
			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	////////////////////////////////////////////////////////////////////
	@Override
	public void updateAcStatusByAcMac( String ac_mac, int onOff, int operStatus, int mode, int acTemp, int speed, int energy){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
			acMapper.updateAcStatusByAcMac(ac_mac, onOff, operStatus, mode, acTemp, speed, energy); 
			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	////////////////////////////////////////////////////////////////////
	@Override
	public void updateGwAndACBinding(String gw_mac, String ac_mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
			String ac_id = acMapper.getAcIdByAcMac( ac_mac);
			
			GatewayMapper gwMapper = session.getMapper(GatewayMapper.class);
			String gw_id = gwMapper.getGWIdByGwMac(gw_mac);
			
			acMapper.updateGwAndACBinding(gw_id, ac_id);
			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{			
			session.close();
		}
	}
	
	////////////////////////////////////////////////////////////////////
	@Override
	public void updateAcTempAndHumidityByAcMac(String mac, int temp, int humidity){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);			
			acMapper.updateAcTempAndHumidityByAcMac(mac, temp, humidity);			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{			
			session.close();
		}
	}
	
	////////////////////////////////////////////////////////////////////
	@Override
	public void updateACField(String mac,String field,String fieldValue){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);			
			acMapper.updateACField(mac, field, fieldValue);			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{			
			session.close();
		}
	}
	
	////////////////////////////////////////////////////////////////////
	@Override
	public void updateDeviceIntField(DeviceSpecific.DeviceType devType, String mac, String field, int value){
		SqlSession session = sqlSessionFactory.openSession();
		DeviceService dev_srv = new DeviceServiceImpl();
		try{
			if(devType == DeviceSpecific.DeviceType.INNOLINKS_AC ||
			           devType == DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET ||
			           devType == DeviceSpecific.DeviceType.INNOLINKS_THERMOSTAT){
				//DeviceLogUtil.updateAcIntInfo2Redis( mac, field, value);
				dev_srv.updateACIntField(mac, field, value);
				
			}else if( devType == DeviceSpecific.DeviceType.ZIGBEE_OO){
				//DeviceLogUtil.updateZigbeeOO2Redis(mac, "onOff", value);
				dev_srv.updateZigbeeOOStatusByMac(mac, value, 1);
				
			}else if( devType == DeviceSpecific.DeviceType.ZIGBEE_OO_ELECTRICITY_METER){
				dev_srv.updateElecMaterIntField(mac, "onOff", value);
			}
			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{			
			session.close();
		}
		
	}
	
	////////////////////////////////////////////////////////////////////
	@Override
	public void updateACIntField(String mac,String field,int fieldValue){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);		
			acMapper.updateACIntField(mac, field, fieldValue);			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{			
			session.close();
		}
	}
	
	////////////////////////////////////////////////////////////////////
	@Override
	public AirCondition getAcByAcMac(String ac_mac) {
		SqlSession session = sqlSessionFactory.openSession();
		
		AirCondition ac = null;
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
			ac = acMapper.getAcByAcMac(ac_mac);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return ac;
	}

	/////////////////////////////////////////////////////////////////////
	@Override
	public String getAcIdByAcMac(String ac_mac){
		SqlSession session = sqlSessionFactory.openSession();
		
		String id = "";
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
			id = acMapper.getAcIdByAcMac(ac_mac);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return id;
	}
	
	/////////////////////////////////////////////////////////////////////
	/*@Override
	
	public void insertAcAndUserBinding(Map<String, Object> params) {
		SqlSession session = sqlSessionFactory.openSession();
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
			AirCondition ac = acMapper.getAcByAcMac(params.get("device_id").toString());
			if(ac == null){
				LOGGER.error("Invalid device_id=" + params.get("device_id").toString());
				return;
			}		
			
			UserMapper userMapper = session.getMapper(UserMapper.class);			
			User user = userMapper.getTbUserUserByUserCode(params.get("user_id").toString());
			if( user == null){
				LOGGER.error("Unknow userCode=" + params.get("user_id") + ", do nothing.");
				return;
			}
			
			String user_id = user.getId();
			String id = params.get("id").toString();
			int dev_type = (int)params.get("device_type");
			int is_primary  = (int)params.get("is_primary");
			params.put("device_id", ac.getId() );
			params.put("user_id", user_id);
			
			acMapper.insertAcAndUserBinding(id, user_id, user.getMobile(), ac.getId(), ac.getMac(), dev_type, is_primary);
			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}

	}*/
	
	/////////////////////////////////////////////////////////////////////
	@Override
	public void deleteAcAndUserBindingById(String id){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			UserMapper mapper = session.getMapper(UserMapper.class);
			mapper.deleteAcAndUserBindingById(id);
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{			
			session.close();
		}
	}

	
	/////////////////////////////////////////////////////////////////////
	@Override
	public void updateACModsigByACMac(String mod_sig, String mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
			acMapper.updateACModsigByACMac(mod_sig, mac);
			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	/////////////////////////////////////////////////////////////////////
	@Override
	public void updateAcRcuIdByAcMac(String mac, int rcu_id, int is_paired){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
			acMapper.updateAcRcuIdByAcMac(mac, rcu_id, is_paired);
			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	/////////////////////////////////////////////////////////////////////
	@Override
	public void updateACCurrentStatusByACMac(String mac, int mode, int temp, int swing_v, int swing_h, int speed, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			AirconditionMapper acMapper = session.getMapper(AirconditionMapper.class);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String date_time = sdf.format(date);
			acMapper.updateACCurrentStatusByACMac(mac, mode, temp, swing_v, swing_h, speed, onOff, operStatus, date_time);			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}

	/////////////////////////////////////////////////////////////////////
	@Override
	public int createGateway(String id, String sn, String mac, String software, String hardware, 
			                  String model, String vendor, int type, int onOff, int operStatus, String ip){
		int result = 0;
		SqlSession session = sqlSessionFactory.openSession();
		try{
			GatewayMapper mapper = session.getMapper(GatewayMapper.class);
			Gateway gw = mapper.getGWByGwMac(mac);
			if(gw == null){
				mapper.createGateway(id, sn, mac, software, hardware, model, vendor, type, onOff, operStatus, ip);
				session.commit();
			}
		}catch(Exception e){
			e.printStackTrace();
			result = 1;
		}finally{
			session.close();
		}
		
		return result;
	}
	/////////////////////////////////////////////////////////////////////
	@Override
	public String getGWIdByGwMac(String gw_mac){
		SqlSession session = sqlSessionFactory.openSession();
		String gw_id = "";
		try{
			GatewayMapper mapper = session.getMapper(GatewayMapper.class);
			gw_id = mapper.getGWIdByGwMac(gw_mac);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return gw_id;
	}
	
	/////////////////////////////////////////////////////////////////////
	@Override
	public Gateway getGWByGWMac(String gw_mac){
		SqlSession session = sqlSessionFactory.openSession();
		Gateway gw = null;
		try{
			GatewayMapper mapper = session.getMapper(GatewayMapper.class);
			gw = mapper.getGWByGwMac(gw_mac);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return gw;
	}
	
	/////////////////////////////////////////////////////////////////////
	@Override
	public List<Gateway> getAllGateways(){
		List<Gateway> gws = null;
		SqlSession session = sqlSessionFactory.openSession();
	
		try{
			GatewayMapper mapper = session.getMapper(GatewayMapper.class);
			gws = mapper.getAllGateways();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return gws;
	}
	
	/////////////////////////////////////////////////////////////////////
	public void updateGWOprStatusByMac(String mac, int onOff, int oprStatus, String ip){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			GatewayMapper gwMapper = session.getMapper(GatewayMapper.class);
			gwMapper.updateGWOprStatusByMac(mac, onOff, oprStatus, ip);		
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	/////////////////////////////////////////////////////////////////////
	public void updateGWOprAndVersionStatusByMac(String mac, int onOff, int oprStatus, String software, String hardware, String ip){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			GatewayMapper gwMapper = session.getMapper(GatewayMapper.class);
			gwMapper.updateGWOprAndVersionStatusByMac(mac, onOff, oprStatus, software, hardware, ip);		
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	/////////////////////////////////////////////////////////////////////
	public ZigbeeOO createZigbeeOO(String id, String sn, String mac, String software, String hardware, String model, String vendor, int type, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
		
		ZigbeeOOMapper mapper = null;
		ZigbeeOO zo = null;
		try{
			mapper = session.getMapper(ZigbeeOOMapper.class);
			zo = mapper.getZiebeeOOByAcMac(mac);
			if(zo != null){
				mapper.deleteZigbeeOODeviceByMac(mac);
			}
			mapper.createZigbeeOO(id, sn, mac, software, hardware, model, vendor, type, onOff, operStatus, "new");
			session.commit();
			
			//zo = mapper.getZiebeeOOByAcMac(mac);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return zo;
	}
	
	/////////////////////////////////////////////////////////////////////
	@Override
	public ZigbeeOO getZiebeeOOByMac(String mac){
		ZigbeeOO zigbeeOO = null;
		SqlSession session = sqlSessionFactory.openSession();
		try{
			ZigbeeOOMapper zigbeeOO_mapper = session.getMapper(ZigbeeOOMapper.class);
			zigbeeOO = zigbeeOO_mapper.getZiebeeOOByAcMac(mac);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}		
		return zigbeeOO;
	}
	
	/////////////////////////////////////////////////////////////////////
	@Override
	public List<ZigbeeOO> getOOsByGwMac(String gw_mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			ZigbeeOOMapper zigbeeOO_mapper = session.getMapper(ZigbeeOOMapper.class);
			return zigbeeOO_mapper.getAcsByGwMac(gw_mac);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}	
		
		return null;
	}
	
	/////////////////////////////////////////////////////////////////////
	@Override
	public List<ZigbeeOO> getAllZiebeeOO(){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			ZigbeeOOMapper zigbeeOO_mapper = session.getMapper(ZigbeeOOMapper.class);
			return zigbeeOO_mapper.getAllZiebeeOO();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}	
		
		return null;
	}
	
	/////////////////////////////////////////////////////////////////////
	public void updateZigbeeOOStatusByMac( String mac, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			ZigbeeOOMapper zigbeeOO_mapper = session.getMapper(ZigbeeOOMapper.class);
			zigbeeOO_mapper.updateZigbeeOOStatusByMac(mac, onOff, operStatus);		
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	/////////////////////////////////////////////////////////////////////
	public void updateOODevIntField(String mac, String field, int value){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			ZigbeeOOMapper zigbeeOO_mapper = session.getMapper(ZigbeeOOMapper.class);
			zigbeeOO_mapper.updateOODevIntField(mac, field, value);		
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	// Below is for DoorWindowsSensor
	/////////////////////////////////////////////////////////////////////
	public DoorWindowSensor createDoowWindowSensor(String id, String sn, String mac, String software, String hardware, String model, String vendor, int type, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
	
		DoorWindowSensorMapper mapper = null;
		DoorWindowSensor sensor = null;		
		try{
			mapper = session.getMapper(DoorWindowSensorMapper.class);
			sensor = mapper.getWinDoorSensorByMac(mac);
			if(sensor != null){
				mapper.deleteDoorWinSensorByMac(mac);
			}
			mapper.createWinDoorSensor(id, sn, mac, software, hardware, model, vendor, type, onOff, operStatus, "new");
			session.commit();
				
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return sensor;
	}
	
	////////////////////////////////////////////////////////////////////
	@Override
	public DoorWindowSensor getWinDoorSensorByMac(String mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			DoorWindowSensorMapper dw_mapper = session.getMapper(DoorWindowSensorMapper.class);
			return dw_mapper.getWinDoorSensorByMac(mac);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return null;		
	}
	
	////////////////////////////////////////////////////////////////////
	@Override
	public void updateWinDoorStatusByMac(String mac, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			DoorWindowSensorMapper dw_mapper = session.getMapper(DoorWindowSensorMapper.class);
			dw_mapper.updateWinDoorStatusByMac(mac, onOff, operStatus);;	
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public void  updateWinDoorFieldByMac(String mac, String field, int value){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			DoorWindowSensorMapper dw_mapper = session.getMapper(DoorWindowSensorMapper.class);
			dw_mapper.updateWinDoorFieldByMac(mac, field, value);	
			
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public List<DoorWindowSensor> getWinDoorSensorsByGwMac(String gw_mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			DoorWindowSensorMapper dw_mapper = session.getMapper(DoorWindowSensorMapper.class);
			return dw_mapper.getWinDoorSensorsByGwMac(gw_mac);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return null;	
	}
		
	// Below is for PirSensor
	/////////////////////////////////////////////////////////////////////
	public PirSensor createPirSensor(String id, String sn, String mac, String software, String hardware, String model, String vendor, int type, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
		
		PirSensorMapper mapper = null;
		PirSensor sensor = null;		
		try{
			mapper = session.getMapper(PirSensorMapper.class);
			sensor = mapper.getPirSensorByMac(mac);
			if(sensor != null){
				mapper.deletePirByMac(mac);
			}
			mapper.createPirSensor(id, sn, mac, software, hardware, model, vendor, type, onOff, operStatus, "new");
			session.commit();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return sensor;
	}

	//////////////////////////////////////////////////////////////////
	@Override
	public PirSensor getPirSensorByMac(String mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			PirSensorMapper mapper = session.getMapper(PirSensorMapper.class);
			return mapper.getPirSensorByMac(mac);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return null;	
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public void updatePirtatusByMac(String mac, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			PirSensorMapper dw_mapper = session.getMapper(PirSensorMapper.class);
			dw_mapper.updatePirtatusByMac(mac, onOff, operStatus);			
			session.commit();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public void updatePirIntField( String mac,  String field, int value){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			PirSensorMapper dw_mapper = session.getMapper(PirSensorMapper.class);
			dw_mapper.updatePirIntField(mac, field, value);			
			session.commit();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	//////////////////////////////////////////////////////////////////
	@Override
	public List<PirSensor> getPirSensorsByGwMac(String gw_mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			PirSensorMapper mapper = session.getMapper(PirSensorMapper.class);
			return mapper.getPirSensorsByGwMac(gw_mac);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return null;	
	}
	
	// Below is for TemperatureHumiditySensorOnline
	/////////////////////////////////////////////////////////////////////
	public TemperatureHumiditySensor createTempAndHumiditySensor(String id, String sn, String mac, String software, String hardware, String model, String vendor, int type, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
		
		TemperatureHumiditySensorMapper mapper = null;
		TemperatureHumiditySensor sensor = null;		
		try{
			mapper = session.getMapper(TemperatureHumiditySensorMapper.class);
			sensor = mapper.getTempAndHumiditySensorByMac(mac);
			if(sensor != null){
				mapper.deleteTempAndHumidtySensorByMac(mac);
			}
			mapper.createWinDoorSensor(id, sn, mac, software, hardware, model, vendor, type, 1, 1, "new");
			session.commit();
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return sensor;
	}
	
	/////////////////////////////////////////////////////////////////////////
	@Override
	public void recordHTStatus(String mac, int temp, int humidity){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			TemperatureHumiditySensorMapper mapper = session.getMapper(TemperatureHumiditySensorMapper.class);
			mapper.recordHTSensorStatus(UUID.randomUUID().toString(), mac, temp, humidity);		
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}

	/////////////////////////////////////////////////////////////////////////
	@Override
	public TemperatureHumiditySensor getTempAndHumiditySensorByMac(String mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			TemperatureHumiditySensorMapper mapper = session.getMapper(TemperatureHumiditySensorMapper.class);
			return mapper.getTempAndHumiditySensorByMac(mac);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return null;	
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public void updateTempAndHumidityByMac(String mac, int temp, int humidity){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			TemperatureHumiditySensorMapper mapper = session.getMapper(TemperatureHumiditySensorMapper.class);
			mapper.updateTempAndHumidityByMac(mac, temp, humidity);;		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public void updateTHIntField(String mac, String field, int value){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			TemperatureHumiditySensorMapper mapper = session.getMapper(TemperatureHumiditySensorMapper.class);
			mapper.updateTHIntField(mac, field, value);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	//////////////////////////////////////////////////////////////////
	@Override
	public List<TemperatureHumiditySensor> getTempHumiditySensorsByGwMac(String gw_mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			TemperatureHumiditySensorMapper mapper = session.getMapper(TemperatureHumiditySensorMapper.class);
			return mapper.getTempHumiditySensorsByGwMac(gw_mac);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return null;
	}
	
	//////////////////////////////////////////////////////////////////
	@Override	
	public void createElectricityMeter(String id, String sn, String mac, String software, String hardware, String model, String vendor,
            int type, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
		
		ZigbeeOOElectricityMeterMapper mapper = null;
		ZigbeeOOElectricityMeter elec_mater = null;		
		try{
			mapper = session.getMapper(ZigbeeOOElectricityMeterMapper.class);
			elec_mater = mapper.getElecMaterByMac(mac);
			if(elec_mater != null){
				mapper.deleteElecMeterByMac(mac);
			}
			mapper.createElectricityMater(id, sn, mac, software, hardware, model, vendor, type,  onOff, operStatus, "new");
			session.commit();
				
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public ZigbeeOOElectricityMeter getElecMaterByMac(String mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			ZigbeeOOElectricityMeterMapper mapper = session.getMapper(ZigbeeOOElectricityMeterMapper.class);
			return mapper.getElecMaterByMac(mac);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return null;
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public List<ZigbeeOOElectricityMeter> getElecMetersByGwMac(String gw_mac){
		
		SqlSession session = sqlSessionFactory.openSession();
		try{
			ZigbeeOOElectricityMeterMapper elec_mater = session.getMapper(ZigbeeOOElectricityMeterMapper.class);
			return elec_mater.getElecMetersByGwMac(gw_mac);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return null;	
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public void updateElecMeterStatusByMac(String mac, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			ZigbeeOOElectricityMeterMapper mapper = session.getMapper(ZigbeeOOElectricityMeterMapper.class);
			mapper.updateElecMeterStatusByMac(mac, onOff, operStatus);	
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public void updateElecMaterIntField( String mac, String field, int value){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			ZigbeeOOElectricityMeterMapper mapper = session.getMapper(ZigbeeOOElectricityMeterMapper.class);
			mapper.updateElecMaterOnoffStatus(mac, value);
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public void deleteElecMeterByMac(String mac){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			ZigbeeOOElectricityMeterMapper mapper = session.getMapper(ZigbeeOOElectricityMeterMapper.class);
			mapper.deleteElecMeterByMac(mac);
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public void createHALamp(String id, String sn, String mac, String software, String hardware, String model, String vendor, int type, 
			int onOff, int operStatus, int illuminance,  int red, int green, int blue){
	
		SqlSession session = sqlSessionFactory.openSession();
		
		ZigbeeHALampMapper mapper = null;
		ZigbeeHALamp ha_lamp = null;		
		try{
			mapper = session.getMapper(ZigbeeHALampMapper.class);
			ha_lamp = mapper.getZigbeeHALampByAcMac(mac);
			if(ha_lamp != null){
				mapper.deleteHALampByMac(mac);
			}
			mapper.createHALamp(id, sn, mac, software, hardware, model, vendor, type,  onOff, operStatus, "new", illuminance, red, green, blue);
			session.commit();
				
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}

	//////////////////////////////////////////////////////////////////
	@Override
	public ZigbeeHALamp getZigbeeHALampByAcMac(String mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			ZigbeeHALampMapper mapper = session.getMapper(ZigbeeHALampMapper.class);
			return mapper.getZigbeeHALampByAcMac(mac);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return null;
		
	}

	//////////////////////////////////////////////////////////////////
	@Override
	public List<ZigbeeHALamp> getHALampsByGwMac(String gw_mac){
		SqlSession session = sqlSessionFactory.openSession();
		try{
			ZigbeeHALampMapper dw_mapper = session.getMapper(ZigbeeHALampMapper.class);
			return dw_mapper.getHALampsByGwMac(gw_mac);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return null;	
		
	}

	
	//////////////////////////////////////////////////////////////////
	@Override
	public void updateHALampIlluminance(String mac,  int illuminance){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			ZigbeeHALampMapper mapper = session.getMapper(ZigbeeHALampMapper.class);
			mapper.updateHALampIlluminance(mac, illuminance);	
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}
		
	//////////////////////////////////////////////////////////////////
	@Override
	public void updateHALampColor(String mac, int illuminance, int red, int green, int blue ){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			ZigbeeHALampMapper mapper = session.getMapper(ZigbeeHALampMapper.class);
			mapper.updateHALampColor(mac, illuminance, red, green, blue);	
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}
	
	//////////////////////////////////////////////////////////////////
	@Override	
	public void updateHALampStatus( String ac_mac, int onOff, int operStatus){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			ZigbeeHALampMapper mapper = session.getMapper(ZigbeeHALampMapper.class);
			mapper.updateHALampStatus(ac_mac, onOff, operStatus);
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	//////////////////////////////////////////////////////////////////
	@Override	
	
	public void updateHALampIntField(String mac, String field, int value){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			ZigbeeHALampMapper mapper = session.getMapper(ZigbeeHALampMapper.class);
			mapper.updateHALampIntField(mac, field, value);		
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public void updateHALampAll( String mac, int onOff, int operStatus, int illuminance, int red, int green, int blue ){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			ZigbeeHALampMapper mapper = session.getMapper(ZigbeeHALampMapper.class);
			mapper.updateHALampAll(mac, onOff, operStatus, illuminance, red, green, blue);	
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	//////////////////////////////////////////////////////////////////
	@Override
	public void deleteHALampByMac(String mac){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			ZigbeeHALampMapper mapper = session.getMapper(ZigbeeHALampMapper.class);
			mapper.deleteHALampByMac(mac);	
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	/////////////////////////////////////////////////////////////////////	
	public void updateDeviceVersion(DeviceSpecific.DeviceType dev_type, String mac, String sw_ver, String hd_ver){
		String table = "";
		
		if(dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC || 
				dev_type == DeviceSpecific.DeviceType.INNOLINKS_AC_POWER_SOCKET ||
				dev_type == DeviceSpecific.DeviceType.INNOLINKS_THERMOSTAT ){	
			table = "spms_ac";
			
			
		}else if(dev_type == DeviceSpecific.DeviceType.ZIGBEE_OO){
			table = "spms_onoff_plug";
			
		}else if(dev_type == DeviceSpecific.DeviceType.ZIGBEE_OO_ELECTRICITY_METER){
			table = "spms_onoff_pm_plug";
			
		}else if(dev_type == DeviceSpecific.DeviceType.PIR_SENSOR){
			table = "spms_pir";
			
		}else if(dev_type == DeviceSpecific.DeviceType.DOOR_WINDDOW_SENSOR){
			table = "spms_win_door";
			
		}else if(dev_type == DeviceSpecific.DeviceType.TEMPERATURE_HUMIDITY_SENSOR){
			table = "spms_ht_sensor";
			
		}else if(dev_type == DeviceSpecific.DeviceType.LIGHT_SWITCH){
			// TODO
			
		}else if(dev_type == DeviceSpecific.DeviceType.TEMPERATURE_HUMIDITY_SENSOR){
			table = "spms_ht_sensor";
			
		}
		
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			AirconditionMapper mapper = session.getMapper(AirconditionMapper.class);
			mapper.updateDevVersion(table, mac, sw_ver, hd_ver);
			session.commit();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			session.close();
		}
		
		
	}
	
	/////////////////////////////////////////////////////////////////////
	@Override
	public void recordWinDoorHistroyStatus( String user_id, String mac, String field, int value){
		SqlSession session = sqlSessionFactory.openSession();
		
		try{
			DeviceHistoricStatusMapper mapper = session.getMapper(DeviceHistoricStatusMapper.class);
			mapper.recordWinDoorHistroyStatus(UUID.randomUUID().toString(), user_id, mac, field, value);	
			session.commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
	/////////////////////////////////////////////////////////////////////
	@Override
	public void ircodeReceivedDBOperation(String gw_mac, String dev_mac, Map<String, Object> params){
		
	}
	/////////////////////////////////////////////////////////////////////
    
   /* @Override
    public Device getGatewayByMacAndSN(String mac,String sn){
        SqlSession session = sqlSessionFactory.openSession();
        Device device = new Device();
        try {
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();
            Map<String,String> gateway = redisContext.getBaseGW(mac + "_" + sn);
            redisContext.destroy();
            if(gateway.size() > 0 && gateway.get("userId") != null){
                device.setId((String) gateway.get("gwId"));
                device.setMac((String) gateway.get("mac"));
                device.setSn((String) gateway.get("sn"));
                device.setUserId((String) gateway.get("userId"));
                device.setDisable(Integer.valueOf(gateway.get("disable").toString()));

                JSONObject jsonObject = new JSONObject(gateway.get("devices").toString());
                JSONArray jsonArray = ((JSONArray)jsonObject.get("devices"));
                List<Map> bindDevices = Lists.newArrayList();
                for(int i =0 ; i< jsonArray.length(); i++) {
                    Map map = Maps.newHashMap();
                    map.put("id",((JSONObject)jsonArray.get(i)).get("deviceId"));
                    map.put("mac",((JSONObject)jsonArray.get(i)).get("deviceMac"));
                    map.put("type",((JSONObject)jsonArray.get(i)).get("deviceType"));
                    map.put("coolerStart",((JSONObject)jsonArray.get(i)).get("coolerStart"));
                    map.put("coolerEnd",((JSONObject)jsonArray.get(i)).get("coolerEnd"));
                    map.put("heaterStart",((JSONObject)jsonArray.get(i)).get("heaterStart"));
                    map.put("heaterEnd",((JSONObject)jsonArray.get(i)).get("heaterEnd"));
                    if(((JSONObject)jsonArray.get(i)).has("sensorEnabled")) {
                        map.put("sensorEnabled",((JSONObject)jsonArray.get(i)).has("sensorEnabled"));
                    } else {
                        map.put("sensorEnabled",null);
                    }
                    map.put("mode",((JSONObject)jsonArray.get(i)).get("mode"));
                    bindDevices.add(map);
                }
                device.setAirconServiceList(bindDevices);
            }
        } finally {
            session.close();
        }

        return device;
    }*/
    /******************************add by hanhao*************************/
    @Override
    public Device getGatewayByMacAndSN(String mac,String sn){
        //SqlSession session = sqlSessionFactory.openSession();
        Device device = new Device();
        try {
            //DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();
            /*****************因为网关那不能提供sn，此处验证改为只按照mac地址进行验证**************
            List<Map<String,String>> gatewayList = redisContext.getBaseGW(mac + "_" + sn);
            ***/
            List<Map<String,String>> gatewayList = redisContext.getBaseGW(mac + "_");
            redisContext.destroy();
            List<Map> bindDevices = Lists.newArrayList();            
            if(gatewayList.size() > 0 && gatewayList.get(0).get("userId") != null){
                device.setId((String) gatewayList.get(0).get("gwId"));
                device.setMac((String) gatewayList.get(0).get("mac"));
                device.setSn((String) gatewayList.get(0).get("sn"));
                device.setUserId((String) gatewayList.get(0).get("userId"));
                device.setDisable(Integer.valueOf(gatewayList.get(0).get("disable").toString()));
                device.setEleArea((String) gatewayList.get(0).get("eleArea"));
                for(Map<String,String> gateway : gatewayList){
	                JSONObject jsonObject = new JSONObject(gateway.get("devices").toString());
	                JSONArray jsonArray = ((JSONArray)jsonObject.get("devices"));
	                for(int i =0 ; i< jsonArray.length(); i++) {
	                    Map map = Maps.newHashMap();
	                    map.put("id",((JSONObject)jsonArray.get(i)).get("deviceId"));
	                    map.put("mac",((JSONObject)jsonArray.get(i)).get("deviceMac"));
	                    map.put("type",((JSONObject)jsonArray.get(i)).get("deviceType"));
	                    map.put("coolerStart",((JSONObject)jsonArray.get(i)).get("coolerStart"));
	                    map.put("coolerEnd",((JSONObject)jsonArray.get(i)).get("coolerEnd"));
	                    map.put("heaterStart",((JSONObject)jsonArray.get(i)).get("heaterStart"));
	                    map.put("heaterEnd",((JSONObject)jsonArray.get(i)).get("heaterEnd"));
	                    if(((JSONObject)jsonArray.get(i)).has("sensorEnabled")) {
	                        map.put("sensorEnabled",((JSONObject)jsonArray.get(i)).has("sensorEnabled"));
	                    } else {
	                        map.put("sensorEnabled",null);
	                    }
	                    map.put("mode",((JSONObject)jsonArray.get(i)).get("mode"));
	                    map.put("eleArea",(String)gatewayList.get(0).get("eleArea"));
	                    bindDevices.add(map);
	                }
	                device.setAirconServiceList(bindDevices);
                }
            }
        } finally {
            //session.close();
        }

        return device;
    }
    /******************************end by hanhao*************************/
    @Override
    public Device getDeviceByMacAndSN(String mac, String sn, String type) {
    	SqlSession session = sqlSessionFactory.openSession();
        Device device = null;
        try {
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            device = deviceMapper.getDeviceByMacAndSN(mac, sn, type);
        } finally {
            session.close();
        }

        return device;
    }

    @Override
    public Device getDeviceById(String id) {
        SqlSession session = sqlSessionFactory.openSession();
        Device device = null;
        try {
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            device = deviceMapper.getDeviceById(id);
        } finally {
            session.close();
            return device;
        }
    }
    /*****************************************
    @Override
    public void updateDevices(List<Map<String,String>> devices){
    	SqlSession session = sqlSessionFactory.openSession(false);
    	try{
	    	for(Map<String,String> device : devices){
	    		 String id = device.get("id");
	    		 String operationStatus = device.get("operationStatus");
//				 LOGGER.info("-----------schedule operationStatus----------"+operationStatus);
	    		 String sessionStr = device.get("session");
	    		 String onOff = device.get("onOff");
	    		 String temperature = device.get("temperature");
	    		 String acTemperature = device.get("acTemperature");
	    		 String power = device.get("power");
	    		 String speed = device.get("speed");
	    		 String direction = device.get("direction");
	    		 String startTime = device.get("startTime");
	    		 if(startTime!=null&&"".equals(startTime)){
	    			 startTime = startTime.substring(startTime.length()-3)+"000";
	    		 }
	    		 String mode = device.get("mode");
	    		 String remain = device.get("remain");
	    		 String accumulatePower = device.get("accumulatePower");
	    		 String server = device.get("server");
	    		 DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
	             deviceMapper.updateByDevice(
	            		 "null".equals(operationStatus)||operationStatus == null?null:Integer.parseInt(operationStatus),
	            				 "null".equals(sessionStr)||sessionStr == null?null:sessionStr,
	            		"null".equals(onOff)||onOff == null?null:Integer.parseInt(onOff),
	            		"null".equals(temperature)||temperature == null?null:Integer.parseInt(temperature),
	            				"null".equals(acTemperature)||acTemperature == null?null:Integer.parseInt(acTemperature),
	            						"null".equals(power)||power == null?null:Integer.parseInt(power),
	            								"null".equals(speed)||speed == null?null:Integer.parseInt(speed),
	            										"null".equals(direction)||direction == null?null:Integer.parseInt(direction),
	            												"null".equals(startTime)||startTime == null?null:new Timestamp(Long.parseLong(startTime)),
	                    "null".equals(mode)||mode == null?null:mode,
	                    "null".equals(remain)||remain == null?null:Integer.parseInt(remain),
	                    		"null".equals(accumulatePower)||accumulatePower == null?null:Long.parseLong(accumulatePower),
	                    				"null".equals(server)||server == null?null:server,
	                    id);
	    	}
	    	session.commit();
    	}catch(Exception e){
    		LOGGER.info("UPDATEDEVICES", e);
    		for(Map<String,String> device : devices){
    			LOGGER.info("UPDATEDEVICESERROR::::"+new JSONObject(device).toString());
    		}
    		session.rollback(true);
    	}finally{
    		session.close();
    	}
    }
	****************************/
    @Override
    public void updateDevices(List<Map<String,String>> devices){    	
    	
    	List<Map<String, Object>> dbList = new ArrayList<Map<String, Object>>();
    	SqlSession session = sqlSessionFactory.openSession(false);
    	DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
    	
    	try{
    		/****************************
	    	for(Map<String,String> device : devices){
	    		 String id = device.get("id");
	    		 String operationStatus = device.get("operationStatus");
				 LOGGER.info("-----------schedule operationStatus----------"+operationStatus);
	    		 String sessionStr = device.get("session");
	    		 String onOff = device.get("onOff");
	    		 String temperature = device.get("temperature");
	    		 String acTemperature = device.get("acTemperature");
	    		 String power = device.get("power");
	    		 String speed = device.get("speed");
	    		 String direction = device.get("direction");
	    		 String startTime = device.get("startTime");
	    		 if(StringUtils.isNotBlank(startTime)){
	    			 startTime = startTime.substring(0,startTime.length()-3)+"000";
	    		 }
	    		 String mode = device.get("mode");
	    		 String remain = device.get("remain");
	    		 String accumulatePower = device.get("accumulatePower");
	    		 String server = device.get("server");
	    		 Map<String, Object> dbmap = new HashMap<String, Object>();
	    		 dbmap.put("operStatus", "null".equals(operationStatus)||operationStatus == null?null:Integer.parseInt(operationStatus));
	    		 dbmap.put("onOff", "null".equals(onOff)||onOff == null?null:Integer.parseInt(onOff));
	    		 dbmap.put("temp", "null".equals(temperature)||temperature == null?null:Integer.parseInt(temperature));
	    		 dbmap.put("acTemp", "null".equals(acTemperature)||acTemperature == null?null:Integer.parseInt(acTemperature));
	    		 dbmap.put("power", "null".equals(power)||power == null?null:Integer.parseInt(power));
	    		 dbmap.put("speed", "null".equals(speed)||speed == null?null:Integer.parseInt(speed));
	    		 dbmap.put("direction", "null".equals(direction)||direction == null?null:Integer.parseInt(direction));
	    		 dbmap.put("startTime", "null".equals(startTime)||startTime == null?null:new Timestamp(Long.parseLong(startTime)));
	    		 dbmap.put("mode", "null".equals(mode)||mode == null?null:mode);
	    		 dbmap.put("remain", "null".equals(remain)||remain == null?null:Integer.parseInt(remain));
	    		 dbmap.put("accumulatePower", "null".equals(accumulatePower)||accumulatePower == null?null:Long.parseLong(accumulatePower));
	    		 dbmap.put("id", id);
	    		 dbmap.put("sessionId", "null".equals(sessionStr)||sessionStr == null?null:sessionStr);
	    		 dbmap.put("server", "null".equals(server)||server == null?null:server);
	             dbList.add(dbmap);	 
	            				 
	    	}
	    	*****************************/
	    	session.update("com.harmazing.mapper.DeviceMapper.batchUpdateDevices", dbList);
	    	session.commit();
	    	LOGGER.info("device size:"+devices.size());
    	}catch(Exception e){
    		LOGGER.info("UPDATEDEVICES", e);
    		for(Map<String,String> device : devices){
    			LOGGER.info("UPDATEDEVICESERROR::::"+new JSONObject(device).toString());
    		}
    		session.rollback(true);
    	}finally{
    		session.close();
    	}
    }

    @Override
    public void updateDevice(Device device) {
        SqlSession session = sqlSessionFactory.openSession(true);
        try {
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            deviceMapper.updateDevice(device.getVender(),
                    device.getModel(),
                    device.getHardware(),
                    device.getSoftware(),
                    device.getCurrentSoftware(),
                    device.getOperationStatus(),
                    device.getSession(),
                    device.getOnOff(),
                    device.getTemperature(),
                    device.getAcTemperature(),
                    device.getPower(),
                    device.getSpeed(),
                    device.getDirection(),
                    device.getStartTime(),
                    device.getMode(),
                    device.getRemain(),
                    device.getServer(),
                    device.getAccumulatePower(),
                    device.getId());
            session.commit(true);
        } catch (Exception e) {
            LOGGER.info("UPDATEDEVICE ERROR", e);
        } finally {
            session.close();
        }

        return ;
    }

    @Override
    public List<Device> getDeviceByParentId(String parentId) {
        return null;
    }

    @Override
    public List<Device> getDeviceByParentIdAndDeviceType(String parentId, String deviceType) {
        return null;
    }

    

   
    @Override
    public void delGWInfo(){
    	 RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();
         redisContext.delBaseGW();
         redisContext.destroy();
    }
    
    
    @Override
    public void setGWInfoToRedis() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            List<Map> gateWayDeviceInfo = deviceMapper.getAllBindingGateWay();
           
            List <String> keys = Lists.newArrayList();
            List <Map> gateWayInfo = Lists.newArrayList();
            StringBuilder nowKey= new StringBuilder();
            for(int i = 0,len=gateWayDeviceInfo.size();i<len;i++) {
            	Map<String,String> gateWay = gateWayDeviceInfo.get(i);
                StringBuilder tempKey = new StringBuilder(gateWay.get("mac").toString()).append("_").append(gateWay.get("sn")==null?"NONE":gateWay.get("sn").toString());
                if(!nowKey.toString().equals(tempKey.toString())) {
                    /*********************缓存不向reids里面写**********/
                	if(keys.size() != 0) {
                        List devicesLastGW = (List) gateWayInfo.get(gateWayInfo.size() - 1).get("devices");
                        if(devicesLastGW.size() > 0) {
                            Map map = new HashMap();
                            map.put("devices",devicesLastGW);
                            gateWayInfo.get(gateWayInfo.size() - 1).put("devices", new JSONObject(map).toString());
                        }
                    }
                    /********************************/
                    keys.add(tempKey.toString());
                    Map map = Maps.newHashMap();
                    map.put("gwId", gateWay.get("gwId"));
                    map.put("mac", gateWay.get("mac"));
                    map.put("sn",gateWay.get("sn"));
                    map.put("userId",gateWay.get("userId"));
                    map.put("status",gateWay.get("status"));
                    map.put("disable", gateWay.get("disable"));
                    map.put("eleArea", gateWay.get("eleArea"));
                    map.put("bizArea", gateWay.get("bizArea"));
                    map.put("devices",Lists.newArrayList());
                    gateWayInfo.add(map);
//                    GateWayInfoCache.set(tempKey.toString(),map);
                }
                nowKey=tempKey;
                Map deviceMap = Maps.newHashMap();
                deviceMap.put("deviceId", gateWay.get("deviceId"));
                deviceMap.put("deviceMac",gateWay.get("deviceMac"));
                deviceMap.put("deviceType",gateWay.get("deviceType"));
                deviceMap.put("coolerStart",gateWay.get("coolerStart"));
                deviceMap.put("coolerEnd",gateWay.get("coolerEnd"));
                deviceMap.put("heaterStart",gateWay.get("heaterStart"));
                deviceMap.put("heaterEnd",gateWay.get("heaterEnd"));
                deviceMap.put("sensorEnabled",gateWay.get("sensorEnabled"));
                deviceMap.put("eleArea",gateWay.get("eleArea"));
                deviceMap.put("mode",gateWay.get("mode"));
                deviceMap.put("id", gateWay.get("deviceId"));
                deviceMap.put("type",gateWay.get("deviceType"));
                deviceMap.put("mac",gateWay.get("deviceMac"));
//                ((List)GateWayInfoCache.get(nowKey.toString()).get("devices")).add(deviceMap);

                ((List)gateWayInfo.get(gateWayInfo.size()-1).get("devices")).add(deviceMap);
            }
            /***********************缓存不向reids里面写**********/
            if(keys.size() != 0) {
                List devicesLastGW = (List) gateWayInfo.get(gateWayInfo.size() - 1).get("devices");
                if(devicesLastGW.size() > 0) {
                    Map map = new HashMap();
                    map.put("devices",devicesLastGW);
                    gateWayInfo.get(gateWayInfo.size() - 1).put("devices", new JSONObject(map).toString());
                }
            }
            RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();
            redisContext.setBaseGW(keys,gateWayInfo,-1);
            redisContext.destroy();
            /***************************************/
        } catch (Exception e) {
            LOGGER.error("CLEAR DEVICE SESSIONBYSERVER, ERROR", e);
        } finally {
            session.close();
        }
    }
    /**更新网关信息到redis**/
    @Override
    public void updateGWInfoToRedis(String userId) {
    	System.out.println("*****************serviceupdate device to redis**************************");
        SqlSession session = sqlSessionFactory.openSession();
        try {
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            List<Map> gateWayDeviceInfo = deviceMapper.getBindingGateWayByUser(userId);
            
            List <Map> devices = Lists.newArrayList();
            Map<String,String> gateWayInfo = Maps.newHashMap(); 
            String key="";
            String mac="";
            String sn="";
            if(gateWayDeviceInfo!=null && gateWayDeviceInfo.size()>0){
	            for(int i = 0,len=gateWayDeviceInfo.size();i<len;i++) {
	            	Map<String,String> gateWay = gateWayDeviceInfo.get(i);
	                if(i==0){
	                	mac=gateWay.get("mac").toString();
	                	sn=gateWay.get("sn").toString();
	                	key=gateWay.get("mac").toString()+"_"+(gateWay.get("sn")==null?"NONE":gateWay.get("sn"));                   
	                	gateWayInfo.put("gwId", gateWay.get("gwId"));
	                	gateWayInfo.put("mac", gateWay.get("mac"));
	                	gateWayInfo.put("sn",gateWay.get("sn"));
	                	gateWayInfo.put("userId",gateWay.get("userId"));
	                	gateWayInfo.put("status",gateWay.get("status"));
	                	gateWayInfo.put("disable", gateWay.get("disable"));
	                	gateWayInfo.put("eleArea", gateWay.get("eleArea"));
	                	gateWayInfo.put("bizArea", gateWay.get("bizArea"));                                      
	                }              
	                Map deviceMap = Maps.newHashMap();
	                deviceMap.put("deviceId", gateWay.get("deviceId"));
	                deviceMap.put("deviceMac",gateWay.get("deviceMac"));
	                deviceMap.put("deviceType",gateWay.get("deviceType"));
	                deviceMap.put("coolerStart",gateWay.get("coolerStart"));
	                deviceMap.put("coolerEnd",gateWay.get("coolerEnd"));
	                deviceMap.put("heaterStart",gateWay.get("heaterStart"));
	                deviceMap.put("heaterEnd",gateWay.get("heaterEnd"));
	                deviceMap.put("sensorEnabled",gateWay.get("sensorEnabled"));
	                deviceMap.put("mode",gateWay.get("mode"));
	                deviceMap.put("type",gateWay.get("deviceType"));
	                devices.add(deviceMap);
	            }
	            Map deviceMap=Maps.newHashMap();
	            deviceMap.put("devices",devices);
	            gateWayInfo.put("devices", new JSONObject(deviceMap).toString());  
	            //Device d=getGatewayByMacAndSN(mac,sn);
	            RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();
	            redisContext.updateBaseGW(key,gateWayInfo);
	            redisContext.destroy();
            }else{
            	List<Device> devs=deviceMapper.getDeviceByUserId(userId);
            	if(devs.size()>0){
            		Device d=devs.get(0);
            		key=(d.getMac()==null?"NONE":d.getMac())+"_"+(d.getSn()==null?"NONE":d.getSn());                   
            		Jedis jedis=RedisContextFactory.getInstance().getJedis();
            		jedis.del(RedisContext.BASE_INFO_PREFIX+RedisContext.BASE_INFO_GATEWAY+key);
            		RedisContextFactory.getInstance().returnJedis(jedis);
            	}
            }
        } catch (Exception e) {
            LOGGER.info("CLEAR DEVICE SESSIONBYSERVER, ERROR", e);
        } finally {
            session.close();
        }
    }
 
    public List<String> getSessionsByArea(String areaId){
  	  SqlSession session = sqlSessionFactory.openSession();
  	  List<String> lst=new ArrayList();
        try {
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            lst=deviceMapper.getSessionsByArea(Config.getInstance().SERVER_IP,areaId);
        }catch(Exception e){
      	  e.printStackTrace();
        }finally{
      	  session.close();
        }
        return lst;
  }
    
    public void setDsmInfo(){
    	 DataSourceSessionFactory dataSourceSessionFactory = DataSourceSessionFactory.getInstance();
         SqlSessionFactory sf= dataSourceSessionFactory.getSqlSessionFactory();
         SqlSession session=sf.openSession();
         SysConfigMapper configMapper=session.getMapper(SysConfigMapper.class);
         List<Map<String,String>> lst=configMapper.getDsmTemp();
         session.close();
         if(lst!=null){
        	 Jedis jedis=RedisContextFactory.getInstance().getJedis();     		
	         for(int i=0;i<lst.size();i++){
	        	 Map m=lst.get(i);
	        	 String deviceId=(String)m.get("deviceId");
	        	 int lowTemp=(Integer)m.get("lowTemp");
	        	 int upperTemp=(Integer)m.get("upperTemp");
	        	 jedis.set(RedisContext.Device_DSM_PREFIX+deviceId,lowTemp+"-"+upperTemp);
	         }
	         RedisContextFactory.getInstance().returnJedis(jedis);
         }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    public void delDsmInfo(){
    	Jedis jedis=RedisContextFactory.getInstance().getJedis();
    	String part=RedisContext.Device_DSM_PREFIX+"*";
    	Set<String> keys=jedis.keys(part);
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            jedis.del(key);
        }  
        RedisContextFactory.getInstance().returnJedis(jedis);
    }
    
    ////////////////////////////////////////////////////////////////////////
    public void delDevicesInfoFromRedis(){
    	
    }
    ////////////////////////////////////////////////////////////////////////
    public void initializeDevicesInfo2Redis(){
    	 RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();    	 
    	 redisContext.delDeviceFromRedisServer();   //Clear all device infos form redis
    	 
    	 String key = "";
    	 try{
    		 List<AirCondition> acs = this.getAllAirConditions();
    		 Iterator<AirCondition> acit = acs.iterator();
    		    		 
    		 while(acit.hasNext()){
    			 AirCondition ac = (AirCondition)acit.next();
    			 key = RedisContext.DEVICE_CTL_PROP_AC + ac.getMac();
    			 
    			 Map<String, String> map = new HashMap<String, String>();
    			 map.put("mac", ac.getMac());
    			 map.put("onOff", String.valueOf(ac.getOnOff()));
    			 map.put("operStatus", String.valueOf(ac.getOperStatus()));
    			 map.put("acTemp", String.valueOf(ac.getAcTemp()));
    			 map.put("temp", String.valueOf(ac.getTemp()));
    			 map.put("mode", String.valueOf(ac.getMode()));
    			 map.put("speed", String.valueOf(ac.getSpeed()));
    			 map.put("upDownSwing", String.valueOf(ac.getUpDownSwing()));
    			 map.put("rcuId", String.valueOf(ac.getRcuId()));
    			 
    			 redisContext.addDevice2Redis(key, map);  			     			     			     			 
    		 }
    		    		 
    		 /////////////////////////////////////////////////
    		 List<ZigbeeOO> zgOOs = this.getAllZiebeeOO();
    		 Iterator<ZigbeeOO> ooit = zgOOs.iterator();
    		 while(ooit.hasNext()){
    			 ZigbeeOO oo = (ZigbeeOO)ooit.next();
    			 key = RedisContext.DEVICE_CTL_PROP_OO + oo.getMac();
    			     			 
    			 Map<String, String> map = new HashMap<String, String>();
    			 map.put("mac", oo.getMac());
    			 map.put("onOff", String.valueOf(oo.getOnOff()));
    			 map.put("operStatus", String.valueOf(oo.getOperStatus()));
    			 
    			 redisContext.addDevice2Redis(key, map);  		
    		 }
    	 }catch(Exception e){
    		 e.printStackTrace();
    	 } 
    	     	 
    	 RedisContextFactory.getInstance().returnRedisContext(redisContext);
    }
    
    /////////////////////////////////////////////////////////////////////////
	public void deleteUserDevice(String mac) {
		new UserDeviceWrapper().deleteUserDeviceByMac(mac);
	}
	
	public class UserDeviceWrapper{
		public void deleteUserDeviceByMac(String mac){
			SqlSession session = sqlSessionFactory.openSession();
				
			UserMapper userMapper = session.getMapper(UserMapper.class);
			
			
			List<String> ud_ids = userMapper.getTbUserDeviceIdsByMAC(mac);
			
			for(String ud_id: ud_ids){
				userMapper.deleteUdGroupByUdId(ud_id);
			}			
			userMapper.deleteUserAndDeviceBindingByDevMac(mac);			
			session.commit();
			session.close();
		}
		
		public void deleteUserDeviceById(String udId){
			SqlSession session = sqlSessionFactory.openSession();
				
			UserMapper userMapper = session.getMapper(UserMapper.class);
			userMapper.deleteUdGroupByUdId(udId);
			
			userMapper.deleteUserAndDeviceBindingByDevId(udId);
			
			session.commit();
			session.close();
		}
	}
}
