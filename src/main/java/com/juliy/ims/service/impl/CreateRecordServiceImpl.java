package com.juliy.ims.service.impl;

import com.juliy.ims.dao.*;
import com.juliy.ims.dao.impl.*;
import com.juliy.ims.entity.Record;
import com.juliy.ims.entity.*;
import com.juliy.ims.entity.model.RecordDO;
import com.juliy.ims.service.CreateRecordService;

import java.util.List;

/**
 * 表单页面业务实现类
 * @author JuLiy
 * @date 2022/12/2 20:00
 */
public class CreateRecordServiceImpl implements CreateRecordService {

    private final RecordDao recordDao = new RecordDaoImpl();
    private final CompanyDao companyDao = new CompanyDaoImpl();
    private final WarehouseDao warehouseDao = new WarehouseDaoImpl();
    private final GoodsDao goodsDao = new GoodsDaoImpl();
    private final InventoryDao invDao = new InventoryDaoImpl();

    @Override
    public void addEntryReceipt(List<RecordDO> recList) {
        recList.forEach(recDO -> {
            recordDao.insert(toRecord(recDO));
            invDao.insertOrUpdate(toEntryInventory(recDO));
        });
    }

    public void addOutReceipt(List<RecordDO> recList) {
        recList.forEach(recDO -> {
            recordDao.insert(toRecord(recDO));
            invDao.insertOrUpdate(toOutInventory(recDO));
        });
    }

    @Override
    public void addALocReceipt(List<RecordDO> recList) {
        recList.forEach(recDO -> {
            //将原始记录转为入库记录
            Record entryRec = toRecord(recDO);
            entryRec.setReceiptType(entryRec.getReceiptType() + "入库");
            entryRec.setOutQty(0);
            entryRec.setWhsId(entryRec.getCompanyId());
            entryRec.setCompanyId(-1);
            recordDao.insert(entryRec);
            Inventory entryInv = toEntryInventory(recDO);
            entryInv.setWhsId(recDO.getCompanyId());
            invDao.insertOrUpdate(entryInv);

            //将原始记录转为出库记录
            Record outRec = toRecord(recDO);
            outRec.setReceiptType(outRec.getReceiptType() + "出库");
            outRec.setEntryQty(0);
            outRec.setCompanyId(-1);
            recordDao.insert(outRec);
            invDao.insertOrUpdate(toEntryInventory(recDO));
        });
    }


    @Override
    public int getGoodsQty(int whsId, int goodsId) {
        return invDao.queryQty(whsId, goodsId);
    }

    @Override
    public Goods getGoods(int id) {
        return goodsDao.query(id);
    }

    @Override
    public List<Integer> getGoodsIdList() {
        return goodsDao.queryAll()
                .stream()
                .map(Goods::getGoodsId)
                .toList();
    }

    @Override
    public List<Company> getSupplierList() {
        return companyDao.queryAll()
                .stream()
                .filter(item -> "供应商".equals(item.getCompanyType()))
                .toList();
    }

    @Override
    public List<Company> getCustomerList() {
        return companyDao.queryAll()
                .stream()
                .filter(item -> "客户".equals(item.getCompanyType()))
                .toList();
    }

    @Override
    public List<Warehouse> getWarehouseList() {
        return warehouseDao.queryAll();
    }

    private Record toRecord(RecordDO recDO) {
        Record rec = new Record();
        rec.setCompanyId(recDO.getCompanyId());
        rec.setWhsId(recDO.getWhsId());
        rec.setReceiptType(recDO.getReceiptType());
        rec.setReceiptId(recDO.getReceiptId());
        rec.setGoodsId(recDO.getGoodsId());
        rec.setEntryQty(recDO.getEntryQty());
        rec.setEntryAmt(recDO.getEntryAmt());
        rec.setOutQty(recDO.getOutQty());
        rec.setOutAmt(recDO.getOutAmt());
        return rec;
    }

    private Inventory toEntryInventory(RecordDO recDO) {
        Inventory inv = new Inventory();
        inv.setWhsId(recDO.getWhsId());
        inv.setGoodsId(recDO.getGoodsId());
        inv.setGoodsQty(recDO.getEntryQty());
        return inv;
    }

    private Inventory toOutInventory(RecordDO recDO) {
        Inventory inv = new Inventory();
        inv.setWhsId(recDO.getWhsId());
        inv.setGoodsId(recDO.getGoodsId());
        inv.setGoodsQty(-recDO.getOutQty());
        return inv;
    }
}
