package com.harmazing.spms.workorder.manager;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.workorder.dto.SpmsPhoneVisitDTO;

/**
 * Created by zcp on 2015/2/3.
 */
public interface SpmsPhoneVisitManager extends IManager {

    /**
     * 保存信息
     * @param spmsPhoneVisitDTO
     * @return SpmsPhoneVisitDTO
     */
    public SpmsPhoneVisitDTO doSave(SpmsPhoneVisitDTO spmsPhoneVisitDTO);
}
