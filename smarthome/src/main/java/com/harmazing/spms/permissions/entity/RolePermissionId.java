package com.harmazing.spms.permissions.entity;

import java.io.Serializable;

public class RolePermissionId implements Serializable{
	private String permissionentities_id;
	private String roleentities_id;
	public String getPermissionentities_id() {
		return permissionentities_id;
	}
	public void setPermissionentities_id(String permissionentities_id) {
		this.permissionentities_id = permissionentities_id;
	}
	public String getRoleentities_id() {
		return roleentities_id;
	}
	public void setRoleentities_id(String roleentities_id) {
		this.roleentities_id = roleentities_id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((permissionentities_id == null) ? 0 : permissionentities_id
						.hashCode());
		result = prime * result
				+ ((roleentities_id == null) ? 0 : roleentities_id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RolePermissionId other = (RolePermissionId) obj;
		if (permissionentities_id == null) {
			if (other.permissionentities_id != null)
				return false;
		} else if (!permissionentities_id.equals(other.permissionentities_id))
			return false;
		if (roleentities_id == null) {
			if (other.roleentities_id != null)
				return false;
		} else if (!roleentities_id.equals(other.roleentities_id))
			return false;
		return true;
	}
	
	
}
