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
     * @return 货品列表
     */
    List<Goods> queryAll();

    /**
     * 查询指定货品
     * @param sql 查询语句
     * @return 符合要求的货品列表；若不存在则列表长度为0
     */
    List<Goods> query(String sql);

    /**
     * 查询货品总数
     * @return 货品总数
     */
    int queryCount();

    /**
     * 查询指定货品总数
     * @param sql 查询语句
     * @return 满足条件的货品总数
     */
    int queryCount(String sql);


    /**
     * 添加新货品
     * @param goods 货品
     * @return 添加成功返回true，添加失败返回false
     */
    boolean insert(Goods goods);

    /**
     * 判断货品名称是否存在
     * @param name 货品名称
     * @return 存在返回true，不存在返回false
     */
    boolean isNameExist(String name);
}
