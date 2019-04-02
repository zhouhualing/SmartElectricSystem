package com.harmazing.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.harmazing.entity.ACClock;
import com.harmazing.mapper.AcClockMapper;
import com.harmazing.service.ACClockService;


public class ACClockServiceImpl extends ServiceImpl implements ACClockService {

	@Override
	public Map<String,List<ACClock>> getACClockByUser(String userId) {
		SqlSession session = sqlSessionFactory.openSession(false);
		Map<String,List<ACClock>> clocks=new HashMap<String,List<ACClock>>();
		try{					
			AcClockMapper ac=session.getMapper(AcClockMapper.class);
			List<Map> lst=ac.getClockSettingByUser(userId);		
			for(int i=0;i<lst.size();i++){				
				Map m=lst.get(i);
				List<ACClock> acs=clocks.get(m.get("mac").toString());
				if(acs!=null){
					acs=new ArrayList<ACClock>();
					clocks.put(m.get("mac").toString(),acs);
				}
				ACClock clock=new ACClock();
				int appDate=0;
				if(m.get("monday")!=null && m.get("monday").equals("1")){
					appDate+=1;
				}
				if(m.get("tuesday")!=null && m.get("tuesday").equals("1")){
					appDate+=2;
				}
				if(m.get("wednesday")!=null && m.get("wednesday").equals("1")){
					appDate+=4;
				}
				if(m.get("thursday")!=null && m.get("thursday").equals("1")){
					appDate+=8;
				}
				if(m.get("friday")!=null && m.get("monday").equals("1")){
					appDate+=16;
				}
				if(m.get("saturday")!=null && m.get("monday").equals("1")){
					appDate+=32;
				}
				if(m.get("sunday")!=null && m.get("monday").equals("1")){
					appDate+=64;
				}
				clock.setAppDate(appDate);				
				clock.setAcState(Integer.parseInt(m.get("on_off").toString()));
				clock.setFanMode(Integer.parseInt(m.get("windspeed").toString()));
				clock.setOperMode(Integer.parseInt(m.get("mode").toString()));
				clock.setTemperature(Integer.parseInt(m.get("temp").toString()));
				clock.setStartTime(Integer.parseInt(m.get("clocking").toString().replace(":","")));
				clock.setDeviceMac(m.get("mac").toString());
				acs.add(clock);
			}			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return clocks;
	}
	
	public List<ACClock> getClockSettingByDevice(String deviceId){
		SqlSession session = sqlSessionFactory.openSession(false);
		List<ACClock> clocks=new ArrayList<ACClock>();
		try{					
			AcClockMapper ac=session.getMapper(AcClockMapper.class);
			List<Map> lst=ac.getClockSettingByDevice(deviceId);	
			for(int i=0;i<lst.size();i++){
				Map m=lst.get(i);
				ACClock clock=new ACClock();
				int appDate=0;
				if(m.get("monday")!=null && m.get("monday").equals("1")){
					appDate+=1;
				}
				if(m.get("tuesday")!=null && m.get("tuesday").toString().equals("1")){
					appDate+=2;
				}
				if(m.get("wednesday")!=null && m.get("wednesday").toString().equals("1")){
					appDate+=4;
				}
				if(m.get("thursday")!=null && m.get("thursday").toString().equals("1")){
					appDate+=8;
				}
				if(m.get("friday")!=null && m.get("friday").toString().equals("1")){
					appDate+=16;
				}
				if(m.get("saturday")!=null && m.get("saturday").toString().equals("1")){
					appDate+=32;
				}
				if(m.get("sunday")!=null && m.get("sunday").toString().equals("1")){
					appDate+=64;
				}
				clock.setAppDate(appDate);
				try{
					clock.setAcState(Integer.parseInt(m.get("on_off").toString()));
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					clock.setFanMode(Integer.parseInt(m.get("windspeed").toString()));
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					clock.setOperMode(Integer.parseInt(m.get("mode").toString()));
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					clock.setTemperature(Integer.parseInt(m.get("temp").toString()));
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					clock.setStartTime(Long.parseLong(m.get("clocking").toString().replace(":","")));
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					clock.setDeviceMac(m.get("mac").toString());
				}catch(Exception e){
					e.printStackTrace();
				}
				clocks.add(clock);
			}			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		return clocks;
	}	
	
	public static void main(String[] args){
		 long s=Long.parseLong("0912");
		 System.out.println(s);
	}
	
}
