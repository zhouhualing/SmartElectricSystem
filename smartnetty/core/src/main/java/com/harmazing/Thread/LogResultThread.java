package com.harmazing.Thread;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Config;
import com.harmazing.DataSourceSessionFactory;
import com.harmazing.cache.ElecResultKeysCache;
import com.harmazing.cache.LogCountErrorCache;
import com.harmazing.cache.MessageCache;
import com.harmazing.cache.TimerCache;
import com.harmazing.entity.ResultData;
import com.harmazing.mapper.DeviceMapper;
import com.harmazing.mapper.ResultDataMapper;
import com.harmazing.service.impl.LogServiceImpl;
import com.harmazing.util.PersistentUtil;
import com.harmazing.util.UUIDGenerator;

public class LogResultThread extends Thread{
	 public Logger LOGGER = LoggerFactory.getLogger(LogResultThread.class);
	 public List updateCache=new ArrayList();
	 public List insertCache=new ArrayList();
	 public long initTime=System.currentTimeMillis();
	 public ConcurrentHashMap<String,List<ResultData>> cache=new ConcurrentHashMap<String,List<ResultData>>();
	 public String key="";
	 public TimerCache timercache;
	 public int interval;
	 public LogResultThread(String key){
		 this.key=key;
		 this.timercache=new TimerCache(1000*60*5);
		 this.interval=Config.getInstance().CLIENT_REPORT_INTERVAL*1000;
	 }
	 public void run() {
		while(true){
			try{
				 Map<String,List<ResultData>> map=null;
				 synchronized (cache) {
					map=cache;
					cache=new ConcurrentHashMap<String,List<ResultData>>();
				 }				
	             if(map!=null && map.size()>0){
//	            	 System.out.println("result data size "+ map.size());	            	 
	            	 updateResultData(map);
	             }
	             
	             long t=System.currentTimeMillis();
	             if(t-initTime>this.interval){
	            	 this.flushCache();	            	 
	             }	             
	             Thread.sleep(5);
	     	}catch(Exception e){
	     		LOGGER.error("appendAirResultData",e);
	     	}
		}
     }
	  public void updateResultData(Map<String, List<ResultData>> areaMap){
		  	if(areaMap==null){
		  		return;
		  	}
	    	//String ukey=UUIDGenerator.randomUUID();
		  	Config conf=Config.getInstance();
			for(Entry en : areaMap.entrySet()){				
				List<ResultData> resultDataList = (List<ResultData>)(en.getValue());
				
				long startTime = resultDataList.get(0).getStartTime();
				long demandTime = resultDataList.get(0).getDemandTime();
				String areaId = resultDataList.get(0).getAreaId();
				String stString = "";
				
				Integer power = 0;
				Integer reactivePower = 0;
				Integer powerFactor = 0;
				Integer apparentPower = 0;
				Integer reactiveEnergy = 0;
				Integer reactiveDemand = 0;
				Integer activeDemand = 0;
				Integer deviceNum = 0;
				long accumulatePower=0;
				
				if(demandTime==0){
					activeDemand =0;
					reactiveDemand =0;
				}else{
					stString = demandTime+"";
					stString = stString.substring(0,stString.length()-3)+"000";
				}				
				
				for(ResultData rd : resultDataList){
					power = power+rd.getPower();
					reactivePower =reactivePower +rd.getReactivePower();
					powerFactor = powerFactor+ rd.getPowerFactor();
					apparentPower = apparentPower+ rd.getApparentPower();
					reactiveEnergy = reactiveEnergy+ rd.getReactiveEnergy();
					reactiveDemand = reactiveDemand+ (rd.getReactiveDemand()==null?0:rd.getReactiveDemand());
					activeDemand =activeDemand + (rd.getActiveDemand()==null?0:rd.getActiveDemand());
					deviceNum=deviceNum+rd.getDeviceNum();
					accumulatePower=accumulatePower+(rd.getAccumulatePower()==null?0:rd.getAccumulatePower());
				}				
		    	String key=areaId+"-"+startTime;
		    	
		    	String uuid=ElecResultKeysCache.get(key);
		    	if(uuid!=null){
//		    		String uuid=timercache.get(key);
		    		Map para=new HashMap();
		    		para.put("id",uuid);
		    		para.put("areaId",areaId);		    		
					para.put("start_time",new Timestamp(startTime));
					para.put("power",power);
					para.put("reactive_power",reactivePower);
					para.put("power_factor",powerFactor/resultDataList.size());
					para.put("apparent_power", apparentPower);
					para.put("reactive_energy", reactiveEnergy);
					para.put("reactive_demand",reactiveDemand);
					para.put("active_demand",activeDemand);
					para.put("accumulatePower", accumulatePower);
					//para.put("demandTime",demandTime);
					para.put("deviceNum",deviceNum);
					this.updateCache.add(para);
		    	}else{
		    		uuid=areaId+UUIDGenerator.randomUUID();		    		
		    		Map para=new HashMap();
		    		para.put("id",uuid);
		    		para.put("areaId",areaId);		    		
					para.put("start_time",new Timestamp(startTime));
					para.put("power",power);
					para.put("reactive_power",reactivePower);
					para.put("power_factor",powerFactor/resultDataList.size());
					para.put("apparent_power", apparentPower);
					para.put("reactive_energy", reactiveEnergy);
					para.put("reactive_demand",reactiveDemand);
					para.put("active_demand",activeDemand);
					para.put("demandTime",demandTime==0?null:new Timestamp(Long.parseLong(stString)));
					para.put("deviceNum",deviceNum);
					para.put("accumulatePower", accumulatePower);
		    		this.insertCache.add(para);
//		    		timercache.set(key,uuid,1000*60*2);
		    		ElecResultKeysCache.addKey(key,uuid,conf.CLIENT_REPORT_INTERVAL*10);
		    	}	
		    	if(this.insertCache.size()==1000 || this.updateCache.size()==1000){
		    		this.flushCache();
		    	}
			}
		
	    }
	  
	  public void flushCache(){
		   if(this.insertCache.size()==0 &&
				   this.updateCache.size()==0){
			   return;
		   }
//		   System.out.println(this.insertCache.size()+"----"+this.updateCache.size());
		   SqlSessionFactory  sqlSessionFactory =DataSourceSessionFactory.getInstance().getSqlSessionFactory();
		   SqlSession session=null;
		   try{
			   session =sqlSessionFactory.openSession(false);
			   ResultDataMapper resultDataMapper = session.getMapper(ResultDataMapper.class);			   
			   if(this.insertCache.size()!=0){				  
				   resultDataMapper.batchInsertResult(this.insertCache);				   
			   }			   
			   if(this.updateCache.size()!=0){
				   resultDataMapper.batchUpdateResult(this.updateCache);				   
			   }
			   session.commit();			 
//			   System.out.println(this.key+" update sucess:"+this.insertCache.size()+"---"+this.updateCache.size());
			   this.insertCache=new ArrayList();
			   this.updateCache=new ArrayList();
			   this.initTime=System.currentTimeMillis();
		   }catch(Exception e){
			  System.out.println(this.key+" update error:"+this.insertCache.size()+"---"+this.updateCache.size());
			  session.rollback();
			  LOGGER.error("",e);
			  for(int i=0;i<insertCache.size();i++){
				  Map m=(Map) insertCache.get(i);
				  String areaid=(String) m.get("areaId");		    		
				  long t=((Timestamp) m.get("start_time")).getTime();
				  ElecResultKeysCache.remove(areaid+"-"+t);				  
			  }
			  LogCountErrorCache.addErrorCache(insertCache, updateCache);
			  this.insertCache=new ArrayList();
			  this.updateCache=new ArrayList();
			  this.initTime=System.currentTimeMillis();
		   }finally{
			   if(session!=null){
				   session.close();
			   }
		   }		   
	  }
	  public void addLog(String key,ResultData rd){
		  	synchronized (cache) {			
				List lst=cache.get(key);
				if(lst==null){
					lst=new ArrayList();
					cache.put(key, lst);			
				}
				lst.add(rd);
		  	}
		}

}
