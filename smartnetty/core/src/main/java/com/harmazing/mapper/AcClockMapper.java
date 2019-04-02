package com.harmazing.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AcClockMapper {
	@Select("select d.mac, clocking,c.mode,on_off,c.temp,windspeed,friday,monday,saturday,startend,sunday,thursday,tuesday "+
			"from spms_user_product_binding b,rairconcurve_clocksetting c ,spms_device d "+
			"where  b.user_id=#{userId} and b.device_id=d.id and  c.spmsDevice_id=b.device_id and c.alone=1 and c.startend=1 "+
			"UNION ALL "+
			"select d.mac,clocking,c.mode,on_off,c.temp,windspeed,r.friday,r.monday,r.saturday,r.startend,r.sunday,r.thursday,r.tuesday "+ 
			"from spms_user_product_binding b,spms_raircon_setting r,rairconcurve_clocksetting c,spms_device d "+
			"where b.user_id=#{userId} and b.device_id=d.id and  b.device_id=r.spmsDevice_id and r.startend=1  "+
			"and  r.id=c.raircon_Setting_id and c.alone<>1 ")
	public List<Map> getClockSettingByUser(@Param("userId") String userId);
	/***********
	@Select("select d.mac, clocking,c.mode,on_off,c.temp,windspeed,friday,monday,saturday,startend,sunday,thursday,tuesday "+
			"from rairconcurve_clocksetting c ,spms_device d "+
			"where c.spmsDevice_id=#{deviceId} and c.spmsDevice_id=d.id  and c.alone=1 and c.startend=1 "+
			"UNION ALL "+
			"select d.mac,clocking,c.mode,on_off,c.temp,windspeed,r.friday,r.monday,r.saturday,r.startend,r.sunday,r.thursday,r.tuesday "+
			"from spms_raircon_setting r,rairconcurve_clocksetting c,spms_device d "+
			"where r.spmsDevice_id=#{deviceId} and r.spmsDevice_id=d.id and r.startend=1  and  r.id=c.raircon_Setting_id and c.alone<>1")
	*****************/
	@Select("select d.mac, clocking,c.mode,on_off,c.temp,windspeed,"
			+ " friday,monday,saturday,startend,sunday,thursday,tuesday "
			+ "	from rairconcurve_clocksetting c ,spms_device d "
			+ "	where c.spmsDevice_id=#{deviceId} and c.spmsDevice_id=d.id  and c.alone=1 and c.startend=1 "
			+ "	UNION ALL "
			+ "	select d.mac,clocking,c.mode,c.on_off,c.temp,c.windspeed,r.startend,"
			+ "cu.friday,cu.monday,cu.saturday,cu.sunday,cu.thursday,cu.tuesday "
			+ "	from  spms_device_curve cu,spms_raircon_setting r,rairconcurve_clocksetting c,spms_device d"
			+ "	where cu.spmsDevice_id=#{deviceId} and cu.spmsDevice_id=d.id and r.startend=1  and  "
			+ " r.id=c.raircon_Setting_id and ( c.alone is null or c.alone<>1 ) and cu.curve_id=r.id")
	public List<Map> getClockSettingByDevice(@Param("deviceId") String deviceId);
	
	@Select("select * "
			+ "from "
			+ "spms_Raircon_Setting r "
			+ "left join RairconCurve_Clocksetting c "
			+ "on r.spmsDevice_id=c.spmsDevice_id "
			+ "left join spms_user u"
			+ "on r.sys_user_id=u.id "
			+ "where u.gw_id=#{gateWayId}")
	public List<Map> getClockSettingByGateWay(String gateWayId);
}
