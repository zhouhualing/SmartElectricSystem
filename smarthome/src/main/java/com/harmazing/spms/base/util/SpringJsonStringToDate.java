package com.harmazing.spms.base.util;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class SpringJsonStringToDate extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		try {
			if(jp.getText().length() <= 0) {
				return null;
			}
			String parrtern = "yyyy-MM-dd HH:mm:ss";
			if(jp.getText().length() == 13) {
				parrtern = "yyyy-MM-dd HH";
			} 
			if(jp.getText().length() == 16) {
				parrtern = "yyyy-MM-dd HH:mm";
			}
			return DateUtil.parseStringToDate(jp.getText(), parrtern);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("日期转换错误。");
		}
	}



}
