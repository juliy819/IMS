package com.juliy.ims.service;

import com.juliy.ims.entity.Goods;
import com.juliy.ims.entity.GoodsType;
import com.juliy.ims.entity.Warehouse;

import java.util.List;

/**
 * 添加新记录业务接口
 * @author JuLiy
 * @date 2022/11/23 10:34
 */
public interface AddNewService {

    /**
     * 添加新仓库
     * @param warehouse 要添加的仓库
     */
    void addNew(Warehouse warehouse);

    /**
     * 添加新货品类别
     * @param goodsType 要添加的货品类别
     */
    void addNew(GoodsType goodsType);

    /**
     * 添加新货品
     * @param goods 要添加的货品
     */
    void addNew(Goods goods);

    /**
     * 判断名称是否已存在
     * @param name      名称 例:货品名、仓库名等
     * @param tableName 表名
     * @return 存在返回true，不存在返回false
     */
    boolean isNameExist(String name, String tableName);

    /**
     * 获取所有货品类别
     * @return 货品类别列表
     */
    List<GoodsType> getGoodsTypeList();
}
