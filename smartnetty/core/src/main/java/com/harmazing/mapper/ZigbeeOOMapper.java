package com.harmazing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.entity.AirCondition;
import com.harmazing.entity.ZigbeeOO;

public interface ZigbeeOOMapper {
	final static String TB_ONOFF_PLUG = "spms_onoff_plug";
	final static String TB_GW = "spms_gateway";
	
	@Insert("INSERT INTO " + TB_ONOFF_PLUG + " (id, sn, mac, software, hardware, model, vender, type, onOff, operStatus, tag) "
			+ "VALUES(#{id}, #{sn}, #{mac}, #{software}, #{hardware}, #{model}, #{vender}, #{type}, #{onOff}, #{operStatus}, #{tag})")
	public void createZigbeeOO(@Param("id") String id,
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
	
	@Select("SELECT * FROM " + TB_ONOFF_PLUG + " WHERE mac=#{ac_mac}")
	public ZigbeeOO getZiebeeOOByAcMac(@Param("ac_mac") String ac_mac);
	
	@Select("select * from " + TB_ONOFF_PLUG + " where gw_id=(select id from " + TB_GW + " where mac=#{gw_mac})")
	public List<ZigbeeOO> getAcsByGwMac(@Param("gw_mac") String gw_mac);
	
	@Select("SELECT * FROM " + TB_ONOFF_PLUG)
	public List<ZigbeeOO> getAllZiebeeOO();
	
	@Update("UPDATE " + TB_ONOFF_PLUG + " SET onOff=#{onOff}, operStatus=#{operStatus} WHERE mac=#{mac} ")
	public void updateZigbeeOOStatusByMac( @Param("mac") String ac_mac, 
									   @Param("onOff") int onOff,
									   @Param("operStatus") int operStatus);
	
	@Update("UPDATE " + TB_ONOFF_PLUG + " SET gw_id=#{gw_id} WHERE id=#{dev_id}")
	public void updateGwAndDeviceBinding(@Param("gw_id") String gw_id, @Param("dev_id") String dev_id);
	
	@Update("UPDATE " + TB_ONOFF_PLUG + " SET ${field}=#{value} WHERE mac=#{mac} ")
	public void updateOODevIntField( @Param("mac") String mac,@Param("field") String field,@Param("value") int value);
	
	@Delete("DELETE FROM " + TB_ONOFF_PLUG + " WHERE mac=#{mac}")
	public void deleteZigbeeOODeviceByMac(@Param("mac") String mac);

}
