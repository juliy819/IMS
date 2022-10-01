package com.juliy.ims.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 货品类别
 * @author JuLiy
 * @date 2022/10/1 15:50
 */
@Data
@NoArgsConstructor
public class GoodsType {
    /** 货品类别编号 */
    private Integer goodsTypeId;
    /** 货品类别名称 */
    private String goodsTypeName;
    /** 是否删除 */
    private Boolean deleted;
}
