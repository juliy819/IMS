package com.juliy.ims.dao.impl;

import com.juliy.ims.dao.RecordDao;
import com.juliy.ims.entity.Record;
import com.juliy.ims.entity.table_unit.RecordDO;
import com.juliy.ims.exception.DaoException;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * record表数据库操作实现类
 * @author JuLiy
 * @date 2022/10/9 23:00
 */
public class RecordDaoImpl extends BaseDao implements RecordDao {
    @Override
    public List<RecordDO> queryAll() {
        String sql = "select a.create_time as biz_date, d.whs_name, a.receipt_type, " +
                "a.receipt_id, b.goods_id, c.goods_type_name, b.goods_name, b.goods_spec, " +
                "a.entry_qty, a.entry_amt, a.out_qty, a.out_amt " +
                "from t_record a " +
                "left join t_goods b on a.goods_id = b.goods_id " +
                "left join t_goods_type c on b.goods_type_id = c.goods_type_id " +
                "left join t_warehouse d on a.whs_id = d.whs_id " +
                "where a.is_deleted != 1";
        return query(sql);
    }

    @Override
    public List<RecordDO> query(String sql) {
        List<RecordDO> list = new ArrayList<>();
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            rs = pStatement.executeQuery();
            while (rs.next()) {
                RecordDO rec = new RecordDO();
                rec.setBizDate(rs.getDate("biz_date"));
                rec.setWhsName(rs.getString("whs_name"));
                rec.setReceiptType(rs.getString("receipt_type"));
                rec.setReceiptId(rs.getString("receipt_id"));
                rec.setGoodsId(rs.getInt("goods_id"));
                rec.setGoodsType(rs.getString("goods_type_name"));
                rec.setGoodsName(rs.getString("goods_name"));
                rec.setGoodsSpec(rs.getString("goods_spec"));
                rec.setEntryQty(rs.getInt("entry_qty"));
                rec.setEntryAmt(rs.getBigDecimal("entry_amt"));
                rec.setOutQty(rs.getInt("out_qty"));
                rec.setOutAmt(rs.getBigDecimal("out_amt"));

                list.add(rec);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return list;
    }

    @Override
    public void insert(Record rec) {
        String sql = "insert into t_record(company_id, whs_id, receipt_type," +
                " receipt_id, goods_id, entry_qty, entry_amt, out_qty, out_amt) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setInt(1, rec.getCompanyId());
            pStatement.setInt(2, rec.getWhsId());
            pStatement.setString(3, rec.getReceiptType());
            pStatement.setString(4, rec.getReceiptId());
            pStatement.setInt(5, rec.getGoodsId());
            pStatement.setInt(6, rec.getEntryQty());
            pStatement.setBigDecimal(7, rec.getEntryAmt());
            pStatement.setInt(8, rec.getOutQty());
            pStatement.setBigDecimal(9, rec.getOutAmt());

            pStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
    }
}
