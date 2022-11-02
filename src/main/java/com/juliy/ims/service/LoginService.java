package com.juliy.ims.service;

import java.io.IOException;

/**
 * 登录页面业务接口
 * @author JuLiy
 * @date 2022/9/27 15:42
 */
public interface LoginService {
    /**
     * 检查账号是否正确
     * @param username 用户名
     * @param password 密码
     * @return true-账号正确; false-账号错误
     */
    boolean loginCheck(String username, String password);

    /**
     * 加载配置文件
     * @throws IOException 若配置文件位置有误
     */
    void loadProperties() throws IOException;

    /**
     * 判断上次是否勾选了记住密码
     * @return true-勾选;false-未勾选
     */
    boolean isRemember();

    /**
     * 获取保存的账号信息
     * @return 索引0为用户名，索引1为密码
     */
    String[] getLoginInfo();

    /**
     * 保存登录账号信息
     * @param username 用户名
     * @param password 密码
     * @param remember 是否记住密码
     * @throws IOException 若配置文件位置有误
     */
    void saveLoginInfo(String username, String password, String remember) throws IOException;
}
