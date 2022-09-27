package com.juliy.ims.service;

/**
 * User业务类实例
 * @author JuLiy
 * @date 2022/9/27 15:42
 */
public interface UserService {
    /**
     * 判断登录用户是否存在
     * @param username 用户名
     * @param password 密码
     * @return boolean 存在为true，不存在为false
     */
    boolean loginCheck(String username, String password);
}
