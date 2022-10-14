package com.juliy.ims.dao.impl;

import com.juliy.ims.dao.UserDao;
import com.juliy.ims.entity.User;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * user表数据库操作实现类
 * @author JuLiy
 * @date 2022/9/27 14:57
 */
public class UserDaoImpl implements UserDao {
    Connection conn;
    PreparedStatement pStatement;
    ResultSet rs;

    @Override
    public User queryUser(String username, String password) {
        conn = JdbcUtil.getConnection();
        String sql = "select user_id, is_deleted from t_user where nickname = ? or username = ? and password = ?";
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, username);
            pStatement.setString(2, username);
            pStatement.setString(3, password);
            rs = pStatement.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setDeleted(rs.getBoolean("is_deleted"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return null;
    }
}
