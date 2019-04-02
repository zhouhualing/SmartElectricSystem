package com.harmazing.spms.potaldefault.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.potaldefault.manager.SpmsPortalDefaultManager;

@Controller
@RequestMapping("/spmsPortalDefault")
public class SpmsPortalDefaultController {
	@Autowired
	private SpmsPortalDefaultManager spmsPortalDefaultManager;
	
	@RequestMapping("/getDefault")
    @ResponseBody
	public Map<String, HashMap<String, Object>> getDefault(){
		Map<String, HashMap<String, Object>> result = new HashMap<String,HashMap<String,Object>>();
		result = spmsPortalDefaultManager.getDefault();
		return result;
	}
	
	@RequestMapping("/changeDefault")
    @ResponseBody
	public Map<String,Object> changeDefault(@RequestBody Map<String,String> info){
		return spmsPortalDefaultManager.changeDefault(info);
	}
}
