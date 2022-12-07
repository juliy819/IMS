package com.juliy.ims.dao.impl;

import com.juliy.ims.exception.DaoException;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author JuLiy
 * @date 2022/12/1 21:48
 */
public class BaseDao {

    Connection conn;
    PreparedStatement pStatement;
    ResultSet rs;

    public boolean isNameExist(String name, String tableName, String columnName) {
        String sql = "select count(*) from " + tableName + " where " + columnName + " = ? limit 1";
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, name);
            rs = pStatement.executeQuery();
            rs.next();
            return rs.getInt("count(*)") == 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
    }

    public int queryCount(String sql) {
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            rs = pStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return 0;
    }
}
