package com.juliy.ims.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录用户
 * @author JuLiy
 * @date 2022/9/27 10:29
 */
@Data
@NoArgsConstructor
public class User {
    /** 用户编号 */
    private Integer userId;
    /** 用户昵称 */
    private String nickname;
    /** 账号 */
    private String username;
    /** 密码 */
    private String password;
    /** 是否删除 */
    private Boolean deleted;
}
