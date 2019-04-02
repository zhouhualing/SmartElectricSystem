package com.harmazing.spms.usersRairconSetting.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.usersRairconSetting.entity.deviceCurve;

public interface deviceCurveDAO extends SpringDataDAO<deviceCurve> {
	
	@Query(" from deviceCurve where spmsDevice.id=:p0 and rairconSetting.id <> :p8 and (" +
			"(monday=:p1 and monday<>0) or " +
			"(tuesday=:p2 and tuesday<>0) or " +
			"(wednesday=:p3 and wednesday<>0) or " +
			"(thursday=:p4 and thursday<>0) or " +
			"(friday=:p5 and friday<>0) or " +
			"(saturday=:p6 and saturday<>0) or " +
			"(sunday=:p7 and sunday<>0) )" )
	public List<deviceCurve> querydeviceCurveForDeviceId(@Param("p0")String deviceId, 
			@Param("p1")int monday, 
			@Param("p2")int tuesday, 
			@Param("p3")int wednesday, 
			@Param("p4")int thursday,
			@Param("p5")int friday, 
			@Param("p6")int saturday, 
			@Param("p7")int sunday, 
			@Param("p8")String curveid);
	
	@Query(" from deviceCurve where rairconSetting.id = :p0 and spmsDevice.id = :p1")
	public List<deviceCurve> queryDeviceCurveForCurveId(@Param("p0")String curveId,@Param("p1")String deviceId);
	
	@Query(nativeQuery=true ,value=  "  select count(1) from spms_device_curve t3,spms_raircon_setting t5 where t5.id = t3.curve_id and t3.spmsDevice_id = :p1 " +
			"and ( ( t3.monday<>0) or"+
			"(t3.tuesday<>0) or "+
			"(t3.wednesday<>0) or "+
			"(t3.thursday<>0) or "+
			"(t3.friday<>0) or "+
			"(t3.saturday<>0) or "+
			"(t3.sunday<>0) ) and "+
			"( "+
			"	(select count(1) from rairconcurve_clocksetting t2 where  t2.raircon_Setting_id = t3.curve_id  and t2.temp <= :p3 and  t2.temp >= :p2 and on_off = 1 ) "+
			"		= (select count(1) from rairconcurve_clocksetting t4 where  t4.raircon_Setting_id = t3.curve_id) "+
			"	and  "+
			"	((select count(1) from rairconcurve_clocksetting t4 where  t4.raircon_Setting_id = t3.curve_id) <> 0) "+
			")")   
	public long queryByDeviceId(@Param("p1")String deviceId, @Param("p2")String minTemp, @Param("p3")String maxTemp);
	
}
