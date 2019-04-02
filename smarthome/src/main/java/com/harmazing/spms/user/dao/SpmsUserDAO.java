package com.harmazing.spms.user.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.user.entity.SpmsUser;

import java.util.List;
import java.util.Map;

@Repository("spmsUserDAO")
public interface SpmsUserDAO extends SpringDataDAO<SpmsUser> {
	
	@Query("from SpmsUser where mobile=:p1")
	public SpmsUser getByMobile(@Param("p1") String mobile);

	@Query("from SpmsUser where idNumber=:p1")
	public SpmsUser getByIdNumber(@Param("p1") String idNumber);
	
	@Query("from SpmsUser where ammeter=:p1")
	public SpmsUser getByAmmeter(@Param("p1") String ammeter);
	
	@Query("from SpmsUser where email=:p1")
	public SpmsUser getByEmail(@Param("p1") String email);
	
//	@Query("from SpmsUser su where su.spmsDevice.id=:p1")
//	public SpmsUser findByGw(@Param("p1") String gwid);

//	@Query(nativeQuery = true,value="SELECT"+
//			" 	t1.id id,"+
//			" 	count(*) size"+
//			" FROM"+
//			" 	spms_user t1"+
//			" JOIN spms_product t2 ON t1.id = t2.user_id"+
//			" WHERE"+
//			" 	t1.id IN :userIds"+
//			" GROUP BY"+
//			" 	t1.id")
//	public List<Object[]> findProductSizeByUserIds(@Param("userIds") List<String> userIds);
	
	/**
	 * 统计订购产品
	 * @param userIds
	 * @return
	 */
//	@Query(nativeQuery = true,value="SELECT"+
//			" 	t1.id id,"+
//			" 	count(*) size"+
//			" FROM"+
//			" 	spms_user t1"+
//			" JOIN spms_product t2 ON t1.id = t2.user_id"+
//			" WHERE"+
//			" 	t1.id IN :userIds and t2.status in ('1','2')"+
//			" GROUP BY"+
//			" 	t1.id")
//	public List<Object[]> findProductSizeByUserIdsAndStatus(@Param("userIds") List<String> userIds);
	
	@Query(nativeQuery = true,value="select id from spms_user")
	public List<String> findAllUser();
	
	@Query(nativeQuery=true, value="select * from spms_user where (mobile =:mobile or email =:email or idNumber =:idNumber)")
	public List<SpmsUser> findByMoblieAndEmailAndIdNumber(@Param("mobile")String mobile,@Param("email")
			String email,@Param("idNumber") String idNumber);
	
}
