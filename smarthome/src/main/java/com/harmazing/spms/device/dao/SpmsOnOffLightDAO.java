package com.harmazing.spms.device.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.device.entity.SpmsOnOffLight;
import com.harmazing.spms.device.entity.SpmsOnOffLight;

@Repository("spmsOnOffLightDAO")
public interface SpmsOnOffLightDAO extends SpringDataDAO<SpmsOnOffLight>{
	@Query(nativeQuery=true, value="select * from spms_onoff_light where mac=:mac")
	public SpmsOnOffLight findWdByMAC(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_light where mac=:mac")
	public SpmsOnOffLight getByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_light where mac in :macs")
	public List<SpmsOnOffLight> findWdByMACs(@Param("macs") List<String>macs);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_light where sn=:sn")
	public SpmsOnOffLight findWdBySN(@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_light where sn in :sns")
	public List<SpmsOnOffLight> findWdBySNs(@Param("sns") List<String>sns);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_onoff_light where mac=:mac")
	public void deleteByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_light where mac=:mac")
	public SpmsOnOffLight findByMAC(@Param("mac") String mac);
}
