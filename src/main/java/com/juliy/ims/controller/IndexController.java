package com.juliy.ims.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * index页面控制器
 * @author JuLiy
 * @date 2022/9/26 23:09
 */
public class IndexController {
    @FXML
    private Label usernameShow;
    @FXML
    private Label passwordShow;

    /**
     * 获取登录用户信息
     * @param username 登录用户名
     * @param password 登陆密码
     */
    public void initData(String username, String password) {
        usernameShow.setText(username);
        passwordShow.setText(password);
    }
}
