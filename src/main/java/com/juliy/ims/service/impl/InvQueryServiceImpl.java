package com.juliy.ims.service.impl;

import com.juliy.ims.dao.InventoryDao;
import com.juliy.ims.dao.impl.InventoryDaoImpl;
import com.juliy.ims.entity.model.InvDO;
import com.juliy.ims.service.InvQueryService;
import com.juliy.ims.utils.CommonUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * 库存查询页面业务实现类
 * @author JuLiy
 * @date 2022/11/16 22:39
 */
public class InvQueryServiceImpl implements InvQueryService {
    private final InventoryDao inventoryDao = new InventoryDaoImpl();

    @Override
    public List<InvDO> getAllInv() {
        return inventoryDao.queryAllInv();
    }

    @Override
    public int getTotalCount() {
        return inventoryDao.queryInvCount();
    }

    @Override
    public int getFilterCount(String whsName, String id, String type, String name, String spec) {
        StringBuilder sql = generateSql(whsName, id, type, name, spec);
        sql.delete(7, sql.indexOf("from") - 1).insert(7, "count(*)");
        return inventoryDao.queryInvCount(String.valueOf(sql));
    }

    @Override
    public ObservableList<InvDO> filterInvByPage(String whsName, String id, String type, String name, String spec, int page, int pageSize) {
        StringBuilder sql = generateSql(whsName, id, type, name, spec);
        int start = (page - 1) * pageSize;
        sql.append("limit ").append(start).append(",").append(pageSize);
        List<InvDO> list = inventoryDao.queryInv(String.valueOf(sql));
        return FXCollections.observableArrayList(list);
    }

    private StringBuilder generateSql(String whsName, String goodsId, String goodsTypeName, String goodsName, String goodsSpec) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.goods_qty, b.goods_id, b.goods_name, b.goods_spec, b.goods_unit, c.goods_type_name, d.whs_name " +
                           "from t_inventory a " +
                           "left join t_goods b on a.goods_id = b.goods_id " +
                           "left join t_goods_type c on b.goods_type_id = c.goods_type_id " +
                           "left join t_warehouse d on a.whs_id = d.whs_id " +
                           "where ");

        if (whsName != null && !"".equals(whsName)) {
            CommonUtil.spliceSql(whsName, "d.whs_name", sql);
        }
        if (goodsId != null && !"".equals(goodsId)) {
            CommonUtil.spliceSql(goodsId, "b.goods_id", sql);
        }
        if (goodsTypeName != null && !"".equals(goodsTypeName)) {
            CommonUtil.spliceSql(goodsTypeName, "c.goods_type_name", sql);
        }
        if (goodsName != null && !"".equals(goodsName)) {
            CommonUtil.spliceSql(goodsName, "b.goods_name", sql);
        }
        if (goodsSpec != null && !"".equals(goodsSpec)) {
            CommonUtil.spliceSql(goodsSpec, "b.goods_spec", sql);
        }

        sql.append("a.is_deleted != 1 ");
        return sql;
    }
}
