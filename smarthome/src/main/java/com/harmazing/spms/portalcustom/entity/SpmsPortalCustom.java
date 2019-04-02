package com.harmazing.spms.portalcustom.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.common.entity.CommonEntity;
import com.harmazing.spms.portalmodules.entity.SpmsPortalModules;

/**
 * 
 * @author yyx
 *since 2015年1月9日
 *按用户自定义的首页portal显示
 */

@Entity
@Table(name="spms_portal_custom")
public class SpmsPortalCustom extends CommonEntity {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private UserEntity user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "modules_id")
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private SpmsPortalModules spmsPortalModules;
	
	private Integer sort;
	
	private String rolecode;
	
	private Integer isdisPlay;

	public Integer getIsdisPlay() {
		return isdisPlay;
	}

	public void setIsdisPlay(Integer isdisPlay) {
		this.isdisPlay = isdisPlay;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public SpmsPortalModules getSpmsPortalModules() {
		return spmsPortalModules;
	}

	public void setSpmsPortalModules(SpmsPortalModules spmsPortalModules) {
		this.spmsPortalModules = spmsPortalModules;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}
	
	
}
