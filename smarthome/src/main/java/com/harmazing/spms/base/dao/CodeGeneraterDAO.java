/**
 * 
 */
package com.harmazing.spms.base.dao;

import com.harmazing.spms.base.entity.CodeGeneraterEntity;
import com.harmazing.spms.base.entity.PermissionEntity;
import com.harmazing.spms.common.dao.SpringDataDAO;
import org.springframework.stereotype.Repository;

/**
 * describe:
 * @author Zhaocaipeng
 * since 2015年1月2日
 */
@Repository("codeGeneraterDAO")
public interface CodeGeneraterDAO extends SpringDataDAO<CodeGeneraterEntity> {

}
