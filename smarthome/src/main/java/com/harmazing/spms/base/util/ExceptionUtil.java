package com.harmazing.spms.base.util;

import com.harmazing.spms.base.exception.WorkFlowConfigException;
import com.harmazing.spms.common.log.ILog;

import java.util.logging.Logger;

/**
 * Created by zcp on 2015/1/20.
 */
public class ExceptionUtil implements ILog{

    public static WorkFlowConfigException throwWorkFlowConfigException(String message) {
        logger.error(message);
        throw new WorkFlowConfigException(message);
    }
}
