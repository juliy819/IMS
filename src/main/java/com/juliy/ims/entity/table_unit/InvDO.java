package com.juliy.ims.entity.table_unit;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存记录，作为Inventory扩展
 * @author JuLiy
 * @date 2022/11/16 22:45
 * @see com.juliy.ims.entity.Inventory
 */
@Data
@NoArgsConstructor
public class InvDO {
    /** 仓库名称 */
    private String whsName;
    /** 货品编号 */
    private Integer goodsId;
    /** 货品类别名称 */
    private String goodsTypeName;
    /** 货品名称 */
    private String goodsName;
    /** 货品规格 */
    private String goodsSpec;
    /** 单位 */
    private String goodsUnit;
    /** 库存量 */
    private Integer goodsQty;
}
