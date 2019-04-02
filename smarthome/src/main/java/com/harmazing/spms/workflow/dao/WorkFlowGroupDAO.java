/**
 * 
 */
package com.harmazing.spms.workflow.dao;

import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.workflow.entity.WorkFlowGroupEntity;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014-06-19
 */
@Repository("workFlowGroupDAO")
public interface WorkFlowGroupDAO extends SpringDataDAO<WorkFlowGroupEntity> {
    
}
