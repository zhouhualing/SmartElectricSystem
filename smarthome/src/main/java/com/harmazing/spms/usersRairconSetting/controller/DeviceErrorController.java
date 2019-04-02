package com.harmazing.spms.usersRairconSetting.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.usersRairconSetting.dto.DeviceErrorDTO;
import com.harmazing.spms.usersRairconSetting.manager.DeviceErrorManager;
import com.harmazing.spms.usersRairconSetting.manager.RairconSettingManager;
/*
 * 空调定时器设置功能
 */
@Controller
@RequestMapping("/deviceError")
public class DeviceErrorController {
	@Autowired
	private RairconSettingManager rairconSettingManager;
	@Autowired
	private DeviceErrorManager deviceErrorManager;
	
	/**
	 * 加载空调异常信息
	 */
	@RequestMapping("/getDeviceError")
	@ResponseBody
	public List<DeviceErrorDTO> getDeviceError(@RequestBody Map<String,Object> m){
		List <DeviceErrorDTO> rl = new ArrayList<DeviceErrorDTO>();
		if(m.get("deviceId")!=null){
			List deviceIds=(   (List)m.get("deviceId")   );
			for (Object deviceId : deviceIds) {
				DeviceErrorDTO deviceErrorDTO =deviceErrorManager.queryDeviceError(deviceId+"");
				if(deviceErrorDTO!=null){
					rl.add(deviceErrorDTO);
				}
			}
		}
		return rl;
	}
}
