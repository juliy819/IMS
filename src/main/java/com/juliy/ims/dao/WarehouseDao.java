package com.juliy.ims.dao;

import com.juliy.ims.entity.Warehouse;

import java.util.List;

/**
 * warehouse表数据库操作接口
 * @author JuLiy
 * @date 2022/10/9 22:57
 */
public interface WarehouseDao {

    /**
     * 查询所有仓库
     * @return 仓库列表
     */
    List<Warehouse> queryAll();

    /**
     * 添加新仓库
     * @param warehouse 仓库
     */
    void insert(Warehouse warehouse);

    /**
     * 判断仓库名是否存在
     * @param name 仓库名
     * @return 存在则返回true，不存在则返回false
     */
    boolean isNameExist(String name);


}
