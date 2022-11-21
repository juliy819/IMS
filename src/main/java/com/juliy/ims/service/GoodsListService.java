package com.juliy.ims.service;

import com.juliy.ims.entity.Goods;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * 货品列表页面业务接口
 * @author JuLiy
 * @date 2022/10/10 16:06
 */
public interface GoodsListService {

    /**
     * 获取所有货品信息
     * @return 货品信息集合
     */
    List<Goods> getAllGoods();

    /**
     * 获取货品总数
     * @return 货品总数
     */
    int getTotalCount();

    /**
     * 获取符合筛选要求的货品数量
     * @param goodsTypeName 货品类别名称
     * @param goodsId       货品编号
     * @param goodsName     货品名称
     * @param goodsSpec     货品规格
     * @return 符合筛选要求的货品数量
     */
    int getFilterCount(String goodsTypeName, String goodsId, String goodsName, String goodsSpec);

    /**
     * 筛选货品，获取参数由下拉框选项给出，若未选择，则默认获取所有货品信息
     * @param goodsTypeName 货品类别名称
     * @param goodsId       货品编号
     * @param goodsName     货品名称
     * @param goodsSpec     货品规格
     * @param page          页面数
     * @param pageSize      页面容量
     * @return 货品信息集合，可视化列表
     */
    ObservableList<Goods> filterGoodsByPage(String goodsTypeName, String goodsId, String goodsName, String goodsSpec, int page, int pageSize);


}
