package com.harmazing.service.impl;

import com.harmazing.DataSourceSessionFactory;
import org.apache.ibatis.session.SqlSessionFactory;


public class ServiceImpl  {
    protected SqlSessionFactory sqlSessionFactory;
    public ServiceImpl() {
        DataSourceSessionFactory dataSourceSessionFactory = DataSourceSessionFactory.getInstance();
        sqlSessionFactory = dataSourceSessionFactory.getSqlSessionFactory();
    }




}
