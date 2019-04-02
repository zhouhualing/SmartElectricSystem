/**
 * 
 */
package com.harmazing.spms.workorder.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.area.dto.AreaDTO;
import com.harmazing.spms.device.dto.SpmsDeviceDTO;
import com.harmazing.spms.device.manager.SpmsDeviceManager;
import com.harmazing.spms.jszc.dto.JSZCDTO;
import com.harmazing.spms.jszc.manager.JSZCManager;
import com.harmazing.spms.workorder.dto.SpmsWorkOrderDTO;
import com.harmazing.spms.workorder.manager.SpmsWorkOrderManager;

@Controller
@RequestMapping("/spmsWorkOrder")
public class SpmsWorkOrderController {
    
    @Autowired
    private SpmsWorkOrderManager spmsWorkOrderManager;
    
    @Autowired
    private JSZCManager jszcManager;
    
    @RequestMapping("/doSave")
    @ResponseBody
    public SpmsWorkOrderDTO doSaveSpmsDevice(@RequestBody SpmsWorkOrderDTO spmsWorkOrderDTO) {
    	return spmsWorkOrderManager.doSave(spmsWorkOrderDTO);
    }
    
    @RequestMapping("/doEdit")
    @ResponseBody
    public SpmsWorkOrderDTO doEditSpmsDevice(SpmsWorkOrderDTO spmsWorkOrderDTO) {
    	return spmsWorkOrderManager.doSave(spmsWorkOrderDTO);
    }

    @RequestMapping("/doModifyDelete")
    @ResponseBody
    public SpmsWorkOrderDTO doModifyDelete(@RequestBody SpmsWorkOrderDTO spmsWorkOrderDTO) {
        return spmsWorkOrderManager.doModifyDelete(spmsWorkOrderDTO);
    }
    
    @RequestMapping("/doDelete")
    @ResponseBody
    public Map<String, Object> doDelete(@RequestBody Map<String,String> info) {
    	return spmsWorkOrderManager.doDelete(info.get("id"));
    }
    
    @RequestMapping(value = "/getById")
	@ResponseBody
	public SpmsWorkOrderDTO getById(@RequestBody Map<String,String> info){
		return spmsWorkOrderManager.getById(info.get("id"));
	}
    
    @RequestMapping(value = "/batchDelete")
	@ResponseBody
    public Map<String,Object> batchDelete(@RequestBody Map<String,String> info){
    	return spmsWorkOrderManager.batchDelete(info.get("ids"));
    }
    @RequestMapping("/validateUser")
	@ResponseBody
	public Map<String,Object> validateUser(@RequestBody Map<String, Object> map){
		return spmsWorkOrderManager.validateUser(map.get("processInstanceId").toString());
	}

    /**
     * 是否确认
     * @param map
     * @return
     */
    @RequestMapping("/isQR")
	@ResponseBody
	public Map<String,Object> isQR(@RequestBody Map<String, Object> map){
		return jszcManager.isQR(map);
	}

    @RequestMapping("/QR")
    @ResponseBody
    public Map<String,Object> QR(@RequestBody Map<String, Object> map){
    	return jszcManager.QR(map);
    }

    @RequestMapping("/unQR")
    @ResponseBody
    public Map<String,Object> unQR(@RequestBody Map<String, Object> map){
    	return jszcManager.unQR(map);
    }
    
}
