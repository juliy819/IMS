package com.juliy.ims.dao.impl;

import com.juliy.ims.dao.WarehouseDao;
import com.juliy.ims.entity.Warehouse;
import com.juliy.ims.exception.DaoException;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * warehouse表数据库操作实现类
 * @author JuLiy
 * @date 2022/10/9 23:00
 */
public class WarehouseDaoImpl implements WarehouseDao {

    Connection conn;
    PreparedStatement pStatement;
    ResultSet rs;

    @Override
    public boolean insert(Warehouse warehouse) {
        String sql = "insert into t_warehouse(whs_name, whs_add, manager_name, manager_Phone, whs_comment) " +
                "values(?, ?, ?, ?, ?)";
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, warehouse.getWhsName());
            pStatement.setString(2, warehouse.getWhsAdd());
            pStatement.setString(3, warehouse.getManagerName());
            pStatement.setString(4, warehouse.getManagerPhone());
            pStatement.setString(5, warehouse.getWhsComment());
            return pStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
    }

    @Override
    public boolean isNameExist(String name) {
        String sql = "select count(*) from t_warehouse where whs_name = ? limit 1";
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, name);
            rs = pStatement.executeQuery();
            rs.next();
            return rs.getInt("count(*)") != 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
    }
}
