package com.harmazing.spms.permissions.manager;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harmazing.spms.base.dao.PermissionDAO;
import com.harmazing.spms.base.dao.QueryDAO;
import com.harmazing.spms.base.dao.RoleDAO;
import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.entity.PermissionEntity;
import com.harmazing.spms.base.entity.RoleEntity;
import com.harmazing.spms.base.util.DynamicSpecifications;
import com.harmazing.spms.base.util.SearchFilter;
import com.harmazing.spms.base.util.SearchFilter.Operator;
import com.harmazing.spms.base.util.StringUtil;
import com.harmazing.spms.permissions.dao.RolePermissionsDAO;
import com.harmazing.spms.permissions.dto.PermissionsDTO;
import com.harmazing.spms.permissions.dto.RolePermissionsDTO;
import com.harmazing.spms.permissions.entity.RolePermission;
import com.harmazing.spms.permissions.manager.RolePermissionsManager;


@Service("RolePermissionsManager")
public class RolePermissionsManagerImpl implements RolePermissionsManager {
	@Autowired 
	private RoleDAO roleDAO;
	@Autowired 
	private PermissionDAO permissionDAO;
	@Autowired 
	private QueryDAO queryDAO;
	@Autowired
    private UserDAO userDAO;
	@Autowired
	private RolePermissionsDAO rolePermissionsDAO;
	
	@Override
	public RolePermissionsDTO getRolePermissionsById(RolePermissionsDTO rolePermissionsDTO) {
		RoleEntity role = roleDAO.findOne(rolePermissionsDTO.getId());
		RolePermissionsDTO roleDto = new RolePermissionsDTO();
		roleDto.setId(role.getId());
		roleDto.setRoleName(role.getRoleName());
		roleDto.setRoleCode(role.getRoleCode());
		
		roleDto.setRolePermissionsList(getAllPermissions());//先取出全部
		
		getRolePermissinos(roleDto);//再为其赋值
		
		return roleDto;
	}

	@Override
	public List<PermissionsDTO> getAllPermissions() {
		List<PermissionsDTO> result = Lists.newArrayList();
		List <SearchFilter> sfs = Lists.newArrayList();
		sfs.add(new SearchFilter("permissionType",Operator.NEQ,3));
		List<PermissionEntity> all = permissionDAO.findAll(DynamicSpecifications.bySearchFilter(sfs, PermissionEntity.class));
		for(int i = 0 ; i < all.size() ;i ++){
			PermissionEntity ptemp = all.get(i);
			if(ptemp.getPermissionType() != 3 && !ptemp.getId().equals("14") && !ptemp.getId().equals("1")){
				PermissionsDTO  pdto = new PermissionsDTO();
				pdto.setId(ptemp.getId());
				pdto.setName(ptemp.getPermissionName());
				pdto.setCode(ptemp.getPermissionCode());
				pdto.setType(ptemp.getPermissionType());
				if(ptemp.getSort()!=null){
					pdto.setSort(ptemp.getSort());
				}
				pdto.setIsRight(0);
				//pdto.setParentId(ptemp.getParentPermissionEntity().getId());
				pdto.setChildren(new ArrayList<PermissionsDTO>());
				
				pdto = findAllChildren(pdto);
				
				result.add(pdto);
			}
		}
		
		return result;
	}
	/*取一个菜单子菜单*/
	public PermissionsDTO findAllChildren(PermissionsDTO parent){
		List <SearchFilter> sfs = Lists.newArrayList();
		sfs.add(new SearchFilter("parentPermissionEntity.id",Operator.EQ,parent.getId()));
		List <PermissionEntity> pL = permissionDAO.findAll(DynamicSpecifications.bySearchFilter(sfs, PermissionEntity.class));
		for(int i = 0 ; i < pL.size() ; i++){
			PermissionEntity ptemp = pL.get(i);
			PermissionsDTO  pdto = new PermissionsDTO();
			
			pdto.setId(ptemp.getId());
			pdto.setName(ptemp.getPermissionName());
			pdto.setCode(ptemp.getPermissionCode());
			pdto.setType(ptemp.getPermissionType());
			//pdto.setSort(ptemp.getSort());
			pdto.setIsRight(0);
			pdto.setParentId(ptemp.getParentPermissionEntity().getId());
			
			parent.getChildren().add(pdto);	
		}
		return parent;
	}
	
	/*根据库内信息为角色的权限赋予显示*/
	public RolePermissionsDTO getRolePermissinos(RolePermissionsDTO rolePermissionsDTO){
		String roleid = rolePermissionsDTO.getId();

		String sql = "select permissionentities_id from role_permission where roleentities_id = '"+roleid+"'";
		List list = queryDAO.getObjects(sql);
		for(int i = 0 ; i < list.size() ;i++){
			String pid = (String) list.get(i);
			doRight(rolePermissionsDTO,pid);
		}
		
		return rolePermissionsDTO;
	}
	
	/*用id设置指定role的菜单为显示*/
	public void doRight(RolePermissionsDTO rolePermissionsDTO,String pid){
		List<PermissionsDTO> pList = rolePermissionsDTO.getRolePermissionsList();
		for(int i = 0 ; i < pList.size() ; i++){
			PermissionsDTO pDto1 = pList.get(i);
			if(pDto1.getId().equals(pid)){
				pDto1.setIsRight(1);
				return;
			}else{
				List<PermissionsDTO> pl = pDto1.getChildren();
				for(int j = 0 ; j < pl.size() ; j++){
					PermissionsDTO pDto2 = pl.get(j);
					if(pDto2.getId().equals(pid)){
						pDto2.setIsRight(1);
						return;
					}
				}
			}
		}
	}

	@Override
	public RolePermissionsDTO doSave(RolePermissionsDTO rolePermissionsDTO) {
		if(!StringUtil.isNUll(rolePermissionsDTO.getId())){
			//原有角色
			String id = rolePermissionsDTO.getId();
			RoleEntity re = roleDAO.findOne(id);
			re.setRoleCode(rolePermissionsDTO.getRoleCode());
			re.setRoleName(rolePermissionsDTO.getRoleName());
			roleDAO.save(re);
			//savePermissions(rolePermissionsDTO);
			//rolePermissionsDTO = getRolePermissionsById(rolePermissionsDTO);
		}else{
			//新角色
			RoleEntity role = new RoleEntity();
			role.setRoleCode(rolePermissionsDTO.getRoleCode());
			role.setRoleName(rolePermissionsDTO.getRoleName());
			roleDAO.save(role);
			rolePermissionsDTO.setId(role.getId());
			//savePermissions(rolePermissionsDTO);
			//rolePermissionsDTO = getRolePermissionsById(rolePermissionsDTO);
		}
		return rolePermissionsDTO;
	}
	
	/*保存某角色的权限配置*/
	public void savePermissions(RolePermissionsDTO rolePermissionsDTO){
		String id = rolePermissionsDTO.getId();
		List<PermissionsDTO> pList = rolePermissionsDTO.getRolePermissionsList();
		for(int i = 0 ; i < pList.size() ; i++){
			PermissionsDTO pDto1 = pList.get(i);
			String sql1 = "DELETE FROM role_permission WHERE roleentities_id ='"+id+" 'and permissionentities_id ='"+pDto1.getId()+"'";
			queryDAO.doExecuteSql(sql1);
			if(pDto1.getIsRight() == 1){
				sql1 = "INSERT INTO role_permission (roleentities_id,permissionentities_id) VALUES ('"+id+"','"+pDto1.getId()+"')";
				queryDAO.doExecuteSql(sql1);
			}
			List<PermissionsDTO> pl = pDto1.getChildren();
			for(int j = 0 ; j < pl.size() ; j++ ){
				PermissionsDTO pDto2 = pl.get(j);
				String sql2 = "DELETE FROM role_permission WHERE roleentities_id ="+id+" and permissionentities_id ='"+pDto2.getId()+"' ";
				queryDAO.doExecuteSql(sql2);
				if(pDto2.getIsRight() == 1){
					sql2 = "INSERT INTO role_permission (roleentities_id,permissionentities_id) VALUES ('"+id+"','"+pDto2.getId()+"')";
					queryDAO.doExecuteSql(sql2);
				}
			}
		}
	}

	@Override
	public void deletePermissions(String roleid) {
		String sql = "DELETE FROM role_permission WHERE roleentities_id ='"+roleid+"'"; 		
		queryDAO.doExecuteSql(sql);
	}

	@Transactional
	@Override
	public Map<String,Object> deleteRole(String roleid) {
		Map<String,Object> result = Maps.newHashMap();
		String sql = "SELECT tuu.id FROM tb_user_user tuu WHERE id IN (SELECT userentities_id FROM user_role WHERE roleentities_id='"+roleid+"')";
		List list = queryDAO.getObjects(sql);
		if(list.size()>0){
			userDAO.doDeleteUsers(list);
		}
		//roleDAO.delete(roleid);
		roleDAO.doDeleteById(roleid);
//		deletePermissions(roleid);
		result.put("success", true);
		result.put("msg","删除成功");
		return result;
	}
	@Transactional
	@Override
	public List<String> deleteRoles(List<String> ids) {
		for(int i = 0 ; i < ids.size() ; i++){
			String id = ids.get(i);
			deleteRole(id);
		}
		return null;
	}

	@Transactional
	@Override
	public Map<String, Object> doSavePermissions(Map<String, Object> info) {
		Map<String,Object> result = Maps.newHashMap();
		String roleid = (String) info.get("id");
		List<String> plist = (List<String>) info.get("List");
		rolePermissionsDAO.deleteRolePermission(roleid);
		for (int i = 0; i < plist.size(); i++) {
			RolePermission rp = new RolePermission();
			rp.setRoleentities_id(roleid);
			rp.setPermissionentities_id(plist.get(i));
			rolePermissionsDAO.save(rp);
		}
		return result;
	}
	
}
