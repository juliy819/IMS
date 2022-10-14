package com.juliy.ims.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前库存
 * @author JuLiy
 * @date 2022/10/1 17:09
 */
@Data
@NoArgsConstructor
public class Inventory {
    /** 库存编号 */
    private Integer inventoryId;
    /** 仓库编号 */
    private Integer whsId;
    /** 货品编号 */
    private Integer goodsId;
    /** 库存量 */
    private Integer goodsQty;
    /** 是否删除 */
    private Boolean deleted;
}
