package com.harmazing.spms.base.manager;

import com.harmazing.spms.common.manager.IManager;

public interface CodeGeneraterManager extends IManager {
	
	public String getCode(Integer type);
}
