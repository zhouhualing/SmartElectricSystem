package com.harmazing.spms.spmslog.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.product.entity.SpmsProduct;
import com.harmazing.spms.spmslog.entity.SpmsLogsUser;

@Repository("userLogsDao")
public interface UserLogsDao extends SpringDataDAO<SpmsLogsUser>{

	@Modifying
	@Query(nativeQuery=true,value="insert into spms_logs_user (url,userId,operateDate,ip,data_before,data_after,modify_user) " +
			"values(:p1,:p2,:p3,:p4,:p5,:p6,:p7)")
	public void saveUserLog(@Param("p1")String url,@Param("p2")String userId,@Param("p3")String operateDate,@Param("p4")String ip
			,@Param("p5")String data_before,@Param("p6")String data_after,@Param("p7")String modify_user);
}
