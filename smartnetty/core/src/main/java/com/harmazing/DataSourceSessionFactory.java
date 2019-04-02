package com.harmazing;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by ming on 14-9-4.
 */
public class DataSourceSessionFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceSessionFactory.class);

    private static volatile DataSourceSessionFactory instance;
    private static volatile SqlSessionFactory sqlSessionFactory;

    private DataSourceSessionFactory() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./conf/mybatis-config.xml");
        } catch (FileNotFoundException e) {
            LOGGER.error("mybatis config file is not found :", e);
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static synchronized DataSourceSessionFactory getInstance() {
        if(instance == null) {
            instance = new DataSourceSessionFactory();
        }
        return instance;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
