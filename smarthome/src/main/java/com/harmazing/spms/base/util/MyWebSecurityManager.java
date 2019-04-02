/**
 * 
 */
package com.harmazing.spms.base.util;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2014-06-25
 */
public class MyWebSecurityManager extends DefaultWebSecurityManager {
    /* (non-Javadoc)
     * @see org.apache.shiro.mgt.DefaultSecurityManager#createSubject(org.apache.shiro.subject.SubjectContext)
     */
    @Override
    public Subject createSubject(SubjectContext subjectContext) {
        // TODO Auto-generated method stub
        return super.createSubject(subjectContext);
    }
    
}
