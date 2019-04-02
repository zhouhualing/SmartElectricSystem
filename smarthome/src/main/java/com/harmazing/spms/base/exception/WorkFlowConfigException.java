/**
 * 
 */
package com.harmazing.spms.base.exception;

import com.harmazing.spms.common.exception.IException;
import com.harmazing.spms.common.log.ILog;

public class WorkFlowConfigException extends RuntimeException implements IException,ILog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public WorkFlowConfigException(String message) {
	    super(message);
    }

}
