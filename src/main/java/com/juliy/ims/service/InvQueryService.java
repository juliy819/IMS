package com.juliy.ims.service;

import com.juliy.ims.entity.table_unit.InvDO;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * 库存查询页面业务接口
 * @author JuLiy
 * @date 2022/11/16 22:39
 */
public interface InvQueryService {

    /**
     * 获取所有库存信息
     * @return 库存信息集合
     */
    List<InvDO> getAllInv();

    /**
     * 获取符合筛选要求的库存记录数量
     * @return 符合要求的库存记录数量
     */
    int getFilterCount(String whsName, String id, String type, String name, String spec);

    /**
     * 筛选库存记录，获取参数由下拉框选项给出，若未选择，则默认获取所有货品信息
     * @param whsName  仓库名
     * @param id       货品id
     * @param type     货品类别名称
     * @param name     货品名称
     * @param spec     货品规格
     * @param page     页面数
     * @param pageSize 页面容量
     * @return 库存信息集合，可视化列表
     */
    ObservableList<InvDO> filterInvByPage(String whsName, String id, String type, String name, String spec, int page, int pageSize);
}
