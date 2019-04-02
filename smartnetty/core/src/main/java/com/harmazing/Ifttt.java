package com.harmazing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.cache.ActiveCommandQueue;
import com.harmazing.cache.OpenNetworkCache;
import com.harmazing.constant.MessageKey;
import com.harmazing.ifttt.IftttRedis;
import com.harmazing.ifttt.IftttThread;
import com.harmazing.ifttt.activemq.IftttComsumer;
import com.harmazing.ifttt.activemq.IftttListener;
import com.harmazing.mq.MQUtil;
import com.harmazing.server.CommandMessageServer;
import com.harmazing.server.DefaultServer;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceServiceImpl;
import com.harmazing.util.UdpLogger;

public class Ifttt {
	public final static Logger LOGGER = LoggerFactory.getLogger(App.class);
//  public final static ExecutorService executorService = Executors.newFixedThreadPool(20);
//  public final static ExecutorService logExecutorService = Executors.newFixedThreadPool(1);
//  public final static ExecutorService resultDataExecutorService = Executors.newFixedThreadPool(5);
//  public final static ExecutorService deviceExecutorService = Executors.newFixedThreadPool(20);	
	  
//  public final static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(20);
//  public final static ScheduledExecutorService logExecutorService = Executors.newScheduledThreadPool(1);
//  public final static ScheduledExecutorService resultDataExecutorService = Executors.newScheduledThreadPool(1);
//  public final static ScheduledExecutorService deviceExecutorService = Executors.newScheduledThreadPool(20);
  
  public static void updateTask() throws Exception {
      LOGGER.info("Starting IfTTT...");
      try {
      	
          LOGGER.info("Start to intialize GW message listening server." );
          ActiveCommandQueue activeQueue = new ActiveCommandQueue();
          
          IftttRedis.syncStrategy2Redis();
          
          IftttThread ifttt = new IftttThread(activeQueue);
          ifttt.start();
          
          MQUtil.init();
          for(int i=0; i<10; ++i){
        	  IftttComsumer consumer = new IftttComsumer(MessageKey.IFTTT_SENSOR, activeQueue);
          }
          /*
          for(int i=0; i<1; ++i){
        	  IftttComsumer consumer = new IftttComsumer(MessageKey.IFTTT_SENSOR, activeQueue);
          }*/
          
         
          
          while(true){
        	  try{
        		  Thread.sleep(600*1000);
        	  }catch(Exception e){
        		  e.printStackTrace();
        	  }
          }
          
      } catch (Exception e) {
          LOGGER.error("server error",e);
          UdpLogger.mtError(e);
      }           
  }
  public static void main(String[] args) throws Exception {
  	updateTask();
	}
}
