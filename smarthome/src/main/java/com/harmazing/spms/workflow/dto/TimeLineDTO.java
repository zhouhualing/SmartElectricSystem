package com.harmazing.spms.workflow.dto;

import com.harmazing.spms.common.dto.CommonDTO;

import java.util.List;

/**
 * Created by zcp on 2015/1/20.
 * 工作流时间线
 */
public class TimeLineDTO extends CommonDTO{

    private List<TimeLineNodeDTO> timeLineNodes;

    private String competingNodeId;

    public List<TimeLineNodeDTO> getTimeLineNodes() {
        return timeLineNodes;
    }

    public void setTimeLineNodes(List<TimeLineNodeDTO> timeLineNodes) {
        this.timeLineNodes = timeLineNodes;
    }

    public String getCompetingNodeId() {
        return competingNodeId;
    }

    public void setCompetingNodeId(String competingNodeId) {
        this.competingNodeId = competingNodeId;
    }
}
