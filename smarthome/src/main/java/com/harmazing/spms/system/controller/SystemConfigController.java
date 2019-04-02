package com.harmazing.spms.system.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.base.util.FtpUtil;
import com.harmazing.spms.base.util.PropertyUtil;
import com.harmazing.spms.system.dto.SystemConfigDto;
import com.harmazing.spms.system.manager.SystemConfigManager;

/**
 * 系统设置管理
 * @author 王冰
 *
 */
@Controller
@RequestMapping(value = "/systemConfig")
public class SystemConfigController {
	@Autowired
	private SystemConfigManager systemConfigManager;
	
	/**
	 * 获取系统配置
	 * @param systemConfigDto
	 * @return
	 */
	@RequestMapping("/getSystemConfig")
	@ResponseBody
	public SystemConfigDto getSystemConfig(@RequestBody SystemConfigDto systemConfigDto){
		return systemConfigManager.getSystemConfig(systemConfigDto);
	}
	
	/**
	 * 保存系统配置
	 * @param systemConfigDto
	 * @return
	 */
	@RequestMapping("/saveSystemConfig")
	@ResponseBody
	public SystemConfigDto saveSystemConfig(@RequestBody SystemConfigDto systemConfigDto){
		try {
			return systemConfigManager.saveSystemConfig(systemConfigDto);
		} catch (Exception e) {
			return systemConfigDto;
		}
	}

	/**
	 * 保存系统配置
	 * @param systemConfigDto
	 * @return
	 */
	@RequestMapping("/saveSoftVersion")
	public void saveSoftVersion(HttpServletRequest request , HttpServletResponse response) throws IOException{
		String result = "";
		response.setContentType("text/html"); 
		try {
			System.out.println(((DefaultMultipartHttpServletRequest)request).getFile("myfiles1").getOriginalFilename());
			System.out.println(((DefaultMultipartHttpServletRequest)request).getFile("myfiles2").getOriginalFilename());
			FtpUtil.uploadFile(PropertyUtil.getPropertyInfo("ftp.url"), Integer.parseInt(PropertyUtil.getPropertyInfo("ftp.port")),
					PropertyUtil.getPropertyInfo("ftp.user"), PropertyUtil.getPropertyInfo("ftp.pwd"), 
					PropertyUtil.getPropertyInfo("ftp.path"), ((DefaultMultipartHttpServletRequest)request).getFile("myfiles1").getOriginalFilename(),
					((DefaultMultipartHttpServletRequest)request).getFile("myfiles1").getInputStream());
			FtpUtil.uploadFile(PropertyUtil.getPropertyInfo("ftp.url"), Integer.parseInt(PropertyUtil.getPropertyInfo("ftp.port")),
					PropertyUtil.getPropertyInfo("ftp.user"), PropertyUtil.getPropertyInfo("ftp.pwd"), 
					PropertyUtil.getPropertyInfo("ftp.path"), ((DefaultMultipartHttpServletRequest)request).getFile("myfiles2").getOriginalFilename(),
					((DefaultMultipartHttpServletRequest)request).getFile("myfiles2").getInputStream());
			
			Map message=new HashMap();
			message.put("messageType","SERVICEUPDATE");
			message.put("commandType",3);
			message.put("updateLink","http://" + PropertyUtil.getPropertyInfo("ftp.url") + ":" + 
					PropertyUtil.getPropertyInfo("ftp.openport") + "/" + ((DefaultMultipartHttpServletRequest)request).getFile("myfiles2").getOriginalFilename());
			CommandUtil.asyncSendMessage(message);
			
			result = "{'success':true,'msg':'上传成功'}";
		} catch (Exception e) {
			result = "{'success':false,'msg':'上传失败'}";
		}
		response.getWriter().write(result);
		response.getWriter().flush();
	}
}
