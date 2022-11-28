package com.juliy.ims.dao.impl;

import com.juliy.ims.dao.GoodsTypeDao;
import com.juliy.ims.entity.GoodsType;
import com.juliy.ims.exception.DaoException;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * goodsType表数据库操作实现类
 * @author JuLiy
 * @date 2022/10/9 22:59
 */
public class GoodsTypeDaoImpl implements GoodsTypeDao {

    Connection conn;
    PreparedStatement pStatement;
    ResultSet rs;

    @Override
    public List<GoodsType> queryAll() {
        List<GoodsType> list = new ArrayList<>();
        String sql = "select goods_type_id, goods_type_name from t_goods_type";
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            rs = pStatement.executeQuery();
            while (rs.next()) {
                GoodsType goodsType = new GoodsType();
                goodsType.setGoodsTypeId(rs.getInt("goods_type_id"));
                goodsType.setGoodsTypeName(rs.getString("goods_type_name"));
                list.add(goodsType);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return list;
    }

    @Override
    public boolean insert(GoodsType goodsType) {
        String sql = "insert into t_goods_type(goods_type_name) values (?)";
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, goodsType.getGoodsTypeName());
            return pStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
    }


    @Override
    public boolean isNameExist(String name) {
        String sql = "select count(*) from t_goods_type where goods_type_name = ? limit 1";
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
