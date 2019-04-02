package com.harmazing.spms.base.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.base.entity.RequestControlEntity;
import com.harmazing.spms.common.dao.SpringDataDAO;

@Repository("requestControlDAO")
public interface RequestControlDAO extends SpringDataDAO<RequestControlEntity> {
	
	@Query("from RequestControlEntity")
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
	public List <RequestControlEntity> cacheFindRequestControl();
}
