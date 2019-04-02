package com.harmazing.spms.jszc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.jszc.dto.JSZCDTO;
import com.harmazing.spms.jszc.entity.JSZCEntity;
import com.harmazing.spms.user.entity.SpmsUser;


@Repository("jszcDao")
public interface JSZCDao extends SpringDataDAO<JSZCEntity>{
	/**
	 * 获取故障原因字典
	 * @return
	 */
	@Query(nativeQuery=true,value="select code,value from dict_jszc_failurecause order by iorder")
	public List<Object[]> listFailureCause();

	/**
	 * 按taskId获取附加信息
	 * @return
	 */
	@Query(value="from JSZCEntity t where t.taskId = :p")
	public List<JSZCEntity> getTaskVariable(@Param("p")String taskId);
}
