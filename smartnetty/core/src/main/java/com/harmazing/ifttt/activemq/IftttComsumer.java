package com.harmazing.ifttt.activemq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.jms.pool.PooledConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import com.harmazing.cache.ActiveCommandQueue;
import com.harmazing.entity.status.PirStatus;
import com.harmazing.entity.status.WinDoorStatus;
import com.harmazing.ifttt.IftttAction;
import com.harmazing.ifttt.IftttCondition;
import com.harmazing.ifttt.IftttEntity;
import com.harmazing.ifttt.IftttService;
import com.harmazing.ifttt.IftttServiceImpl;
import com.harmazing.mq.MQUtil;
import com.harmazing.protobuf.DeviceProtos.DeviceSpecific;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceServiceImpl;
import com.jcraft.jsch.Logger;

public class IftttComsumer implements MessageListener {
	public static PooledConnection conn = null;
	public Session session = null;
	public Destination dest = null;
	
	public MessageConsumer consumer = null;
	
	private Map<String, List<JSONArray>> mac_cond_map = null;
	
	private RedisContext rc = null;
	
	private IftttCondition condition = null;
	private IftttAction action = null;
	
	private DeviceService dev_srv = null;
	
	////////////////////////////////////////////////////////
	public IftttComsumer(String str_dest, ActiveCommandQueue queue){
		try{
			conn = MQUtil.getConn();
			conn.start();
			
			session  = conn.createSession(Boolean.FALSE,  Session.AUTO_ACKNOWLEDGE);
			dest     = session.createQueue( str_dest+"?consumer.prefetchSize=1");
			consumer =session.createConsumer(dest);		
			consumer.setMessageListener(this);
			
			mac_cond_map = new HashMap<String, List<JSONArray>>();
			preAnalysisStrategy();
			
			rc = RedisContextFactory.getInstance().getRedisContext();
			
			dev_srv = new DeviceServiceImpl();
			condition = new IftttCondition(dev_srv);
			action = new IftttAction(dev_srv, queue);
			
		}catch(JMSException e){
			e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////
	@Override
	public void onMessage(Message msg) {
		try{
			ObjectMessage obj_msg = (ObjectMessage)msg;
			Map<String, String> obj_map = (Map)obj_msg.getObject();
			int dev_type = Integer.parseInt(obj_map.get("type"));
			String mac = obj_map.get("mac");			
			
			if(dev_type == DeviceSpecific.DeviceType.DOOR_WINDDOW_SENSOR_VALUE){
				if(mac_cond_map.containsKey(mac) ){
					int open = Integer.parseInt(obj_map.get("open"));
					
					Set<String>  keys = rc.keys("IFTTT:RT:"+mac+"*");
					java.util.Iterator<String> it = keys.iterator();				
					
					while(it.hasNext()){
						String key = it.next();
						if( key.contains(mac)){
							Map<String, String> map = rc.getIftttStrategyFromRedis(key);
							String str_con = map.get("condition");
							String str_action = map.get("action");
							
							Map<String, String> rt_map = new HashMap<String, String>();
							rt_map.put("onOff", String.valueOf(open));
							
							if(condition.check(str_con, true, rt_map)){					
								action.execute(str_action);
							}
						}
					}
					
				}
				
				
			}else if(dev_type == DeviceSpecific.DeviceType.PIR_SENSOR_VALUE){
				
				
				
				System.out.println("***************** recevied PIR Alarmed = 1");
			}
		
	
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	/////////////////////////////////////////////////////////
	private void preAnalysisStrategy(){
		IftttService srv = new IftttServiceImpl();
		List<IftttEntity> list = srv.getAllEntities();
		for(int i=0; i<list.size(); ++i){
			IftttEntity ifttt = (IftttEntity)list.get(i);
			String id = ifttt.getId();
			String condition = ifttt.getCondition();
			
			JSONObject obj = new JSONObject(condition);
			JSONArray array = obj.getJSONArray("condition");
						
			for(int j=0; j<array.length(); ++j){
				JSONObject con = (JSONObject)array.get(j);
				try{
					String mac = (String)con.getString("mac");
					if(mac_cond_map.containsKey(mac)){
						mac_cond_map.get(mac).add(array);					
					}else{
						List<JSONArray> l = new ArrayList<JSONArray>();
						l.add(array);
						mac_cond_map.put(mac,  l);
					}
				}catch(Exception e){
					
				}
			}			
		}
	}	
}
