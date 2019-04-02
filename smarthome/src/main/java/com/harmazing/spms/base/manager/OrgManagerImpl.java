/**
 * 
 */
package com.harmazing.spms.base.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.harmazing.spms.base.dao.OrgDAO;
import com.harmazing.spms.base.dto.TreeDTO;
import com.harmazing.spms.base.entity.OrgEntity;
import com.harmazing.spms.base.manager.OrgManager;
import com.harmazing.spms.base.util.DynamicSpecifications;
import com.harmazing.spms.base.util.SearchFilter;
import com.harmazing.spms.base.util.SearchFilter.Operator;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月7日
 */
@Service
public class OrgManagerImpl implements OrgManager{
    
    @Autowired
    private OrgDAO orgDAO;
    
    /* (non-Javadoc)
     * @see com.harmazing.spms.base.manager.OrgManager#getOrgTree(com.harmazing.spms.base.dto.TreeDTO)
     */
    public List<TreeDTO> getOrgTree(TreeDTO treeDTO) {
	List <OrgEntity> orgEntities = null;
	if(treeDTO.getpId() == null) {
	    if(treeDTO.getId() == null) {
		orgEntities = orgDAO.findAll(DynamicSpecifications.bySearchFilter(new SearchFilter("parent.id",Operator.ISNULL,"isnull"), OrgEntity.class));
	    } else {
		orgEntities = Lists.newArrayList();
		orgEntities.add(orgDAO.findOne(treeDTO.getId()));
	    }
	} else {
	    orgEntities = orgDAO.findAll(DynamicSpecifications.bySearchFilter(new SearchFilter("parent.id",treeDTO.getpId()), OrgEntity.class));
	}
	
	List <TreeDTO> treeDTOs = Lists.newArrayList();
	for(OrgEntity orgEntity : orgEntities) {
	    TreeDTO tempTreeDTO = new TreeDTO();
	    tempTreeDTO.setId(orgEntity.getId());
	    tempTreeDTO.setName(orgEntity.getName());
	    tempTreeDTO.setCode(orgEntity.getCode());
	    if(orgEntity.getChildList().size() >0) {
		tempTreeDTO.setIsParent(true);
	    }
	    treeDTOs.add(tempTreeDTO);
	}
	return treeDTOs;
    }
}
