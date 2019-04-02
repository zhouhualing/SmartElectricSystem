package com.harmazing.Thread;

import java.util.Iterator;
import java.util.Map;

import com.harmazing.cache.OpenNetworkCacheElem;
import com.harmazing.Config;
import com.harmazing.cache.OpenNetworkCache;

public class OpenNetworkThread extends Thread {
	public void run() {
		
		 while(true){
			 Map<String, OpenNetworkCacheElem> map = OpenNetworkCache.map;
			 
			 Iterator<Map.Entry<String, OpenNetworkCacheElem>> it = map.entrySet().iterator();
			 while(it.hasNext()){
				 Map.Entry<String, OpenNetworkCacheElem> entry = it.next();
				 String key = entry.getKey();
				 OpenNetworkCacheElem elem = entry.getValue();
				 
				 long time = System.currentTimeMillis();
				 if( (time - elem.time) > Config.getInstance().THREAD_OPENNETWORK_TIMEOUT){ 
					 it.remove();
					 System.out.println( key + " had been removed!");
				 }
			 }
			 
			 try{
				 Thread.sleep(2000);
			 }catch(Exception e){
				 e.printStackTrace();
			 }
		 }
    }
}
