package com.harmazing.ifttt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.harmazing.Config;
import com.harmazing.redis.RedisContext;
import com.harmazing.redis.RedisContextFactory;

public class IftttRedis {
	
	/////////////////////////////////////////////////////////////
	public static void syncStrategy2Redis(){
		String RT_RSP_KEY  = "IFTTT:RT:";
		String NRT_RSP_KEY = "IFTTT:NRT:";
		
		RedisContext rc = RedisContextFactory.getInstance().getRedisContext();		
		
		IftttService ifttt_srv = new IftttServiceImpl();
		List<IftttEntity> entities = ifttt_srv.getAllEntities();
		
		String realtime_rsp_dev_type = Config.getInstance().IFTTT_REALTIMERESPONSE_SENSORTYPE;		
		
		if( entities != null){
			for( int i=0; i<entities.size(); ++i){
				IftttEntity ifttt = (IftttEntity)entities.get(i);
				String str_con = ifttt.getCondition();
				JSONObject obj = new JSONObject(str_con);
				JSONArray array = obj.getJSONArray("condition");
				
				Map<String, String> map = new HashMap<String, String>();
				map.put("condition", str_con);
				map.put("action", ifttt.getAction());
				
				boolean need_realtime_rsp = false;
				for( int j=0; j<array.length(); ++j){
					JSONObject con = array.getJSONObject(j);
					int dev_type = (int)con.get("type");
					
					if(realtime_rsp_dev_type.contains(""+dev_type)){
						String key = (RT_RSP_KEY + con.get("mac") + ":" + ifttt.getId());						
						rc.addDevice2Redis(key, map);
						
						need_realtime_rsp = true;
					}
				}
				
				if( !need_realtime_rsp){
					String key = (NRT_RSP_KEY + ifttt.getId());
					rc.addDevice2Redis(key, map);	
					
					need_realtime_rsp = false;
				}			
			}
		}
		
		rc.destroy();
	}
	
	/////////////////////////////////////////////////////////////	

}
