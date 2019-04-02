/**
 * 
 */
package com.harmazing.spms.workflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.harmazing.spms.common.entity.CommonEntity;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014-07-07
 */
@Entity
@Table(name="tb_workflow_status")
public class WorkFlowStatusEntity extends CommonEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    private String processInstanceId;
    
    /**
     * 0001 完成 0002 
     */
    @Column(length=4)
    private String flag;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
