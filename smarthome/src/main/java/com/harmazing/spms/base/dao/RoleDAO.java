/**
 * 
 */
package com.harmazing.spms.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.base.entity.RoleEntity;
import com.harmazing.spms.common.dao.SpringDataDAO;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月5日
 */
@Repository("roleDAO")
public interface RoleDAO extends SpringDataDAO<RoleEntity> {
    
    @Query(nativeQuery=true,value="SELECT"+
	    " 	t1.id userId,"+
	    " 	t1.userCode,"+
	    " 	t1.userName,"+
	    " 	t3.id,"+
	    " 	t3.roleCode,"+
	    " 	t3.roleName"+
	    " FROM"+
	    " 	tb_user_user t1"+
	    " JOIN user_role t2 ON t1.id = t2.userentities_id"+
	    " JOIN tb_user_role t3 ON t2.roleentities_id = t3.id"+
	    " WHERE"+
	    " 	t1.userCode = :userCode"+
	    " AND t3.roleCode IN :roleCodes")
    public List<Object[]> findByUserCodeAndINRoleCodes(@Param("userCode")String userCode, @Param("roleCodes")List<String> roleCodes);

	@Query(nativeQuery=true,value="SELECT"+
			" 	t1.id userId,"+
			" 	t1.userCode,"+
			" 	t1.userName,"+
			" 	t3.id,"+
			" 	t3.roleCode,"+
			" 	t3.roleName"+
			" FROM"+
			" 	tb_user_user t1"+
			" JOIN user_role t2 ON t1.id = t2.userentities_id"+
			" JOIN tb_user_role t3 ON t2.roleentities_id = t3.id"+
			" WHERE t3.roleCode IN :roleCodes")
	public List<Object[]> findUserInfoByRoleCodes(@Param("roleCodes")List<String> roleCodes);
}
