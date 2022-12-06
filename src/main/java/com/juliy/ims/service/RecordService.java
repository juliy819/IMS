package com.juliy.ims.service;

import com.juliy.ims.entity.Company;
import com.juliy.ims.entity.Goods;
import com.juliy.ims.entity.Warehouse;
import com.juliy.ims.entity.model.RecordDO;

import java.util.List;

/**
 * 表单页面业务接口
 * @author JuLiy
 * @date 2022/12/2 19:59
 */
public interface RecordService {

    /**
     * 添加入库单据
     * @param recList 记录列表
     */
    void addEntryReceipt(List<RecordDO> recList);

    /**
     * 添加出库单据
     * @param recList 记录列表
     */
    void addOutReceipt(List<RecordDO> recList);

    /**
     * 添加调拨单据
     * @param recList 记录列表
     */
    void addALocReceipt(List<RecordDO> recList);

    /**
     * 获取货品库存数量
     * @param whsId   仓库编号
     * @param goodsId 货品编号
     * @return 货品库存数量
     */
    int getGoodsQty(int whsId, int goodsId);

    /**
     * 获取指定编号的货品
     * @param id 货品编号
     * @return 对应的货品
     */
    Goods getGoods(int id);

    /**
     * 获取所有货品编号
     * @return 货品编号列表
     */
    List<Integer> getGoodsIdList();

    /**
     * 获取所有供应商
     * @return 供应商列表
     */
    List<Company> getSupplierList();

    /**
     * 获取所有客户
     * @return 客户列表
     */
    List<Company> getCustomerList();

    /**
     * 获取所有仓库
     * @return 仓库列表
     */
    List<Warehouse> getWarehouseList();
}
