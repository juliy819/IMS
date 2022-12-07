package com.juliy.ims.service;

import com.juliy.ims.entity.table_unit.RecordDO;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * 记录查询页面业务接口
 * @author JuLiy
 * @date 2022/12/6 15:43
 */
public interface RecordQueryService {

    /**
     * 获取所有记录信息
     * @return 记录信息集合
     */
    List<RecordDO> getAllRec();

    /**
     * 获取符合筛选要求的记录数量
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @param rec       筛选要求，参数为仓库、货品编号、货品类别
     * @return 符合要求的记录数量
     */
    int getFilterCount(String startDate, String endDate, RecordDO rec);

    /**
     * 筛选记录，获取参数由下拉框选项给出，若未选择，则默认获取所有记录
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @param rec       筛选要求，参数为仓库、货品编号、货品类别
     * @param page      页面数
     * @param pageSize  页面容量
     * @return 库存信息集合，可视化列表
     */
    ObservableList<RecordDO> filterRecByPage(String startDate, String endDate, RecordDO rec, int page, int pageSize);
}
