package com.harmazing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.harmazing.entity.ZigbeeOOElectricityMeter;

public interface ZigbeeOOElectricityMeterMapper {
	final static String TB_GW         = "spms_gateway";
	final static String TB_ELEC_METER = "spms_onoff_pm_plug";
	
	@Insert("INSERT INTO " + TB_ELEC_METER + " (id, sn, mac, software, hardware, model, vender, type, onOff, operStatus, tag)" +
			" VALUES(#{id}, #{sn}, #{mac}, #{software}, #{hardware}, #{model}, #{vender}, #{type}, #{onOff}, #{operStatus}, #{tag})")
	public void createElectricityMater(@Param("id") String id,
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
		
	@Select("SELECT * FROM " + TB_ELEC_METER  + " WHERE mac= #{mac}")
	public ZigbeeOOElectricityMeter getElecMaterByMac(@Param("mac") String mac);
	
	@Select("select * from " + TB_ELEC_METER + " where gw_id=(select id from " + TB_GW + " where mac=#{gw_mac})")
	public List<ZigbeeOOElectricityMeter> getElecMetersByGwMac(@Param("gw_mac") String gw_mac);
	
	@Update("UPDATE " + TB_ELEC_METER + " SET onOff=#{onOff}, operStatus=#{operStatus} WHERE mac=#{mac} ")
	public void updateElecMeterStatusByMac(@Param("mac") String mac, @Param("onOff") int onOff, @Param("operStatus") int operStatus);

	@Update("UPDATE " + TB_ELEC_METER + " SET ${field}=#{value} WHERE mac=#{mac} ")
	public void updateElecMaterIntField( @Param("mac") String mac,@Param("field") String field,@Param("value") int value);
	
	@Update("UPDATE " + TB_ELEC_METER + " SET onOff=#{onOff} WHERE mac=#{mac} ")
	public void updateElecMaterOnoffStatus( @Param("mac") String mac, @Param("onOff") int onOff);
	
	@Delete("DELETE FROM " + TB_ELEC_METER + " WHERE mac=#{mac}")
	public void deleteElecMeterByMac(@Param("mac") String mac);
	
	@Update("UPDATE " + TB_ELEC_METER + " SET gw_id=#{gw_id} WHERE id=#{dev_id}")
	public void updateGwAndDeviceBinding(@Param("gw_id") String gw_id, @Param("dev_id") String dev_id);

}
