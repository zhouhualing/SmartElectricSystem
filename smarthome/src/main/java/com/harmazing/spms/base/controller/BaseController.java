package com.harmazing.spms.base.controller;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

@Controller
public class BaseController {
	 /** 
     *  
     * @param runtimeException 
     * @return 
     */  
    @ExceptionHandler(RuntimeException.class)  
    public @ResponseBody  
    Map<String,Object> runtimeExceptionHandler(RuntimeException runtimeException) {   
        Map<String, Object> model =  Maps.newTreeMap();
        model.put("status", false);  
        return model;  
    } 
}
