package com.harmazing.spms.base.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginOutController {
	
	@RequestMapping("/doLoginOut")
	@ResponseBody
	public String doLoginOut(@RequestBody Map<String, String> info) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "/common/view/login.html";
	}
}
