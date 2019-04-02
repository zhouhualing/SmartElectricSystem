package com.harmazing.spms.log.dto;

import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.common.dto.CommonDTO;
import com.harmazing.spms.user.dto.SpmsUserDTO;

public class SpmsLogDTO extends CommonDTO {
	
	private static final long serialVersionUID = 1L;
	private String type; 

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private UserDTO createBy;	
	// 创建者
		private SpmsUserDTO spmsUserDTO;		// 创建者
		private String remoteAddr; 	// 操作用户的IP地址
		private String requestUri; 	// 操作的URI
		private String method; 		// 操作的方式
		private String params; 		// 操作提交的数据
		private String userAgent;	// 操作用户代理信息
		private String exception; 
		private Date CreateDate;
		public Date getCreateDate() {
			return CreateDate;
		}
		public String getType() {
			return type;
		}
		

		public UserDTO getCreateBy() {
			return createBy;
		}
		public void setCreateBy(UserDTO createBy) {
			this.createBy = createBy;
		}
		public void setType(String type) {
			this.type = type;
		}
		public SpmsUserDTO getSpmsUserDTO() {
			return spmsUserDTO;
		}
		public void setSpmsUserDTO(SpmsUserDTO spmsUserDTO) {
			this.spmsUserDTO = spmsUserDTO;
		}
		public String getRemoteAddr() {
			return remoteAddr;
		}
		public void setRemoteAddr(String remoteAddr) {
			this.remoteAddr = remoteAddr;
		}
		public String getRequestUri() {
			return requestUri;
		}
		public void setRequestUri(String requestUri) {
			this.requestUri = requestUri;
		}
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
		public String getParams() {
			return params;
		}
		public void setParams(String params) {
			this.params = params;
		}
		public String getUserAgent() {
			return userAgent;
		}
		public void setUserAgent(String userAgent) {
			this.userAgent = userAgent;
		}
		public String getException() {
			return exception;
		}
		public void setException(String exception) {
			this.exception = exception;
		}
		public void setCreateDate(Date createDate) {
			CreateDate = createDate;
		}
	
}
