package com.juliy.ims.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 出入库记录
 * @author JuLiy
 * @date 2022/10/1 17:47
 */
@Data
@NoArgsConstructor
public class Record {
    /** 记录编号 */
    private Integer recordId;
    /** 业务日期 */
    private Date bizDate;
    /** 仓库编号 */
    private Integer whsId;
    /** 单据类型 */
    private String docType;
    /** 单据编号 */
    private String docId;
    /** 货品编号 */
    private Integer goodsId;
    /** 入库数量 */
    private Integer entryQty;
    /** 入库金额 */
    private BigDecimal entryAmt;
    /** 出库数量 */
    private Integer outQty;
    /** 出库金额 */
    private BigDecimal outAmt;
    /** 是否删除 */
    private Boolean deleted;
}
