package com.harmazing.spms.portalcustom.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.util.UserUtil;
import com.harmazing.spms.portalcustom.manager.SpmsPortalCustomManager;

@Controller
@RequestMapping("/spmsPortalCustom")
public class SpmsPortalCustomController {
	@Autowired
	private SpmsPortalCustomManager spmsPortalCustomManager;
	
	@RequestMapping("/getSort")
    @ResponseBody
	public Map<Integer, HashMap<String, String>> getSort(){
		UserEntity user = UserUtil.getCurrentUser();
		return spmsPortalCustomManager.getSort(user);
	}
	
	@RequestMapping("/getCanAddSort")
    @ResponseBody
	public Map<String,String> getCanAddSort(){
		UserEntity user = UserUtil.getCurrentUser();
		return spmsPortalCustomManager.getCanAddSort(user);
	}
	
	@RequestMapping("/addSort")
    @ResponseBody
	public synchronized Map<String,Object> addSort(@RequestBody  Map<String,String> info){
		UserEntity user = UserUtil.getCurrentUser();
		String divname = info.get("divname");
		return spmsPortalCustomManager.addSort(user, divname);
	}
	
	@RequestMapping("/deleteSort")
    @ResponseBody
	public Map<String,Object> deleteSort(@RequestBody Map<String,String> info){
		UserEntity user = UserUtil.getCurrentUser();
		String divname = info.get("divname");
		return spmsPortalCustomManager.deleteSort(user, divname);
	}
	
	@RequestMapping("/changeSort")
    @ResponseBody
	public Map<String,Object> changeSort(@RequestBody Map<String,String> sort){
		UserEntity user = UserUtil.getCurrentUser();
		return spmsPortalCustomManager.changeSort(user, sort);
	}
}
