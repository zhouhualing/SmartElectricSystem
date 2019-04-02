package com.harmazing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.entity.TemperatureHumiditySensor;

public interface TemperatureHumiditySensorMapper {
	final static String TB_GW         = "spms_gateway";
	final static String TB_HT_SENSOR  = "spms_ht_sensor";
	final static String TB_HT_SENSOR_STATUS  = "spms_ht_sensor_status";
	
	@Insert("INSERT INTO " + TB_HT_SENSOR + " (id, sn, mac, software, hardware, model, vender, type, onOff, operStatus, tag)" +
			" VALUES(#{id}, #{sn}, #{mac}, #{software}, #{hardware}, #{model}, #{vender}, #{type}, #{onOff}, #{operStatus}, #{tag})")
	public void createWinDoorSensor(@Param("id") String id,
			                  @Param("sn") String sn,
			                  @Param("mac") String mac,
			                  @Param("software") String software,
			                  @Param("hardware") String hardware,
			                  @Param("model") String model,
			                  @Param("vender") String vendor,
			                  @Param("type") int type,
			                  @Param("onOff") int onOff,
			                  @Param("operStatus") int operStatus,
			                  @Param("tag") String tag);
	
	
	@Select("SELECT * FROM " + TB_HT_SENSOR  + " WHERE mac= #{mac}")
	public TemperatureHumiditySensor getTempAndHumiditySensorByMac(@Param("mac") String mac);
		
	@Update("UPDATE " + TB_HT_SENSOR + " SET temp=#{temp}, humidity=#{humidity} WHERE mac=#{mac}")
	public void updateTempAndHumidityByMac(@Param("mac") String mac, @Param("temp") int temp, @Param("humidity") int humidity);

	@Update("UPDATE " + TB_HT_SENSOR + " SET ${field}=#{value} WHERE mac=#{mac} ")
    public void updateTHIntField( @Param("mac") String mac,@Param("field") String field,@Param("value") int value);
	
	@Select("SELECT * FROM " + TB_HT_SENSOR + " WHERE gw_id=(select id from " + TB_GW + " WHERE mac=#{gw_mac})")
	public List<TemperatureHumiditySensor> getTempHumiditySensorsByGwMac(@Param("gw_mac") String gw_mac);
	
	@Delete("DELETE FROM " + TB_HT_SENSOR + " WHERE mac=#{mac}")
	public void deleteTempAndHumidtySensorByMac(@Param("mac") String mac);
	
	@Insert("INSERT INTO " + TB_HT_SENSOR_STATUS + " (id, mac, temp, humidity)" +
			" VALUES(#{id}, #{mac}, #{temp}, #{humidity})")
	public void recordHTSensorStatus(@Param("id") String id,
			                  @Param("mac") String mac,
			                  @Param("temp") int temp,
			                  @Param("humidity") int humidity);
	
	@Update("UPDATE " + TB_HT_SENSOR + " SET gw_id=#{gw_id} WHERE id=#{dev_id}")
	public void updateGwAndDeviceBinding(@Param("gw_id") String gw_id, @Param("dev_id") String dev_id);
}
