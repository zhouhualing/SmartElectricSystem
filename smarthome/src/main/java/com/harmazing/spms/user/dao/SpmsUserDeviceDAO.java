package com.harmazing.spms.user.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.*;
import com.harmazing.spms.user.entity.SpmsUserDevice;;

@Repository("spmsUserDeviceDAO")
public interface SpmsUserDeviceDAO extends SpringDataDAO<SpmsUserDevice> {
	
	@Query(nativeQuery=true, value="select * from spms_user_device where user_id=:user_id")
	public List<SpmsUserDevice> findByUserId(@Param("user_id") String user_id);
	
	@Query(nativeQuery=true, value="select * from spms_user_device where mobile=:mobile")
	public List<SpmsUserDevice> findByUserMobile(@Param("mobile") String mobile);
	
	@Query(nativeQuery=true, value="select * from spms_user_device where mobile=:mobile and is_primary = :is_primary")
	public List<SpmsUserDevice> findByUserMobile(@Param("mobile") String mobile, @Param("is_primary") Integer is_primary);
	
	@Query(nativeQuery=true, value="select * from spms_user_device where user_id=:user_id and device_id = :device_id")
	public SpmsUserDevice findByUserIdAndDeviceId(@Param("user_id") String user_id,@Param("device_id") String device_id);
	
	@Query(nativeQuery=true, value="select * from spms_user_device where user_id=:user_id and device_id = :device_id and is_primary = :is_primary")
	public SpmsUserDevice findByUserIdAndDeviceId(@Param("user_id") String user_id,@Param("device_id") String device_id,@Param("is_primary") Integer is_primary);
	
	@Query(nativeQuery=true, value="select * from spms_user_device where mobile=:mobile and mac = :mac")
	public SpmsUserDevice findByMM(@Param("mobile") String mobile,@Param("mac") String mac);
	
	@Query(nativeQuery=true, value="select * from spms_user_device where mobile=:mobile and mac = :mac and is_primary = :is_primary")
	public SpmsUserDevice findByMM(@Param("mobile") String mobile,@Param("mac") String mac,@Param("is_primary") Integer is_primary);
	
	@Query(nativeQuery=true, value="select * from spms_user_device where user_id=:user_id and is_primary = :is_primary")
	public List<SpmsUserDevice> findByUserId(@Param("user_id") String user_id, @Param("is_primary") Integer is_primary);
	
	@Query(nativeQuery=true, value="select * from spms_user_device where mac=:mac and is_primary = :is_primary")
	public List<SpmsUserDevice> findByDeviceMac(@Param("mac") String mac,@Param("is_primary") Integer is_primary);	
	
	@Query(nativeQuery=true, value="select * from spms_user_device where device_id=:device_id")
	public List<SpmsUserDevice> findByDeviceId(@Param("device_id") String device_id);
	
	@Query(nativeQuery=true, value="select * from spms_user_device where device_id=:device_id and is_primary = :is_primary")
	public List<SpmsUserDevice> findByDeviceId(@Param("device_id") String device_id,@Param("is_primary") Integer is_primary);
	
	@Query(nativeQuery=true, value="select * from spms_user_device where user_id=:user_id and device_type=2")
	public List<SpmsUserDevice> findAcsByUserId(@Param("user_id") String user_id);
	@Query(nativeQuery=true, value="select * from spms_user_device where user_id=:user_id and device_type=7")
	public List<SpmsUserDevice> findWdsByUserId(@Param("user_id") String user_id);
	@Query(nativeQuery=true, value="select * from spms_user_device where user_id=:user_id and device_type=8")
	public List<SpmsUserDevice> findPirsByUserId(@Param("user_id") String user_id);
	
	@Query(nativeQuery=true, value="select * from spms_user_device where user_id=:user_id and device_type=:device_type")
	public List<SpmsUserDevice> findDevsByUserId(@Param("user_id") String user_id,@Param("device_type") Integer device_type);
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_user_device where device_id=:dev_id")
	public void deletebyDevId(@Param("dev_id") String dev_id);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_user_device where mac=:dev_mac")
	public void deletebyDevMac(@Param("dev_mac") String dev_mac);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_user_device where id=:id")
	public void deletebyId(@Param("id") String id);
	

}	
 