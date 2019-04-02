/**
 * 
 */
package com.harmazing.spms.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.device.dto.SpmsWarningSettingDTO;
import com.harmazing.spms.device.manager.SpmsWarningSettingManager;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月10日
 */
@Controller
@RequestMapping("/warningSetting")
public class SpmsWarningSettingController {

    @Autowired
    private SpmsWarningSettingManager spmsWarningSettingManager;
    
    @RequestMapping("/getAreaSetInfo")
    @ResponseBody
    public SpmsWarningSettingDTO getAreaSetInfo(@RequestBody SpmsWarningSettingDTO spmsWarningSettingDTO) {
	return spmsWarningSettingManager.getAreaSetInfo(spmsWarningSettingDTO);
    }
    @RequestMapping("/doSaveAreaSet")
    @ResponseBody
    public SpmsWarningSettingDTO doSaveAreaSet( SpmsWarningSettingDTO spmsWarningSettingDTO) {
    	
    	return spmsWarningSettingManager.doSaveAreaSet(spmsWarningSettingDTO);
    }
    @RequestMapping("/deleteAreaSet")
    @ResponseBody
    public String deleteAreaSet(@RequestBody SpmsWarningSettingDTO spmsWarningSettingDTO) {
    	return spmsWarningSettingManager.deleteAreaSet(spmsWarningSettingDTO)+"";
    }
}
