package com.harmazing.spms.helper;

import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Collection;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.dao.SpmsAirConditionDAO;
import com.harmazing.spms.device.dao.SpmsCentralAcDAO;
import com.harmazing.spms.device.dao.SpmsGatewayDAO;
import com.harmazing.spms.device.dao.SpmsHtSensorDAO;
import com.harmazing.spms.device.dao.SpmsLampDAO;
import com.harmazing.spms.device.dao.SpmsOnOffLightDAO;
import com.harmazing.spms.device.dao.SpmsOnOffPlugDAO;
import com.harmazing.spms.device.dao.SpmsOnOffPmPlugDAO;
import com.harmazing.spms.device.dao.SpmsPirDAO;
import com.harmazing.spms.device.dao.SpmsPlugAcDAO;
import com.harmazing.spms.device.dao.SpmsWinDoorDAO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.device.entity.SpmsCentralAc;
import com.harmazing.spms.device.entity.SpmsDeviceBase;
import com.harmazing.spms.device.entity.SpmsGateway;
import com.harmazing.spms.device.entity.SpmsHtSensor;
import com.harmazing.spms.device.entity.SpmsLamp;
import com.harmazing.spms.device.entity.SpmsOnOffLight;
import com.harmazing.spms.device.entity.SpmsOnOffPlug;
import com.harmazing.spms.device.entity.SpmsOnOffPmPlug;
import com.harmazing.spms.device.entity.SpmsPir;
import com.harmazing.spms.device.entity.SpmsPlugAc;
import com.harmazing.spms.device.entity.SpmsWinDoor;
import com.harmazing.spms.base.util.SpringUtil;

public class DeviceDAOAccessor {
	
	private Map< Integer, DeviceDAOData > daoMap = Maps.newHashMap();
	public Map<Integer, DeviceDAOData> getDaoMap() {
		return daoMap;
	}

	private List<DeviceDAOData> lists = Lists.newArrayList();
	
	private  static  DeviceDAOAccessor instance = new DeviceDAOAccessor();
	private static boolean initialized = false;

	public static DeviceDAOAccessor getInstance() {
		if(!initialized){
			instance.initialize();
			initialized = true;			
		}
		return instance;
	}
	
	public void initialize(){
		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsGatewayDAO"),SpmsGatewayDAO.class,SpmsGateway.class,1));
		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsAirConditionDAO"),SpmsAirConditionDAO.class,SpmsAirCondition.class,2));
		
//		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsPlugAcDAO"),SpmsPlugAcDAO.class,SpmsPlugAc.class,3));
//		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsCentralAcDAO"),SpmsCentralAcDAO.class,SpmsCentralAc.class,4));
		//TODO delete
		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsAirConditionDAO"),SpmsAirConditionDAO.class,SpmsAirCondition.class,3));
		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsAirConditionDAO"),SpmsAirConditionDAO.class,SpmsAirCondition.class,4));
		
		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsOnOffPlugDAO"),SpmsOnOffPlugDAO.class,SpmsOnOffPlug.class,5));
		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsOnOffPmPlugDAO"),SpmsOnOffPmPlugDAO.class,SpmsOnOffPmPlug.class,6));
		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsWinDoorDAO"),SpmsWinDoorDAO.class,SpmsWinDoor.class,7));
		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsPirDAO"),SpmsPirDAO.class,SpmsPir.class,8));
		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsHtSensorDAO"),SpmsHtSensorDAO.class,SpmsHtSensor.class,9));
		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsOnOffLightDAO"),SpmsOnOffLightDAO.class,SpmsOnOffLight.class,10));
		lists.add(new DeviceDAOData(SpringUtil.getBeanByName("spmsLampDAO"),SpmsLampDAO.class,SpmsLamp.class,11));
		
		
		for(DeviceDAOData data : lists){
			daoMap.put(data.getType(),data);
		}
	}
	
	public SpringDataDAO<? extends SpmsDeviceBase> getDAO(Integer type) {
		if(type > daoMap.size() || type < 0)
			return null;
		return daoMap.get(type).getDao();
	}	
	
	public SpringDataDAO<? extends SpmsDeviceBase> getDAO(String type) {

		int t = -1;
		try {
			t = Integer.parseInt(type);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return getDAO(t);
	}
	
	public Class getClz(Integer type) {
		if(type > daoMap.size() || type < 0)
			return null;
		return daoMap.get(type).getDaoClass();
	}
	
	public String getClsName(Integer type) {
		if(type > daoMap.size() || type < 0)
			return "";
		return getClz(type).getName();
	}
	
	public String getDevName(Integer type) {
		if(type > daoMap.size() || type < 0)
			return null;
		return daoMap.get(type).getDevClass().getName();
	}
	
	public Class getDevClz(Integer type) {
		if(type > daoMap.size() || type < 0)
			return null;
		return daoMap.get(type).getDevClass();
	}
	
	public List<DeviceDAOData> getAllDAO() {
		return lists;
	}

}
