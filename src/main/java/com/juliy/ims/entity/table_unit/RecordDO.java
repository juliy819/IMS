package com.juliy.ims.entity.table_unit;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author JuLiy
 * @date 2022/12/4 15:29
 */
@Data
public class RecordDO {
    /** 记录编号 */
    private Integer recordId;
    /** 公司编号 */
    private Integer companyId;
    /** 仓库编号 */
    private Integer whsId;
    /** 仓库名称 */
    private String whsName;
    /** 单据类型 */
    private String receiptType;
    /** 单据编号 */
    private String receiptId;
    /** 货品编号 */
    private Integer goodsId;
    /** 货品类别 */
    private String goodsType;
    /** 货品名称 */
    private String goodsName;
    /** 货品规格 */
    private String goodsSpec;
    /** 参考进价 */
    private BigDecimal refPurPrice;
    /** 参考售价 */
    private BigDecimal refSellPrice;
    /** 入库数量 */
    private Integer entryQty;
    /** 入库金额 */
    private BigDecimal entryAmt;
    /** 出库数量 */
    private Integer outQty;
    /** 出库金额 */
    private BigDecimal outAmt;
    /** 业务日期 */
    private Date bizDate;
}
