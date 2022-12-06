package com.juliy.ims.service.impl;

import com.juliy.ims.dao.CompanyDao;
import com.juliy.ims.dao.GoodsDao;
import com.juliy.ims.dao.GoodsTypeDao;
import com.juliy.ims.dao.WarehouseDao;
import com.juliy.ims.dao.impl.CompanyDaoImpl;
import com.juliy.ims.dao.impl.GoodsDaoImpl;
import com.juliy.ims.dao.impl.GoodsTypeDaoImpl;
import com.juliy.ims.dao.impl.WarehouseDaoImpl;
import com.juliy.ims.entity.Company;
import com.juliy.ims.entity.Goods;
import com.juliy.ims.entity.GoodsType;
import com.juliy.ims.entity.Warehouse;
import com.juliy.ims.exception.SwitchErrorException;
import com.juliy.ims.service.AddNewService;

import java.util.List;

/**
 * 添加新记录业务实现类
 * @author JuLiy
 * @date 2022/11/23 10:35
 */
public class AddNewServiceImpl implements AddNewService {

    private final WarehouseDao warehouseDao = new WarehouseDaoImpl();
    private final GoodsTypeDao goodsTypeDao = new GoodsTypeDaoImpl();
    private final GoodsDao goodsDao = new GoodsDaoImpl();
    private final CompanyDao companyDao = new CompanyDaoImpl();


    @Override
    public void addNew(Warehouse warehouse) {
        warehouseDao.insert(warehouse);
    }

    @Override
    public void addNew(GoodsType goodsType) {
        goodsTypeDao.insert(goodsType);
    }

    @Override
    public void addNew(Goods goods) {
        goodsDao.insert(goods);
    }

    @Override
    public void addNew(Company company) {
        companyDao.insert(company);
    }


    @Override
    public boolean isNameExist(String name, String tableName) {
        switch (tableName) {
            case "t_warehouse" -> {
                return warehouseDao.isNameExist(name);
            }
            case "t_goods_type" -> {
                return goodsTypeDao.isNameExist(name);
            }
            case "t_goods" -> {
                return goodsDao.isNameExist(name);
            }
            case "t_company" -> {
                return companyDao.isNameExist(name);
            }
            default -> throw new SwitchErrorException();
        }
    }

    @Override
    public List<GoodsType> getGoodsTypeList() {
        return goodsTypeDao.queryAll();
    }
}
