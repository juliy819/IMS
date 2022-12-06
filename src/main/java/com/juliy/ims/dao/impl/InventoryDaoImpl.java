package com.juliy.ims.dao.impl;

import com.juliy.ims.dao.InventoryDao;
import com.juliy.ims.entity.Inventory;
import com.juliy.ims.entity.model.InvDO;
import com.juliy.ims.exception.DaoException;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * inventory表数据库操作实现类
 * @author JuLiy
 * @date 2022/10/9 22:59
 */
public class InventoryDaoImpl extends BaseDao implements InventoryDao {

    @Override
    public List<InvDO> queryAll() {
        String sql = "select a.goods_qty, b.goods_id, b.goods_name, b.goods_spec, b.goods_unit, c.goods_type_name, d.whs_name " +
                "from t_inventory a " +
                "left join t_goods b on a.goods_id = b.goods_id " +
                "left join t_goods_type c on b.goods_type_id = c.goods_type_id " +
                "left join t_warehouse d on a.whs_id = d.whs_id " +
                "where a.is_deleted != 1";
        return query(sql);
    }

    @Override
    public List<InvDO> query(String sql) {
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
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return list;
    }

    @Override
    public int queryQty(int whsId, int goodsId) {
        String sql = "select goods_qty from t_inventory where whs_id = ? and goods_id = ?";
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setInt(1, whsId);
            pStatement.setInt(2, goodsId);
            rs = pStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("goods_qty");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return 0;
    }

    @Override
    public void insertOrUpdate(Inventory inv) {
        String sql = "insert into t_inventory (whs_id, goods_id, goods_qty) " +
                "values (?, ?, ?) " +
                "on duplicate key update goods_qty = goods_qty + ?";
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setInt(1, inv.getWhsId());
            pStatement.setInt(2, inv.getGoodsId());
            pStatement.setInt(3, inv.getGoodsQty());
            pStatement.setInt(4, inv.getGoodsQty());

            pStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
    }
}
