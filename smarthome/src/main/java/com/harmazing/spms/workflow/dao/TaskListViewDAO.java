package com.harmazing.spms.workflow.dao;

import org.springframework.stereotype.Repository;

import com.harmazing.spms.common.dao.SpringDataDAO;
import com.harmazing.spms.workflow.entity.TaskListView;

@Repository("taskListViewDAO")
public interface TaskListViewDAO extends SpringDataDAO<TaskListView> {

}
