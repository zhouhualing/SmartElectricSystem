package com.harmazing.spms.workorder.dto;

import com.harmazing.spms.base.dto.WorkFlowCommonDTO;
import com.harmazing.spms.workflow.dto.WorkFlowDTO;

/**
 * Created by zcp on 2015/2/3.
 */
public class SpmsPhoneVisitDTO extends WorkFlowCommonDTO {

    private String info;

    private Integer infoType;

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
}
