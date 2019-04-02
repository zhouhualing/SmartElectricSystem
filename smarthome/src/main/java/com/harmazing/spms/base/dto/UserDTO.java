/**
 * 
 */
package com.harmazing.spms.base.dto;

import java.util.Date;
import com.harmazing.spms.common.dto.CommonDTO;

public class UserDTO extends CommonDTO {
    
	private String userName;

	private String userCode;
	
	private String no;
	
	private Integer userType;
	
	private String orgName;
	
	private String orgId;
	
	private String companyName;
	
	private String companyId;
	
	private String password;
	
	private  String token;

	private Integer sort;
	
	private String phoneNumber;
	
	private String mobile;
	
	private String mobileMac;

	private String nickname;
	
	private  Byte[] image;
	
	private  String sex;

	private String birthday;
	
	private String address;

	private String email;
	
	private Integer status;
	
	private OrgDTO orgDTO;
	
	private String roleIds;
	
	private String loginIp;
	
	private Date loginDate;
	
	private String mark;
	
	private Date createDate;
	
	private String confirmPasword;
	
	private String userTypeText;
	
	public String getUserName() {
	    return userName;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public void setUserName(String userName) {
	    this.userName = userName;
	}

	public Integer getSort() {
	    return sort;
	}

	public String getUserTypeText() {
	    return userTypeText;
	}


	public void setUserTypeText(String userTypeText) {
	    this.userTypeText = userTypeText;
	}


	public Integer getStatus() {
		return status;
	}


	public void setSort(Integer sort) {
	    this.sort = sort;
	}


	public Date getLoginDate() {
	    return loginDate;
	}


	public String getPassword() {
	    return password;
	}


	public void setPassword(String password) {
	    this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setLoginDate(Date loginDate) {
	    this.loginDate = loginDate;
	}


	public Date getCreateDate() {
	    return createDate;
	}


	public void setCreateDate(Date createDate) {
	    this.createDate = createDate;
	}


	public String getConfirmPasword() {
	    return confirmPasword;
	}


	public void setConfirmPasword(String confirmPasword) {
	    this.confirmPasword = confirmPasword;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getLoginIp() {
	    return loginIp;
	}


	public void setLoginIp(String loginIp) {
	    this.loginIp = loginIp;
	}

	public String getMark() {
	    return mark;
	}


	public void setMark(String mark) {
	    this.mark = mark;
	}


	public String getUserCode() {
	    return userCode;
	}


	public void setUserCode(String userCode) {
	    this.userCode = userCode;
	}


	public String getOrgName() {
	    return orgName;
	}


	public void setOrgName(String orgName) {
	    this.orgName = orgName;
	}


	public String getCompanyName() {
	    return companyName;
	}


	public void setCompanyName(String companyName) {
	    this.companyName = companyName;
	}


	public String getRoleIds() {
	    return roleIds;
	}


	public void setRoleIds(String roleIds) {
	    this.roleIds = roleIds;
	}


	public String getNo() {
	    return no;
	}


	public void setNo(String no) {
	    this.no = no;
	}


	public Integer getUserType() {
	    return userType;
	}


	public void setUserType(Integer userType) {
	    this.userType = userType;
	}


	public String getPhoneNumber() {
	    return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
	    this.phoneNumber = phoneNumber;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getMobileMac() {
		return mobileMac;
	}

	public void setMobileMac(String mobileMac) {
		this.mobileMac = mobileMac;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Byte[] getImage() {
		return image;
	}

	public void setImage(Byte[] image) {
		this.image = image;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public OrgDTO getOrgDTO() {
	    return orgDTO;
	}

	public void setOrgDTO(OrgDTO orgDTO) {
	    this.orgDTO = orgDTO;
	}
}
