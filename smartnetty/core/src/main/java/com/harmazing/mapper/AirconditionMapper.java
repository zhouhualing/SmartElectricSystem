package com.harmazing.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.harmazing.entity.AirCondition;

public interface AirconditionMapper {
	final static String TB_AC       = "spms_ac";
	final static String TB_GW       = "spms_gateway";
	
	@Insert("INSERT INTO " + TB_AC + " (id, sn, mac, software, hardware, model, vender, type, onOff, operStatus, tag) "
			+ "VALUES(#{id}, #{sn}, #{mac}, #{software}, #{hardware}, #{model}, #{vender}, #{type}, #{onOff}, #{operStatus}, #{tag})")
	public void createAC(@Param("id") String id,
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
	
	@Select("SELECT * FROM " + TB_AC + " WHERE gw_id= #{gw_id}")	
    public List<AirCondition> getAcsByGwId(@Param("gw_id") String gw_id);
	
	@Select("SELECT * FROM " + TB_AC )	
    public List<AirCondition> getAllAirConditions();
	
	@Select("SELECT id FROM " + TB_AC + " WHERE mac=#{ac_mac}")	
    public String getAcIdByAcMac(@Param("ac_mac") String ac_mac);
		
	@Select("SELECT * FROM " + TB_AC + " WHERE mac=#{ac_mac}")
	public AirCondition getAcByAcMac(@Param("ac_mac") String ac_mac);
	
	@Select("select * from " + TB_AC + " where gw_id=(select id from " + TB_GW + " where mac=#{gw_mac})")
	public List<AirCondition> getAcsByGwMac(@Param("gw_mac") String gw_mac);
	
	@Update("UPDATE " + TB_AC + " SET gw_id=#{gw_id} WHERE id=#{ac_id}")
	public void updateGwAndACBinding(@Param("gw_id") String gw_id, @Param("ac_id") String ac_id);
	
	@Update("UPDATE " + TB_AC + " SET onOff=#{onOff}, operStatus=#{operStatus} WHERE mac=#{mac}")
	public void updateOpstatus(@Param("mac") String ac_mac,
			                   @Param("onOff") int onOff, 
			                   @Param("operStatus") int operStatus);
	
	@Update("UPDATE " + TB_AC + " SET onOff=#{onOff}, operStatus=#{operStatus}, mode=#{mode}, acTemp=#{acTemp}, speed=#{speed}, energy=#{energy} WHERE mac=#{mac} ")
	public void updateAcStatusByAcMac( @Param("mac") String ac_mac, 
									   @Param("onOff") int onOff,
									   @Param("operStatus") int operStatus,
									   @Param("mode") int mode,
									   @Param("acTemp") int acTemp,
									   @Param("speed") int speed,
									   @Param("energy") int energy);
	
	@Update("UPDATE " + TB_AC + " SET rcuId=#{rcuId}, isPaired=#{isPaired}  WHERE mac=#{mac} ")
	public void updateAcRcuIdByAcMac( @Param("mac") String ac_mac, 
									  @Param("rcuId") int rcuId,
									  @Param("isPaired") int isParied);
	
	@Update("UPDATE " + TB_AC + " SET modesig=#{modesig} WHERE mac=#{mac} ")
	public void updateACModsigByACMac( @Param("mac") String mac, @Param("modesig") String mod_sig);
	
	
	@Update("UPDATE " + TB_AC + " SET temp=#{temp}, humidity=#{humidity}  WHERE mac=#{mac} ")
	public void updateAcTempAndHumidityByAcMac( @Param("mac") String ac_mac, 
									  @Param("temp") int temp,
									  @Param("humidity") int humidity);

	@Update("UPDATE "  + TB_AC + " SET mac=#{mac}, mode=#{mode}, acTemp=#{temp}, upDownSwing=#{udswing}, leftRightSwing=#{lrswing}, speed=#{speed}, onOff=#{onOff}, operStatus=#{operStatus}, startTime=#{startTime} WHERE mac=#{mac}" )
	public void updateACCurrentStatusByACMac(@Param("mac") String mac, @Param("mode") int mode, @Param("temp") int temp, @Param("udswing") int udswing, @Param("lrswing") int lrswing, @Param("speed") int speed, @Param("onOff") int onOff, @Param("operStatus") int operStatus,@Param("startTime") String startTime);
	
	
    
    @Update("UPDATE " + TB_AC + " SET ${field}=#{value} WHERE mac=#{mac} ")
    public void updateACField( @Param("mac") String mac,@Param("field") String field,@Param("value") String value);
    
    @Update("UPDATE " + TB_AC + " SET ${field}=#{value} WHERE mac=#{mac} ")
    public void updateACIntField( @Param("mac") String mac,@Param("field") String field,@Param("value") int value);
        
	@Select("SELECT id FROM " + TB_AC + " WHERE mac= #{mac}")	
    public String getIdByMac(@Param("mac") String mac);
	
	@Delete("DELETE FROM " + TB_AC + " WHERE mac=#{mac}")
	public void deleteACByMac(@Param("mac") String mac);
	
	@Update("UPDATE ${table} SET software=#{sw_ver}, hardware=#{hd_ver}  WHERE mac=#{mac} ")
	public void updateDevVersion( @Param("table") String table,
								  @Param("mac") String mac,
							      @Param("sw_ver") String sw_ver,
							      @Param("hd_ver") String hd_ver);
	
}
