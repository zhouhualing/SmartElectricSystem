package com.harmazing.spms.base.exception;

import com.harmazing.spms.base.dto.QueryDTO;
import com.harmazing.spms.common.exception.IException;
import com.harmazing.spms.common.log.ILog;

public class QueryException extends RuntimeException implements IException, ILog {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * exception construction function
	 * @param msg
	 * @param throwable
	 */
	public QueryException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
	/**
	 * exception construction function
	 * @param queryDTO
	 * @param throwable
	 */
	public QueryException(QueryDTO queryDTO, Throwable throwable) {
		super("queryId:"+queryDTO.getQueryId()+" and baseClass:"+queryDTO.getBaseClass() +"  query Error.", throwable);
	}
}
