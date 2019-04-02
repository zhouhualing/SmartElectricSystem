package com.harmazing.ifttt;

import java.util.List;

import org.apache.ibatis.annotations.Select;


public interface IftttMapper {
	final static String TB_IFTTT_STRATEGY = "ifttt_strategy";
	final static String TB_IFTTT_DEV_STRATEGY_BINDING = "ifttt_device_stragegy_binding";
	
	@Select("SELECT * FROM " + TB_IFTTT_DEV_STRATEGY_BINDING)
	public List<IftttDevStrategyBindingEntigy> getAllDevStrategyBinding();
		
	@Select("select * from " + TB_IFTTT_STRATEGY + " WHERE enabled=1")
	public List<IftttEntity> getAllEntities();
}
