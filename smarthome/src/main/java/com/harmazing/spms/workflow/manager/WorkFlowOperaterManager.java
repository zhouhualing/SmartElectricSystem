package com.harmazing.spms.workflow.manager;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.workflow.dto.WorkFlowOperaterDTO;
import com.harmazing.spms.workflow.entity.WorkFlowOperaterEntity;

public interface WorkFlowOperaterManager extends IManager{
	
	public List<WorkFlowOperaterEntity> getWorkFlowOperaterByWorkFLowTypeId(String workFlowTypeId);
	
	public List<WorkFlowOperaterEntity> getWorkFlowOperaterBySearchFilter(Specification <WorkFlowOperaterEntity> specification);
	
	public WorkFlowOperaterEntity doSaveOperater(WorkFlowOperaterDTO workFlowOperaterDTO);
}
