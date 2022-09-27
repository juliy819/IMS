package com.juliy.ims.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * User实体类
 * @author JuLiy
 * @date 2022/9/27 10:29
 */
@Data
@NoArgsConstructor
public class User {
    private int id;
    private String username;
    private String password;
    private Date gmtCreate;
    private Date gmtModified;
}
