package com.harmazing.spms.potaldefault.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.spms.base.dao.RoleDAO;
import com.harmazing.spms.base.entity.RoleEntity;
import com.harmazing.spms.portalcustom.dao.SpmsPortalCustomDAO;
import com.harmazing.spms.portalcustom.entity.SpmsPortalCustom;
import com.harmazing.spms.portalmodules.dao.SpmsPortalModulesDAO;
import com.harmazing.spms.portalmodules.entity.SpmsPortalModules;
import com.harmazing.spms.potaldefault.dao.SpmsPortalDefaultDAO;
import com.harmazing.spms.potaldefault.entity.SpmsPortalDefault;
import com.harmazing.spms.potaldefault.manager.SpmsPortalDefaultManager;
@Service
public class SpmsPortalDefaultManagerImpl implements SpmsPortalDefaultManager{

	@Autowired
	private SpmsPortalDefaultDAO spmsPortalDefaultDAO;
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private SpmsPortalModulesDAO spmsPortalModulesDAO;
	@Autowired
	private SpmsPortalCustomDAO spmsPortalCustomDAO;

	@Override
	public Map<String, HashMap<String, Object>> getDefault() {
		Map<String, HashMap<String, Object>> result = new HashMap<String,HashMap<String,Object>>();
		
		List<RoleEntity> roles = roleDAO.findAll();//找到所有的ROLE
		
		for(int i = 0 ; i < roles.size() ; i++){
			String roleCode = roles.get(i).getRoleCode();
			String roleName = roles.get(i).getRoleName();
			
			List<SpmsPortalDefault> list = spmsPortalDefaultDAO.getByRoleCode(roleCode);
			if(list ==null || list.size()==0){
				HashMap<String, Object> temp = new HashMap<String, Object>();
				temp.put("roleName", roleName);
				result.put(roleCode, temp);
			}
			
			if(list !=null && list.size()!=0){
				HashMap<String, Object> temp = new HashMap<String, Object>();
				temp.put("roleName", roleName);
				for(SpmsPortalDefault spd : list){
					temp.put(spd.getModules().getDivName(), true);
				}
				
				result.put(roleCode, temp);
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Object> changeDefault(Map<String , String > info) {
		Map<String,Object> result = new HashMap<String, Object>();
		Set<String> keys = info.keySet();
		Iterator it = keys.iterator();
		String rolecode = info.get("rolecode");
		if(rolecode.isEmpty()){
			rolecode = "001";
		}
		while(it.hasNext()){
			String key = (String) it.next();
			if(key.equals("rolecode")){
			}else{
				if(info.get(key).equals("1")){
					SpmsPortalDefault spd = spmsPortalDefaultDAO.getByRoleCodeModules(rolecode,key);
					if(spd == null || spd.getId() == null){
						SpmsPortalDefault s = new SpmsPortalDefault();
						SpmsPortalModules spm = spmsPortalModulesDAO.getByDivname(key);
						s.setRoleCode(rolecode);
						s.setModules(spm);
						spmsPortalDefaultDAO.save(s);
						//spmsPortalDefaultDAO.save(s);
					}
				}else{
					SpmsPortalDefault spd = spmsPortalDefaultDAO.getByRoleCodeModules(rolecode,key);
					if(spd!=null && spd.getId()!=null){
						SpmsPortalModules spm = spd.getModules();
						spmsPortalDefaultDAO.delete(spd);
					}
					
					List<SpmsPortalCustom> temp = spmsPortalCustomDAO.getByRoleCodeDivName(rolecode, key);
					for (int i = 0; i < temp.size(); i++) {
						SpmsPortalCustom spc = temp.get(i);
						spc.setIsdisPlay(0);
						spmsPortalCustomDAO.save(spc);
					}
				}
			}
		}
		
		return result;
	}


}
