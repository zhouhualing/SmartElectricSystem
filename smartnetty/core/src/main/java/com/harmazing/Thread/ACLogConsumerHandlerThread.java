package com.harmazing.Thread;

import java.util.List;

import com.harmazing.Config;
import com.harmazing.cache.ACLogConsumerCache;
import com.harmazing.mq.ACLogConsumer;

public class ACLogConsumerHandlerThread extends Thread {
	public int interval;
	public ACLogConsumerHandlerThread(){
		interval=Config.getInstance().CLIENT_REPORT_INTERVAL*1000;
	}
	public void run(){
		while(true){
			try {
				List<ACLogConsumer> lst=ACLogConsumerCache.cache;
				for(int i=0;i<lst.size();i++){
					lst.get(i).flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
