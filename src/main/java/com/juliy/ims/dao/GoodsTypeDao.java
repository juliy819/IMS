package com.juliy.ims.dao;

import com.juliy.ims.entity.GoodsType;

import java.util.List;

/**
 * goodsType表数据库操作接口
 * @author JuLiy
 * @date 2022/10/9 22:54
 */
public interface GoodsTypeDao {

    /**
     * 查询所有货品类别
     * @return 货品类别列表
     */
    List<GoodsType> queryAll();

    /**
     * 添加新货品类别
     * @param goodsType 货品类别
     * @return 添加成功返回true，添加失败返回false
     */
    boolean insert(GoodsType goodsType);


    /**
     * 判断货品类别名称是否存在
     * @param name 货品类别名称
     * @return 存在返回true，不存在返回false
     */
    boolean isNameExist(String name);
}
