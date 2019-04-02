package com.harmazing.spms.desktop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.desktop.entity.SpmsProductMFee;
@Repository("productMFeeDAO")
public interface SpmsProductMFeeDAO extends SpringDataDAO<SpmsProductMFee>{
	@Query("from SpmsProductMFee where spmsUser.id=:userId and month=:month and year=:year")
	public List<SpmsProductMFee> findAllByUser(@Param("userId") String userId,
			@Param("month") Integer month,@Param("year") Integer year);
}
