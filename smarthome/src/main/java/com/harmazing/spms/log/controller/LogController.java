package com.harmazing.spms.log.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



import com.harmazing.spms.log.dto.SpmsLogDTO;
import com.harmazing.spms.log.manager.SpmsLogManager;

@Controller
@RequestMapping("/spmsLog")
public class LogController {
	@Autowired
    private SpmsLogManager spmsLogManager;
    
    @RequestMapping("/doSave")
    @ResponseBody
    public SpmsLogDTO doSaveSpmsLog(@RequestBody SpmsLogDTO spmsLogDTO) {
	return spmsLogManager.doSave(spmsLogDTO);
	
    }
    @RequestMapping("/getDetail")
    @ResponseBody
    public SpmsLogDTO getDetailSpmsLog(@RequestBody String id) {
	return spmsLogManager.getDetail(id);
	
    }
  
}
