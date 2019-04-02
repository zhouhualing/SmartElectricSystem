package com.harmazing.spms.base.dto;

import com.harmazing.spms.common.dto.CommonDTO;
import com.harmazing.spms.workflow.dto.WorkFlowDTO;

public class WorkFlowCommonDTO extends CommonDTO{


    /**
     * 工作流DTO
     */
    private WorkFlowDTO workFlowDTO = new WorkFlowDTO();

    /**
     * 工单id
     */
    private String spmsWorkOrderId;

    public String getSpmsWorkOrderId() {
        return spmsWorkOrderId;
    }

    public void setSpmsWorkOrderId(String spmsWorkOrderId) {
        this.spmsWorkOrderId = spmsWorkOrderId;
    }

    public WorkFlowDTO getWorkFlowDTO() {

        return workFlowDTO;
    }

    public void setWorkFlowDTO(WorkFlowDTO workFlowDTO) {
        this.workFlowDTO = workFlowDTO;
    }
}
