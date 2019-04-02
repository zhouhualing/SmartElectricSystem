package com.harmazing.spms.spmslog.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.spmslog.entity.SpmsLogsDevice;

@Repository("deviceLogsDao")
public interface DeviceLogsDao extends SpringDataDAO<SpmsLogsDevice> {
	
	@Modifying
	@Query(nativeQuery=true,value="insert into spms_logs_device (url,deviceId,operateDate,ip,data_before,data_after,modify_user) " +
			"values(:p1,:p2,:p3,:p4,:p5,:p6,:p7)")
	public void saveDeviceLog(@Param("p1")String url,@Param("p2")String deviceId,@Param("p3")String operateDate,@Param("p4")String ip
			,@Param("p5")String data_before,@Param("p6")String data_after,@Param("p7")String modify_user);
}
