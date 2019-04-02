package com.harmazing.spms.product.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.product.entity.SpmsProductType;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014年12月31日
 */
@Repository("spmsProductTypeDAO")
public interface  SpmsProductTypeDAO extends SpringDataDAO<SpmsProductType>{
    //逻辑删
    @Modifying
    @Query("update SpmsProductType t set t.deleteStauts = 1 where t.id in :ids")
    
    public void doDeleteProductTypes(@Param("ids")List <String> ids);
    
    @Query("from SpmsProductType where deleteStauts != 1")
    public List<SpmsProductType> getAll();
	
}
