package com.harmazing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.harmazing.entity.Gateway;


public interface GatewayMapper {
	final static String TB_GW       = "spms_gateway";
	
	//@SelectProvider(type=DeviceSqlProvider.class, method="getGWIdByGwMac")
	@Select("SELECT id FROM " + TB_GW + " WHERE mac= #{gw_mac}")
	public String getGWIdByGwMac(@Param("gw_mac") String gw_mac);
	
	@Select("SELECT * FROM " + TB_GW  + " WHERE mac= #{gw_mac}")
	public Gateway getGWByGwMac(@Param("gw_mac") String gw_mac);
	
	@Select("SELECT * FROM " + TB_GW  + " WHERE id= #{id}")
	public Gateway getGWByGwId(@Param("id") String id);
	
	@Select("SELECT * FROM " + TB_GW)
	@ResultMap("GwResultMap")
	public List<Gateway> getAllGateways();
	
	@Select("select id from spms_gateway d where d.onOff!=1 AND status=2")
    public List<String> getGwState();
	
	@Select("SELECT id FROM " + TB_GW + " WHERE mac= #{mac}")	
    public String getIdByMac(@Param("mac") String mac);
	
	@Insert("INSERT INTO " + TB_GW + " (id, sn, mac, software, hardware, model, vender, type, onOff, operStatus, ip) VALUES(#{id}, #{sn}, #{mac}, #{software}, #{hardware}, #{model}, #{vender}, #{type}, #{onOff}, #{operStatus}, #{ip})")
	public void createGateway(@Param("id") String id,
			                  @Param("sn") String sn,
			                  @Param("mac") String mac,
			                  @Param("software") String software,
			                  @Param("hardware") String hardware,
			                  @Param("model") String model,
			                  @Param("vender") String vendor,
			                  @Param("type") int type,
			                  @Param("onOff") int onOff,
			                  @Param("operStatus") int operStatus,
			                  @Param("ip") String ip);
	
	@Update("UPDATE " + TB_GW + " SET onOff=#{onOff}, operStatus=#{operStatus}, ip=#{ip} WHERE mac=#{mac} ")
	public void updateGWOprStatusByMac(@Param("mac") String mac, @Param("onOff") int onOff, @Param("operStatus") int operStatus, @Param("ip") String ip);
	
	@Update("UPDATE " + TB_GW + " SET onOff=#{onOff}, operStatus=#{operStatus}, software=#{software}, hardware=#{hardware}, ip=#{ip} WHERE mac=#{mac} ")
	public void updateGWOprAndVersionStatusByMac(@Param("mac") String mac, 
			                                     @Param("onOff") int onOff, 
			                                     @Param("operStatus") int operStatus,
			                                     @Param("software") String software,
			                                     @Param("hardware") String hardware,
			                                     @Param("ip") String ip);

}
