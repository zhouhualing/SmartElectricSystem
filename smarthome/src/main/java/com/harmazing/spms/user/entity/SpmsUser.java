package com.harmazing.spms.user.entity;

import javax.persistence.*;
import com.harmazing.spms.workorder.entity.SpmsWorkOrder;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.common.entity.CommonEntity;

@Entity
@Table(name = "spms_user")
public class SpmsUser extends CommonEntity{

	private static final long serialVersionUID = 1L;
	
	//用户类型--试用
	public static final Integer USER_TYPE_TRIAL = 1;
	
	//用户类型--商用
	public static final Integer USER_TYPE_BUSINESS = 2;
	
	
	/* 帐户ID */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sys_user_id")
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private UserEntity user;
	/* 用户类型 */
	//1试用 2商用 dict dict_spmsuser_type
	private Integer type;
	/* 姓名 */
	@Length(min = 1, max = 32)
	private String fullname;
	/* 住址 */
	@Length(min = 1, max = 256)
	private String address;
	/* 电话 */
	@Length(min = 1, max = 20)
	private String mobile;
	
	/* 手机 mac 地址*/
	@Length(min = 1, max = 20)
	private String mobileMac;
		/* 邮箱 */
//	@Length(min = 1, max = 64)
	private String email;

    /* 身份证号 */
//    @Length(min = 1, max = 20)
    private String idNumber;

	/* user与设备用户中间表 */
	/*FetchType change from FetchType.EAGER to FetchType.LAZY*/
//    @OneToMany(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY,mappedBy = "spmsUser")
//    @JsonIgnore
//	private List<SpmsUserAc> spmsUserAcs;

	@ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinColumn(name="spmsWorkOrder_id",foreignKey = @ForeignKey(name="none"))
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private SpmsWorkOrder spmsWorkOrder;

	@Transient
	private Integer productSize;

	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static Integer getUserTypeTrial() {
		return USER_TYPE_TRIAL;
	}
	public static Integer getUserTypeBusiness() {
		return USER_TYPE_BUSINESS;
	}
	public static String getDelFlagNormal() {
		return DEL_FLAG_NORMAL;
	}
	public static String getDelFlagDelete() {
		return DEL_FLAG_DELETE;
	}

	public SpmsWorkOrder getSpmsWorkOrder() {
		return spmsWorkOrder;
	}

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getProductSize() {
		return productSize;
	}

	public void setProductSize(Integer productSize) {
		this.productSize = productSize;
	}

	public void setSpmsWorkOrder(SpmsWorkOrder spmsWorkOrder) {
		this.spmsWorkOrder = spmsWorkOrder;
	}

	/* 删除标记（0：正常；1：删除；2：审核；） */
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";

}