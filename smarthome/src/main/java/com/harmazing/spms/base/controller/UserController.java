/**
 * 
 */
package com.harmazing.spms.base.controller;

import java.util.List;
import java.util.Map;

import com.harmazing.spms.base.dto.TreeDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.manager.UserManager;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserManager userManager;
    
    @RequestMapping("/getInfo")
    @ResponseBody
    public UserDTO getUserInfo(@RequestBody UserDTO userDTO) {
	return userManager.getUserInfo(userDTO);
    }
    
    @RequestMapping("/deleteUsers")
    @ResponseBody
	public String deleteUsers(@RequestBody Map<String, String> info) {
    	
		return userManager.deleteUsers(info.get("ids"))+"";
	}
    
    @RequestMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(@RequestBody Map<String, String> info) {
    	
    	return userManager.deleteUser(info.get("ids"))+"";
    }
    
    @RequestMapping("/editUser")
    @ResponseBody
    public UserDTO doEdit(UserDTO userDTO) {
    	
    	return userManager.doEdit(userDTO);
    }
    
    @RequestMapping("/editUserJson")
    @ResponseBody
    public UserDTO doEditJson(@RequestBody UserDTO userDTO) {
    	
    	return userManager.doEdit(userDTO);
    }
    
    @RequestMapping("/getCurrentUserInfo")
    @ResponseBody
    public UserDTO getCurrentUserInfo() {
    	
    	return userManager.getCurrentUserInfo();
    }

    @RequestMapping("/getUsersByUserCods")
    @ResponseBody
    public List<TreeDTO> getUsersByUserCods(@RequestBody List<String> userCodes) {
    	
        return userManager.getUsersByUserCods(userCodes);
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public UserDTO addUser(@RequestBody UserDTO userDTO) {
    	
        return userManager.addUser(userDTO);
    }
    
    
    /**
     * 取得系统中的所有的角色
     * @return
     */
    @RequestMapping("/getAllRole")
    @ResponseBody
    public Map<String,Object> getAllRole(){
    	
    	return userManager.getAllRole();
    }
}
