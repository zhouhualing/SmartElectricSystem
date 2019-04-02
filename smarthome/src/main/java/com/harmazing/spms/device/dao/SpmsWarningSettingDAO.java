/**
 * 
 */
package com.harmazing.spms.device.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.device.entity.SpmsWarningSetting;
import com.harmazing.spms.usersRairconSetting.entity.RairconSetting;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月10日
 */
@Repository("spmsWarningSettingDAO")
public interface SpmsWarningSettingDAO extends SpringDataDAO<SpmsWarningSetting> {

	@Query("from SpmsWarningSetting where id=:p1")
	public SpmsWarningSetting FindOneObject(@Param("p1")String id);
}
