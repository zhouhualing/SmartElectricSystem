package com.harmazing.mapper;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by ming on 14-9-10.
 */
public class LogSqlProvider {
    public final static String AC_STATUS_TB_NAME = "spms_ac_status";
    public final static String GATEWAY_STATUS_TB_NAME = "spms_gw_status";
    
    public String batchInsertGWLog(Map<String, List<Map<String,Object>>> params){
        List<Map<String,Object>> list = (List<Map<String,Object>>)params.get("list");
        if(list!=null){
            StringBuffer sb = new StringBuffer("insert into ").append(GATEWAY_STATUS_TB_NAME).append(" ( id , device_id , status , user_id , create_time) values ");
            MessageFormat mf = new MessageFormat("(#'{'list[{0}].id}, #'{'list[{0}].deviceId}, #'{'list[{0}].status}, #'{'list[{0}].userId}, #'{'list[{0}].timestamp})");
            for (int i=0; i<list.size(); i++) {
                sb.append(mf.format(new Object[]{i}));  
                if (i < list.size() - 1) {  
                    sb.append(",");  
                }  
            }
//            System.out.println(sb.toString());
            return sb.toString();
        }
        return null;
    }
    public String batchInsertACLog(Map<String, List<Map<String,Object>>> params){
        List<Map<String,Object>> list = (List<Map<String,Object>>)params.get("list");
        if(list!=null){
            StringBuffer sb = new StringBuffer("insert into ").append(AC_STATUS_TB_NAME)
            		.append("( id , device_id , on_off , temp , humidity, power , accumulatePower , start_time , reactivePower , reactiveEnergy ,"
            				+ " apparentPower , voltage , current , frequency , powerFactor , demandTime ,period , activeDemand ,"
            				+ " reactiveDemand ) values ");
            MessageFormat mf = new MessageFormat("(#'{'list[{0}].id}, #'{'list[{0}].deviceId}, #'{'list[{0}].onOff}, "
            		+ "#'{'list[{0}].currentTemperature}, + #'{'list[{0}].humidity}, #'{'list[{0}].power}, #'{'list[{0}].accumulatePower}, #'{'list[{0}].currentTime},"
            		+ "#'{'list[{0}].reactivePower}," +
		            "#'{'list[{0}].reactiveEnergy},#'{'list[{0}].apparentPower},#'{'list[{0}].voltage},#'{'list[{0}].current},#'{'list[{0}].frequency},"
		            + "#'{'list[{0}].powerFactor},#'{'list[{0}].startTime}," +
		            "#'{'list[{0}].period},#'{'list[{0}].activeDemand},#'{'list[{0}].reactiveDemand})");
            for (int i=0; i<list.size(); i++) {
                sb.append(mf.format(new Object[]{i}));  
                if (i < list.size() - 1) {  
                    sb.append(",");  
                }  
            }
//            System.out.println(sb.toString());
            return sb.toString();
        }
        return null;
    }
}
