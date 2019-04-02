package com.harmazing.spms.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.user.entity.*;

@Repository("userProductBindingDAO")
public interface SpmsUserProductBindingDAO  extends SpringDataDAO<SpmsUserProductBinding> {
	
//	@Query("from SpmsUserProductBinding where spmsUser.id=:p1")
//	public List<SpmsUserProductBinding> getListByUserId(@Param("p1") String userId);
//	@Query("from SpmsUserProductBinding where spmsDevice.id=:p1 or gwid=:p1")
//	public List<SpmsUserProductBinding> findByDevice(@Param("p1") String deviceId);
//	@Query("from SpmsUserProductBinding where spmsUser.id=:p1 and spmsProduct.id=:p2")
//	public List<SpmsUserProductBinding> getUserBinding(@Param("p1")String userId,@Param("p2")String productId);
//	@Query("from SpmsUserProductBinding where spmsUser.id=:userId")
//	public List<SpmsUserProductBinding> getBindDeviceByUserId(@Param("userId")String userId);
	
//	@Modifying
//	@Query("delete from SpmsUserProductBinding supb where supb.spmsDevice.id=:deviceId")
//	public void cancelDeviceBind(@Param("deviceId")String deviceId);
	
}
