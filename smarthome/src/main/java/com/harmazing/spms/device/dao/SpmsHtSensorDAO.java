package com.harmazing.spms.device.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsHtSensor;
import com.harmazing.spms.device.entity.SpmsLamp;

@Repository("spmsHtSensorDAO")
public interface SpmsHtSensorDAO extends SpringDataDAO<SpmsHtSensor>{
	@Query(nativeQuery=true, value="select * from spms_ht_sensor where mac=:mac")
	public SpmsHtSensor findPirByMAC(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_ht_sensor where mac=:mac")
	public SpmsHtSensor getByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_ht_sensor where mac in :macs")
	public List<SpmsHtSensor> findPirByMACs(@Param("macs") List<String>macs);
	
	@Query(nativeQuery=true, value="select * from spms_ht_sensor where sn=:sn")
	public SpmsHtSensor findPirBySN(@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_ht_sensor where sn in :sns")
	public List<SpmsHtSensor> findPirBySNs(@Param("sns") List<String>sns);
	
	@Query(nativeQuery=true, value="select * from spms_ht_sensor where (mac=:mac or sn=:sn)")
	public List<SpmsHtSensor> findByMacAndSn(@Param("mac") String mac,@Param("sn") String sn);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_ht_sensor where mac=:mac")
	public void deleteByMac(@Param("mac") String mac);	
	
	@Query(nativeQuery=true, value="select * from spms_ht_sensor where mac=:mac")
	public SpmsHtSensor findByMAC(@Param("mac") String mac);
}
