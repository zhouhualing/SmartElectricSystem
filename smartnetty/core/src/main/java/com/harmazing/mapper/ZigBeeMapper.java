package com.harmazing.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface ZigBeeMapper {
	@Insert("insert into spms_gateway_zigbee"
			+ "(gwid,channel,channelMask,txpower,updateTime)"
			+ "values("
			+ "#{gwid},#{channel},#{channelMask},#{txpower},now()"
			+ ")")
	public void insertGwZigbee(@Param("gwid")String gwid,@Param("channel")int channel,
			@Param("channelMask")int channelMask,@Param("txpower")int txpower);
	
	@Insert("insert into spms_ac_zigbee"
			+ "(deviceId,averageRxRssi,averageRxLQI,averageTxRssi,averageTxLQI,updateTime)"
			+ "values("
			+ "#{deviceId},#{rxsi},#{rxqi},#{txsi},#{txqi},now()"
			+ ")")
	public void insertAcZigbee(@Param("deviceId")String deviceId,@Param("rxsi") int rxsi,
			@Param("rxqi") int rxqi,@Param("txsi") int txsi,@Param("txqi") int txqi);
	
	@Delete("delete from  spms_gateway_zigbee where gwid=#{gwid}")
	public void delGwZigbee(@Param("gwid")String gwid);
	
	@Delete("delete from  spms_ac_zigbee where deviceId=#{deviceId}")
	public void delAcZigbee(@Param("deviceId")String deviceId);
	
	public void batchInsertGwZigbee(List lst);
	public void batchInsertAcZigbee(List lst);
	public void batchDelGwZigbee(List lst);
	public void batchDelAcZigbee(List lst);
}
