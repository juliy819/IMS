package com.juliy.ims.dao.impl;

import com.juliy.ims.dao.WarehouseDao;
import com.juliy.ims.entity.Warehouse;
import com.juliy.ims.exception.DaoException;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * warehouse表数据库操作实现类
 * @author JuLiy
 * @date 2022/10/9 23:00
 */
public class WarehouseDaoImpl extends BaseDao implements WarehouseDao {

    @Override
    public List<Warehouse> queryAll() {
        List<Warehouse> list = new ArrayList<>();
        String sql = "select whs_id, whs_name from t_warehouse where is_deleted != 1";
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            rs = pStatement.executeQuery();
            while (rs.next()) {
                Warehouse warehouse = new Warehouse();
                warehouse.setWhsId(rs.getInt("whs_id"));
                warehouse.setWhsName(rs.getString("whs_name"));
                list.add(warehouse);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return list;
    }

    @Override
    public void insert(Warehouse warehouse) {
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
            pStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
    }

    @Override
    public boolean isNameExist(String name) {
        return super.isNameExist(name, "t_warehouse", "whs_name");
    }
}
