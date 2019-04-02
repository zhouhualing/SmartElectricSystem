package com.harmazing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.entity.PirSensor;
import com.harmazing.entity.ZigbeeOO;

public interface PirSensorMapper {
	final static String TB_GW   = "spms_gateway";
	final static String TB_PIR  = "spms_pir";
	final static String TB_WIN_DOOR = "spms_win_door";
	
	@Insert("INSERT INTO " + TB_PIR + " (id, sn, mac, software, hardware, model, vender, type, onOff, operStatus, tag)" +
			" VALUES(#{id}, #{sn}, #{mac}, #{software}, #{hardware}, #{model}, #{vender}, #{type}, #{onOff}, #{operStatus}, #{tag})")
	public void createPirSensor(@Param("id") String id,
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
	
	
	@Select("SELECT * FROM " + TB_PIR  + " WHERE mac= #{mac}")
	public PirSensor getPirSensorByMac(@Param("mac") String mac);
		
	@Update("UPDATE " + TB_WIN_DOOR + " SET onOff=#{onOff}, operStatus=#{operStatus} WHERE mac=#{mac} ")
	public void updatePirtatusByMac(@Param("mac") String mac, @Param("onOff") int onOff, @Param("operStatus") int operStatus);
	
	@Update("UPDATE " + TB_WIN_DOOR + " SET ${field}=#{value} WHERE mac=#{mac} ")
	public void updatePirIntField( @Param("mac") String mac,@Param("field") String field,@Param("value") int value);

	@Select("select * from " + TB_PIR + " where gw_id=(select id from " + TB_GW + " where mac=#{gw_mac})")
	public List<PirSensor> getPirSensorsByGwMac(@Param("gw_mac") String gw_mac);
	
	@Delete("DELETE FROM " + TB_PIR + " WHERE mac=#{mac}")
	public void deletePirByMac(@Param("mac") String mac);
	
	@Update("UPDATE " + TB_PIR + " SET gw_id=#{gw_id} WHERE id=#{dev_id}")
	public void updateGwAndDeviceBinding(@Param("gw_id") String gw_id, @Param("dev_id") String dev_id);
}
