package com.harmazing.spms.desktop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.desktop.entity.SpmsAccountBill;
import com.harmazing.spms.device.entity.*;


/**
 * billDAO
 * @author hanhao
 * @version v1.0
 */
@Repository("spmsAccountBillDAO")
public interface SpmsAccountBillDAO extends SpringDataDAO<SpmsAccountBill> {
	
	@Query(nativeQuery=true, value="from SpmsAccountBill where spmsUser.id=:userId")
	public List<SpmsAccountBill> findAllByUser(@Param("userId") String userId);
}
