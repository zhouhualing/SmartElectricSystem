/**
 * 
 */
package com.harmazing.spms.base.manager;

import java.util.Map;

import com.harmazing.spms.common.manager.IManager;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月2日
 */
public interface PermissionManager extends IManager {
    
    /**
     * 获取菜单
     * parentId: 为null获取一级菜单
     */
    public Map<String,Object> getMenu(Map<String,String> info);
    
    public Map<String, Object> getMenuByuserRole();
    
    public Map<String,Object> getMenuTreeByRole(Map<String, String> info);
}
