package com.juliy.ims.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 供应商
 * @author JuLiy
 * @date 2022/10/1 17:20
 */
@Data
@NoArgsConstructor
public class Supplier {
    /** 供应商编号 */
    private Integer supplierId;
    /** 供应商名称 */
    private String supplierName;
    /** 供应商地址 */
    private String supplierAdd;
    /** 供应商银行名称 */
    private String supplierBankName;
    /** 供应商银行账户 */
    private String supplierBankAcct;
    /** 供应商税号 */
    private String supplierTaxId;
    /** 联系人名称 */
    private String contactName;
    /** 联系人职位 */
    private String contactPost;
    /** 联系人电话 */
    private String contactPhone;
    /** 联系人邮箱 */
    private String contactEmail;
    /** 备注 */
    private String supplierComment;
    /** 是否删除 */
    private Boolean deleted;
}
