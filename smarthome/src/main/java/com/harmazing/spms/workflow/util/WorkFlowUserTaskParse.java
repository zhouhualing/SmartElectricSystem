package com.harmazing.spms.workflow.util;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;

import com.harmazing.spms.base.util.DynamicSpecifications;
import com.harmazing.spms.base.util.SearchFilter;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.workflow.entity.WorkFlowOperaterEntity;
import com.harmazing.spms.workflow.manager.WorkFlowOperaterManager;

public class WorkFlowUserTaskParse extends UserTaskParseHandler{

	@Override
	protected void executeParse(BpmnParse bpmnParse, UserTask userTask) {
		
		WorkFlowOperaterManager workFlowOperaterManager = SpringUtil.getBeanByName("workFlowOperaterManager");
		List <SearchFilter> searchFilters = new ArrayList<SearchFilter>();
		searchFilters.add(new SearchFilter("workFlowTypeId", bpmnParse.getBpmnModel().getProcesses().get(0).getId()));
		searchFilters.add(new SearchFilter("operatorId",userTask.getId()));
		List <WorkFlowOperaterEntity> workFlowOperaterEntities =  workFlowOperaterManager.getWorkFlowOperaterBySearchFilter(DynamicSpecifications.bySearchFilter(searchFilters, WorkFlowOperaterEntity.class));
		//if multi get the first
		if(workFlowOperaterEntities.size() >= 1) {
			if("0002".equals(workFlowOperaterEntities.get(0).getCountersign())) {
				userTask.setLoopCharacteristics(new MultiInstanceLoopCharacteristics());
				userTask.getLoopCharacteristics().setInputDataItem("${"+WorkFlowCoreUtil.MULTILIST+"}");
				userTask.getLoopCharacteristics().setElementVariable(WorkFlowCoreUtil.MULTIVAR);
				ArrayList<String> groups = new ArrayList<String>();
				groups.add("${"+WorkFlowCoreUtil.MULTIVAR+"}");
				userTask.setCandidateGroups(groups);
//				userTask.setAssignee("${"+WorkFlowCoreUtil.MULTIVAR+"}");
			} else if("0003".equals(workFlowOperaterEntities.get(0).getCountersign())){
				userTask.setLoopCharacteristics(new MultiInstanceLoopCharacteristics());
				userTask.getLoopCharacteristics().setInputDataItem("${"+WorkFlowCoreUtil.MULTILIST+"}");
				userTask.getLoopCharacteristics().setElementVariable(WorkFlowCoreUtil.MULTIVAR);
//				ArrayList<String> groups = new ArrayList<String>();
//				groups.add("${"+WorkFlowCoreUtil.MULTIVAR+"}");
//				userTask.setCandidateGroups(groups);
				userTask.setAssignee("${"+WorkFlowCoreUtil.MULTIVAR+"}");		    	
			}
		}
		super.executeParse(bpmnParse, userTask);	
	}
}
