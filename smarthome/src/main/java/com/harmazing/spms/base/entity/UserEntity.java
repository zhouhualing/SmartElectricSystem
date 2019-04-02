package com.harmazing.spms.base.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.bson.types.Binary;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.type.BlobType;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.common.entity.CommonEntity;

@Entity
@Table(name = "tb_user_user")
public class UserEntity extends CommonEntity{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户名称
	 */
	@Column(name = "userName")
	@Length(min=1, max=100)
	private String userName;
	/**
	 * 用户编号
	 */
	@Column(name = "userCode")
	@Length(min=1, max=100)
	private String userCode;
	/**
	 * 工号
	 */
	@Length(min=1, max=100)
	private String no;
	
	/**
	 * 用户类型（0 管理员，1 普通用户） TODO delete 2 订户） 
	 */
	@Column(name = "userType", columnDefinition="int default 1")
	@NotNull
	private Integer userType=1;
	/**
	 * 密码
	 */
	@Length(min=1, max=100)
	@Column(name = "password")
	private String password;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "expire_time")
	private Long expireTime;
	
	/**
	 * The pwd encrypt id.
	 */
	@ManyToOne(cascade={CascadeType.REFRESH},fetch=FetchType.LAZY)
	@JoinColumn(name="pwd_type_id")
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private ArithmeticEntity encryptArithmetic;

	private Integer sort;
	
	@Length(min=0, max=10)
	private String sex = "男";
	
	@Length(min=0, max=200)
	private String birthday;
	
	@Length(min=0, max=200)
	private String address;
	
	/**
	 * 电话
	 */
	@Length(min=0, max=200)
	@Column(name = "phone_Number")
	private String phoneNumber;
	/**
	 * 手机
	 */
	@Length(min=0, max=200)
	@Column(name = "mobile")
	private String mobile;	
	
	/**
	 * 手机
	 */
	@Length(min=0, max=200)
	@Column(name = "mobile_mac")
	private String mobileMac;	
	/**
	 * 电子邮箱
	 */
	@Email @Length(min=0, max=200)
	private String email;
	
	/**
	 * 昵称
	 */
	private String nickname;
	
	/**
	 * 头像
	 */
//	@Lob @Basic(fetch=FetchType.LAZY) 
//    private Byte[] image;

	/**
	 * 数据字典【dict_user_status】 用户状态 值：【0正常、1删除、2冻结】
	 */
	@Column(name = "status")
	private Integer status = 0;
	
	/**
	 * 角色
	 */
	@ManyToMany(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinTable(name = "user_role",joinColumns = {@JoinColumn(name = "userentities_id")},inverseJoinColumns = {@JoinColumn(name = "roleentities_id")})
	@JsonIgnore
	private List<RoleEntity> roleEntities;
	/**
	 * 权限
	 */
	@ManyToMany(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
	@JoinTable(name = "user_permission",joinColumns = {@JoinColumn(name = "userentities_id")},inverseJoinColumns = {@JoinColumn(name = "permissionentities_id")})
	@JsonIgnore
	private List<PermissionEntity> permissionEntities;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="org_id")
	@JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
	private OrgEntity orgEntity;
	
	@ManyToOne
	@JsonIgnore
	private OrgEntity companyEntity;
	
	@Column(name="mark_",length=4000)
	private String mark;
	
	@Length(min=0, max=50)
	private String loginIp;	// 最后登陆IP
	    
	private Date loginDate;	// 最后登陆日期
	
	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public OrgEntity getCompanyEntity() {
	    return companyEntity;
	}

	public void setCompanyEntity(OrgEntity companyEntity) {
	    this.companyEntity = companyEntity;
	}

	public String getMark() {
	    return mark;
	}

	public void setMark(String mark) {
	    this.mark = mark;
	}

	public String getUserCode(){
		return userCode;
	}

	public void setUserCode(String userCode){
		this.userCode = userCode;
	}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public static long getSerialversionuid(){
		return serialVersionUID;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public List<RoleEntity> getRoleEntities() {
	    return roleEntities;
	}

	public void setRoleEntities(List<RoleEntity> roleEntities) {
	    this.roleEntities = roleEntities;
	}

	public List<PermissionEntity> getPermissionEntities() {
	    return permissionEntities;
	}

	public void setPermissionEntities(List<PermissionEntity> permissionEntities) {
	    this.permissionEntities = permissionEntities;
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

	/**
	 * @return the mobilePhone
	 */
	public String getMobile(){
		return mobile;
	}

	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public void setAddress(String address){
		this.address = address;
	}

	/**
	 * @return the mobilePhone
	 */
	public String getAddress(){
		return address;
	}

	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public void setMobile(String mobile){
		this.mobile = mobile;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail(){
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}	

//	public Byte[] getImage() {
//		return image;
//	}
//
//	public void setImage(Byte[] image) {
//		this.image = image;
//	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}	
	
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getMobileMac() {
		return mobileMac;
	}

	public void setMobileMac(String mobileMac) {
		this.mobileMac = mobileMac;
	}

	public Integer getSort() {
		return sort;
	}
	
	public void setSort(Integer sort) {
		this.sort = sort;
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

	public OrgEntity getOrgEntity() {
	    return orgEntity;
	}

	public void setOrgEntity(OrgEntity orgEntity) {
	    this.orgEntity = orgEntity;
	}
	
	public ArithmeticEntity getEncryptArithmetic() {
		return encryptArithmetic;
	}

	public void setEncryptArithmetic(ArithmeticEntity encryptArithmetic) {
		this.encryptArithmetic = encryptArithmetic;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj ==null || !(obj instanceof UserEntity)) {
			return false;
		}
		
		if(this.getId() == null) {
			return false;
		}
		if(this.getId().equals(((UserEntity)obj).getId())) {
			return true;
		} else {
			return false;
		}
	}
	public static String DEFAULT_PWD = "123456";
}
