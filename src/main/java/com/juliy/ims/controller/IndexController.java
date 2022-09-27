package com.juliy.ims.controller;

import com.juliy.ims.entity.User;
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
     * @param user 登录用户信息
     */
    public void initData(User user) {
        usernameShow.setText(user.getUsername());
        passwordShow.setText(user.getPassword());
    }
}
