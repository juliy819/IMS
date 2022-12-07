package com.juliy.ims.dao;

import com.juliy.ims.entity.Inventory;
import com.juliy.ims.entity.table_unit.InvDO;

import java.util.List;

/**
 * inventory表数据库操作接口
 * @author JuLiy
 * @date 2022/10/9 22:56
 */
public interface InventoryDao {

    /**
     * 查询所有库存信息
     * @return 库存信息列表
     */
    List<InvDO> queryAll();

    /**
     * 查询指定库存信息
     * @param sql 查询语句
     * @return 符合要求的库存信息列表；若不存在则列表长度为0
     */
    List<InvDO> query(String sql);

    /**
     * 查找指定仓库中某一种货品的数量
     * @param whsId   仓库编号
     * @param goodsId 货品编号
     * @return 若仓库中有对应的货品则返回其数量，否则返回0
     */
    int queryQty(int whsId, int goodsId);

    /**
     * 查询某一种货品的总库存量
     * @param goodsId 货品编号
     * @return 总库存量
     */
    int queryQty(int goodsId);

    /**
     * 查询指定库存信息
     * @param sql 查询语句
     * @return 满足条件的库存信息总数
     */
    int queryCount(String sql);

    /**
     * 插入库存信息，若存在则更新数量
     * @param inv 库存信息
     */
    void insertOrUpdate(Inventory inv);
}
