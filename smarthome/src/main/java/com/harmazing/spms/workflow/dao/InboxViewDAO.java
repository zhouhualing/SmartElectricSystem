package com.harmazing.spms.workflow.dao;


import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.workflow.entity.InboxView;

@Repository("inboxViewDAO")
public interface InboxViewDAO extends SpringDataDAO<InboxView> {

}
