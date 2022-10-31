package com.juliy.ims.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * JDBC工具类
 * @author JuLiy
 * @date 2022/9/27 15:06
 */
public class JdbcUtil {
    /** 数据库驱动 */
    private static final String DRIVER_CLASS;
    /** 数据库连接 */
    private static final String URL;
    /** 数据库用户名 */
    private static final String USER;
    /** 数据库密码 */
    private static final String PASSWORD;

    private static final Logger log = Logger.getLogger(JdbcUtil.class);

    static {
        try {
            //读取配置文件中的数据库信息
            InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("info.properties");
            Properties props = new Properties();
            props.load(in);
            DRIVER_CLASS = props.getProperty("driverClass");
            URL = props.getProperty("url");
            USER = props.getProperty("user");
            PASSWORD = props.getProperty("password");
            assert in != null;
            in.close();
            //加载数据库驱动
            Class.forName(DRIVER_CLASS);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("获取数据库配置文件信息失败");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("加载驱动失败");
        }
    }

    /**
     * 建立数据库连接
     * @return 数据库连接对象
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("链接数据库的url或用户名密码错误,请检查您的配置文件");
        }
    }

    /**
     * 释放资源
     * @param rs   结果集对象
     * @param stmt 语句执行对象
     * @param conn 连接对象
     */
    public static void release(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();//将连接关闭
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
