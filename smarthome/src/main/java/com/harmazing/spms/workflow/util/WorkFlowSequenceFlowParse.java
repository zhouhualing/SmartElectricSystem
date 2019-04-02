package com.harmazing.spms.workflow.util;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.SequenceFlowParseHandler;

public class WorkFlowSequenceFlowParse extends SequenceFlowParseHandler{

	@Override
	protected void executeParse(BpmnParse bpmnParse, SequenceFlow sequenceFlow) {
		sequenceFlow.setConditionExpression("${ACTION==\""+sequenceFlow.getId()+"\"}");
		super.executeParse(bpmnParse, sequenceFlow);
	}
}
