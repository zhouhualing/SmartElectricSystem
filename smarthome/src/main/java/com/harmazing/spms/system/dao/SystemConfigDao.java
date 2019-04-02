package com.harmazing.spms.system.dao;

import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.system.entity.SystemConfig;

@Repository("systemConfigDao")
public interface SystemConfigDao extends SpringDataDAO<SystemConfig>{
	
}
