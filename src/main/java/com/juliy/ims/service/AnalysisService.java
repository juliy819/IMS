package com.juliy.ims.service;

import com.juliy.ims.entity.Goods;
import com.juliy.ims.entity.table_unit.InvAnalysisDO;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * 库存分析页面业务接口
 * @author JuLiy
 * @date 2022/12/6 20:28
 */
public interface AnalysisService {

    /**
     * 获取所有货品信息
     * @return 货品信息集合
     */
    List<Goods> getAllGoods();

    /**
     * 获取符合筛选要求的库存记录数量
     * @return 符合要求的库存记录数量
     */
    int getFilterCount(String tableType, String id, String type, String name, String spec);

    /**
     * 筛选库存记录，获取参数由下拉框选项给出，若未选择，则默认获取所有货品信息
     * @param page     页面数
     * @param pageSize 页面容量
     * @return 库存信息集合，可视化列表
     */
    ObservableList<InvAnalysisDO> filterByPage(String tableType, int page, int pageSize);
}
