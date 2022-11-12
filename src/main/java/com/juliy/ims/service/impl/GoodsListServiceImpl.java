package com.juliy.ims.service.impl;

import com.juliy.ims.dao.GoodsDao;
import com.juliy.ims.dao.impl.GoodsDaoImpl;
import com.juliy.ims.entity.Goods;
import com.juliy.ims.service.GoodsListService;

import java.util.List;

/**
 * 货品列表页面业务实现类
 * @author JuLiy
 * @date 2022/10/10 16:30
 */
public class GoodsListServiceImpl implements GoodsListService {
    private final GoodsDao goodsDao = new GoodsDaoImpl();

    @Override
    public List<Goods> getGoodsList() {
        return goodsDao.queryAllGoods();
    }
}