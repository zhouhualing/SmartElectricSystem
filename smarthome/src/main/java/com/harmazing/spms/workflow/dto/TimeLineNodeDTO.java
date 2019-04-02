package com.harmazing.spms.workflow.dto;

import com.google.common.collect.Lists;
import com.harmazing.spms.common.dto.CommonDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by zcp on 2015/1/20.
 * 工作流时间线节点
 */
public class TimeLineNodeDTO extends CommonDTO {

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 是否已经完成
     */
    private boolean completed;

    /**
     * 节点类型 0用户任务 1开始 2结束
     */
    private int nodeType;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 任务节点信息
     */
    private List<Map<String,Object>> taskInfos = Lists.newArrayList();

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public List<Map<String, Object>> getTaskInfos() {
        return taskInfos;
    }

    public void setTaskInfos(List<Map<String, Object>> taskInfos) {
        this.taskInfos = taskInfos;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
