package com.harmazing.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.App;
import com.harmazing.cache.MessageCache;
import com.harmazing.service.impl.LogServiceImpl;

public class ACLogThread extends Thread {
	 public Logger LOGGER = LoggerFactory.getLogger(ACLogThread.class);
	 
	 public void run() {
		 while(true){
	     	try{
	//         	System.out.println("*******************get log result Data***********************");
	//          Stack<Map<String, String>> stack = RedisContext.getResultLogList();                        
	         	Stack<Map<String, String>> stack = MessageCache.getResult();
	             if(stack!=null && stack.size()>0){
	             	new LogServiceImpl().appendAirResultData(stack);
	             }		
	             Thread.sleep(10);
	     	}catch(Exception e){
	     		LOGGER.error("appendAirResultData",e);
	     	}
		 }
     }
}
