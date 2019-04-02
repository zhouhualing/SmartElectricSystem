/**
 * 
 */
package com.harmazing.spms.base.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harmazing.spms.common.entity.CommonEntity;

@Entity
@Table(name = "tb_user_role")
public class RoleEntity extends CommonEntity {
    
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * role code
     */
    @Column(length=50)
    @NotNull
    private String roleCode;
    
    /**
     * role name
     */
    @Column(length=100)
    private String roleName;
    
    /**
     * 对应的用户
     */
    @ManyToMany(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",joinColumns = {@JoinColumn(name = "roleentities_id")},inverseJoinColumns = {@JoinColumn(name = "userentities_id")})
    @JsonIgnore
    private List <UserEntity> userEntities;

    /**
     * 对应的权限
     */
    @ManyToMany(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinTable(name = "role_permission",joinColumns = {@JoinColumn(name = "roleentities_id")},inverseJoinColumns = {@JoinColumn(name = "permissionentities_id")})
    @JsonIgnore
    private List <PermissionEntity> permissionEntities;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(List<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public List<PermissionEntity> getPermissionEntities() {
        return permissionEntities;
    }

    public void setPermissionEntities(List<PermissionEntity> permissionEntities) {
        this.permissionEntities = permissionEntities;
    }

}
