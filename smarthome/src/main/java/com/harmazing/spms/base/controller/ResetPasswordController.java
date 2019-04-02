package com.harmazing.spms.base.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.exception.DesException;
import com.harmazing.spms.base.manager.UserManager;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.DesUtils;
import com.harmazing.spms.base.util.MailBean;
import com.harmazing.spms.base.util.MailUtils;
import com.harmazing.spms.base.util.PropertyUtil;

/**
 * 密码修改
 * @author wang.bing
 *
 */
@Controller
@RequestMapping("/resetPassword")
public class ResetPasswordController {
	@Autowired
	private UserManager userManager;
	
	/**
	 * 跳转到邮件发送页面
	 * @return
	 */
	@RequestMapping("/goSendEmail")
	public String goSendEmail(){
		return "redirect:/view/resetpassword/resetpassword_sendemail.html";
	}
	
	/**
	 * 判断用户输入用户名是否正确
	 * @param userDTO
	 * @return
	 */
	@RequestMapping("/checkUserCode")
	@ResponseBody
	public Map<String, Object> checkUserCode(@RequestBody UserDTO userDTO){
		Map<String, Object> map = new HashMap<String, Object>();
		UserEntity ue = userManager.getByUserCode(userDTO.getUserCode());
		if(null != ue){
			map.put("success", true);
		}else{
			map.put("success", false);
		}
		return map;
	}

	/**
	 * 发送邮件
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return
	 */
	@RequestMapping(value = "/doSendEmail",method=RequestMethod.POST)
	public String doSendEmail(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
		try {
			//获取链接
			String currUrl = httpServletRequest.getRequestURI();
			String url = httpServletRequest.getRequestURI().substring(0,currUrl.lastIndexOf("/")) + "/goResetPassword?";
			//获取用户
			String userCode = httpServletRequest.getParameter("userCode");
			UserEntity ue = userManager.getByUserCode(userCode);
			//拼接用户id
			DesUtils des = new DesUtils(PropertyUtil.getPropertyInfo("des.key"));
			userCode = des.encrypt(userCode);
			url = url + "u=" + userCode + "&";
			//拼接时间
			url = url + "t=" + des.encrypt(System.currentTimeMillis() + "");
			url = PropertyUtil.getPropertyInfo("sendemail.rooturl") + url;
			System.out.println(url);
			// 发送邮件
			MailBean mb = new MailBean();
			mb.setTo(ue.getEmail());
			System.out.println(ue.getEmail());
			mb.setFrom(PropertyUtil.getPropertyInfo("sendemail.from"));
			mb.setUsername(PropertyUtil.getPropertyInfo("sendemail.user"));
			mb.setPassword(PropertyUtil.getPropertyInfo("sendemail.password"));
			mb.setHost(PropertyUtil.getPropertyInfo("sendemail.smtp"));
			mb.setSubject("密码找回");
			StringBuffer sb = new StringBuffer();
			sb.append("<h3>请您单击以下链接进行密码重置</h3>");
			sb.append("<h3>该链接将于1小时后失效，请您尽快重置密码</h3>");
			sb.append("<a href=\"");
			sb.append(url);
			sb.append("\">");
			sb.append(url);
			sb.append("</a>");
			sb.append("<br/><font style=\"color:red;font-size:12px;\">注:如该链接无法打开，请复制链接到浏览器地址栏打开。</font>");
			mb.setContent(sb.toString());
			if(MailUtils.sendMail(mb)){
				return "redirect:/view/resetpassword/resetpassword_sendemail_success.html";
			}else{
				return "redirect:/view/resetpassword/resetpassword_sendemail_unsuccess.html";
			}
			
		} catch (Exception e) {
//			throw new DesException("发送邮件出现问题！");
			return "redirect:/view/resetpassword/resetpassword_sendemail_unsuccess.html";
		}
	}

	/**
	 * 发送邮件
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return
	 */
	@RequestMapping(value = "/doSendEmailMobile",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> doSendEmailMobile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String userCode = httpServletRequest.getParameter("userCode");
			UserEntity ue = userManager.getByUserCode(userCode);
			if(null == ue){
				map.put("success", false);
				map.put("msg", "该用户不存在");
				return map;
			}
			
			//获取链接
			String currUrl = httpServletRequest.getRequestURI();
			String url = httpServletRequest.getRequestURI().substring(0,currUrl.lastIndexOf("/")) + "/goResetPassword?";
			//拼接用户id
			DesUtils des = new DesUtils(PropertyUtil.getPropertyInfo("des.key"));
			userCode = des.encrypt(userCode);
			url = url + "u=" + userCode + "&";
			//拼接时间
			url = url + "t=" + des.encrypt(System.currentTimeMillis() + "");
			url = PropertyUtil.getPropertyInfo("sendemail.rooturl") + url;
			System.out.println(url);
			// 发送邮件
			MailBean mb = new MailBean();
			mb.setTo(ue.getEmail());
			System.out.println(ue.getEmail());
			mb.setFrom(PropertyUtil.getPropertyInfo("sendemail.from"));
			mb.setUsername(PropertyUtil.getPropertyInfo("sendemail.user"));
			mb.setPassword(PropertyUtil.getPropertyInfo("sendemail.password"));
			mb.setHost(PropertyUtil.getPropertyInfo("sendemail.smtp"));
			mb.setSubject("密码找回");
			StringBuffer sb = new StringBuffer();
			sb.append("<h3>请您单击以下链接进行密码重置</h3>");
			sb.append("<h3>该链接将于1小时后失效，请您尽快重置密码</h3>");
			sb.append("<a href=\"");
			sb.append(url);
			sb.append("\">");
			sb.append(url);
			sb.append("</a>");
			sb.append("<br/><font style=\"color:red;font-size:12px;\">注:如该链接无法打开，请复制链接到浏览器地址栏打开。</font>");
			mb.setContent(sb.toString());
			if(MailUtils.sendMail(mb)){
				map.put("success", true);
				map.put("msg", "已发送修改密码链接到您的邮箱，请查收并尽快修改密码！");
				return map;
			}else{
				map.put("success", false);
				map.put("msg", "发送邮件失败");
				return map;
			}
			
		} catch (Exception e) {
			map.put("success", false);
			map.put("msg", "发送邮件失败");
			return map;
		}
	}
	
	/**
	 * 跳转用户修改密码界面
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return
	 */
	@RequestMapping(value = "/goResetPassword",method=RequestMethod.GET)
	public String goResetPassword(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
		String time = httpServletRequest.getParameter("t");
		String userCode = httpServletRequest.getParameter("u");
		try {
			DesUtils des;
			des = new DesUtils(PropertyUtil.getPropertyInfo("des.key"));
			//验证时间
			time = des.decrypt(time);
			if(null == time || "".equals(time)){
				return "redirect:/view/resetpassword/resetpassword_sendemail_wrongurl.html";
			}else if(System.currentTimeMillis() - Long.parseLong(time) > Long.parseLong(PropertyUtil.getPropertyInfo("sendemail.time"))){
				return "redirect:/view/resetpassword/resetpassword_sendemail_timeout.html";
			}else{
				//验证用户
				String realUserCode = des.decrypt(userCode);
				httpServletRequest.getSession().setAttribute("userCode", realUserCode);
				UserEntity ue = userManager.getByUserCode(realUserCode);
				if(null == ue){
					return "redirect:/view/resetpassword/resetpassword_sendemail_wrongurl.html";
				}
			}
			return "redirect:/view/resetpassword/resetpassword_resetpassword.html";
		} catch (Exception e) {
			System.out.println("url解密出现问题！");
			return "redirect:/view/resetpassword/resetpassword_sendemail_wrongurl.html";
		}
		
	}
	
	/**
	 * 修改密码
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return
	 */
	@RequestMapping(value = "/doResetPassword",method=RequestMethod.POST)
	public String doResetPassword(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
		String password = httpServletRequest.getParameter("password");
		String userCode = (String) httpServletRequest.getSession().getAttribute("userCode");
		if(null == userCode && "".equals(userCode)){
			return "redirect:/view/resetpassword/resetpassword_resetpassword_unsuccess.html";
		}
		try {
			UserEntity ue = userManager.getByUserCode(userCode);
			ue.setPassword(password);
			UserDTO ud = new UserDTO();
			BeanUtils.copyProperties(ue, ud);
			userManager.doEdit(ud);
			return "redirect:/view/resetpassword/resetpassword_resetpassword_success.html";
		} catch (Exception e) {
			throw new DesException("密码修改失败！");
		}
		
	}
}
