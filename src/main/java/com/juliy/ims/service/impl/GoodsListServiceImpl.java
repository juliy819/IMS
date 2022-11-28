package com.juliy.ims.service.impl;

import com.juliy.ims.dao.GoodsDao;
import com.juliy.ims.dao.impl.GoodsDaoImpl;
import com.juliy.ims.entity.Goods;
import com.juliy.ims.service.GoodsListService;
import com.juliy.ims.utils.CommonUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * 货品列表页面业务实现类
 * @author JuLiy
 * @date 2022/10/10 16:30
 */
public class GoodsListServiceImpl implements GoodsListService {

    private final GoodsDao goodsDao = new GoodsDaoImpl();

    @Override
    public List<Goods> getAllGoods() {
        return goodsDao.queryAll();
    }

    @Override
    public int getTotalCount() {
        return goodsDao.queryCount();
    }

    @Override
    public int getFilterCount(String goodsTypeName, String goodsId, String goodsName, String goodsSpec) {
        StringBuilder sql = generateSql(goodsTypeName, goodsId, goodsName, goodsSpec);
        sql.delete(7, sql.indexOf("from") - 1).insert(7, "count(*)");
        return goodsDao.queryCount(String.valueOf(sql));
    }

    @Override
    public ObservableList<Goods> filterGoodsByPage(String goodsTypeName, String goodsId, String goodsName, String goodsSpec, int page, int pageSize) {
        StringBuilder sql = generateSql(goodsTypeName, goodsId, goodsName, goodsSpec);
        int start = (page - 1) * pageSize;
        sql.append("limit ").append(start).append(",").append(pageSize);
        List<Goods> list = goodsDao.query(String.valueOf(sql));
        return FXCollections.observableArrayList(list);
    }

    private StringBuilder generateSql(String goodsTypeName, String goodsId, String goodsName, String goodsSpec) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.*, b.goods_type_name " +
                           "from t_goods a " +
                           "left join t_goods_type b on a.goods_type_id = b.goods_type_id " +
                           "where ");

        if (goodsTypeName != null && !"".equals(goodsTypeName)) {
            CommonUtil.spliceSql(goodsTypeName, "b.goods_type_name", sql);
        }
        if (goodsId != null && !"".equals(goodsId)) {
            CommonUtil.spliceSql(goodsId, "a.goods_id", sql);
        }
        if (goodsName != null && !"".equals(goodsName)) {
            CommonUtil.spliceSql(goodsName, "a.goods_name", sql);
        }
        if (goodsSpec != null && !"".equals(goodsSpec)) {
            CommonUtil.spliceSql(goodsSpec, "a.goods_spec", sql);
        }

        sql.append("a.is_deleted != 1 ");
        return sql;
    }
}