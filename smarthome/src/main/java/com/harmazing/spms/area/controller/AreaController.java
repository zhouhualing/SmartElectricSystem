package com.harmazing.spms.area.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.area.dto.AreaDTO;
import com.harmazing.spms.area.dto.AreaTreeDTO;
import com.harmazing.spms.area.entity.Area;
import com.harmazing.spms.area.manager.AreaManager;
import com.harmazing.spms.base.util.BeanUtils;

@Controller
@RequestMapping("/area")
public class AreaController {
    
	@Autowired
	private AreaManager areaManager;
	
	@RequestMapping("/doSave")
	@ResponseBody
	public AreaDTO daSaveAreaDTO(@RequestBody AreaDTO areaDTO){
		return areaManager.doSave(areaDTO);
	}
	@RequestMapping(value = "/getById")
	@ResponseBody
	public AreaDTO getById(@RequestBody Map<String,String> info){
		return areaManager.getById(info.get("id"));
	}
	
	
	@RequestMapping(value = "/hasName")
	@ResponseBody
	public Map<String, Object> hasName(@RequestBody Map<String,String> info){
		Map<String, Object> result = new HashMap<String, Object>();
		if(info.get("myParent").equals("")){
			result.put("success", true);
			return result;
		}
		
		if(!info.get("myid").equals("")){
			if(info.get("myid").equals(info.get("myParent"))){
				result.put("success", false);
				result.put("msg", "不能设置自身为上级区域");
			}
			Area area = areaManager.findById(info.get("myid"));
			String parentid = area.getParent().getId();
			String name = area.getName();
			if(parentid.equals(info.get("myParent")) && name.equals(info.get("myName"))){
				result.put("success", true);
				return result;
			}
		}
		int num = areaManager.hasName(info.get("myParent"), info.get("myName"));
		if(num == 0){
			result.put("success", true);
		}else{
			result.put("success", false);
			result.put("msg", "同级下已存在本区域！请查证");
		}
		return result;
	}
	
	@RequestMapping(value = "/deleteById")
	@ResponseBody
	public Map<String,Object> deleteById(@RequestBody Map<String,String> info){
		return areaManager.doDelete(info.get("id"));
	}
	
	@RequestMapping("/getFirstLevelArea")
	@ResponseBody
	public List <AreaTreeDTO> getFirstLevelArea(@RequestBody AreaTreeDTO areaTreeDTO) {
		return areaManager.findFirstArea(areaTreeDTO);
	}
	
	@RequestMapping("/findChildrenByParent")
	@ResponseBody
	public List<AreaTreeDTO> findChildrenByParent(@RequestBody AreaTreeDTO areaTreeDTO){
		return areaManager.findChildrenByParent(areaTreeDTO);
	}
}
