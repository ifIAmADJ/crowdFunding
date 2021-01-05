package com.iproject.crowd.test;


import com.iproject.crowd.entity.Admin;
import com.iproject.crowd.mapper.AdminMapper;
import com.iproject.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


// 在测试类上标记必要的注解，Spring 整合 Junit。
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml", "classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;


    @Test
    public void testService() {
        adminService.saveAdmin(
                new Admin(null, "jerry123", "1234567", "234@qq.com", "jerry", null)
        );
    }

    @Test
    public void testLogger() {

        // 1.获取 Logger 对象
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);

        logger.debug("This is a debug level");

        logger.info("this is a logger level");

        logger.warn("this is a warning level");

        logger.error("this is a error level");
    }

    //    测试 MBG 自动生成的 Mapper;
    @Test
    public void testMapper() {

        for (int i = 18; i < 238; i++) {
            adminMapper.insert(
                    new Admin(null, "user"+i, "root", "user"+i+"@qq.com", "tomYeah"+i, null)
            );
        }
    }


    //  测试基本的数据库连接
    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

}
