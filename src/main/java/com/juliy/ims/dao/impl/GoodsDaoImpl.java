package com.juliy.ims.dao.impl;

import com.juliy.ims.dao.GoodsDao;
import com.juliy.ims.entity.Goods;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * goods表数据库操作实现类
 * @author JuLiy
 * @date 2022/10/9 22:58
 */
public class GoodsDaoImpl implements GoodsDao {
    Connection conn;
    PreparedStatement pStatement;
    ResultSet rs;

    @Override
    public List<Goods> queryAllGoods() {
        String sql = "select a.*, b.goods_type_name from t_goods a, t_goods_type b " +
                "where a.goods_type_id = b.goods_type_id and a.is_deleted != 1";
        return queryGoods(sql);
    }

    @Override
    public List<Goods> queryGoods(String sql) {
        List<Goods> list = new ArrayList<>();
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            rs = pStatement.executeQuery();
            while (rs.next()) {
                Goods goods = new Goods();
                wrapGoods(goods);
                list.add(goods);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return list;
    }

    @Override
    public int queryGoodsCount() {
        String sql = "select count(*) from t_goods";
        return queryGoodsCount(sql);
    }

    @Override
    public int queryGoodsCount(String sql) {
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

    /** 将结果集中的数据封装到对象中 */
    private void wrapGoods(Goods goods) throws SQLException {
        goods.setGoodsId(rs.getInt("goods_id"));
        goods.setGoodsTypeId(rs.getInt("goods_type_id"));
        goods.setGoodsTypeName(rs.getString("goods_type_name"));
        goods.setGoodsName(rs.getString("goods_name"));
        goods.setGoodsSpec(rs.getString("goods_spec"));
        goods.setGoodsUnit(rs.getString("goods_unit"));
        goods.setRefPurPrice(rs.getBigDecimal("ref_pur_price"));
        goods.setRefSellPrice(rs.getBigDecimal("ref_sell_price"));
        goods.setMaxQty(rs.getInt("max_qty"));
        goods.setMinQty(rs.getInt("min_qty"));
        goods.setGoodsComment(rs.getString("goods_comment"));
        goods.setDeleted(rs.getBoolean("is_deleted"));
    }
}
