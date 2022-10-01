package com.juliy.ims.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户
 * @author JuLiy
 * @date 2022/10/1 17:38
 */
@Data
@NoArgsConstructor
public class Customer {
    /** 客户编号 */
    private Integer customerId;
    /** 客户名称 */
    private String customerName;
    /** 客户地址 */
    private String customerAdd;
    /** 客户银行名称 */
    private String customerBankName;
    /** 客户银行账户 */
    private String customerBankAcct;
    /** 客户税号 */
    private String customerTaxId;
    /** 联系人名称 */
    private String contactName;
    /** 联系人职位 */
    private String contactPost;
    /** 联系人电话 */
    private String contactPhone;
    /** 联系人邮箱 */
    private String contactEmail;
    /** 备注 */
    private String customerComment;
    /** 是否删除 */
    private Boolean deleted;
}
