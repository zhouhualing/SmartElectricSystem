package com.harmazing.spms.usersRairconSetting.dao;

import java.util.List;

import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.usersRairconSetting.entity.Clocksetting;
import com.harmazing.spms.usersRairconSetting.entity.RairconSetting;

public interface SpmsRairconCurveSettingDAO extends SpringDataDAO<Clocksetting> {
	/*
	 * 加载用户设备空调曲线列表
	 */
	@Query("from Clocksetting where rairconSetting.id=:p1 order by clocking")
	public List<Clocksetting> getSpmsRairconCurveSettingByCurveid(@Param("p1")String curveid);
	/*
	 * 根据rairconSettingID  删除相应记录 
	 */
	@Modifying
	@Query("delete from Clocksetting where rairconSetting.id=:p1")
	public void delClock(@Param("p1")String curveid);
	/**
	 * 修改重复日期
	 * @param curveid
	 * @param monday
	 * @param tuesday
	 * @param wednesday
	 * @param thursday
	 * @param friday
	 * @param saturday
	 * @param sunday
	 */
	@Modifying
	@Query(nativeQuery=true,value="update RairconCurve_Clocksetting set" +
			" monday=:p2, " +//星期一
			" tuesday=:p3, " +//星期二
			" wednesday=:p4, " +//星期三
			" thursday=:p5, " +//星期四
			" friday=:p6, " +//星期五
			" saturday=:p7, " +//星期六
			" sunday=:p8 " +//星期日
			"where id=:p1 and alone = 0 ")
	public void updateWeek(@Param("p1")String curveid, 
						@Param("p2")String monday,//星期一
						@Param("p3")String tuesday,//星期二
						@Param("p4")String wednesday,//星期三
						@Param("p5")String thursday,//星期四
						@Param("p6")String friday,//星期五
						@Param("p7")String saturday,//星期六
						@Param("p8")String sunday
			);
	@Query("from Clocksetting where spmsDevice.id=:p1 and alone = 1 order by clocking")
	public List<Clocksetting> QueryTimingSet(@Param("p1")String deviceid);
	
	@Query("from Clocksetting where rairconSetting.id=:p1 and  alone = :p2 and startend = 1 order by clocking")
	public List<Clocksetting> queryClocksByCurveId(@Param("p1")String curveId, @Param("p2")String alone);
	
	@Query(" from Clocksetting where spmsDevice.id=:p0 and clocking = :p8 and id <> :p9 and (" +
			"(monday=:p1 and monday<>0) or " +
			"(tuesday=:p2 and tuesday<>0) or " +
			"(wednesday=:p3 and wednesday<>0) or " +
			"(thursday=:p4 and thursday<>0) or " +
			"(friday=:p5 and friday<>0) or " +
			"(saturday=:p6 and saturday<>0) or " +
			"(sunday=:p7 and sunday<>0) )" )
	public List<Clocksetting> queryCurveRepeat(@Param("p0")String deviceId,
			@Param("p1")int monday, 
			@Param("p2")int tuesday, 
			@Param("p3")int wednesday, 
			@Param("p4")int thursday,
			@Param("p5")int friday, 
			@Param("p6")int saturday,
			@Param("p7")int sunday, 
			@Param("p8")String clocking, 
			@Param("p9")String clockId
			);
	@Query(" from Clocksetting where spmsDevice.id=:p0 and clocking = :p8 and (" +
			"(monday=:p1 and monday<>0) or " +
			"(tuesday=:p2 and tuesday<>0) or " +
			"(wednesday=:p3 and wednesday<>0) or " +
			"(thursday=:p4 and thursday<>0) or " +
			"(friday=:p5 and friday<>0) or " +
			"(saturday=:p6 and saturday<>0) or " +
			"(sunday=:p7 and sunday<>0) )" )
	public List<Clocksetting> queryCurveRepeat(@Param("p0")String deviceId,
			@Param("p1")int monday, 
			@Param("p2")int tuesday, 
			@Param("p3")int wednesday, 
			@Param("p4")int thursday,
			@Param("p5")int friday, 
			@Param("p6")int saturday,
			@Param("p7")int sunday, 
			@Param("p8")String clocking
			);
	@Query("select count(*) from Clocksetting where spmsDevice.id=:p1")
	public long getRairconClockCount(@Param("p1")String deviceId);
	
	@Query("select count(*) from Clocksetting where rairconSetting.id=:p1 and (  (temp <= :p3 and temp >= :p2) or (on_off = 0)  )")
	public long queryClocksCountByCurveId(@Param("p1")String curveId, @Param("p2")Integer minTemps,
			@Param("p3")Integer maxTemps);
	
	@Query("select count(*) from Clocksetting where rairconSetting.id=:p1 ")
	public long queryClocksCountByCurveIdAll(@Param("p1")String curveId);
	
}
