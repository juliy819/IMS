package com.juliy.ims.service;

/**
 * 登录页面业务接口
 * @author JuLiy
 * @date 2022/9/27 15:42
 */
public interface LoginService {
    /**
     * 登录检查，当输入为空或错误时抛出对应异常
     * @param username 用户名
     * @param password 密码
     */
    void loginCheck(String username, String password);
}
