package com.harmazing.spms.common.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

/**
 * the spring date dao. 
 * @author Zhaocaipeng
 * since 2013年12月12日
 */
@JsonIgnoreType
@NoRepositoryBean
public interface SpringDataDAO <T> extends JpaRepository<T, Serializable>, IDAO, JpaSpecificationExecutor<T>{
    
	@Modifying
	@Query("delete from  #{#entityName} t where t.id=:id ")
	public void doDeleteById(@Param("id") String id);
	
}
