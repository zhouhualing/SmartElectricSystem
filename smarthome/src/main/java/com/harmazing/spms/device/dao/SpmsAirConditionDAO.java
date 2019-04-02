package com.harmazing.spms.device.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsAirCondition;

@Repository("spmsAirConditionDAO")
public interface SpmsAirConditionDAO extends SpringDataDAO<SpmsAirCondition>{
	@Query(nativeQuery=true, value="select * from spms_ac where mac=:mac")
	public SpmsAirCondition findAcByMAC(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_ac where mac in :macs")
	public List<SpmsAirCondition> findAcByMACs(@Param("macs") List<String>macs);
	
	@Query(nativeQuery=true, value="select * from spms_ac where sn=:sn")
	public SpmsAirCondition findAcBySN(@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_ac where sn in :sns")
	public List<SpmsAirCondition> findAcBySNs(@Param("sns") List<String>sns);
	
	@Query(nativeQuery=true, value="select * from spms_ac where (mac=:mac or sn=:sn)")
	public List<SpmsAirCondition> findByMacAndSn(@Param("mac") String mac,@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_ac where mac=:mac and type = :type")
	public SpmsAirCondition findByMacAndType(@Param("mac") String mac,@Param("type")String type);
	
	@Query(nativeQuery=true, value="select * from spms_ac where mac=:mac")
	public SpmsAirCondition getByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_ac where (mac=:no or sn=:no) and type=:type")
	public SpmsAirCondition findDeviceByMacorSn(@Param("no") String no,@Param("type")String type);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_ac where mac=:mac")
	public void deleteByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_ac where mac=:mac")
	public SpmsAirCondition findByMAC(@Param("mac") String mac);
}
