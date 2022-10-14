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
        List<Goods> list = new ArrayList<>();
        conn = JdbcUtil.getConnection();
        String sql = "select * from t_goods";
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
    public Goods queryGoodsById(Integer id) {
        Goods goods = null;
        conn = JdbcUtil.getConnection();
        String sql = "select * from t_goods where goods_id = ?";
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setInt(1, id);
            rs = pStatement.executeQuery();
            while (rs.next()) {
                goods = new Goods();
                wrapGoods(goods);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return goods;
    }

    /** 将结果集中的数据封装到对象中 */
    private void wrapGoods(Goods goods) throws SQLException {
        goods.setGoodsId(rs.getInt(1));
        goods.setGoodsTypeId(rs.getInt(2));
        goods.setGoodsName(rs.getString(3));
        goods.setGoodsSpec(rs.getString(4));
        goods.setGoodsUnit(rs.getString(5));
        goods.setRefPurPrice(rs.getBigDecimal(6));
        goods.setRefSellPrice(rs.getBigDecimal(7));
        goods.setMaxQty(rs.getInt(8));
        goods.setMinQty(rs.getInt(9));
        goods.setGoodsComment(rs.getString(10));
        goods.setDeleted(rs.getBoolean(11));
    }
}
