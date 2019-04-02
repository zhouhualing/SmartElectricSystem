/**
 * 
 */
package com.harmazing.spms.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.base.entity.PermissionEntity;
import com.harmazing.spms.common.dao.SpringDataDAO;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月2日
 */
@Repository("permissionDAO")
public interface PermissionDAO extends SpringDataDAO<PermissionEntity> {
    
    @Query(nativeQuery=true,value= "SELECT"+
	    "		t4.*"+
	    "	FROM"+
	    "		tb_user_user t1"+
	    "	JOIN user_role t2 on t1.id = t2.userentities_id"+
	    "	JOIN role_permission t3 ON t2.roleentities_id = t3.roleentities_id"+
	    "	JOIN tb_user_permission t4 ON t3.permissionentities_id = t4.id"+
	    " WHERE"+
	    "	t4.parentPermissionEntity_id IS NULL"+
	    " AND t1.userCode = :userCode order by t4.sort")
    public List<PermissionEntity> getMenuByParentIdNull(@Param("userCode") String userCode);
    
    public PermissionEntity findByPermissionCode(String code);
}
