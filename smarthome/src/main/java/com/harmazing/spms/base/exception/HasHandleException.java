/**
 * 
 */
package com.harmazing.spms.base.exception;

import com.harmazing.spms.common.exception.IException;
import com.harmazing.spms.common.log.ILog;

public class HasHandleException extends RuntimeException implements IException, ILog {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    public HasHandleException(String message) {
	super(message);
    }

}
