package com.harmazing.spms.helper;

import java.util.Map;

import com.google.common.collect.Maps;

public class RestUrlMapper {
	static Map<String, String> map = Maps.newHashMap();
	static
	{
		map.put("fanspeed", "speed");
		map.put("temperature", "acTemp");
	}
	
	public String process(String input){
		if(!map.containsKey(input))
			return input;
		return map.get(input);
	}
	

}
