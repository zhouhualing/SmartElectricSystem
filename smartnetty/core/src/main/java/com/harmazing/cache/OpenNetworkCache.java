package com.harmazing.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.server.DefaultServerHandler;


public class OpenNetworkCache extends Thread {	
	public final static Logger LOGGER = LoggerFactory.getLogger(OpenNetworkCache.class);
	
	public static Map<String,OpenNetworkCacheElem> map=new ConcurrentHashMap<String, OpenNetworkCacheElem>();
	
	public synchronized void  pushInfo(String gw_mac, String user_code, int seqnum, long time){
		OpenNetworkCacheElem elem = new OpenNetworkCacheElem();
		elem.userCode = user_code;
		elem.time = time;
		elem.seqnum = seqnum;
		elem.mac = gw_mac;
		map.put( gw_mac, elem);
	}
	public synchronized OpenNetworkCacheElem get(String gw_mac){
		return map.get(gw_mac);
	}
	
	public synchronized void remove(String gw_mac){
		map.remove(gw_mac);
	}
	
	public static OpenNetworkCacheElem findMatchedResponseBySeq(int seq ){
		return map.get(seq);		
	}
	
	public synchronized int count(){
		return map.size();
	}
	
	public void run(){
		OpenNetworkCacheElem elem = null;		
		while(true){
			Iterator it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry entry = (Map.Entry) it.next();
				elem = (OpenNetworkCacheElem)entry.getValue();
				long current_time = System.currentTimeMillis();
				if( (current_time - elem.time) > 60*1000){
					LOGGER.debug("OpenNetworkCache.run, remove seq_num=" + elem.seqnum + ", mac="+ elem.mac);
					it.remove();					
				}
			}
			try {
				Thread.sleep(5*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
