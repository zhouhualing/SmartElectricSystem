/**
 * 
 */
package com.harmazing.spms.base.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.common.entity.CommonEntity;

@Entity
@Table(name = "tb_user_permission")
public class PermissionEntity extends CommonEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    @Column(length=50)
    @NotNull
    //TODO
    private String permissionCode;
    
    @Column(length=100)
    private String permissionName;
    
    /**
     * 字典：DICT_PERMISSION_TYPE
     * 权限类别
     * 1 一级直接访问菜单
     * 2 一级有子菜单菜单
     * 3 一级菜单的子菜单 
     */
    private Integer permissionType;
	
    /**
     * 权限对应的url
     */
    @Column(length=150)
    private String url;

	
    /**
     * 父权限
     */
    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
    @JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
    private PermissionEntity parentPermissionEntity;
    
    @Column(length=2000)
    private String parentPermissionPath;
	
    /**
     * 对应的用户
     */
    @ManyToMany(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
    @JoinTable(name="user_permission",joinColumns={@JoinColumn(name="permissionentities_id")},inverseJoinColumns={@JoinColumn(name="userentities_id")})
    @JsonIgnore
    private Set<UserEntity> userEntities;

    /**
     * 对应的角色
     */
    @ManyToMany(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
    @JoinTable(name="role_permission",joinColumns={@JoinColumn(name="permissionentities_id")},inverseJoinColumns={@JoinColumn(name="roleentities_id")})
    @JsonIgnore
    private Set<RoleEntity> roleEntities;

    /**
     * 排序
     */
    @Column(name="sort")
    private Integer sort;

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public String getParentPermissionPath() {
        return parentPermissionPath;
    }

    public void setParentPermissionPath(String parentPermissionPath) {
        this.parentPermissionPath = parentPermissionPath;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PermissionEntity getParentPermissionEntity() {
        return parentPermissionEntity;
    }

    public void setParentPermissionEntity(PermissionEntity parentPermissionEntity) {
        this.parentPermissionEntity = parentPermissionEntity;
    }

    public Set<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(Set<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public Set<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(Set<RoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }    
    
}
