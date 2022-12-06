package com.juliy.ims.dao;

import com.juliy.ims.entity.Company;

import java.util.List;

/**
 * company表数据库操作接口
 * @author JuLiy
 * @date 2022/10/9 22:56
 */
public interface CompanyDao {

    /**
     * 查询所有公司
     * @return 公司列表
     */
    List<Company> queryAll();

    /**
     * 添加新公司
     * @param company 公司
     * @return 添加成功返回true，添加失败返回false
     */
    boolean insert(Company company);

    /**
     * 判断公司名称是否存在
     * @param name 公司名称
     * @return 存在返回true，不存在返回false
     */
    boolean isNameExist(String name);

}
