package com.harmazing.spms.product.manager;

import java.util.List;
import java.util.Map;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.product.dto.SpmsProductTypeDTO;
import com.harmazing.spms.product.entity.SpmsProductType;
/**
 * describe:
 * @author TanFan
 * 产品 服务接口类
 * since 2014年12月31日
 */
public interface SpmsProductTypeManager extends IManager {
	 /**
     * save ProductType
     * @param SpmsProductTypeDTO
     * @return  SpmsProductTypeDTO
     */
    public SpmsProductTypeDTO doSave(SpmsProductTypeDTO spmsProductTypeDTO);
    /**
     * Update ProductType
     * @param SpmsProductTypeDTO
     * @return  SpmsProductTypeDTO
     */
    public SpmsProductTypeDTO doUpdate(SpmsProductTypeDTO spmsProductTypeDTO);
    /**
     * Update ProductType
     * @param String
     * @return  SpmsProductTypeDTO
     */
	public SpmsProductTypeDTO doQuery(String id);
    /**
     * Update ProductType
     * @param String
     * @return  Map
     */
	public Map<String, Object>  doDelete(String  id);
    /**
     * Delete  ProductType 批量物理删除
     * @param int[]
     * @return  Boolean
     */
	public Boolean  doDeleteAll(int[] data1);
    /**
     * DTO ProductType
     * @param SpmsProductTypeDTO
     * @return  
     */
	public List<SpmsProductType> doFindAll(int[] data1);
    /**
     * 获取 所有的 SpmsProductType 
     * @param SpmsProductTypeDTO
     * @return  
     */
    public List<SpmsProductTypeDTO> getAll();
    /**
     * 物理批量删除   ProductType
     * @param list 集合 集合里面是ProductType long类型 ID
     * @return  
     */
	public Map<String, Object> deleteProductAll(List<String> ids);
}
