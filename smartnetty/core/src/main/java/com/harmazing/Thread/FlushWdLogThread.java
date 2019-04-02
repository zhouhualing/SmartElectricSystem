package com.harmazing.Thread;


import java.util.ArrayList;
import java.util.List;

import com.harmazing.mq.WinDoorLogConsumer;

public class FlushWdLogThread extends Thread{
	
	public List<WinDoorLogConsumer> consumers=new ArrayList<WinDoorLogConsumer>();
	public void addThread(WinDoorLogConsumer consumer){
		consumers.add(consumer);
	}
	
	public void run(){
		while(true){
			for(int i=0;i<consumers.size();i++){
				WinDoorLogConsumer consumer=consumers.get(i);
				consumer.addLog(null);
			}
			try {
				Thread.sleep(1000*60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
