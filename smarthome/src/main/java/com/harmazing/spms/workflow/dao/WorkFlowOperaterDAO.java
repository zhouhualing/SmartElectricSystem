package com.harmazing.spms.workflow.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.workflow.entity.WorkFlowOperaterEntity;

@Repository
public interface WorkFlowOperaterDAO extends SpringDataDAO<WorkFlowOperaterEntity> {

	/**
	 * 根据processInstanceId获取对应的任务节点信息
	 * @return list<Object>
	 */
	@Query(nativeQuery = true,value = "SELECT"+
			"	t1.TASK_DEF_KEY_ AS taskKey,"+
			"	t3.userName AS userName,"+
			"	t1.START_TIME_ AS startTime,"+
			"	t1.DUE_DATE_ AS dueDate,"+
			"	t1.END_TIME_ AS endTime,"+
			"	t4.MESSAGE_ AS message,"+
			"	t1.Id_ AS taskId"+
			" FROM"+
			"	act_hi_taskinst t1"+
			" JOIN act_hi_procinst t2 ON t1.PROC_INST_ID_ = t2.ID_"+
			" left JOIN tb_user_user t3 ON t1.ASSIGNEE_ = t3.userCode"+
			" LEFT JOIN act_hi_comment t4 ON t1.ID_ = t4.TASK_ID_"+
			" AND t4.TYPE_ = '0001' "+
			" where t2.ID_ = :processInstanceId")
	public List<Object> findTaskInfoByProcessInstanceId(@Param("processInstanceId") String processInstanceId);

	/**
	 * 根据角色编号获取对应的所有人信息.
	 * @param RoleCodes
	 * @return
	 */
    @Query(nativeQuery=true,value="SELECT"+
	    	"	t1.id as roleId,"+
	    	"	t1.roleCode as roleCode,"+
	   	 	"	t1.roleName as roleName,"+
	    	"	t3.id as userId,"+
	   	 	"	t3.userCode as userCode,"+
	   	 	"	t3.userName as userName"+
	   	 	" FROM"+
	   	 	"	tb_user_role t1"+
	   	 	" JOIN user_role t2 ON t1.id = t2.roleentities_id"+
	   	 	" JOIN tb_user_user t3 ON t2.userentities_id = t3.id"+
	    	" WHERE"+
	   	 	"	t1.roleCode IN :roleCodes")
    public List <Object> findSelectInfoByRoleCodes(@Param("roleCodes") List <String> RoleCodes);

}
