package com.harmazing.spms.workorder.controller;

import com.harmazing.spms.workorder.dto.SpmsPhoneVisitDTO;
import com.harmazing.spms.workorder.manager.SpmsPhoneVisitManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/phoneVisit")
public class SpmsPhoneVisitController {

    @Autowired
    private SpmsPhoneVisitManager spmsPhoneVisitManager;

    @RequestMapping("/doSave")
    @ResponseBody
    public SpmsPhoneVisitDTO doSave(@RequestBody SpmsPhoneVisitDTO spmsPhoneVisitDTO) {
        spmsPhoneVisitManager.doSave(spmsPhoneVisitDTO);
        return spmsPhoneVisitDTO;
    }
}
