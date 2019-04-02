package com.harmazing;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Thread.ACLogConsumerHandlerThread;
import com.harmazing.Thread.ACLogThread;
import com.harmazing.Thread.FlushWdLogThread;
import com.harmazing.Thread.LogResultThread;
import com.harmazing.Thread.SaveGWLogThread;
import com.harmazing.cache.ACLogConsumerCache;
import com.harmazing.cache.LogCountErrorCache;
import com.harmazing.cache.MessageCache;
import com.harmazing.constant.MessageKey;
import com.harmazing.mq.ACLogConsumer;
import com.harmazing.mq.AcZigbeeConsumer;
import com.harmazing.mq.DeviceInfoConsumer;
import com.harmazing.mq.DeviceStatusConsumer;
import com.harmazing.mq.GWLogConsumer;
import com.harmazing.mq.GwZigbeeConsumer;
import com.harmazing.mq.MQUtil;
import com.harmazing.mq.WinDoorLogConsumer;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import com.harmazing.server.CommandMessageServer;
import com.harmazing.server.DefaultServer;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceServiceImpl;
import com.harmazing.service.impl.LogServiceImpl;

/**
 *
 */
public class Client 
{
    public final static Logger LOGGER = LoggerFactory.getLogger(Client.class);
    
    public final static ExecutorService executorService = Executors.newFixedThreadPool(100);

    public static void updateTask() throws Exception {
        LOGGER.info("Starting Server...");
        try {
        	//加载解析配置文件到内存
            Config config = Config.getInstance();            
            //初始化ACTIVEMQ
            MQUtil.init();
            
            int logConsumer=0;
            int infoConsumer=0;
            int statusConsumer=0;
            
            logConsumer=Integer.parseInt(config.getPro("activemq.logConsumer"));
            statusConsumer=Integer.parseInt(config.getPro("activemq.statusConsumer"));
            infoConsumer=Integer.parseInt(config.getPro("activemq.infoConsumer"));
            
            int gwConsumer=Integer.parseInt(config.getPro("activemq.gwConsumer"));
            int wdConsumer=Integer.parseInt(config.getPro("activemq.wdConsumer"));
            int zigbeeConsumer=Integer.parseInt(config.getPro("activemq.zigbeeConsumer"));
            //创建MQ消费者统计设备信息
            for(int i=0;i<logConsumer;i++){
            	ACLogConsumerCache.add(new ACLogConsumer(MessageKey.AC_LOG_PREFIX));
            }  
            //定时处理没导入成功的数据
            new LogCountErrorCache.ErrorHandlerThread().start();
            
            new  ACLogConsumerHandlerThread().start();
            
            //创建MQ消费者更新设备状态
            for(int i=0;i<infoConsumer;i++){
            	new DeviceInfoConsumer(MessageKey.DEVICE_INFO_PREFIX);
            }  
            //处理网关日志
            SaveGWLogThread gwlog=new SaveGWLogThread();            
            for(int i=0;i<gwConsumer;i++){
            	gwlog.addConsumer(new GWLogConsumer(MessageKey.GW_LOG_PREFIX));
            }  
            gwlog.start();
            //定时清理门窗缓存日志
            FlushWdLogThread wdlog=new FlushWdLogThread();
            for(int i=0;i<wdConsumer;i++){
            	wdlog.addThread(new WinDoorLogConsumer(MessageKey.WD_LOG_PREFIX));
            }            
            wdlog.start();
            
            //处理网关Zigbee信息
            for(int i=0;i<zigbeeConsumer;i++){
            	new GwZigbeeConsumer(MessageKey.GATEWAY_ZIGBEE);
            }
            //处理空调Zigbee信息
            for(int i=0;i<zigbeeConsumer;i++){
            	new AcZigbeeConsumer(MessageKey.AC_ZIGBEE);
            }
          
            System.out.println("client start");
        } catch (Exception e) {
            LOGGER.error("server error",e);
        }
           
    }
    public static void main(String[] args) throws Exception {
    	updateTask();
	}
}
