package com.juliy.ims.dao;

import com.juliy.ims.entity.User;

/**
 * user表数据库操作接口
 * @author JuLiy
 * @date 2022/9/27 14:57
 */
public interface UserDao {
    /**
     * 查询用户是否存在
     * @param username 用户名
     * @param password 密码
     * @return 若存在，返回该用户对象，否则返回null
     */
    User queryUser(String username, String password);

}
