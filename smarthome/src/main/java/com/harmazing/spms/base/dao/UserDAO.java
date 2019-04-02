/**
 * 
 */
package com.harmazing.spms.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.user.entity.SpmsUser;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014年10月4日
 */
@Repository("userDAO")
public interface UserDAO extends SpringDataDAO<UserEntity> {
    
    public UserEntity findByUserCode(String code);
 
//    物理删
//    @Modifying
//    @Query("delete from UserEntity t where t.id in :ids")
//    public void doDeleteUsers(@Param("ids")List <Long> ids);
    
    //逻辑删
    @Modifying
    @Query("update UserEntity t set t.status = 2 where t.id in :ids")
    public void doDeleteUsers(@Param("ids")List <String> ids);
    
    @Query("from UserEntity t where t.userCode =:p1")
    public UserEntity getByUserCode(@Param("p1")String usercode);
    
	@Query("from UserEntity t where t.mobile=:p1")
	public UserEntity getByMobile(@Param("p1") String mobile);
	
	@Query("select max(userCode) from UserEntity")
	public String getMaxUserCode();    
}
