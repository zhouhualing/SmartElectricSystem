/**
 * 
 */
package com.harmazing.spms.base.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.harmazing.spms.base.entity.DSMEntity;
import com.harmazing.spms.common.dao.SpringDataDAO;

@Repository("dSMDAO")
public interface DSMDAO extends SpringDataDAO<DSMEntity> {
	
}
