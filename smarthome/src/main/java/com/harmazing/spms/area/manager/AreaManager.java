/**
 * 
 */
package com.harmazing.spms.area.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.harmazing.spms.area.dto.AreaDTO;
import com.harmazing.spms.area.dto.AreaTreeDTO;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.base.dto.QueryTranDTO;
import com.harmazing.spms.common.manager.IManager;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014年12月31日
 */
public interface AreaManager extends IManager {
    
    /**
     * save device
     * @param spmsDeviceDTO
     * @return
     */
    public AreaDTO doSave(AreaDTO areaDTO);
    
    public Map<String,Object> doDelete(String id);
    
    public Area findById(String id);
    
    public AreaDTO getById(String id);
    
    public Integer hasName(String myParent, String myName);
    
    /**
     * get first level area
     * @param areaDTO
     * @return List<AreaTreeDTO>
     */
    public List<AreaTreeDTO> findFirstArea(AreaTreeDTO areaTreeDTO); 
    
    /**
     * 查找子节点
     * @param areaTreeDTO
     * @return
     */
    public List<AreaTreeDTO> findChildrenByParent(AreaTreeDTO areaTreeDTO);
    
    /**
     * 获取区域中最大的sort
     * @return
     */
    public Integer getMaxSort();

    /**
     * 获取区域树。
     * @param queryTranDTO
     * @return QueryTranDTO
     * @throws IOException
     */
    public QueryTranDTO getAreaTree(QueryTranDTO queryTranDTO) throws IOException;
    
}
