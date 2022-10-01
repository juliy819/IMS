package com.juliy.ims.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 仓库
 * @author JuLiy
 * @date 2022/10/1 17:11
 */
@Data
@NoArgsConstructor
public class WareHouse {
    /** 仓库编号 */
    private Integer whsId;
    /** 仓库名称 */
    private String whsName;
    /** 仓库地址 */
    private String whsAdd;
    /** 负责人姓名 */
    private String managerName;
    /** 负责人电话 */
    private String managerPhone;
    /** 备注 */
    private String whsComment;
    /** 是否删除 */
    private Boolean deleted;
}
