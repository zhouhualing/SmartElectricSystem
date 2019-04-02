package com.harmazing.spms.base.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.harmazing.spms.base.dto.QueryCustomDTO;
import com.harmazing.spms.base.dto.QueryTranDTO;
import com.harmazing.spms.base.manager.QueryCustomManager;
import com.harmazing.spms.base.manager.QueryManager;
import com.harmazing.spms.base.util.SpringUtil;

@Controller
public class QueryController {
	
	@Autowired
	@Qualifier("queryManager")
	private QueryManager queryManager;
	
	@RequestMapping(value="/query/executeSearch")
	public ModelAndView doQuery(@RequestBody QueryTranDTO queryTranDTO, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) throws IOException {
	    queryManager.executeSearch(queryTranDTO);
	    MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
	    ModelAndView modelAndView = new ModelAndView(mappingJackson2JsonView);
	    modelAndView.addObject("data", queryTranDTO);
	    return modelAndView;
	}
	
	@RequestMapping(value="/query/queryCustomModify")
	@ResponseBody
	public QueryCustomDTO doSaveQueryModify(@RequestBody QueryCustomDTO queryCustomDTO) {
		QueryCustomManager queryCustomManager = SpringUtil.getBeanByName("queryCustomManager");
		
		queryCustomManager.doSaveQueryCustomModify(queryCustomDTO);
		return queryCustomDTO;
	}
	
	@RequestMapping(value="/query/doDelete")
	@ResponseBody
	public QueryTranDTO doDeleteObject(@RequestBody QueryTranDTO queryTranDTO) {
		queryManager.doDeleteObjectById(queryTranDTO);
		return queryTranDTO;
	}
	

}
