package com.harmazing.spms.spmsuc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.helper.EnumTypesConsts;
import com.harmazing.spms.helper.ObjectWraperDTO;
import com.harmazing.spms.spmsuc.Manager.SpmsAppQueryManager;
import com.harmazing.spms.spmsuc.dto.DeviceStatusDTO;

@RestController
@RequestMapping("app/his/")
public class SpmsAppQeuryController {
	@Autowired
	private QueryDAO queryDAO;
	@Autowired
	private SpmsAppQueryManager spmsAppQueryManager;
	
	@RequestMapping(value = "/device/{type}", method = RequestMethod.POST)
    @ResponseBody
	public Object gets(@RequestBody DeviceStatusDTO statusDTO,@PathVariable Integer type){	
		ObjectWraperDTO dto = ObjectWraperDTO.getDefaultWraperDTO();
		if(type == EnumTypesConsts.DeviceType.Dev_Type_Wd){
			dto.setData(spmsAppQueryManager.getWdStatus(statusDTO));
		}else if(type == EnumTypesConsts.DeviceType.Dev_Type_OnOffPmPlug){
			dto.setData(spmsAppQueryManager.getPmPlugStatus(statusDTO));
		}else if(type == EnumTypesConsts.DeviceType.Dev_Type_HtSensor){
			dto.setData(spmsAppQueryManager.getHtSensorStatus(statusDTO));
		}else if(type == EnumTypesConsts.DeviceType.Dev_Type_Pir){
			dto.setData(spmsAppQueryManager.getPirStatus(statusDTO));
		}	
		return dto;
	}	
	
	@RequestMapping(value = "/wd", method = RequestMethod.POST)
    @ResponseBody
	public Object getWdStatus(@RequestBody DeviceStatusDTO statusDTO){
		ObjectWraperDTO dto = ObjectWraperDTO.getDefaultWraperDTO();
		dto.setData(spmsAppQueryManager.getWdStatus(statusDTO));
		return dto;
	}
	
	@RequestMapping(value = "/pmp", method = RequestMethod.POST)
    @ResponseBody
	public Object getPmPlugStatus(@RequestBody DeviceStatusDTO statusDTO){	
		ObjectWraperDTO dto = ObjectWraperDTO.getDefaultWraperDTO();
		dto.setData(spmsAppQueryManager.getPmPlugStatus(statusDTO));	
		return dto;
	}
	@RequestMapping(value = "/ht", method = RequestMethod.POST)
    @ResponseBody
	public Object getHtSensorStatus(@RequestBody DeviceStatusDTO statusDTO){	
		ObjectWraperDTO dto = ObjectWraperDTO.getDefaultWraperDTO();
		dto.setData(spmsAppQueryManager.getHtSensorStatus(statusDTO));	
		return dto;
	}
	@RequestMapping(value = "/pir", method = RequestMethod.POST)
    @ResponseBody
	public Object getPirStatus(@RequestBody DeviceStatusDTO statusDTO){	
		ObjectWraperDTO dto = ObjectWraperDTO.getDefaultWraperDTO();
		dto.setData(spmsAppQueryManager.getPirStatus(statusDTO));		
		return dto;
	}
	

}
