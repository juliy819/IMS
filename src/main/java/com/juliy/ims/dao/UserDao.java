package com.juliy.ims.dao;

/**
 * User数据库操作类
 * @author JuLiy
 * @date 2022/9/27 14:57
 */
public interface UserDao {
    /**
     * 查询用户是否存在
     * @param username 用户名
     * @param password 密码
     * @return boolean 存在为true，不存在为false
     */
    boolean queryUser(String username, String password);

}
