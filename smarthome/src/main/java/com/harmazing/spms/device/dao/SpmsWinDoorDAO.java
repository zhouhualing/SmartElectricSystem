package com.harmazing.spms.device.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.device.entity.SpmsWinDoor;

@Repository("spmsWinDoorDAO")
public interface SpmsWinDoorDAO extends SpringDataDAO<SpmsWinDoor>{
	@Query(nativeQuery=true, value="select * from spms_win_door where mac=:mac")
	public SpmsWinDoor findWdByMAC(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_win_door where mac=:mac")
	public SpmsWinDoor getByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_win_door where mac in :macs")
	public List<SpmsWinDoor> findWdByMACs(@Param("macs") List<String>macs);
	
	@Query(nativeQuery=true, value="select * from spms_win_door where sn=:sn")
	public SpmsWinDoor findWdBySN(@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_win_door where sn in :sns")
	public List<SpmsWinDoor> findWdBySNs(@Param("sns") List<String>sns);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_win_door where mac=:mac")
	public void deleteByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_win_door where mac=:mac")
	public SpmsWinDoor findByMAC(@Param("mac") String mac);
}
