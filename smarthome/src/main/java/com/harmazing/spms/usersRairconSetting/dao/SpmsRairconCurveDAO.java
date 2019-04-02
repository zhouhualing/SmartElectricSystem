package com.harmazing.spms.usersRairconSetting.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.usersRairconSetting.entity.RairconSetting;

public interface SpmsRairconCurveDAO extends SpringDataDAO<RairconSetting> {
	/*
	 * 加载用户设备空调曲线列表
	 */
	@Query("from RairconSetting where spmsDevice.id=:p1 order by createDate")
	public List<RairconSetting> getSpmsRairconCurveByDeviceId(@Param("p1")String deviceId);
	
	@Query("from RairconSetting where spmsUser.id=:p1  order by monday desc,tuesday desc,wednesday desc,thursday desc,friday desc,saturday desc,sunday desc,createDate desc")
	public List<RairconSetting> getSpmsRairconCurveByUserId(@Param("p1")String userId);
	
	@Query("from RairconSetting where spmsUser.id=:p1  order by createDate desc")
	public List<RairconSetting> getSpmsRairconCurveByUserIdForApp(@Param("p1")String userId);
	
	@Query("select count(*) from RairconSetting where spmsDevice.id=:p1")
	public long getRairconCurveCount(@Param("p1")String deviceId);
	
	@Query("select count(*) from RairconSetting where spmsUser.id=:p1")
	public long getRairconCurveCountForUser(@Param("p1")String userId);
	
	@Query(" from RairconSetting where spmsDevice.id=:p0 and id <> :p8 and (" +
			"(monday=:p1 and monday<>0) or " +
			"(tuesday=:p2 and tuesday<>0) or " +
			"(wednesday=:p3 and wednesday<>0) or " +
			"(thursday=:p4 and thursday<>0) or " +
			"(friday=:p5 and friday<>0) or " +
			"(saturday=:p6 and saturday<>0) or " +
			"(sunday=:p7 and sunday<>0) )" )
	public List<RairconSetting> queryCurveRepeat(@Param("p0")String deviceId,
			@Param("p1")int monday, 
			@Param("p2")int tuesday, 
			@Param("p3")int wednesday, 
			@Param("p4")int thursday,
			@Param("p5")int friday, 
			@Param("p6")int saturday, 
			@Param("p7")int sunday, 
			@Param("p8")String curveid);
	@Query("select count(*) from RairconSetting where spmsUser.id=:p1")
	public long countForUser(@Param("p1")String userId);
	
	@Query(" from RairconSetting where spmsUser.id=:p1 order by createDate desc")
	public List<RairconSetting> findAllByUser(@Param("p1")String userId);
	
	@Query(" select count(*) from RairconSetting where spmsUser.id=:p1 ")
	public long countForUserId(@Param("p1")String userId);
	
	
	
}
