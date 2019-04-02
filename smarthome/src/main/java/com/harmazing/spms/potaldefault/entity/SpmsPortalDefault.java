package com.harmazing.spms.potaldefault.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.common.entity.CommonEntity;
import com.harmazing.spms.portalmodules.entity.SpmsPortalModules;

/**
 * 
 * @author yyx since 2015年1月7日 按用户角色的首页portal显示
 */
@Entity
@Table(name = "spms_portal_default")
public class SpmsPortalDefault extends CommonEntity {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 角色编码
	 */
	@Length(min = 0, max = 10)
	@Column(name = "role_code")
	private String roleCode;

	/**
	 * 显示的模块
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "modules_id")
	@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
	private SpmsPortalModules modules;

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public SpmsPortalModules getModules() {
		return modules;
	}

	public void setModules(SpmsPortalModules modules) {
		this.modules = modules;
	}

}
