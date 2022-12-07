package com.juliy.ims.entity.table_unit;

import com.juliy.ims.entity.Goods;
import lombok.Data;

/**
 * 库存分析表格数据载体
 * @author JuLiy
 * @date 2022/12/6 23:59
 */
@Data
public class InvAnalysisDO {
    /** 货品编号 */
    private Integer id;
    /** 货品类别名称 */
    private String type;
    /** 货品名称 */
    private String name;
    /** 货品规格 */
    private String spec;
    /** 单位 */
    private String unit;
    /** 当前库存量 */
    private Integer curQty;
    /** 最高库存量 */
    private Integer maxQty;
    /** 超储量 */
    private Integer overQty;
    /** 最低库存量 */
    private Integer minQty;
    /** 短缺量 */
    private Integer underQty;

    public static InvAnalysisDO parseFromGoods(Goods goods) {
        InvAnalysisDO inv = new InvAnalysisDO();
        inv.id = goods.getGoodsId();
        inv.type = goods.getGoodsTypeName();
        inv.name = goods.getGoodsName();
        inv.spec = goods.getGoodsSpec();
        inv.unit = goods.getGoodsUnit();
        inv.maxQty = goods.getMaxQty();
        inv.minQty = goods.getMinQty();
        return inv;
    }
}
