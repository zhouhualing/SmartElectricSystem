package com.harmazing.spms.jszc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.base.dto.DictDTO;
import com.harmazing.spms.base.util.DictUtil;
import com.harmazing.spms.jszc.dto.JSZCDTO;
import com.harmazing.spms.jszc.dto.JSZCMainDTO;
import com.harmazing.spms.jszc.manager.JSZCManager;

@Controller
@RequestMapping("/jszc")
public class JSZCController {
	@Autowired
	private JSZCManager jszcManager;

	@RequestMapping("/listFailureCause")
	@ResponseBody
	public List<Object[]> listFailureCause(){
		List<Object[]> list = jszcManager.listFailureCause();
		return list; 
	}

	@RequestMapping("/listBxCause")
	@ResponseBody
	public List<DictDTO> listBxCause(){
		List<DictDTO> list = DictUtil.getDictValues("jszc_bxCause");
		return list;
	}

	@RequestMapping("/tdReason")
	@ResponseBody
	public List<DictDTO> listTdReason(){
		List<DictDTO> list = DictUtil.getDictValues("unsubscribe_tdReason");
		return list;
	}
	
	@RequestMapping("/doSave")
    @ResponseBody
	public JSZCMainDTO doSave(@RequestBody JSZCMainDTO jszcMainDTO){
		jszcManager.doSave(jszcMainDTO.getJszcdtos());
		return jszcMainDTO;
	}

	@RequestMapping("/listVariablesByProcessId")
	@ResponseBody
	public List<Map<String, Object>> listVariablesByProcessId(@RequestBody Map<String, Object> info){
		List<Map<String, Object>> list = jszcManager.listVariablesByProcId(info.get("processId").toString());
		return list;
	}
}
