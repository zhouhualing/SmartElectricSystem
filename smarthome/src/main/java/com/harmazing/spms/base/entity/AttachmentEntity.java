package com.harmazing.spms.base.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.base.util.UserUtil;

@Entity
@Table(name="Tb_attachement_attachment")
public class AttachmentEntity  {

	/**
	 * UUID
	 */
	@Id
	private String id;
	
	@Version
	private Long version;
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 附件名称
	 */
	private String fileName;
	
	/**
	 * 附件类型
	 */
	private String filePostfix;

	/**
	 * 业务类型
	 */
	private String businessType;
	
	
	/**
	 * 业务id
	 */
	private String businessId;
	
	
	/**
	 * 文件路径
	 */
	private String filePath;
	
	/**
	 * 文件大小
	 */
	private Long fileSize;

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
		if(this.lastModifyDate == null) {
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

	public Long getFileSize() {
		return fileSize;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFileName() {
		return fileName;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePostfix() {
		return filePostfix;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFilePostfix(String filePostfix) {
		this.filePostfix = filePostfix;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

}
