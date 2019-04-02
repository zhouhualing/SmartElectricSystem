/**
 * 
 */
package com.harmazing.spms.base.exception;

import com.harmazing.spms.common.exception.IException;
import com.harmazing.spms.common.log.ILog;

public class LoginFailtureException  extends RuntimeException implements IException, ILog{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    public LoginFailtureException(String message) {
	super(message);
    }

}
