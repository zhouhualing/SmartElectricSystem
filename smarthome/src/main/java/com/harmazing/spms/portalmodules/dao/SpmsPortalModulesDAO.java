package com.harmazing.spms.portalmodules.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.portalmodules.entity.SpmsPortalModules;


@Repository("spmsPortalModulesDAO")
public interface SpmsPortalModulesDAO  extends SpringDataDAO<SpmsPortalModules> {
	@Query("from SpmsPortalModules where divName=:p1")
	public SpmsPortalModules getByDivname(@Param("p1") String divname);
}
