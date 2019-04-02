/**
 * 
 */
package com.harmazing.spms.base.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.base.manager.PermissionManager;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月2日
 */
@Controller
@RequestMapping("/permission")
public class PermissionController {
    
    @Autowired
    private PermissionManager permissionManager;
    
    @RequestMapping("/getMenu")
    @ResponseBody
    public Map<String,Object> getMenu(@RequestBody Map <String,String> info) {
	return permissionManager.getMenu(info);
    }
    
    @RequestMapping("/getMenuByuserRole")
    @ResponseBody
    public Map<String,Object> getMenuByuserRole(@RequestBody Map <String,String> info) {
	return permissionManager.getMenuByuserRole();
    }
    
    
    @RequestMapping("/getMenuTreeByRole")
    @ResponseBody
    public Map<String,Object> getMenuTreeByRole(@RequestBody Map <String,String> info) {
	return permissionManager.getMenuTreeByRole(info);
    }
}
