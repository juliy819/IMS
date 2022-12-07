package com.juliy.ims.service.impl;

import com.juliy.ims.dao.GoodsDao;
import com.juliy.ims.dao.InventoryDao;
import com.juliy.ims.dao.impl.GoodsDaoImpl;
import com.juliy.ims.dao.impl.InventoryDaoImpl;
import com.juliy.ims.entity.Goods;
import com.juliy.ims.entity.table_unit.InvAnalysisDO;
import com.juliy.ims.service.AnalysisService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

import static com.juliy.ims.service.impl.GoodsListServiceImpl.generateSql;

/**
 * 库存分析页面业务实现类
 * @author JuLiy
 * @date 2022/12/6 20:28
 */
public class AnalysisServiceImpl implements AnalysisService {

    private final InventoryDao inventoryDao = new InventoryDaoImpl();
    private final GoodsDao goodsDao = new GoodsDaoImpl();

    private List<InvAnalysisDO> analysisList;
    private List<InvAnalysisDO> filterList;
    private String sql = "";

    @Override
    public List<Goods> getAllGoods() {
        return goodsDao.queryAll();
    }

    @Override
    public int getFilterCount(String tableType, String id, String type, String name, String spec) {
        String newSql = String.valueOf(generateSql(type, id, name, spec));
        if (!newSql.equals(sql)) {
            sql = newSql;
            initAnalysisList(sql);
        }

        if ("over".equals(tableType)) {
            filterList = analysisList.stream().filter(inv -> inv.getOverQty() > 0).toList();
        } else {
            filterList = analysisList.stream().filter(inv -> inv.getUnderQty() < 0).toList();
        }

        return filterList.size();
    }

    @Override
    public ObservableList<InvAnalysisDO> filterByPage(String tableType, int page, int pageSize) {
        if ("over".equals(tableType)) {
            filterList = analysisList.stream().filter(inv -> inv.getOverQty() > 0).toList();
        } else {
            filterList = analysisList.stream().filter(inv -> inv.getUnderQty() < 0).toList();
        }

        int start = (page - 1) * pageSize;
        int end = start + pageSize;
        List<InvAnalysisDO> pageList = filterList.subList(start, Math.min(end, filterList.size()));

        return FXCollections.observableArrayList(pageList);
    }

    private synchronized void initAnalysisList(String sql) {
        analysisList = goodsDao.query(sql)
                .stream()
                .map(InvAnalysisDO::parseFromGoods)
                .toList();

        analysisList.forEach(inv -> {
            int curQty = inventoryDao.queryQty(inv.getId());
            inv.setCurQty(curQty);
            //计算超储量和短缺量
            inv.setOverQty(Math.max(curQty - inv.getMaxQty(), 0));
            inv.setUnderQty(Math.min(curQty - inv.getMinQty(), 0));
        });
    }
}
