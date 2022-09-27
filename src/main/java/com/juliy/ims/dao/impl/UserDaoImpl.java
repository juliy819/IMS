package com.juliy.ims.dao.impl;

import com.juliy.ims.dao.UserDao;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * User数据库操作类实例
 * @author JuLiy
 * @date 2022/9/27 14:57
 */
public class UserDaoImpl implements UserDao {
    Connection conn;
    PreparedStatement pStatement;
    ResultSet rs;

    @Override
    public boolean queryUser(String username, String password) {
        conn = JdbcUtil.getConnection();
        String sql = "select id from t_user where username = ? and password = ?";
        try {
            pStatement = Objects.requireNonNull(conn).prepareStatement(sql);
            pStatement.setString(1, username);
            pStatement.setString(2, password);
            rs = pStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return false;
    }
}
