package com.harmazing.spms.log.dao;

import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.log.entity.SpmsLog;
@Repository("spmsLogDAO")
public interface SpmsLogDAO extends SpringDataDAO<SpmsLog>{



}
