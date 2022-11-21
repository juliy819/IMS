package com.juliy.ims.dao.impl;

import com.juliy.ims.dao.InventoryDao;
import com.juliy.ims.entity.DO.InvDO;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * inventory表数据库操作实现类
 * @author JuLiy
 * @date 2022/10/9 22:59
 */
public class InventoryDaoImpl implements InventoryDao {
    Connection conn;
    PreparedStatement pStatement;
    ResultSet rs;

    @Override
    public List<InvDO> queryAllInv() {
        String sql = "select a.goods_qty, b.goods_id, b.goods_name, b.goods_spec, b.goods_unit, c.goods_type_name, d.whs_name " +
                "from t_inventory a " +
                "left join t_goods b on a.goods_id = b.goods_id " +
                "left join t_goods_type c on b.goods_type_id = c.goods_type_id " +
                "left join t_warehouse d on a.whs_id = d.whs_id " +
                "where a.is_deleted != 1";
        return queryInv(sql);
    }

    @Override
    public List<InvDO> queryInv(String sql) {
        List<InvDO> list = new ArrayList<>();
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            rs = pStatement.executeQuery();
            while (rs.next()) {
                InvDO inv = new InvDO();
                inv.setWhsName(rs.getString("whs_name"));
                inv.setGoodsId(rs.getInt("goods_id"));
                inv.setGoodsTypeName(rs.getString("goods_type_name"));
                inv.setGoodsName(rs.getString("goods_name"));
                inv.setGoodsSpec(rs.getString("goods_spec"));
                inv.setGoodsUnit(rs.getString("goods_unit"));
                inv.setGoodsQty(rs.getInt("goods_qty"));
                list.add(inv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return list;
    }

    @Override
    public int queryInvCount() {
        String sql = "select count(*) from t_inventory";
        return queryInvCount(sql);
    }

    @Override
    public int queryInvCount(String sql) {
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            rs = pStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return 0;
    }
}
