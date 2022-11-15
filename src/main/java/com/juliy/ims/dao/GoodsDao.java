package com.juliy.ims.dao;

import com.juliy.ims.entity.Goods;

import java.util.List;

/**
 * goods表数据库操作接口
 * @author JuLiy
 * @date 2022/10/9 22:52
 */
public interface GoodsDao {
    /**
     * 查询所有货品信息
     * @return 货品列表；若不存在则列表长度为0
     */
    List<Goods> queryAllGoods();

    /**
     * 查询指定货品
     * @param sql 查询语句
     * @return 符合要求的货品列表；若不存在则列表长度为0
     */
    List<Goods> queryGoods(String sql);
}
