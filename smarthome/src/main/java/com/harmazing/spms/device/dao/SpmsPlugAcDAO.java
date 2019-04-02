package com.harmazing.spms.device.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsPlugAc;
import com.harmazing.spms.device.entity.SpmsPir;
import com.harmazing.spms.device.entity.SpmsPlugAc;
import com.harmazing.spms.device.entity.SpmsPlugAc;

@Repository("spmsPlugAcDAO")
public interface SpmsPlugAcDAO extends SpringDataDAO<SpmsPlugAc> {	
	
	@Query(nativeQuery=true, value="select * from spms_plug_ac where mac=:mac")
	public SpmsPlugAc findAcByMAC(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_plug_ac where mac in :macs")
	public List<SpmsPlugAc> findAcByMACs(@Param("macs") List<String>macs);
	
	@Query(nativeQuery=true, value="select * from spms_plug_ac where sn=:sn")
	public SpmsPlugAc findAcBySN(@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_plug_ac where sn in :sns")
	public List<SpmsPlugAc> findAcBySNs(@Param("sns") List<String>sns);
	
	@Query(nativeQuery=true, value="select * from spms_plug_ac where (mac=:mac or sn=:sn)")
	public List<SpmsPlugAc> findByMacAndSn(@Param("mac") String mac,@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_plug_ac where mac=:mac and type = :type")
	public SpmsPlugAc findByMacAndType(@Param("mac") String mac,@Param("type")String type);
	
	@Query(nativeQuery=true, value="select * from spms_plug_ac where mac=:mac")
	public SpmsPlugAc getByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_plug_ac where (mac=:no or sn=:no) and type=:type")
	public SpmsPlugAc findDeviceByMacorSn(@Param("no") String no,@Param("type")String type);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_plug_ac where mac=:mac")
	public void deleteByMac(@Param("mac") String mac);
	
	
	@Query(nativeQuery=true, value="select * from spms_plug_ac where mac=:mac")
	public SpmsPlugAc findByMAC(@Param("mac") String mac);
}
