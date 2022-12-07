package com.juliy.ims.service.impl;

import com.juliy.ims.dao.RecordDao;
import com.juliy.ims.dao.impl.RecordDaoImpl;
import com.juliy.ims.entity.table_unit.RecordDO;
import com.juliy.ims.service.RecordQueryService;
import com.juliy.ims.utils.CommonUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * 记录查询页面业务实现类
 * @author JuLiy
 * @date 2022/12/6 15:44
 */
public class RecordQueryServiceImpl implements RecordQueryService {

    private final RecordDao recordDao = new RecordDaoImpl();

    @Override
    public List<RecordDO> getAllRec() {
        return recordDao.queryAll();
    }

    @Override
    public int getFilterCount(String startDate, String endDate, RecordDO rec) {
        StringBuilder sql = generateSql(startDate, endDate, rec);
        sql.delete(7, sql.indexOf("from") - 1).insert(7, "count(*)");
        return recordDao.queryCount(String.valueOf(sql));
    }

    @Override
    public ObservableList<RecordDO> filterRecByPage(String startDate, String endDate, RecordDO rec, int page, int pageSize) {
        //生成sql语句并在结尾添加limit，查询指定条数
        StringBuilder sql = generateSql(startDate, endDate, rec);
        int start = (page - 1) * pageSize;
        sql.append("limit ").append(start).append(",").append(pageSize);
        List<RecordDO> list = recordDao.query(String.valueOf(sql));
        return FXCollections.observableArrayList(list);
    }

    /**
     * 生成sql语句
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @param rec       记录信息
     * @return sql语句，末尾带空格
     */
    private StringBuilder generateSql(String startDate, String endDate, RecordDO rec) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.create_time as biz_date, d.whs_name, a.receipt_type, " +
                           "a.receipt_id, b.goods_id, c.goods_type_name, b.goods_name, b.goods_spec, " +
                           "a.entry_qty, a.entry_amt, a.out_qty, a.out_amt " +
                           "from t_record a " +
                           "left join t_goods b on a.goods_id = b.goods_id " +
                           "left join t_goods_type c on b.goods_type_id = c.goods_type_id " +
                           "left join t_warehouse d on a.whs_id = d.whs_id " +
                           "where ")
                .append("(Date(a.create_time) between '")
                .append(startDate)
                .append("' and '")
                .append(endDate)
                .append("') and ");

        String whsName = rec.getWhsName();
        String receiptType = rec.getReceiptType();
        String goodsId = String.valueOf(rec.getGoodsId());
        String goodsType = rec.getGoodsType();

        if (whsName != null && !"".equals(whsName)) {
            CommonUtil.spliceSql(whsName, "d.whs_name", sql);
        }
        if (receiptType != null && !"".equals(receiptType)) {
            CommonUtil.spliceSql(receiptType, "a.receipt_type", sql);
        }
        if (!"-1".equals(goodsId)) {
            CommonUtil.spliceSql(goodsId, "b.goods_id", sql);
        }
        if (goodsType != null && !"".equals(goodsType)) {
            CommonUtil.spliceSql(goodsType, "c.goods_type_name", sql);
        }

        sql.append("a.is_deleted != 1 ");
        return sql;
    }
}
