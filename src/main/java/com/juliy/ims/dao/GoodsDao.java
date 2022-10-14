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
     * @return 货品列表；查询失败情况：列表长度为0
     */
    List<Goods> queryAllGoods();

    /**
     * 根据货品编号查询指定货品信息
     * @param id 货品编号
     * @return 指定货品对象；查询失败情况：对象为null
     */
    Goods queryGoodsById(Integer id);
}
