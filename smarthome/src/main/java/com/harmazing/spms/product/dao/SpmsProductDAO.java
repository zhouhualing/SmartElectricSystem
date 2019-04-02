/**
 * 
 */
package com.harmazing.spms.product.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.product.entity.SpmsProduct;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014年12月31日
 */
@Repository("spmsProductDAO")
public interface SpmsProductDAO extends SpringDataDAO<SpmsProduct>{
	
	@Query("from SpmsProduct where spmsUser.id=:p1")
	public List<SpmsProduct> getSpmsProductByUserid(@Param("p1")String userid);

	@Query("from SpmsProduct where spmsUser.id=:p1 and status = 1")
	public List<SpmsProduct> getSpmsProductByUseridAndStatus(@Param("p1")String userid);
}
