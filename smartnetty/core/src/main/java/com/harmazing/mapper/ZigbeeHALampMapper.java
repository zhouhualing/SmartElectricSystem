package com.harmazing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.harmazing.entity.ZigbeeHALamp;

public interface ZigbeeHALampMapper {
	final static String TB_HA_LAMP = "spms_lamp";
	final static String TB_GW = "spms_gateway";
	
	@Insert("INSERT INTO " + TB_HA_LAMP + " (id, sn, mac, software, hardware, model, vender, type, onOff, operStatus, tag, illuminance, red, green, blue)"
			+ "VALUES(#{id}, #{sn}, #{mac}, #{software}, #{hardware}, #{model}, #{vender}, #{type}, #{onOff}, #{operStatus}, #{tag}, #{illuminance}, #{red}, #{green}, #{blue})")
	public void createHALamp(@Param("id") String id,
			                  @Param("sn") String sn,
			                  @Param("mac") String mac,
			                  @Param("software") String software,
			                  @Param("hardware") String hardware,
			                  @Param("model") String model,
			                  @Param("vender") String vendor,
			                  @Param("type") int type,
			                  @Param("onOff") int onOff,
			                  @Param("operStatus") int operStatus,
			                  @Param("tag") String tag,
			                  @Param("illuminance") int illuminance,
			                  @Param("red") int red,
			                  @Param("green") int green,
			                  @Param("blue") int blue);
	
	@Select("SELECT * FROM " + TB_HA_LAMP + " WHERE mac=#{mac}")
	public ZigbeeHALamp getZigbeeHALampByAcMac(@Param("mac") String mac);
	
	@Select("select * from " + TB_HA_LAMP + " where gw_id=(select id from " + TB_GW + " where mac=#{gw_mac})")
	public List<ZigbeeHALamp> getHALampsByGwMac(@Param("gw_mac") String gw_mac);
		
	@Update("UPDATE " + TB_HA_LAMP + " SET illuminance=#{illuminance} WHERE mac=#{mac} ")
	public void updateHALampIlluminance( @Param("mac") String mac, @Param("illuminance") int illuminance);
	
	@Update("UPDATE " + TB_HA_LAMP + " SET onOff=#{onOff}, operStatus=#{operStatus} WHERE mac=#{mac} ")
	public void updateHALampStatus( @Param("mac") String ac_mac, 
								    @Param("onOff") int onOff,
									@Param("operStatus") int operStatus);
	
	@Update("UPDATE " + TB_HA_LAMP + " SET ${field}=#{value} WHERE mac=#{mac} ")
	public void updateHALampIntField( @Param("mac") String mac,@Param("field") String field,@Param("value") int value);
	
	@Update("UPDATE " + TB_HA_LAMP + " SET onOff=#{onOff}, operStatus=#{operStatus}, illuminance=#{illuminance}, red=#{red}, green=#{green}, blue=#{blue} WHERE mac=#{mac} ")
	public void updateHALampAll( @Param("mac") String mac, 
								   @Param("onOff") int onOff,
								   @Param("operStatus") int operStatus,
								   @Param("illuminance") int illuminance,
			                       @Param("red") int red, 
			                       @Param("green") int green, 
			                       @Param("blue") int blue );
	
	@Update("UPDATE " + TB_HA_LAMP + " SET illuminance=#{illuminance}, red=#{red}, green=#{green}, blue=#{blue} WHERE mac=#{mac} ")
	public void updateHALampColor( @Param("mac") String mac, @Param("illuminance") int illuminance, @Param("red") int red, @Param("green") int green, @Param("blue") int blue );
	
	@Delete("DELETE FROM " + TB_HA_LAMP + " WHERE mac=#{mac}")
	public void deleteHALampByMac(@Param("mac") String mac);

}
