package com.juliy.ims.dao;

import com.juliy.ims.entity.DO.InvDO;

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
    List<InvDO> queryAllInv();

    /**
     * 查询指定库存信息
     * @param sql 查询语句
     * @return 符合要求的库存信息列表；若不存在则列表长度为0
     */
    List<InvDO> queryInv(String sql);

    /**
     * 查询库存信息
     * @return 库存信息总数
     */
    int queryInvCount();

    /**
     * 查询指定库存信息
     * @param sql 查询语句
     * @return 满足条件的库存信息总数
     */
    int queryInvCount(String sql);
}
