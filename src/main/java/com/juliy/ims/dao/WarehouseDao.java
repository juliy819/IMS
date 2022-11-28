package com.juliy.ims.dao;

import com.juliy.ims.entity.Warehouse;

/**
 * warehouse表数据库操作接口
 * @author JuLiy
 * @date 2022/10/9 22:57
 */
public interface WarehouseDao {

    /**
     * 添加新仓库
     * @param warehouse 仓库
     * @return 添加成功返回true，添加失败返回false
     */
    boolean insert(Warehouse warehouse);

    /**
     * 判断仓库名是否存在
     * @param name 仓库名
     * @return 存在则返回true，不存在则返回false
     */
    boolean isNameExist(String name);


}
