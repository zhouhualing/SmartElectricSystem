package com.harmazing.spms.device.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.device.entity.SpmsOnOffPlug;

@Repository("spmsOnOffPlugDAO")
public interface SpmsOnOffPlugDAO extends SpringDataDAO<SpmsOnOffPlug>{
	@Query(nativeQuery=true, value="select * from spms_onoff_plug where mac=:mac")
	public SpmsOnOffPlug findWdByMAC(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_plug where mac=:mac")
	public SpmsOnOffPlug getByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_plug where mac in :macs")
	public List<SpmsOnOffPlug> findWdByMACs(@Param("macs") List<String>macs);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_plug where sn=:sn")
	public SpmsOnOffPlug findWdBySN(@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_plug where sn in :sns")
	public List<SpmsOnOffPlug> findWdBySNs(@Param("sns") List<String>sns);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_onoff_plug where mac=:mac")
	public void deleteByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_onoff_plug where mac=:mac")
	public SpmsOnOffPlug findByMAC(@Param("mac") String mac);
}
