package com.harmazing.spms.usersRairconSetting.toolsClass;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.harmazing.spms.base.util.CommandUtil;
import com.harmazing.spms.base.util.SpringUtil;
import com.harmazing.spms.base.util.UserUtil;
import com.harmazing.spms.user.dao.SpmsUserDAO;
import com.harmazing.spms.user.entity.SpmsUser;

public class sendMessage {
	
	
	/**
	 * 舒适曲线设置
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void sendCurveMessage(String deviceId,String userId){
		if("".equals(userId)){
			String mobile=UserUtil.getCurrentUser().getMobile();
			SpmsUserDAO userDao=SpringUtil.getBeanByName("spmsUserDAO");
			if(mobile != null){
				SpmsUser user=userDao.getByMobile(mobile);
				userId = user.getId();
			}
		}
		Map m = new HashMap();
		m.put("deviceId", deviceId);
		m.put("userId", userId);
		m.put("messageType","TIMERCURV");
		CommandUtil.asyncSendMessage(m);
	}
	/**
	 * 定时设置
	 */
	/**
	 * 接口返回
	 */
	public static Map<String,Object> reusltMap(boolean b){
		Map m = new HashMap();
		m.put("result", b);
		return m;
	}
	
	/**
	 * 获取当前spms用户Id
	 * @return
	 */
	public static String getSpmsUserId(){
		String mobile=UserUtil.getCurrentUser().getMobile();
		SpmsUserDAO userDao=SpringUtil.getBeanByName("spmsUserDAO");
		String userId = "";
		if(mobile != null){
			SpmsUser user=userDao.getByMobile(mobile);
			userId = user.getId();
		}
		return userId;
	}
	
	/*------当前系统时间------*/
	public static String getTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return df.format(new Date());// new Date()为获取当前系统时间
	}
	 /**
     * Get Request
     * http请求
     * @return
     * @throws Exception
     */
    public static String doGet(String urlS) throws Exception {
    	//System.out.println(urlS);
        URL localURL = new URL(urlS);
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        
        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }
        
        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        //System.out.println(resultBuffer.toString());
        return resultBuffer.toString();
    }
    /**
     * 获取城市编码
     * @param name
     * @return
     */
    public static String getCityCode(String name){
    	if("true".equals(JDomXml.cityMap.get("bo")+"")){
    		return JDomXml.cityMap.get(name)+"";
    	}else{
    		JDomXml.getCityCode();
    		return JDomXml.cityMap.get(name)+"";
    	}
    }
}
