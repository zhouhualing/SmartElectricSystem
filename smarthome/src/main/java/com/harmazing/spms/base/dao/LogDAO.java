package com.harmazing.spms.base.dao;

import org.springframework.stereotype.Repository;

import com.harmazing.spms.base.entity.LogEntity;
import com.harmazing.spms.common.dao.SpringDataDAO;

/**
 * log DAO.
 * @author Zhaocaipeng
 * since 2013-9-24
 */
@Repository
public interface LogDAO extends SpringDataDAO<LogEntity> {

}
