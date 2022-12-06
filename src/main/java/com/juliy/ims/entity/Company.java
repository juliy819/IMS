package com.juliy.ims.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公司
 * @author JuLiy
 * @date 2022/10/1 17:20
 */
@Data
@NoArgsConstructor
public class Company {
    /** 公司编号 */
    private Integer companyId;
    /** 类别，客户/供应商 */
    private String companyType;
    /** 公司名称 */
    private String companyName;
    /** 公司地址 */
    private String companyAdd;
    /** 银行名称 */
    private String bankName;
    /** 银行账户 */
    private String bankAcct;
    /** 税号 */
    private String taxId;
    /** 联系人名称 */
    private String contactName;
    /** 联系人职位 */
    private String contactPost;
    /** 联系人电话 */
    private String contactPhone;
    /** 联系人邮箱 */
    private String contactEmail;
    /** 备注 */
    private String companyComment;
    /** 是否删除 */
    private Boolean deleted;
}
