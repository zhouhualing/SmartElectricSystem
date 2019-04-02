/**
 * 
 */
package com.harmazing.spms.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsAirCondition;
import com.harmazing.spms.user.entity.SpmsUdGroup;
import com.harmazing.spms.user.entity.SpmsUser;
import com.harmazing.spms.user.entity.UdGroupEntity;

/**
 * describe:
 * @author Hualing
 * since 2014年10月4日
 */
@Repository("spmsUdGroupDAO")
public interface SpmsUdGroupDAO extends SpringDataDAO<SpmsUdGroup> {
	@Query(nativeQuery=true, value="select * from spms_ud_group where ud_group_id=:groupId")
	public List<SpmsUdGroup> findByGroupId(@Param("groupId") String groupId);
	
	@Query(nativeQuery=true, value="select * from spms_ud_group where ud_id=:udId")
	public List<SpmsUdGroup> findByUdId(@Param("udId") String udId);
	
	@Query(nativeQuery=true, value="select * from spms_ud_group where ud_id=:udId and ud_group_id=:groupId")
	public SpmsUdGroup findByUdIdAndGroupId(@Param("udId") String udId,@Param("groupId") String groupId);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_ud_group where ud_group_id=:groupId")
	public void deletebyGroupId(@Param("groupId") String groupId);
	
	@Modifying
	@Query(nativeQuery=true, value="delete from spms_ud_group where ud_id=:udId")
	public void deletebyUdId(@Param("udId") String udId);
}
