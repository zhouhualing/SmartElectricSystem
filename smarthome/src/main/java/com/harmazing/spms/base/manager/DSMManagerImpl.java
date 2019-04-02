/**
 * 
 */
package com.harmazing.spms.base.manager;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harmazing.spms.base.dao.DSMDAO;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.dto.DSMDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.spms.base.manager.DSMManager;

@Service("dsmManager")
public class DSMManagerImpl implements DSMManager {
    
    @Autowired
    private DSMDAO dsmDAO;
    
    @Autowired
    private QueryDAO queryDAO;

	@SuppressWarnings("finally")
	@Override
	public DSMDTO getUserInfo(DSMDTO dSMDTO) {
		 SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		List<  Map<String, Object>  > l = queryDAO.getMapObjects( "select * from spms_ndr_dsm t where t.faId = '"+dSMDTO.getId()+"'" );
		if( l != null && l.size() != 0 ){
			for (Map<String, Object> map : l) {
				try {
					dSMDTO.setFaId(map.get("faId")+"");
					dSMDTO.setId(map.get("id")+"");
					dSMDTO.setInfoType(  Integer.parseInt(map.get("infoType")+"")  );
					dSMDTO.setReleaseDate( map.get("releaseDate") != null ? sdf.parse(map.get("releaseDate")+"") : null);
					dSMDTO.setAbateDate(map.get("abateDate") != null ? sdf.parse(map.get("abateDate")+"") : null);
					dSMDTO.setConfirmDate(map.get("confirmDate") != null ? sdf.parse(map.get("confirmDate")+"") : null);
					dSMDTO.setMsgDes(map.get("msgDes")+"");
					dSMDTO.setReleaseUser(map.get("releaseUser")+"");
					dSMDTO.setSchemeCode(map.get("schemeCode")+"");
					dSMDTO.setSchemeName(map.get("schemeName")+"");
					dSMDTO.setSchemeType(map.get("schemeType")+"");
					dSMDTO.setResType(map.get("resType")+"");
					dSMDTO.setImpDate(map.get("impDate") != null ? sdf.parse(map.get("impDate")+"") : null);
					
					dSMDTO.setStartDate(map.get("startDate") != null ? sdf.parse(map.get("startDate")+"") : null);
					dSMDTO.setEndDate(map.get("endDate") != null ? sdf.parse(map.get("endDate")+"") : null);
					
					dSMDTO.setStartDateS(map.get("startDate")+"" );
					dSMDTO.setEndDateS(map.get("endDate")+"" );
					
					dSMDTO.setPlanReduceTargetP( map.get("planReduceTargetP") != null ? Double.valueOf(map.get("planReduceTargetP")+"") : 0 );
					dSMDTO.setPlanReduceTargetQ( map.get("planReduceTargetQ") != null ? Double.valueOf(map.get("planReduceTargetQ")+"") : 0 );
					dSMDTO.setPromisedReduceTargetP( map.get("promisedReduceTargetP") != null ? Double.valueOf(map.get("promisedReduceTargetP")+"") : 0 );
					dSMDTO.setPromisedReduceTargetQ( map.get("promisedReduceTargetQ") != null ? Double.valueOf(map.get("promisedReduceTargetQ")+"") : 0 );
					dSMDTO.setActualReduceP( map.get("actualReduceP") != null ? Double.valueOf(map.get("actualReduceP")+"") : 0 );
					dSMDTO.setActualReduceQ( map.get("actualReduceQ") != null ? Double.valueOf(map.get("actualReduceQ")+"") : 0 );
					dSMDTO.setStartConditions(map.get("startConditions") != null ? map.get("startConditions")+"" : "");
					dSMDTO.setContact(map.get("contact") !=null ? map.get("contact")+"" : "");
					dSMDTO.setContactPhone(map.get("contactPhone") != null ? map.get("contactPhone")+"" : "");
					dSMDTO.setSchemePeople(map.get("schemePeople") != null ? map.get("schemePeople")+"" : "");
					dSMDTO.setSchemeDate(map.get("schemeDate") != null ? map.get("schemeDate")+"" : "");
					dSMDTO.setSchemeIntroduction(map.get("schemeIntroduction") != null ? map.get("schemeIntroduction")+"" : "");
					dSMDTO.setStatus(map.get("status") != null ? map.get("status")+"" : "");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					return dSMDTO;
				}
			}
			
		}
		return dSMDTO;
	}

	@SuppressWarnings("finally")
	@Override
	public boolean promiseSet(DSMDTO dSMDTO) {
		// TODO Auto-generated method stub
		boolean r = true;
		try {
			if(dSMDTO.getStatus()!=null && "REFUSED".equals(dSMDTO.getStatus())){
				queryDAO.doExecuteSql("UPDATE spms_ndr_dsm SET status = '"+
						dSMDTO.getStatus()+"' "+
						" WHERE faId = '"+dSMDTO.getFaId()+"'");
			}else{
				queryDAO.doExecuteSql("UPDATE spms_ndr_dsm SET " +
						"promisedReduceTargetP = "+dSMDTO.getPromisedReduceTargetP()+"," +
						"promisedReduceTargetQ = "+dSMDTO.getPromisedReduceTargetQ()+"," +
						"status = '"+dSMDTO.getStatus()+"'"+
						" WHERE faId = '"+dSMDTO.getFaId()+"'");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			r = false;
			e.printStackTrace();
		}finally{
			return r;
		}
	}

	@SuppressWarnings({ "finally", "unchecked" })
	@Override
	public boolean deleteDSM(Map<String, Object> m) {
		boolean r = true;
		try {
			if( m.get("faId")!=null ){
				String sql = "delete from spms_ndr_dsm "+
						" WHERE faId = '"+(m.get("faId")+"'");
				queryDAO.doExecuteSql(sql);
			}else{
				List<Object> l = (List<Object>) m.get("id");
				String ids = "";
				for (Object object : l) {
					Map<String,Object> mm = new HashMap<String,Object>();
					mm  = ((Map<String,Object>)object);
					ids  +="'"+ mm.get("faId")+"',";
				}
				if(ids.length()>0){
					String sql = "delete from spms_ndr_dsm "+
							" WHERE faId in ("+ids.substring(0,ids.length()-1)+")";
					queryDAO.doExecuteSql(sql);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			r = false;
			e.printStackTrace();
		}finally{
			return r;
		}
	}
}
