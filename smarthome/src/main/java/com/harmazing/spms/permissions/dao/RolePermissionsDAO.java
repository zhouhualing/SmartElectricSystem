package com.harmazing.spms.permissions.dao;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.permissions.entity.RolePermission;
import com.harmazing.spms.user.entity.SpmsUser;



@Repository("rolePermissionsDAO")
public interface RolePermissionsDAO extends SpringDataDAO<RolePermission> {
	@Modifying
	@Query(nativeQuery=true,value="DELETE FROM role_permission WHERE roleentities_id =:p")
	public void deleteRolePermission(@Param("p")String roleId);
}
