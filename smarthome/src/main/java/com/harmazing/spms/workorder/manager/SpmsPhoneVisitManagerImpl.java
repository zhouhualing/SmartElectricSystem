package com.harmazing.spms.workorder.manager;

import com.harmazing.spms.workorder.dao.SpmsPhoneVisitDAO;
import com.harmazing.spms.workorder.dao.SpmsWorkOrderDAO;
import com.harmazing.spms.workorder.dto.SpmsPhoneVisitDTO;
import com.harmazing.spms.workorder.entity.SpmsPhoneVisit;
import com.harmazing.spms.workorder.entity.SpmsWorkOrder;
import com.harmazing.spms.workorder.manager.SpmsPhoneVisitManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by zcp on 2015/2/3.
 */
@Service("spmsPhoneVisitManager")
public class SpmsPhoneVisitManagerImpl implements SpmsPhoneVisitManager {

    @Autowired
    private SpmsPhoneVisitDAO spmsPhoneVisitDAO;

    @Autowired
    private SpmsWorkOrderDAO spmsWorkOrderDAO;

    @Transactional
    public SpmsPhoneVisitDTO doSave(SpmsPhoneVisitDTO spmsPhoneVisitDTO) {
        SpmsPhoneVisit spmsPhoneVisit = new SpmsPhoneVisit();
//        spmsPhoneVisit.setInfo(spmsPhoneVisitDTO.getInfo());
        spmsPhoneVisit.setInfoType(spmsPhoneVisitDTO.getInfoType());
        if(spmsPhoneVisitDTO.getSpmsWorkOrderId() != null) {
            SpmsWorkOrder spmsWorkOrder = spmsWorkOrderDAO.findOne(spmsPhoneVisitDTO.getSpmsWorkOrderId());
            spmsPhoneVisit.setSpmsWorkOrder(spmsWorkOrder);
        }
        spmsPhoneVisitDAO.save(spmsPhoneVisit);
        return spmsPhoneVisitDTO;
    }
}
