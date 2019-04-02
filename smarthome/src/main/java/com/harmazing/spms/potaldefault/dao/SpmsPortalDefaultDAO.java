package com.harmazing.spms.potaldefault.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.portalmodules.entity.SpmsPortalModules;
import com.harmazing.spms.potaldefault.entity.SpmsPortalDefault;


@Repository("spmsPortalDefaultDAO")
public interface SpmsPortalDefaultDAO   extends SpringDataDAO<SpmsPortalDefault> {
	
	@Query("from SpmsPortalDefault where roleCode=:p1")
	public List<SpmsPortalDefault> getByRoleCode(@Param("p1") String rolecode);
	@Query("from SpmsPortalDefault where roleCode=:p1 and modules.divName=:p2")
	public SpmsPortalDefault getByRoleCodeModules(@Param("p1") String rolecode,@Param("p2") String modules);
}
