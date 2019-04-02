package com.harmazing.mapper;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.python.antlr.ast.Str;

import com.harmazing.util.UdpLogger;

public class DeviceSqlProvider {
	
	final static String TABLE_NAME  = "spms_device";
	final static String TB_AC       = "spms_ac";
	final static String TB_GW       = "spms_gateway";
	final static String TB_USER_DEV = "spms_user_device";
	
	////////////// Added by Anfeng /////////////////
	public String getAcsByGwId( String gw_id){
		String sql = "SELECT * FROM " + TB_AC + " WHERE gw_id= #{gw_id}";
		
		return sql;
	}
		
	///////////////////////////////////////////////
	public String getAcByAcMac(String ac_mac){
		String sql = "SELECT * FROM " + TB_AC + " WHERE ac_mac=#{ac_mac}";
		
		return sql;
	}

	///////////////////////////////////////////////
	public String updateGwAndACBinding(String gw_id, String ac_id){
		String sql = "UPDATE " + TB_AC + " SET gw_id=#{gw_id} WHERE id=#{ac_id}";
		
		return sql;
	}
	
	///////////////////////////////////////////////
	public String insertAcAndUserBinding( Map<String, Object> params){
		String sql = "INSERT INTO " + TB_USER_DEV + " (id, device_id, device_type, is_primary, user_id) VALUES (";
		
		Object id          = params.get("id");
		Object device_id   = params.get("device_id");
		Object device_type = params.get("device_type");
		Object is_primary  = params.get("is_primary");
		Object user_id     = params.get("user_id");
		
		if( id != null )          sql += "#{id},";
		if( device_id != null )   sql += "#{device_id},";
		if( device_type != null ) sql += "#{device_type},";
		if( is_primary != null )  sql += "#{is_primary},";
		if( user_id != null )     sql += "#{user_id}";
		
		sql += ")";
		
		return sql;		
	}	
	
	////////////////////////////////////////////////
	public String deleteAcAndUserBinding( Map<String, Object> params){
		String sql = "DELETE FROM " + TB_USER_DEV + "WHERE ";
		
		Object device_id = params.get("device_id");
		Object user_id   = params.get("user_id");
		
		if( device_id != null ) sql += ("device_id = #{device_id} AND ");
		if( user_id != null )   sql += ("user_id = #{user_id}");
		
		return sql;
	}	
	
	////////////////////////////////////////////////
	public String getAllGateways(){
		String sql = "SELECT * FROM " + TB_GW;
		
		return sql;
	}
	////////////////////////////////////////////////
	public String getGWIdByGwMac(String gw_mac){
		String sql = "SELECT id FROM " + TB_GW + " WHERE mac= #{gw_mac}";
		
		return sql;
	}
	
	////////////// End Add//////////////////////////
	public String updateByDevice(Map<String,Object> parameters){
		String sql = "UPDATE " + TABLE_NAME + " set sessionId = #{session},  server = #{server} ";
		
		Object operationStatus = parameters.get("operationStatus");
		if(operationStatus != null ){
	        sql += " , operStatus = #{operationStatus}";
		}		
		/*String session = (String) parameters.get("session");
		if(session != null ){
	        sql += ", sessionId = #{session}";
		}*/
		Object onOff =  parameters.get("onOff");
		if(onOff != null ){
	        sql += ", onOff = #{onOff}";
		}
		Object temperature =  parameters.get("temperature");
		if(temperature != null ){
	        sql += ", temp = #{temperature}";
		}
		Object acTemperature =  parameters.get("acTemperature");
		if(acTemperature != null ){
	        sql += ", acTemp = #{acTemperature}";
		}
		Object power =  parameters.get("power");
		if(power != null ){
	        sql += ", power = #{power}";
		}
		Object speed =  parameters.get("speed");
		if(speed != null ){
	        sql += ", speed = #{speed}";
		}
		Object direction =  parameters.get("direction");
		if(direction != null ){
	        sql += ", direction = #{direction}";
		}
		Object startTime = parameters.get("startTime");
		if(startTime != null ){
	        sql += ", startTime = #{startTime}";
		}
		String mode = (String) parameters.get("mode");
		if(mode != null ){
	        sql += ", mode = #{mode}";
		}
		Object remain = parameters.get("remain");
		if(remain != null ){
	        sql += ", remain = #{remain}";
		}
		Object accumulatePower = parameters.get("accumulatePower");
		if(accumulatePower != null ){
	        sql += ", accumulatePower = #{accumulatePower}";
		}
		/*Object server = parameters.get("server");
		if(server != null ){
	        sql += ", server = #{server}";
		}*/
          
        sql += " where id = #{id}";
        
        return sql;
	}
	
	public String getGatewayByMacAndSN(Map<String,Object> parameters){
		
		String sql = "SELECT DISTINCT supb.user_id as userid,sd.id,sd.mac,sd.type as type FROM spms_user_product_binding supb,spms_device sd WHERE supb.gwId = sd.id AND sd.mac=#{mac}";
		
		Object sn = parameters.get("sn");			
		
		if(sn != null && !"NONE".equalsIgnoreCase(sn.toString())){
			sql += " AND sd.sn=#{sn}";
		}
		
		return sql;
	}
	public String batchUpdateDevices(Map<String, List<Map<String,Object>>> params){
		List<Map<String,Object>> list = (List<Map<String,Object>>)params.get("list");
        if(list!=null){
            StringBuffer sb = new StringBuffer("update ").append(TABLE_NAME + " set ");
            MessageFormat mf = new MessageFormat("operStatus=#'{'list[{0}].operStatus}, onOff=#'{'list[{0}].onOff}, temp=#'{'list[{0}].temp}, acTemp=#'{'list[{0}].acTemp}, power=#'{'list[{0}].power}," +
            		"speed=#'{'list[{0}].speed},direction=#'{'list[{0}].direction},startTime=#'{'list[{0}].startTime},mode=#'{'list[{0}].mode}," +
            		"remain=#'{'list[{0}].remain},accumulatePower=#'{'list[{0}].accumulatePower},sessionId=#'{'list[{0}].sessionId},server=#'{'list[{0}].server} ");
            MessageFormat mf2 = new MessageFormat("id=#'{'list[{0}].id}");
            for (int i=0; i<list.size(); i++) {
                sb.append(mf.format(new Object[]{i}));
                sb.append("where ").append(mf2.format(new Object[]{i}));
                if (i < list.size() - 1) {  
                    sb.append(",");  
                } 
            }
//            System.out.println(sb.toString());
            return sb.toString();
        }
        return null;
	}
	
	public String batchUpdateAc(Map<String, List<Map<String,Object>>> params){
		List<Map<String,Object>> list = (List<Map<String,Object>>)params.get("list");
        if(list!=null){
            StringBuffer sb = new StringBuffer("update ").append(TB_AC + " set ");
            MessageFormat mf = new MessageFormat("operStatus=#'{'list[{0}].operStatus}, onOff=#'{'list[{0}].onOff}, temp=#'{'list[{0}].temp}, acTemp=#'{'list[{0}].acTemp}, power=#'{'list[{0}].power}," +
            		"speed=#'{'list[{0}].speed},direction=#'{'list[{0}].direction},startTime=#'{'list[{0}].startTime},mode=#'{'list[{0}].mode}," +
            		"accumulatePower=#'{'list[{0}].accumulatePower},sessionId=#'{'list[{0}].sessionId} ");
            MessageFormat mf2 = new MessageFormat("id=#'{'list[{0}].id}");
            for (int i=0; i<list.size(); i++) {
                sb.append(mf.format(new Object[]{i}));
                sb.append("where ").append(mf2.format(new Object[]{i}));
                if (i < list.size() - 1) {  
                    sb.append(",");  
                } 
            }
//            System.out.println(sb.toString());
            String str= sb.toString();
            String str1 = str.substring(0,str.length()/2);
            String str2 = str.substring(str.length()/2 + 1,str.length());
        	UdpLogger.mtImportant("batchUpdateAc sql: \n" + str1 );
        	UdpLogger.mtImportant("batchUpdateAc sql: \n" + str2 );
            return sb.toString();
        }
        return null;
	}
	
	
}
