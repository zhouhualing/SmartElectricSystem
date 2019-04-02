/**
 * 
 */
package com.harmazing.spms.workorder.manager;

import java.util.List;
import java.util.Map;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.workorder.dto.SpmsWorkOrderDTO;
import com.harmazing.spms.workorder.entity.SpmsWorkOrder;
import org.springframework.data.jpa.domain.Specification;

/**
 * describe:
 * @author yyx
 * since 2014年12月31日
 */
public interface SpmsWorkOrderManager extends IManager {
    
    /**
     * save workorder
     * @param SpmsWorkOrderDTO
     * @return
     */
    public SpmsWorkOrderDTO doSave(SpmsWorkOrderDTO spmsWorkOrderDTO);
    
    public SpmsWorkOrderDTO getById(String id);
    
    public Map<String,Object> doDelete(String id);
    
    public Map<String,Object> batchDelete(String ids);
    
    public Map<String,Object> validateUser(String processInstanceId);

    public List<SpmsWorkOrder> findBySpe(Specification Specification);

    public SpmsWorkOrderDTO doModifyDelete(SpmsWorkOrderDTO spmsWorkOrderDTO);
}
