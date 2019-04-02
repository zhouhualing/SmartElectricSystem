package com.harmazing.spms.device.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.device.entity.SpmsPir;
import com.harmazing.spms.device.entity.SpmsWinDoor;

@Repository("spmsPirDAO")
public interface SpmsPirDAO extends SpringDataDAO<SpmsPir>{
	@Query(nativeQuery=true, value="select * from spms_pir where mac=:mac")
	public SpmsPir findPirByMAC(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_pir where mac=:mac")
	public SpmsPir getByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_pir where mac in :macs")
	public List<SpmsPir> findPirByMACs(@Param("macs") List<String>macs);
	
	@Query(nativeQuery=true, value="select * from spms_pir where sn=:sn")
	public SpmsPir findPirBySN(@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_pir where sn in :sns")
	public List<SpmsPir> findPirBySNs(@Param("sns") List<String>sns);
	
	@Query(nativeQuery=true, value="select * from spms_pir where (mac=:mac or sn=:sn)")
	public List<SpmsAirCondition> findByMacAndSn(@Param("mac") String mac,@Param("sn") String sn);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_pir where mac=:mac")
	public void deleteByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_pir where mac=:mac")
	public SpmsPir findByMAC(@Param("mac") String mac);
}
