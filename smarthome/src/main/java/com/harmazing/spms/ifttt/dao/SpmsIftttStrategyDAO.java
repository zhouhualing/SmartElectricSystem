package com.harmazing.spms.ifttt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.ifttt.entity.SpmsIftttStrategy;

@Repository("spmsIftttStrategyDAO")
public interface SpmsIftttStrategyDAO extends SpringDataDAO<SpmsIftttStrategy> {
	
	@Query(nativeQuery=true, value="select * from ifttt_stragegy_test where mac=:mac")
	public List<SpmsIftttStrategy> findByMAC(@Param("mac") String mac);
}
