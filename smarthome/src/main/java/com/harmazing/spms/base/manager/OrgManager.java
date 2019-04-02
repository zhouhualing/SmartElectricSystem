/**
 * 
 */
package com.harmazing.spms.base.manager;

import java.util.List;

import com.harmazing.spms.base.dto.TreeDTO;
import com.harmazing.spms.common.manager.IManager;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月7日
 */
public interface OrgManager extends IManager {
    
    /**
     * get org tree
     * 1.treeDTO pId is null and id is null getFirstLevelNode.
     * 2.treeDTO pId is null and id is not null get id.
     * 3.treeDTO pid is not null get the children of pid.
     * @param treeDTO
     * @return List<TreeDTO>
     */
    public List<TreeDTO> getOrgTree(TreeDTO treeDTO);
}
