package com.harmazing.service;

import com.harmazing.entity.AirCondition;
import com.harmazing.entity.Device;
import com.harmazing.entity.DoorWindowSensor;
import com.harmazing.entity.Gateway;
import com.harmazing.entity.PirSensor;
import com.harmazing.entity.TemperatureHumiditySensor;
import com.harmazing.entity.ZigbeeHALamp;
import com.harmazing.entity.ZigbeeOO;
import com.harmazing.entity.ZigbeeOOElectricityMeter;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by ming on 14-9-2.
 */
public interface DeviceService {
	
	public Device getGatewayByMacAndSN(String mac,String sn);
	public void createDevice(DeviceSpecific.DeviceType dev_type, String id, String sn, String mac, String software, String hardware, String model, String vendor, int type, int onOff, int operStatus);
	public void updateDeviceIntField(DeviceSpecific.DeviceType devType, String mac, String field, int value);
	
	/// Below is for AC
	public List<AirCondition> getAcsByGwMac(String gw_mac);
	public AirCondition getAcByAcMac(String ac_mac);
	
	public void handleDeviceSpecific(Map<String, Object> params, DeviceSpecific.DeviceType dev_type);
	public String getAcIdByAcMac(String ac_mac);
	public List<AirCondition> getAllAirConditions();
	public void updateGwAndACBinding(String gw_mac, String ac_mac);
	public void deleteAcAndUserBindingById(String id);
	public void updateOpstatus(String ac_mac, int onOff, int operStatus);
	public void updateAcStatusByAcMac( String ac_mac, int onOff, int operStatus, int mode, int acTemp, int speed, int energy);
	public void updateACModsigByACMac(String mac, String mod_sig);
	public void updateAcRcuIdByAcMac(String mac, int rcui_id, int isPaired);
	public void updateAcTempAndHumidityByAcMac(String mac, int temp, int humidity);
	public void updateACCurrentStatusByACMac(String mac, int mode, int temp, int swing_v, int swing_h, int speed, int onOff, int operStatus);
		
	public void ircodeReceivedDBOperation(String gw_mac, String dev_mac, Map<String, Object> params);
	public void updateGWOprStatusByMac(String mac, int onOff, int operStatus, String ip);
	public void updateGWOprAndVersionStatusByMac(String mac, int onOff, int operStatus, String software, String hardware, String ip);
	
	public void updateACField(String mac,String field,String fieldValue);
	public void updateACIntField(String mac,String field,int fieldValue);
	
	public String getTbUserDeviceIdByUserIdAndDeviceId(String dev_id, String user_id);

	/// Below is for GW
	public int createGateway(String id, String sn, String mac, String software, String hardware, String model, String vendor, int type, int onOff, int operStatus, String ipAddr);
	public String getGWIdByGwMac(String gw_mac); 
	public Gateway getGWByGwId(String id);
	public Gateway getGWByGWMac(String gw_mac);
	public List<Gateway> getAllGateways();
	
	/// bElow is for ZigebeeOO
	//public ZigbeeOO createZigbeeOO(String id, String sn, String mac, String software, String hardware, String model, String vendor, int type, int onOff, int operStatus);
	public ZigbeeOO getZiebeeOOByMac(String mac);
	public List<ZigbeeOO> getOOsByGwMac(String gw_mac);
	public List<ZigbeeOO> getAllZiebeeOO();
	public void updateZigbeeOOStatusByMac( String ac_mac, int onOff, int operStatus);
	public void updateOODevIntField(String mac, String field, int value);
	
	// Below is for DoorWindowsSensor
	public DoorWindowSensor getWinDoorSensorByMac(String mac);
	public void updateWinDoorStatusByMac(String mac, int onOff, int operStatus);
	public void updateWinDoorFieldByMac(String mac, String field, int value);
	public List<DoorWindowSensor> getWinDoorSensorsByGwMac(String gw_mac);
	
	// Below is for PirSensor
	public PirSensor getPirSensorByMac(String mac);
	public void updatePirtatusByMac(String mac, int onOff, int operStatus);
	public void updatePirIntField( String mac,  String field, int value);
	public List<PirSensor> getPirSensorsByGwMac(String gw_mac);
	
	// Below is for TemperatureHumiditySensorOnline
	public TemperatureHumiditySensor getTempAndHumiditySensorByMac(String mac);
	public void updateTempAndHumidityByMac(String mac, int temp, int humidity);
	public void updateTHIntField(String mac, String field, int value);
	public List<TemperatureHumiditySensor> getTempHumiditySensorsByGwMac(String gw_mac);
	public void recordHTStatus(String mac, int temp, int humidity);
	
	// Below is for Temperature Himidity SensorOnLine
	public void createElectricityMeter(String id, String sn, String mac, String software, String hardware, String model, String vendor,
            int type, int onOff, int operStatus);
	public ZigbeeOOElectricityMeter getElecMaterByMac(String mac);
	public List<ZigbeeOOElectricityMeter> getElecMetersByGwMac(String gw_mac);
	public void updateElecMeterStatusByMac(String mac, int onOff, int operStatus);
	public void updateElecMaterIntField( String mac, String field, int value);
	public void deleteElecMeterByMac(String mac);
	
	// Below is for HALamp
	public void createHALamp(String id, String sn, String mac, String software, String hardware, String model, String vendor, int type, 
			int onOff, int operStatus, int illuminance,  int red, int green, int blue);
	public ZigbeeHALamp getZigbeeHALampByAcMac(String mac);
	public List<ZigbeeHALamp> getHALampsByGwMac(String gw_mac);
	public void updateHALampIlluminance(String mac,  int illuminance);
	public void updateHALampColor(String mac, int illuminance, int red, int green, int blue );
	public void updateHALampStatus( String ac_mac, int onOff, int operStatus);
	public void updateHALampIntField(String mac, String field, int value);
	public void updateHALampAll( String mac, int onOff, int operStatus, int illuminance, int red, int green, int blue );
	public void deleteHALampByMac(String mac);
	
	public void updateDeviceVersion(DeviceSpecific.DeviceType dev_type, String mac, String sw_ver, String hd_ver);
	
	
	////////////////// 
	public void recordWinDoorHistroyStatus( String user_id, String mac, String field, int status);
    
	
	/** 根据Mac地址和序列号获取设备
     * @param mac   设备地址
     * @param sn    设备序列号
     * @param type  设备类型
     * @return
     */
	
    public Device getDeviceByMacAndSN(String mac, String sn, String type);

    /**
     * 根据设备id获取设备
     * @param id    设备id
     * @return
     */
    public Device getDeviceById(String id);

    /**
     * 批量更新所有设备信息
     * @param devices
     */
    public void updateDevices(List<Map<String,String>> devices);
    
    /**
     * 更新设备
     * @param device    设备
     */
    public void updateDevice(Device device);

    /**
     * 根据父设备id获取设备列表
     * @param parentId  父设备id
     * @return
     */
    public List<Device> getDeviceByParentId(String parentId);

    /**
     * 根据父设备id和设备类型获取设备列表
     * @param parentId
     * @param deviceType
     * @return
     */
    public List<Device> getDeviceByParentIdAndDeviceType(String parentId, String deviceType);

    public void setGWInfoToRedis();
    /**
     * 更新用户的网关信息到redis
     * @param userId
     */
    public void updateGWInfoToRedis(String userId);
   
    /**
     * 删除缓存的网关信息
     */
    public void delGWInfo();
    /**
     * 设置dsm信息到redis
     */
    public void setDsmInfo();
    /**
     * 删除redis中的dsm信息
     */
    public void delDsmInfo();
    
    /**
     * 
     */
    public void initializeDevicesInfo2Redis();
    
    /**
     * 
     */
    public void deleteUserDevice(String mac);
}
