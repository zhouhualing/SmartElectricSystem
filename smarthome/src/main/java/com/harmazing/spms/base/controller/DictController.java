package com.harmazing.spms.base.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.spms.base.dto.DictDTO;
import com.harmazing.spms.base.manager.DictManager;

@Controller
public class DictController {
	
	@Autowired
	private DictManager dictManager;
	
	@RequestMapping("/dict/getDicts")
	@ResponseBody
	public Map <String, List<DictDTO>> getDicts(@RequestBody List <String> tableNames) {
		return dictManager.getDicts(tableNames);
	}
}
