package com.harmazing;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Thread.ACLogConsumerHandlerThread;
import com.harmazing.Thread.ACLogThread;
import com.harmazing.Thread.LogResultThread;
import com.harmazing.Thread.SyncGwState;
import com.harmazing.cache.ACLogConsumerCache;
import com.harmazing.cache.ActiveCommandQueue;
import com.harmazing.cache.MessageCache;
import com.harmazing.cache.OpenNetworkCache;
import com.harmazing.constant.MessageKey;
import com.harmazing.ifttt.IftttThread;
import com.harmazing.mq.ACLogConsumer;
import com.harmazing.mq.DeviceInfoConsumer;
import com.harmazing.mq.MQUtil;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import com.harmazing.server.CommandMessageServer;
import com.harmazing.server.DefaultServer;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceServiceImpl;
import com.harmazing.service.impl.LogServiceImpl;
import com.harmazing.util.MessageUtil;
import com.harmazing.util.UdpLogger;

import com.mysql.jdbc.V1toV2StatementInterceptorAdapter;


/**
 *
 */
public class App 
{
    public final static Logger LOGGER = LoggerFactory.getLogger(App.class);
//    public final static ExecutorService executorService = Executors.newFixedThreadPool(20);
//    public final static ExecutorService logExecutorService = Executors.newFixedThreadPool(1);
//    public final static ExecutorService resultDataExecutorService = Executors.newFixedThreadPool(5);
//    public final static ExecutorService deviceExecutorService = Executors.newFixedThreadPool(20);	
	  
//    public final static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(20);
//    public final static ScheduledExecutorService logExecutorService = Executors.newScheduledThreadPool(1);
//    public final static ScheduledExecutorService resultDataExecutorService = Executors.newScheduledThreadPool(1);
//    public final static ScheduledExecutorService deviceExecutorService = Executors.newScheduledThreadPool(20);
    
    public static void updateTask() throws Exception {
        LOGGER.info("Starting Server...");
        try {
        	//加载解析配置文件到内存
            Config config = Config.getInstance();           
            //启动ACTIVEMQ
            MQUtil.init();
           
            DeviceService deviceService = new DeviceServiceImpl();
            
            //put deviceInfo to redis
            if( config.DATABASE_TO_REDIS == true){
            	deviceService.initializeDevicesInfo2Redis();
            }
            
            //删除dsm信息
            deviceService.delDsmInfo();
            //把dsm信息添加到redis
            deviceService.setDsmInfo();
           

            
            
            LOGGER.info("Start to intialize OpenNetworkCache." );
            OpenNetworkCache openNetworkCache = new OpenNetworkCache();
            openNetworkCache.start();
            LOGGER.info("OpenNetowrkCache started succesfully.");
            
            
            //Create ActiveCommandQueue
            LOGGER.info("Start to intialize GW message listening server." );
            ActiveCommandQueue activeQueue = new ActiveCommandQueue();
            DefaultServer server = new DefaultServer(activeQueue, openNetworkCache);
            server.start();
            LOGGER.info("GW message listening server started succesfully."); 
           
            /*********redis监听，负责监听管理端发来的命令***********/
            LOGGER.info("Start to intialize CommandMessageServer." );
            CommandMessageServer commandMessageServer = new CommandMessageServer(activeQueue, openNetworkCache);
            commandMessageServer.start();
            LOGGER.info("Redis server stard succesfully.");
            
            if(config.IFTTT_ENABLED){
	            IftttThread ifttt = new IftttThread(activeQueue);
	            ifttt.start();
	            LOGGER.info("IFTTT started succesfully.:");
            }
            
            /******************启动同步网关状态的线程************/
            //new SyncGwState().start();
            LOGGER.info("Netty platform started succesfully.");
        } catch (Exception e) {
            LOGGER.error("server error",e);
            UdpLogger.mtError(e);
        }           
    }
    public static void main(String[] args) throws Exception {
    	
        ArrayList<Object> al = new ArrayList<Object>();
        al.add("ff");
        al.add("gg");
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(al);
        System.out.println(str);
        
        net.sf.json.JSONArray array = net.sf.json.JSONArray.fromObject(str);
        
    	updateTask();
	}
}
