package com.harmazing.spms.workflow.dao;

import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.workflow.entity.WorkFlowOperaterGroupEntity;

@Repository("workFlowOperaterGroupDAO")
public interface WorkFlowOperaterGroupDAO extends SpringDataDAO<WorkFlowOperaterGroupEntity> {

}
