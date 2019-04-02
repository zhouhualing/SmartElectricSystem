package com.harmazing.spms.workorder.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.harmazing.spms.common.entity.CommonEntity;

import javax.persistence.*;

/**
 * Created by zcp on 2015/2/3.
 */
@Entity
@Table(name="tb_workorder_visit")
public class SpmsPhoneVisit extends CommonEntity {

    /**
     * 回访信息
     */
    @Column(length = 2000)
    private String info;

    /**
     * 满意程度
     * 1非常满意 2满意 3不满意 4其他
     */
    private Integer infoType;

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    @JoinColumn(name="spmsWorkOrder_id",foreignKey = @ForeignKey(name="none"))
    @JsonIgnoreProperties( value={"hibernateLazyInitializer","handler"})
    private SpmsWorkOrder spmsWorkOrder;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getInfoType() {
        return infoType;
    }

    public void setInfoType(Integer infoType) {
        this.infoType = infoType;
    }

    public SpmsWorkOrder getSpmsWorkOrder() {
        return spmsWorkOrder;
    }

    public void setSpmsWorkOrder(SpmsWorkOrder spmsWorkOrder) {
        this.spmsWorkOrder = spmsWorkOrder;
    }
}
