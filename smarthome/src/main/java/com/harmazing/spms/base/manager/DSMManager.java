/**
 * 
 */
package com.harmazing.spms.base.manager;

import java.util.Map;

import com.harmazing.spms.base.dto.DSMDTO;
import com.harmazing.spms.common.manager.IManager;

public interface DSMManager extends IManager {

	com.harmazing.spms.base.dto.DSMDTO getUserInfo(DSMDTO dSMDTO);

	boolean promiseSet(DSMDTO dSMDTO);

	boolean deleteDSM(Map<String, Object> m);
    
}
