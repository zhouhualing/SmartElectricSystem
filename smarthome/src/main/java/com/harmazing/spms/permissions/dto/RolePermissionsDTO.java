package com.harmazing.spms.permissions.dto;

import java.util.List;

import com.harmazing.spms.common.dto.CommonDTO;

/**
 * 用户管理Entity
 *
 * @author 吴列鹏
 * @version 1.0
 */
public class RolePermissionsDTO extends CommonDTO {
    /* 角色ID */
    private String user;
    /* 角色名  */
    private String roleName;
    /* 角色编码  */
    private String roleCode;
    /* 角色权限树形列表  */
    private List<PermissionsDTO> rolePermissionsList;
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public List<PermissionsDTO> getRolePermissionsList() {
		return rolePermissionsList;
	}

	public void setRolePermissionsList(List<PermissionsDTO> rolePermissionsList) {
		this.rolePermissionsList = rolePermissionsList;
	}
    
    

}