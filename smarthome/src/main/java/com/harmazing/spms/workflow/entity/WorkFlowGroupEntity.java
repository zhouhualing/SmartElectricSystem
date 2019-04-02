/**
 * 
 */
package com.harmazing.spms.workflow.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.harmazing.spms.base.entity.RoleEntity;
import com.harmazing.spms.common.entity.CommonEntity;

/**
 * describe:the group of user for workflow.
 * @author Zhaocaipeng
 * since 2014-06-19
 */
@Entity
@Table(name="tb_workflow_group")
public class WorkFlowGroupEntity extends CommonEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * group name
     */
    private String groupName;
    
    /**
     * group code
     */
    private String groupCode;
    
    private Integer sort;
    
    /**
     * the user of group
     */
    @ManyToMany(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
    @JoinTable(name="workflowgroup_role")
    private List <RoleEntity> roleEntities;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public List<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(List<RoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
