package com.harmazing.spms.base.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.spms.base.dao.LogDAO;
import com.harmazing.spms.base.entity.LogEntity;
import com.harmazing.spms.base.manager.LogManager;

/**
 *LogManagerImpl.java 
 *@author Zhaocaipeng
 * since 2014年3月13日 上午9:21:27
 */
@Service("logManager")
public class LogManagerImpl implements LogManager {

	@Autowired
	private LogDAO logDAO;
	
	@Override
	public LogEntity doSaveLog(LogEntity logEntity) {
		logDAO.save(logEntity);
		return logEntity;
	}

}
