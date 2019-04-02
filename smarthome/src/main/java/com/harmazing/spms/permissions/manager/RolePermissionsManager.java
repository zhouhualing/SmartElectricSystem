package com.harmazing.spms.permissions.manager;



import java.util.List;
import java.util.Map;

import com.harmazing.spms.common.manager.IManager;
import com.harmazing.spms.permissions.dto.PermissionsDTO;
import com.harmazing.spms.permissions.dto.RolePermissionsDTO;


public interface RolePermissionsManager extends IManager{
	/*按ID取信息*/
    public RolePermissionsDTO getRolePermissionsById(RolePermissionsDTO rolePermissionsDTO);
    /*取得所有菜单选项*/
    public List<PermissionsDTO> getAllPermissions();
    /*保存配置*/
    public RolePermissionsDTO doSave(RolePermissionsDTO rolePermissionsDTO);
    /*删除角色配置*/
    public void deletePermissions(String roleid);
    /*删除角色*/
    public Map<String,Object> deleteRole(String roleid);
    /*批量删除角色*/
    public List<String> deleteRoles(List<String> ids);
    /*保存角色的显示权限*/
    public Map<String,Object> doSavePermissions(Map<String,Object> info);
}
