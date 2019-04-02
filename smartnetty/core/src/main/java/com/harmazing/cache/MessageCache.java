package com.harmazing.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.harmazing.entity.ResultData;

public class MessageCache {

	public static Stack<Map<String,String>> message=new Stack<Map<String,String>>();
	public static Stack<Map<String,String>> result=new Stack<Map<String,String>>();
	public static Map<String,List<ResultData>> logResult=new HashMap<String,List<ResultData>>();
	
	public static void put(Map<String,String> log){
		message.push(log);
		result.push(log);
//		System.out.println("put to cache");
	}
	
	public synchronized  static void addLog(String key,ResultData rd){
		List lst=logResult.get(key);
		if(lst==null){
			lst=new ArrayList();
			logResult.put(key, lst);			
		}
		lst.add(rd);
	}
	
	public static Map<String,List<ResultData>> getResultData(){
		Map<String,List<ResultData>> map=new HashMap<String,List<ResultData>>();
		if(logResult.size()>10000){
			System.out.println("logResult size:"+logResult.size());
		}
		synchronized(logResult){
			map.putAll(logResult);
			logResult.clear();
		}
		return map;
	}
	
	public static List<Map<String,String>> get(){	
		System.out.println("get message "+message.size());
		synchronized (message) {
			List lst=new ArrayList();
			if(message.size()<1000){
				lst.addAll(message);
				message.clear();
				System.out.println("get message "+message.size());
				
			}else{
				for(int i=0;i<1000;i++){
					lst.add(message.pop());
				}		
				System.out.println("get message "+message.size());
			}
			
			return lst;
		}
		
	}
	public static Stack<Map<String,String>> getResult(){	
//		System.out.println("get result data "+result.size());
		synchronized (result) {
			Stack<Map<String,String>> lst=new Stack<Map<String,String>>();
			if(result.size()<5000){
				lst.addAll(result);				
				result.clear();
//				System.out.println("get result data "+result.size());
			}else{
				for(int i=0;i<5000;i++){
					lst.push(result.pop());
				}
				System.out.println("get result data "+result.size());
			}
//			System.out.println("get result data sucess");
			return lst;
		}
		
	}
	public static void main(String[] args){
		Map m1=new HashMap();
		Map m2=new HashMap();
		m1.put("test1","test1");
		m2.putAll(m1);
		m1.clear();
		System.out.println(m1.get("test"));
		System.out.println(m2.get("test1"));
	}
	
}
