package com.juliy.ims.service.impl;

import com.juliy.ims.dao.GoodsDao;
import com.juliy.ims.dao.impl.GoodsDaoImpl;
import com.juliy.ims.entity.Goods;
import com.juliy.ims.service.GoodsListService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 货品列表页面业务实现类
 * @author JuLiy
 * @date 2022/10/10 16:30
 */
public class GoodsListServiceImpl implements GoodsListService {

    private final Logger log = Logger.getLogger(GoodsListServiceImpl.class);
    private final GoodsDao goodsDao = new GoodsDaoImpl();

    /**
     * sql语句拼接<br>
     * 拼接后格式为(末尾有空格)：(查询的内容) and
     * @param str  要拆分的关键词字符串，各关键词间以","分隔
     * @param head 语句头;如goods_type_name
     * @param sql  要拼接的sql语句
     */
    private void spliceSql(String str, String head, StringBuilder sql) {
        sql.append("(");
        String[] arr = str.split(",");
        for (String s : arr) {
            sql.append(head).append(" = ").append("'").append(s).append("'").append(" or ");
        }
        sql.delete(sql.lastIndexOf("or") - 1, sql.length()).append(") and ");
    }

    @Override
    public ObservableList<Goods> getGoodsList(String goodsTypeName, String goodsId, String goodsName, String goodsSpec) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.*, b.goods_type_name from t_goods a, t_goods_type b where ");

        if (goodsTypeName != null && !"".equals(goodsTypeName)) {
            spliceSql(goodsTypeName, "b.goods_type_name", sql);
        }
        if (goodsId != null && !"".equals(goodsId)) {
            spliceSql(goodsId, "a.goods_id", sql);
        }
        if (goodsName != null && !"".equals(goodsName)) {
            spliceSql(goodsName, "a.goods_name", sql);
        }
        if (goodsSpec != null && !"".equals(goodsSpec)) {
            spliceSql(goodsSpec, "a.goods_spec", sql);
        }

        sql.append("a.goods_type_id = b.goods_type_id and a.is_deleted != 1");

        log.info("sql: " + sql);
        //
        List<Goods> list = goodsDao.queryGoods(String.valueOf(sql));
        return FXCollections.observableArrayList(list);
    }
}