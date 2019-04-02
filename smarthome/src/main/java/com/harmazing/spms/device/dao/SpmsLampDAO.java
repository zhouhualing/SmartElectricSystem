package com.harmazing.spms.device.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsLamp;
import com.harmazing.spms.device.entity.SpmsOnOffPmPlug;
import com.harmazing.spms.device.entity.SpmsLamp;

@Repository("spmsLampDAO")
public interface SpmsLampDAO extends SpringDataDAO<SpmsLamp>{
	@Query(nativeQuery=true, value="select * from spms_lamp where mac=:mac")
	public SpmsLamp findWdByMAC(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_lamp where mac=:mac")
	public SpmsLamp getByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_lamp where mac in :macs")
	public List<SpmsLamp> findWdByMACs(@Param("macs") List<String>macs);
	
	@Query(nativeQuery=true, value="select * from spms_lamp where sn=:sn")
	public SpmsLamp findWdBySN(@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_lamp where sn in :sns")
	public List<SpmsLamp> findWdBySNs(@Param("sns") List<String>sns);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_lamp where mac=:mac")
	public void deleteByMac(@Param("mac") String mac);	
	
	@Query(nativeQuery=true, value="select * from spms_lamp where mac=:mac")
	public SpmsLamp findByMAC(@Param("mac") String mac);
}
