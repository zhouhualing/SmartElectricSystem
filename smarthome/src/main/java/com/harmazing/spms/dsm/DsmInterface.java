package com.harmazing.spms.dsm;

import java.util.HashMap;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.uuid.Logger;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.util.CommandUtil;

@Controller
@RequestMapping("/dsm")
public class DsmInterface {	
	@Autowired
	public QueryDAO queryDao;
	/*********************
	@RequestMapping(value = "setTempRange")
	@ResponseBody
	public boolean sendTempTange(int lowTemp,int upperTemp,String areaId){
		boolean success=true;
		try{
			Map message=new HashMap();
			message.put("messageType","SERVICEUPDATE");
			message.put("commandType",4);
			message.put("lowTemp",lowTemp);
			message.put("upperTemp",upperTemp);
			message.put("areaId",areaId);	
			
			String delSql="delete from spms_dsm where areaId='"+areaId+"'";
			String insertSql="insert spms_dsm(areaId,lowTemp,upperTemp,updateTime)values("
					+ "'"+areaId+"',"+lowTemp+","+upperTemp+",now())";
			queryDao.doExecuteSql(delSql);
			queryDao.doExecuteSql(insertSql);
			
			CommandUtil.asyncSendMessage(message);
		}catch(Exception e){
			success=false;
			e.printStackTrace();
		}
		return success;
	}
	************************************/
	@RequestMapping(value = "setTempRange")
	@ResponseBody
	public boolean sendTempTange(String dsmlist){
		boolean success=true;
		String[] dsms=dsmlist.split(";");
		for(int i=0;i<dsms.length;i++){
			try{
				String[] dsmarr=dsms[i].split(",");
				if(dsmarr.length<3){					
					continue;
				}else{		
					
					String deviceId=dsmarr[0];					
					int lowTemp=Integer.parseInt(dsmarr[1]);
					int upperTemp=Integer.parseInt(dsmarr[2]);
					Map message=new HashMap();
					message.put("messageType","SERVICEUPDATE");
					message.put("commandType",4);
					message.put("lowTemp",lowTemp);
					message.put("upperTemp",upperTemp);
					message.put("deviceId",deviceId);	
					
					
					String delSql="delete from spms_dsm where deviceId='"+deviceId+"'";
					String insertSql="insert spms_dsm(deviceId,lowTemp,upperTemp,updateTime)values("
							+ "'"+deviceId+"',"+lowTemp+","+upperTemp+",now())";
					queryDao.doExecuteSql(delSql);
					queryDao.doExecuteSql(insertSql);
					
					CommandUtil.asyncSendMessage(message);
				}
			}catch(Exception e){
				success=false;
				e.printStackTrace();
			}
		}
		return success;
	}
}
