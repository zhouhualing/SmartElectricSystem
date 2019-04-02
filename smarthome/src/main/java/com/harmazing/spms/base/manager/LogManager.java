package com.harmazing.spms.base.manager;

import com.harmazing.spms.base.entity.LogEntity;
import com.harmazing.spms.common.manager.IManager;

/**
 *LogManager.java 
 *@author Zhaocaipeng
 * since 2014年3月13日 上午9:20:37
 */
public interface LogManager  extends IManager {
	
	public LogEntity doSaveLog(LogEntity logEntity);
}
