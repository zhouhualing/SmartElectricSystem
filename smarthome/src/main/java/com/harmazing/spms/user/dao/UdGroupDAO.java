/**
 * 
 */
package com.harmazing.spms.user.dao;

import org.springframework.stereotype.Repository;
import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.user.entity.UdGroupEntity;

/**
 * describe:
 * @author Hualing
 * since 2014年10月4日
 */
@Repository("udGroupDAO")
public interface UdGroupDAO extends SpringDataDAO<UdGroupEntity> {

}
