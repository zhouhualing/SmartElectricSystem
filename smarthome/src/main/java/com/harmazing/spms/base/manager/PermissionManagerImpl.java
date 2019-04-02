/**
 * 
 */
package com.harmazing.spms.base.manager;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;









import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.base.dao.PermissionDAO;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.dto.UserDTO;
import com.harmazing.spms.base.entity.PermissionEntity;
import com.harmazing.spms.base.entity.UserEntity;
import com.harmazing.spms.base.manager.PermissionManager;
import com.harmazing.spms.base.util.BeanUtils;
import com.harmazing.spms.base.util.DynamicSpecifications;
import com.harmazing.spms.base.util.SearchFilter;
import com.harmazing.spms.base.util.UserUtil;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月2日
 */
@Service("permissionManager")
public class PermissionManagerImpl implements PermissionManager {

    @Autowired
    private PermissionDAO permissionDAO;
    @Autowired
    private QueryDAO queryDAO;
    
    
    /* (non-Javadoc)
     * @see com.harmazing.spms.base.manager.PermissionManager#getMenu(java.util.Map)
     */
    @Override
    public Map<String, Object> getMenu(Map<String, String> info) {
	List<PermissionEntity> infos =null;
    Sort sort = new Sort(Sort.Direction.ASC,"sort");
	if(info == null) {
	    infos = permissionDAO.getMenuByParentIdNull(UserUtil.getCurrentUserCode());
	} else {
	    infos = permissionDAO.findAll(DynamicSpecifications.bySearchFilter(new SearchFilter("parentPermissionEntity.id", info.get("id").toString()),PermissionEntity.class),sort);
	}
	
	Map <String,Object> result = Maps.newHashMap();
	List <Map<String,String>> permiList = Lists.newArrayList();
	for(PermissionEntity permissionEntity : infos) {
	    Map <String,String> map = Maps.newHashMap();
	    map.put("id", permissionEntity.getId().toString());
	    map.put("permissionName", permissionEntity.getPermissionName());
	    map.put("permissionType", permissionEntity.getPermissionType().toString());
	    map.put("url", permissionEntity.getUrl());
	    permiList.add(map);
	}
	
	UserDTO userDTO = new UserDTO();
	BeanUtils.copyProperties(UserUtil.getCurrentUser(), userDTO);
	result.put("permission", permiList);
	result.put("userInfo", userDTO);
	return result;
    }
    
    @Override
    public Map<String, Object> getMenuByuserRole(){
    	/*if(UserUtil.getCurrentUser().getId().equals("1")){
    		Map<String, String> info = Maps.newHashMap();
    		info = null;
    		return getMenu(info);
    	}*/
    	Map <String,Object> result = Maps.newHashMap();
    	UserDTO userDTO = new UserDTO();
    	List <Map<String,String>> permiList = Lists.newArrayList();
    	BeanUtils.copyProperties(UserUtil.getCurrentUser(), userDTO);
    	result.put("userInfo", userDTO);
    	UserEntity user = UserUtil.getCurrentUser();
    	Map<String,PermissionEntity> temp = Maps.newLinkedHashMap();
    	PermissionEntity shouye = permissionDAO.findOne("1");
    	temp.put(shouye.getId(), shouye);
    	String userid = user.getId();
    	String sql = "SELECT roleentities_id FROM user_role WHERE userentities_id = '"+userid+"'";
    	List list = queryDAO.getObjects(sql);
    	for(int i = 0 ; i < list.size() ; i++){
    		String roleid = (String) list.get(i);
    		sql = "SELECT id FROM tb_user_permission tup WHERE tup.id in (SELECT permissionentities_id FROM role_permission WHERE roleentities_id = '"+roleid+"') ORDER BY sort asc";
    		List t1 = queryDAO.getObjects(sql);//本角色的所有菜单id
    		for(int j = 0 ; j < t1.size() ; j ++ ){
    			String pid = (String) t1.get(j);
    			PermissionEntity p = permissionDAO.findOne(pid);
    			if(p.getPermissionType()!=3){
    				temp.put(p.getId(), p);
    			}
    		}
    	}
    	
    	Set<String> key = temp.keySet();
    	for (Iterator it = key.iterator(); it.hasNext();) {
    		String s = (String) it.next();
    		PermissionEntity pe = temp.get(s);
    		
    		
    		Map <String,String> map = Maps.newHashMap();
    	    map.put("id", pe.getId().toString());
    	    map.put("permissionName", pe.getPermissionName());
    	    map.put("permissionType", pe.getPermissionType().toString());
    	    map.put("url", pe.getUrl());
    	    permiList.add(map);
    	}
    	result.put("permission", permiList);
    	return result;
    }

	@Override
	public Map<String, Object> getMenuTreeByRole(Map<String, String> info) {
		String parentid  = info.get("id").toString();
		
		Map <String,Object> result = Maps.newHashMap();
    	UserDTO userDTO = new UserDTO();
    	List <Map<String,String>> permiList = Lists.newArrayList();
    	BeanUtils.copyProperties(UserUtil.getCurrentUser(), userDTO);
    	result.put("userInfo", userDTO);
    	UserEntity user = UserUtil.getCurrentUser();
    	Map<String,PermissionEntity> temp = Maps.newHashMap();
    	String userid = user.getId();
    	String sql = "SELECT roleentities_id FROM user_role WHERE userentities_id = '"+userid+"'";
    	List<Object> list = queryDAO.getObjects(sql);
    	for(int i = 0 ; i < list.size() ; i++){
    		String roleid = (String) list.get(i);
    		sql = "SELECT id FROM tb_user_permission tup WHERE tup.id in (SELECT permissionentities_id FROM role_permission WHERE roleentities_id = '"+roleid+"') ORDER BY sort asc";
    		List<Object> t1 = queryDAO.getObjects(sql);//本角色的所有菜单id
    		for(int j = 0 ; j < t1.size() ; j ++ ){
    			String pid = (String) t1.get(j);
    			PermissionEntity p = permissionDAO.findOne(pid);
    			if(p.getParentPermissionEntity()!=null && p.getParentPermissionEntity().getId()!=null){
    				if(p.getParentPermissionEntity().getId().endsWith(parentid)){
        				temp.put(p.getId(), p);
        			}
    			}
    		}
    	}
    	Set<String> key = temp.keySet();
    	for (Iterator it = key.iterator(); it.hasNext();) {
    		String s = (String) it.next();
    		PermissionEntity pe = temp.get(s);
    		
    		
    		Map <String,String> map = Maps.newHashMap();
    	    map.put("id", pe.getId().toString());
    	    map.put("sort", pe.getSort()+"");
    	    map.put("permissionName", pe.getPermissionName());
    	    map.put("permissionType", pe.getPermissionType().toString());
    	    map.put("url", pe.getUrl());
    	    permiList.add(map);
    	}
    	Collections.sort(permiList,new Comparator<Map<String,String>>() {

			@Override
			public int compare(Map<String, String> o1, Map<String, String> o2) {
				return o1.get("sort").compareTo(o2.get("sort"));
			}
		});
    	result.put("permission", permiList);
    	return result;
	}
    
    

}
