package com.harmazing.spms.permissions.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "role_permission")
@IdClass(RolePermissionId.class)
public class RolePermission {
	private String permissionentities_id;
	private String roleentities_id;
	
	@Id
	@Column(name="permissionentities_id")
	public String getPermissionentities_id() {
		return permissionentities_id;
	}
	public void setPermissionentities_id(String permissionentities_id) {
		this.permissionentities_id = permissionentities_id;
	}
	@Id
	@Column(name="roleentities_id")
	public String getRoleentities_id() {
		return roleentities_id;
	}
	public void setRoleentities_id(String roleentities_id) {
		this.roleentities_id = roleentities_id;
	}
	
}
