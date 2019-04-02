package com.harmazing.spms.base.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.harmazing.spms.base.util.UserUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.harmazing.spms.base.exception.LoginFailtureException;
import com.harmazing.spms.base.util.UsernamePasswordActionToken;




@Controller
public class LoginController {
	
	@RequestMapping(value="/doLogin", method=RequestMethod.GET)
	public String goLogin(HttpServletRequest httpServletRequest) {
	    
	    UsernamePasswordActionToken usernamePasswordActionToken = new UsernamePasswordActionToken(httpServletRequest.getParameter("userName"), httpServletRequest.getParameter("password"));
		String returnView = "";
		try {
		    	Subject subject = SecurityUtils.getSubject();
        		if(httpServletRequest.getParameter("userName") == null) {
        		    if(subject.isAuthenticated()) {
        			 returnView = "redirect:/view/common/index.html";
        		    } else {
        			returnView = "redirect:/view/common/login.html";
        		    }
        		} else {
                    subject.login(usernamePasswordActionToken);
                    returnView = "redirect:/view/common/index.html";
        		}
		} catch(Exception e) {
		    throw new LoginFailtureException("登录出错.");
		}
        if((returnView.indexOf("index.html")!=-1)&&(UserUtil.getCurrentUser().getUserType() == 2)) {
            //订户
            returnView = "redirect:/view/user/user_new.html";
        }
        return returnView;
	}
	
	@RequestMapping(value="/doLogin", method=RequestMethod.POST)
	public String goLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
	    	
	    // 如果已经登录，则跳转到管理首页
	    Subject subject = SecurityUtils.getSubject();
		String returnView = "";
		if(subject.isAuthenticated()) {
			returnView = "redirect:/view/common/index.html";
		} else {
			returnView = "redirect:/view/common/login.html?err=001";
		}
        if((returnView.indexOf("index.html")!=-1)&&(UserUtil.getCurrentUser().getUserType() == 2)) {
            //订户
            returnView = "redirect:/view/user/user_new.html";
        }
		return returnView;		
	}
	
	@RequestMapping("/goMain")
	public String toMain() {
        String url = "redirect:/view/common/index.html";
        if(UserUtil.getCurrentUser().getUserType() == 2) {
            //订户
            url = "/view/user/user_new.html";
        }
		return url;
	}
	
}
