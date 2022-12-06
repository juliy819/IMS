package com.juliy.ims.dao.impl;

import com.juliy.ims.dao.RecordDao;
import com.juliy.ims.entity.Record;
import com.juliy.ims.exception.DaoException;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.SQLException;

/**
 * record表数据库操作实现类
 * @author JuLiy
 * @date 2022/10/9 23:00
 */
public class RecordDaoImpl extends BaseDao implements RecordDao {
    @Override
    public boolean insert(Record rec) {
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

            return pStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
    }
}
