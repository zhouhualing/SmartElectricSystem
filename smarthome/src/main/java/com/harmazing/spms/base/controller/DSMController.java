/**
 * 
 */
package com.harmazing.spms.base.controller;


import java.util.List;
import java.util.Map;

import com.harmazing.spms.base.dto.DSMDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.base.manager.DSMManager;
import com.harmazing.spms.usersRairconSetting.toolsClass.sendMessage;

@Controller
@RequestMapping("/dsm")
public class DSMController {
    
    @Autowired
    private DSMManager dsmManager;
    /**
     * 查询dsm信息
     * @param DSMDTO
     * @return
     */
    @RequestMapping("/getInfo")
    @ResponseBody
    public DSMDTO getUserInfo(@RequestBody DSMDTO DSMDTO) {
    	return dsmManager.getUserInfo(DSMDTO);
    }
    
    /**
     * 承诺目标设置
     * @param DSMDTO
     * @return
     */
    @RequestMapping("/promiseSet")
    @ResponseBody
    public Map<String,Object> promiseSet(@RequestBody DSMDTO DSMDTO) {
    	return sendMessage.reusltMap(   dsmManager.promiseSet(DSMDTO)   );
    }
    /**
     * 承诺目标设置
     * @param DSMDTO
     * @return
     */
    @RequestMapping("/deleteDSM")
    @ResponseBody
    public Map<String,Object> deleteDSM(@RequestBody Map<String,Object> m) {
    	return sendMessage.reusltMap(   dsmManager.deleteDSM(m)   );
    }
    
    
}
