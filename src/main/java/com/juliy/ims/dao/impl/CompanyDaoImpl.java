package com.juliy.ims.dao.impl;

import com.juliy.ims.dao.CompanyDao;
import com.juliy.ims.entity.Company;
import com.juliy.ims.exception.DaoException;
import com.juliy.ims.utils.JdbcUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * company表数据库操作实现类
 * @author JuLiy
 * @date 2022/10/9 23:00
 */
public class CompanyDaoImpl extends BaseDao implements CompanyDao {

    @Override
    public List<Company> queryAll() {
        List<Company> list = new ArrayList<>();
        String sql = "select company_id, company_name, company_type from t_company where is_deleted != 1";
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            rs = pStatement.executeQuery();
            while (rs.next()) {
                Company company = new Company();
                company.setCompanyId(rs.getInt("company_id"));
                company.setCompanyName(rs.getString("company_name"));
                company.setCompanyType(rs.getString("company_type"));
                list.add(company);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
        return list;
    }

    @Override
    public boolean insert(Company company) {
        String sql = "insert into t_company(company_type, company_name, company_add, contact_name, contact_phone, " +
                "contact_email, contact_post, bank_name, bank_acct, tax_id, supplier_comment) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conn = JdbcUtil.getConnection();
        try {
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, company.getCompanyType());
            pStatement.setString(2, company.getCompanyName());
            pStatement.setString(3, company.getCompanyAdd());
            pStatement.setString(4, company.getContactName());
            pStatement.setString(5, company.getContactPhone());
            pStatement.setString(6, company.getContactEmail());
            pStatement.setString(7, company.getContactPost());
            pStatement.setString(8, company.getBankName());
            pStatement.setString(9, company.getBankAcct());
            pStatement.setString(10, company.getTaxId());
            pStatement.setString(11, company.getCompanyComment());

            return pStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            JdbcUtil.release(rs, pStatement, conn);
        }
    }

    @Override
    public boolean isNameExist(String name) {
        return super.isNameExist(name, "t_company", "company_name");
    }
}
