package com.harmazing.spms.base.exception;

import com.harmazing.spms.common.exception.IException;
import com.harmazing.spms.common.log.ILog;

/**
 * 加密解密算法错误
 * @author george
 *
 */
public class DesException extends RuntimeException implements IException, ILog {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

	public DesException(String message) {
		super(message);
	}
    
}
