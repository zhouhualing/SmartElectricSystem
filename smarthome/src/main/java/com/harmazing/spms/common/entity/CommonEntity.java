package com.harmazing.spms.common.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.util.UserUtil;

/**
 * clickmed's Common Entity,which contains the createUser,createDate,lastCreateUser and so on.
 * 含有一些常用属性的公共entity
 * author Zhaocaipeng
 * since 2013-9-9
 */
@MappedSuperclass
public class CommonEntity extends GenericEntity {
	
	// 显示/隐藏
	public static final String SHOW = "1";
	public static final String HIDE = "0";
	
	// 是/否
	public static final String YES = "1";
	public static final String NO = "0";

	// 删除标记（0：正常；1：删除；2：审核；）
	public static final String FIELD_DEL_FLAG = "delFlag";
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	public static final String DEL_FLAG_AUDIT = "2";
    
	/**
	 * serialVersionUID
	 */
	protected static final long serialVersionUID = 1L;

	/**
	 * The createUser.
	 */
	@ManyToOne(cascade={CascadeType.REFRESH},fetch=FetchType.LAZY)
	@JoinColumn(name="createUser_id")
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private UserEntity createUser;
	
	/**
	 * The createDate.
	 */
	@Column(name="createDate")
	@CreatedDate
	private Date createDate;
	
	/**
	 * The lastModifyUser.
	 */
	@ManyToOne(cascade={CascadeType.REFRESH},fetch=FetchType.LAZY)
	@JoinColumn(name="lastModifyUser_id")
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private UserEntity lastModifyUser;
	
	/**
	 * The lastModifyDate.
	 */
	@Column(name="lastModifyDate")
	@LastModifiedDate
	private Date lastModifyDate;
	
	@PrePersist
	public void preSave() {
		if((this.getId() == null) && (this.createUser == null)) {
			this.createDate = new Date();
			this.lastModifyDate = new Date();
			this.createUser = UserUtil.getCurrentUser();
			this.lastModifyUser = UserUtil.getCurrentUser();
		} else {
			this.lastModifyDate = new Date();
			this.lastModifyUser = UserUtil.getCurrentUser();
		}
	}
	
	
	public UserEntity getCreateUser() {
		return createUser;
	}

	public void setCreateUser(UserEntity createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public UserEntity getLastModifyUser() {
		return lastModifyUser;
	}

	public void setLastModifyUser(UserEntity lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}	
}
