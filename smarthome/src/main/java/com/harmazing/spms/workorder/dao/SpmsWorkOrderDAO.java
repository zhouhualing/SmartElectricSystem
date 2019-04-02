/**
 * 
 */
package com.harmazing.spms.workorder.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.workorder.entity.SpmsWorkOrder;

/**
 * describe:
 * @author 
 * since 2014年12月30日
 */
@Repository("spmsWorkOrderDAO")
public interface SpmsWorkOrderDAO extends SpringDataDAO<SpmsWorkOrder> {
	@Query("from SpmsWorkOrder where idNumber = :p")
	public List<SpmsWorkOrder> findByIdNumber(@Param("p") String idNumber);
	@Query("from SpmsWorkOrder where spmsUserMoblie = :p1 and imp =:p2")
	public List<SpmsWorkOrder> findByMoblieAndImp(@Param("p1") String spmsUserMoblie,@Param("p2")  Integer imp);
    
}	
 