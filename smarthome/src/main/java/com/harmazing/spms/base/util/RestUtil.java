package com.harmazing.spms.base.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;

public class RestUtil {
	//////////////////////////////////////////////////////////////////
	public static String httpGet( String url){
		String response = "";
		
		String uri = url;
		try{
			URL obj = new URL(uri);		
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			int responseCode = con.getResponseCode();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			response = org.apache.commons.io.IOUtils.toString(br);
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static String httpPostJson( String url,String jsonData) throws UnsupportedEncodingException{
		String response = "";
		String encoding="UTF-8";
		
		byte[] data = jsonData.getBytes(encoding);
		String uri = url;
		try{
			URL obj = new URL(uri);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);
			con.setRequestProperty("Content-Length", String.valueOf(data.length));
			con.setConnectTimeout(5 * 1000);

			OutputStream outStream = con.getOutputStream();
			outStream.write(data);
			outStream.flush();
			outStream.close();
			System.out.println(con.getResponseCode());
			if(con.getResponseCode()==200){
	            InputStream inStream = con.getInputStream();   
				BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
				response = org.apache.commons.io.IOUtils.toString(br);
				br.close();
	        }

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return response;
	}

	public static String httpPostMap( String url,Map<String,Object> mapdata) throws UnsupportedEncodingException, JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(mapdata);
		return httpPostJson(url,json);
	}	
}
