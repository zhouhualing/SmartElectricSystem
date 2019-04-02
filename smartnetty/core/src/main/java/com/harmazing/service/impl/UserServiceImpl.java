package com.harmazing.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.entity.Device;
import com.harmazing.mapper.DeviceMapper;
import com.harmazing.mapper.UserMapper;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import com.harmazing.service.UserService;
import com.harmazing.util.UUIDGenerator;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by ming on 14-9-15.
 */
public class UserServiceImpl extends ServiceImpl implements UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    ///////////////////////////////////////////////////////////
    @Override
    public String getTbUserUserIdByUserCode(String user_code){
    	 SqlSession sqlSession = sqlSessionFactory.openSession();
         String id = "";
         try {
             UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
             id = userMapper.getTbUserUserIdByUserCode(user_code);
         } catch (Exception e) {
             LOGGER.info("GETUSERIDBYELEAREAID ERROR", e);
         } finally {
             sqlSession.close();            
         }
         return id;
    }
    
    //////////////////////////////////////////////////////////
    @Override
    public List<String> getUserIdByEleAreaId(String id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<String> list = null;
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            list = userMapper.selectUserIdByEleAreaId(id);
        } catch (Exception e) {
            LOGGER.info("GETUSERIDBYELEAREAID ERROR", e);
        } finally {
            sqlSession.close();
            return list;
        }
    }

    @Override
    public List<String> getUserIdByBizAreaId(String id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<String> list = null;
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            list = userMapper.selectUserIdByBizAreaId(id);
        } catch (Exception e) {
            LOGGER.info("GETUSERIDBYELEAREAID ERROR", e);
        } finally {
            sqlSession.close();
            return list;
        }
    }

    @Override
    public String getUserIdByGatewayId(String gatewayId) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        String userId = null;
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userId = userMapper.selectUserIdByGatewayId(gatewayId);
        } catch (Exception e) {
            LOGGER.info("GETUSERIDBYGATEWAYID ERROR", e);
        } finally {
            sqlSession.close();
            return userId;
        }
    }

	@Override
	public String findEleAreaById(String userId) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
        String eleArea = null;
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            eleArea = userMapper.findEleAreaById(userId);
        } catch (Exception e) {
            LOGGER.info("findEleArea ERROR", e);
        } finally {
            sqlSession.close();
            return eleArea;
        }
	}
	
	 public Device getUserGW(String mac,String sn){
		 /***************************
		 System.out.println("*****************serviceupdate device to redis**************************");
	        SqlSession session = sqlSessionFactory.openSession();
	        try {
	            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
	            List<Map> gateWayDeviceInfo = deviceMapper.getBindingGateWayByUser(userId);
	            RedisContext redisContext = RedisContextFactory.getInstance().getRedisContext();
	            List <Map> devices = Lists.newArrayList();
	            Map<String,String> gateWayInfo = Maps.newHashMap(); 
	            String key="";
	            String mac="";
	            String sn="";
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
	            redisContext.updateBaseGW(key,gateWayInfo);
	            redisContext.destroy();
	        } catch (Exception e) {
	            LOGGER.info("CLEAR DEVICE SESSIONBYSERVER, ERROR", e);
	        } finally {
	            session.close();
	        }
		 
		***************************************/
		 SqlSession sqlSession = sqlSessionFactory.openSession();
	     Device device=new Device();
	        try {
	            DeviceMapper dm = sqlSession.getMapper(DeviceMapper.class);	            
	            List<Map> gatewayList=dm.getBindingGateWayByMacAndSn(mac, sn);		           
	            List<Map> bindDevices = Lists.newArrayList(); 	            
	            if(gatewayList.size() > 0 && gatewayList.get(0).get("userId") != null){
	                device.setId((String) gatewayList.get(0).get("gwId"));
	                device.setMac((String) gatewayList.get(0).get("mac"));
	                device.setSn((String) gatewayList.get(0).get("sn"));
	                device.setUserId((String) gatewayList.get(0).get("userId"));
	                device.setDisable(Integer.valueOf(gatewayList.get(0).get("disable").toString()));
	                device.setEleArea((String) gatewayList.get(0).get("eleArea"));
	                for(Map gateway : gatewayList){
	                    Map map = Maps.newHashMap();
	                    map.put("id",gateway.get("deviceId"));
	                    map.put("mac",gateway.get("deviceMac"));
	                    map.put("type",gateway.get("deviceType"));
	                    map.put("coolerStart",gateway.get("coolerStart"));
	                    map.put("coolerEnd",gateway.get("coolerEnd"));
	                    map.put("heaterStart",gateway.get("heaterStart"));
	                    map.put("heaterEnd",gateway.get("heaterEnd"));
	                    map.put("sensorEnabled",gateway.get("sensorEnabled"));	                 
	                    map.put("mode",gateway.get("mode"));
	                    map.put("eleArea",gateway.get("eleArea"));
	                    bindDevices.add(map);
	                }
	                device.setAirconServiceList(bindDevices);
	              }
	        } catch (Exception e) {
	            LOGGER.info("findEleArea ERROR", e);
	        } finally {
	            sqlSession.close();
	            return device;
	        }
	 }
	 
	 public String findSpmsUserIdByUserCode(String userCode){
		 SqlSession sqlSession = sqlSessionFactory.openSession();
	        String userId = null;
	        try {
	            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
	            userId = userMapper.findSpmsUserIdByUserCode( userCode);
	        } catch (Exception e) {
	            LOGGER.info("findSpmsUserIdByUserCode ERROR", e);
	        } finally {
	            sqlSession.close();
	        }
	        
	        return userId;
	 }
	 
	 public void insertUserAndDeviceBinding(String device_id,  int device_type, int is_primary, String user_id, int version ){
		 SqlSession sqlSession = sqlSessionFactory.openSession();
	        String userId = null;
	        try {
	            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
	            String id = UUIDGenerator.randomUUID();
	            userMapper.insertUserAndDeviceBinding(id, device_id, device_type, is_primary, user_id, version);
	        } catch (Exception e) {
	            LOGGER.info("findSpmsUserIdByUserCode ERROR", e);
	        } finally {
	        	sqlSession.commit();
	            sqlSession.close();
	        }
	        
	 }

}
