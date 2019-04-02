package com.harmazing.spms.user.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.user.entity.SpmsUserAsyncEntity;

import java.util.List;
import java.util.Map;

@Repository("spmsUserAsyncDAO")
public interface SpmsUserAsyncDAO extends SpringDataDAO<SpmsUserAsyncEntity> {
	
	@Query(nativeQuery = true,value="select * from spms_user_async where mobile=:p1")
	public SpmsUserAsyncEntity getByMobile(@Param("p1") String mobile);

	@Query(nativeQuery = true,value="select image from spms_user_async where mobile=:p1")
	public Byte[] getImageByMobile(@Param("p1") String mobile);
}
