package com.juliy.ims.service;

import com.juliy.ims.entity.Goods;

import java.util.List;

/**
 * 货品列表页面业务接口
 * @author JuLiy
 * @date 2022/10/10 16:06
 */
public interface GoodsListService {

    /**
     * 获取货品信息集合
     * @return 货品信息集合
     */
    List<Goods> getGoodsList();
}
