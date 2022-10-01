package com.juliy.ims.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 货品
 * @author JuLiy
 * @date 2022/10/1 15:40
 */
@Data
@NoArgsConstructor
public class Goods {
    /** 货品编号 */
    private Integer goodsId;
    /** 货品类别编号 */
    private Integer goodsTypeId;
    /** 货品名称 */
    private String goodsName;
    /** 货品规格 */
    private String goodsSpec;
    /** 单位 */
    private String goodsUnit;
    /** 参考进价 */
    private BigDecimal refPurPrice;
    /** 参考售价 */
    private BigDecimal refSellPrice;
    /** 最高库存 */
    private Integer maxQty;
    /** 最高库存 */
    private Integer minQty;
    /** 备注 */
    private String goodsComment;
    /** 是否删除 */
    private Boolean deleted;
}
