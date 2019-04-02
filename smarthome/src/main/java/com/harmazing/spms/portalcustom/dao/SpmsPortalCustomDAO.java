package com.harmazing.spms.portalcustom.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.portalcustom.entity.SpmsPortalCustom;


@Repository("spmsPortalCustomDAO")
public interface SpmsPortalCustomDAO  extends SpringDataDAO<SpmsPortalCustom> {
	@Query("from SpmsPortalCustom where user.id=:p1 and isdisplay = 1")
	public List<SpmsPortalCustom> getByUserId(@Param("p1") String userid);
	@Query("from SpmsPortalCustom where user.id=:p1")
	public List<SpmsPortalCustom> getByUserIdAll(@Param("p1") String userid);
	@Query("from SpmsPortalCustom where user.id=:p1 and spmsPortalModules.divName=:p2")
	public SpmsPortalCustom getByUserIdDivName(@Param("p1") String userid,@Param("p2") String divname);
	@Query("from SpmsPortalCustom where rolecode=:p1 and spmsPortalModules.divName=:p2")
	public List<SpmsPortalCustom> getByRoleCodeDivName(@Param("p1") String rolecode,@Param("p2") String divname);
}
