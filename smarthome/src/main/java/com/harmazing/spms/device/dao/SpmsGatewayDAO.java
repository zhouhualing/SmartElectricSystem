package com.harmazing.spms.device.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.device.entity.SpmsGateway;

@Repository("spmsGatewayDAO")
public interface SpmsGatewayDAO extends SpringDataDAO<SpmsGateway> {
	
	@Query(nativeQuery=true, value="select * from spms_gateway where mac=:mac")
	public SpmsGateway findGwByMAC(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_gateway where mac=:mac")
	public SpmsGateway getByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_gateway where ip=:ip")
	public List<SpmsGateway> getByIp(@Param("ip") String ip);
	
	@Query(nativeQuery=true, value="select * from spms_gateway where sn=:sn")
	public List<SpmsGateway> getBySn(@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_gateway where mac in :macs")
	public List<SpmsGateway> findGwByMACs(@Param("macs") List<String>macs);
	
	@Query(nativeQuery=true, value="select * from spms_gateway where sn=:sn")
	public SpmsGateway findGwBySN(@Param("sn") String sn);
	
	@Query(nativeQuery=true, value="select * from spms_gateway where sn in :sns")
	public List<SpmsGateway> findGatewayBySNs(@Param("sns") List<String>sns);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_gateway where mac=:mac")
	public void deleteByMac(@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_gateway where mac=:mac")
	public SpmsGateway findByMAC(@Param("mac") String mac);
	
}
