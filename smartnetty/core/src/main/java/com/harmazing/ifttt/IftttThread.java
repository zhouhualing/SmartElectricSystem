package com.harmazing.ifttt;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.python.icu.util.CharsTrie.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.harmazing.Config;
import com.harmazing.cache.ActiveCommandQueue;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceServiceImpl;


public class IftttThread extends Thread {
	public static Logger LOGGER = LoggerFactory.getLogger(IftttThread.class);
	private final static String channel = "ifttt_channel";
	private int adjust_period = 5; 
	private int temp_correction = 0;
	private int humidity_correction = 0;
	
	private int regulation_interval = 0;
	
	private ActiveCommandQueue activeQueue = null;
	
	
	IftttCondition condition = null;
	IftttAction action = null; 
	
	IftttService ifttt_srv = null;
	DeviceService dev_srv = null;
	
	public IftttThread( ActiveCommandQueue queue){
		this.activeQueue = queue;
		adjust_period = Config.getInstance().IFTTT_PERIOD;
		temp_correction = Config.getInstance().IFTTT_CORRECTION_TEMP;
		humidity_correction = Config.getInstance().IFTTT_CORRECTION_HUMIDITY;
		regulation_interval = Config.getInstance().IFTTT_TIME_REGULATION_INTERVAL * 60 * 1000;
		
		ifttt_srv = new IftttServiceImpl();
		dev_srv = new DeviceServiceImpl();
		
		condition = new IftttCondition(dev_srv);
		action = new IftttAction(dev_srv, queue);
		
		this.setName("IFTTT thread");
	}
	
	
	///////////////////////////////////////////////////////////////
	public void run(){
		//runWithRedis();
		runWithDB();
	}
	
	///////////////////////////////////////////////////////////////
	public void runWithDB(){	
		
		while(true){
			
			try{					
				LOGGER.info("**** Start to regulation and adjustment...");
				this.sleep(adjust_period*60*1000);
				
				
				List<IftttEntity> entities = ifttt_srv.getAllEntities();
				if( entities != null){
					for( int i=0; i<entities.size(); ++i){
						IftttEntity ifttt = (IftttEntity)entities.get(i);
						String str_con = ifttt.getCondition();
						if( condition.check(str_con, false, null)){
							LOGGER.info("**** Make adjust-and-control, condition:\n" + str_con);
							
							String str_action = ifttt.getAction();
							action.execute(str_action);
							LOGGER.info("**** Make adjust-and-control, Action: \n" + str_action);
						}
					}
				}			
				
			}catch(Exception e){
				e.printStackTrace();
				
			}
		}
	}
	
	///////////////////////////////////////////////////////////////
	public void runWithRedis(){
		while(true){
			RedisContext rc = null;
			
			try{
				LOGGER.info("**** runWithRedis: Start to regulation and adjustment...");
				this.sleep(adjust_period*60*1000);
				//this.sleep(5*1000);
				
				rc = RedisContextFactory.getInstance().getRedisContext();
				
				Set<String>  keys = rc.keys("IFTTT:NRT:*");
				java.util.Iterator<String> it = keys.iterator();
				
				while(it.hasNext()){
					String key = (String)it.next();
					Map<String, String> map = rc.getIftttStrategyFromRedis(key);
					String str_con    = map.get("condition");
					String str_action = map.get("action");
					
					if( condition.check(str_con, false, null)){
						LOGGER.info("**** Make adjust-and-control, condition:\n" + str_con);
						
						action.execute(str_action);
						LOGGER.info("**** Make adjust-and-control, Action: \n" + str_action);
					}
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				rc.destroy();
			}
		}
	}	
	
	////////////////////////////////////////////////////////////
}
