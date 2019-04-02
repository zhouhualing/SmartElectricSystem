package com.harmazing.spms.base.util;

import java.text.ParseException;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class SpringStringToDate implements Converter<String, Date> {
	
	private String parttern;
	
	public SpringStringToDate(String parttern) {
		this.parttern = parttern;
	}
	
	@Override
	public Date convert(String source) {
		try {
			if(StringUtils.isEmpty(source)) {
				return null;
			}
			
			String parrtern = "yyyy-MM-dd HH:mm:ss";
			
			if(source.length() == 10) {
				parrtern = "yyyy-MM-dd";
			} 
			
			if(source.length() == 13) {
				parrtern = "yyyy-MM-dd HH";
			} 
			if(source.length() == 16) {
				parrtern = "yyyy-MM-dd HH:mm";
			}

			return DateUtil.parseStringToDate(source,parrtern);
		} catch(ParseException parseException) {
			throw new RuntimeException("日期解析错误。");
		}
	}

	public String getParttern() {
		return parttern;
	}

	public void setParttern(String parttern) {
		this.parttern = parttern;
	}
	

}
