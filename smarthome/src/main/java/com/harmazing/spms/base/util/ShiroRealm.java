package com.harmazing.spms.base.util;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.harmazing.spms.base.dao.UserDAO;
import com.harmazing.spms.base.entity.UserEntity;

/**
 * shiro real(jdbc realm).
 * @author Zhaocaipeng
 * since 2013-12-12
 */
public class ShiroRealm extends JdbcRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return super.doGetAuthorizationInfo(principals);
    }
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    
	UsernamePasswordActionToken upToken = (UsernamePasswordActionToken) token;
        String userCode = upToken.getUsername();

        // Null username is invalid
        if (userCode == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }
        
        UserDAO userDAO = (UserDAO)SpringUtil.getBeanByName("userDAO");
        UserEntity userEntityDB = null;
		try {
			userEntityDB = userDAO.findByUserCode(userCode);
		} catch (Exception e) {
			e.printStackTrace();
		}

        
        if (userEntityDB == null) {
            throw new UnknownAccountException("not exsit userName.");
        }
         
        if(userEntityDB.getStatus()!= 0) {
        	 throw new UnknownAccountException("user frozen");
        }
        SimpleAuthenticationInfo saInfo = new SimpleAuthenticationInfo(userCode, userEntityDB.getPassword(), getName());
 
        return saInfo;
    }

}
