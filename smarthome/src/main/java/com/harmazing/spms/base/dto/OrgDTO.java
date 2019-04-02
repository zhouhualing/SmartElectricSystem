/**
 * 
 */
package com.harmazing.spms.base.dto;

import java.util.Date;
import com.harmazing.spms.common.dto.CommonDTO;

public class OrgDTO extends CommonDTO {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    	
    private String parentId;	// 父级编号
	
    private String parentIds; // 所有父级编号
    
    private String areaId;		// 归属区域
    
    private String code; 	// 机构编码
    
    private String name; 	// 机构名称
    
    private Integer type; 	// 机构类型（1：公司；2：部门；3：小组）
    
    private Integer grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
    
    private String address; // 联系地址
    
    private String zipCode; // 邮政编码
    
    private String master; 	// 负责人
    
    private String phone; 	// 电话
    
    private String fax; 	// 传真
    
    private String email; 	// 邮箱
    
    private String loginIp;	// 最后登陆IP
    
    private Date loginDate;	// 最后登陆日期

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
