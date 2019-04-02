package com.harmazing.spms.device.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.device.entity.SpmsOnOffPmPlug;
import com.harmazing.spms.device.entity.SpmsOnOffPmPlug;

@Repository("spmsOnOffPmPlugDAO")
public interface SpmsOnOffPmPlugDAO extends SpringDataDAO<SpmsOnOffPmPlug>{
	@Query(nativeQuery=true, value="select * from spms_onoff_pm_plug where mac=:mac")
	public SpmsOnOffPmPlug findWdByMAC(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_pm_plug where mac=:mac")
	public SpmsOnOffPmPlug getByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_pm_plug where mac in :macs")
	public List<SpmsOnOffPmPlug> findWdByMACs(@Param("macs") List<String>macs);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_pm_plug where sn=:sn")
	public SpmsOnOffPmPlug findWdBySN(@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_pm_plug where sn in :sns")
	public List<SpmsOnOffPmPlug> findWdBySNs(@Param("sns") List<String>sns);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_onoff_pm_plug where mac=:mac")
	public void deleteByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_pm_plug where mac=:mac")
	public SpmsOnOffPmPlug findByMAC(@Param("mac") String mac);
}
