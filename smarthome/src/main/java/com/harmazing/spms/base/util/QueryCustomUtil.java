package com.harmazing.spms.base.util;


/**
 * query custom util.
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public class QueryCustomUtil {
	
	public static final String GETHOSPITALTARGETDB =  "select t.id,"+
												      "       t2.targetname,"+
												      "       t3.hospitalname,"+
												      "       t3.hospitalcode"+
													  "     from tb_hospital_target t"+
													  "	   left join tb_target_target t2"+
													  "	     on t.targetentity_id = t2.id"+
													  "   left join tb_hospital_businessdata t1"+
													  "	     on t.id = t1.hospitaltargetentity_id"+
													  "	    and t1.year = '2012'"+
													  "	  where t1.hospitaltargetentity_id is null"+
													  "	    and datacapturemethod = '0001'"+
													  "	    and frequencyanalysis = '0004'";
}
