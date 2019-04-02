package com.harmazing.spms.log.manager;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.log.dto.SpmsLogDTO;

public interface SpmsLogManager extends IManager{

	public SpmsLogDTO doSave(SpmsLogDTO spmsLogDTO);

	public SpmsLogDTO getDetail(String id);

	
	
}
