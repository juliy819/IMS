package com.juliy.ims.controller;

import javafx.scene.control.Label;

/**
 * @author JuLiy
 * @date 2022/9/26 23:09
 */
public class IndexController {
    public Label usernameShow;
    public Label passwordShow;

    public void initData(String username, String password) {
        usernameShow.setText(username);
        passwordShow.setText(password);
    }
}
