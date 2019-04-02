/**
 * 
 */
package com.harmazing.spms.base.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.common.entity.CommonEntity;

@Entity
@Table(name = "tb_org_org")
public class OrgEntity extends CommonEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private OrgEntity parent;	// 父级编号
	
    @Length(min=1, max=255)
    private String parentIds; // 所有父级编号
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="area_id")
    private Area area;		// 归属区域
    
    @Length(min=0, max=100)
    private String code; 	// 机构编码
    
    @Length(min=1, max=100)
    private String name; 	// 机构名称
    
    @Length(min=1, max=1)
    private Integer type; 	// 机构类型（1：公司；2：部门；3：小组）
    
    @Length(min=1, max=1)
    private Integer grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
    
    @Length(min=0, max=255)
    private String address; // 联系地址
    
    @Length(min=0, max=100)
    private String zipCode; // 邮政编码
    
    @Length(min=0, max=100)
    private String master; 	// 负责人
    
    @Length(min=0, max=200)
    private String phone; 	// 电话
    
    @Length(min=0, max=200)
    private String fax; 	// 传真
    
    @Length(min=0, max=200)
    private String email; 	// 邮箱
    
    @OneToMany(mappedBy = "orgEntity", fetch=FetchType.LAZY)
    @JsonIgnore
    private List<UserEntity> userList = Lists.newArrayList();   // 拥有用户列表
	
    @OneToMany(mappedBy = "parent", fetch=FetchType.LAZY)
    @JsonIgnore
    private List<OrgEntity> childList = Lists.newArrayList();// 拥有子机构列表

    public OrgEntity getParent() {
        return parent;
    }

    public void setParent(OrgEntity parent) {
        this.parent = parent;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
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

    public List<UserEntity> getUserList() {
        return userList;
    }

    public void setUserList(List<UserEntity> userList) {
        this.userList = userList;
    }

    public List<OrgEntity> getChildList() {
        return childList;
    }

    public void setChildList(List<OrgEntity> childList) {
        this.childList = childList;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
