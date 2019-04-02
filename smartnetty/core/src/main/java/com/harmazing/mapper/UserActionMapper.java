package com.harmazing.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface UserActionMapper {
	final static String TB_USER_ACTION = "spms_useraction";
	
	////////////////////////////////////////////////////////////////
	@Insert("INSERT " + TB_USER_ACTION+ " (id, mac, status) VALUES(#{id}, #{mac}, #{status}")
	public void record4Sensor( @Param("table") String table,
						       @Param("mac") String mac,
						       @Param("status") int status);

	//////////////////////////////////////////////////////////////
	@Insert("INSERT " + TB_USER_ACTION+ " (id, mac, status, on_off, mode, temp, speed) "
			+ " VALUES(#{id}, #{mac}, #{status}, #{on_off}, #{mode}, #{temp}, #{speed}")
	public void record4Ac( @Param("table") String table,
						   @Param("mac") String mac,
						   @Param("status") int status,
						   @Param("on_off") int on_off,
						   @Param("mode") int mode,
						   @Param("temp") int temp,
						   @Param("speed") int speed);
	
	//////////////////////////////////////////////////////////////
	@Insert("INSERT " + TB_USER_ACTION+ " (id, mac, status, on_off, illuminance, red, green, blue) "
			+ " VALUES(#{id}, #{mac}, #{status}, #{on_off}, #{illuminance}, #{red}, #{green}, #{blue} ")
	public void record4Lamp( @Param("table") String table,
						   @Param("mac") String mac,
						   @Param("status") int status,
						   @Param("on_off") int on_off,
						   @Param("illuminance") int illuminance,
						   @Param("red") int red,
						   @Param("green") int green,
						   @Param("blue") int blue);

	//////////////////////////////////////////////////////////////
	@Insert("INSERT " + TB_USER_ACTION+ " (id, mac, status, temp, humidity) "
			+ " VALUES(#{id}, #{mac}, #{status}, #{temp}, #{humidity}")
	public void record4HTSensor( @Param("table") String table,
						   @Param("mac") String mac,
						   @Param("status") int status,
						   @Param("temp") int temp,
						   @Param("humidity") int humidity);
}
