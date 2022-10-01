package com.juliy.ims.controller;

import com.juliy.ims.common.Context;
import com.juliy.ims.entity.User;
import com.juliy.ims.service.UserService;
import com.juliy.ims.service.impl.UserServiceImpl;
import com.leewyatt.rxcontrols.controls.RXPasswordField;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * 登录界面控制器
 * @author JuLiy
 * @date 2022/9/26 22:49
 */
public class LoginController extends RootController {

    public Label usernameLabel;
    public RXTextField usernameField;
    public RXPasswordField passwordField;
    private final UserService userService = new UserServiceImpl();

    public void initialize() {

    }

    /**
     * 登录功能
     * @throws IOException 由StageManager.createStage()引起
     */
    public void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if ("".equals(username)) {
            Context.operation.showAlert(Alert.AlertType.WARNING, "登录消息", "用户名不能为空!");
            return;
        }
        if ("".equals(password)) {
            Context.operation.showAlert(Alert.AlertType.WARNING, "登录消息", "密码不能为空!");
            return;
        }
        if (!userService.loginCheck(username, password)) {
            Context.operation.showAlert(Alert.AlertType.ERROR, "登录消息", "用户名或密码错误！请重新输入");
            return;
        }
        Context.operation.showAlert(Alert.AlertType.INFORMATION, "登录消息", "登录成功！");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Context.operation.createStage("main", "main", true);
        Context.operation.jump("login", "main");
    }

    /**
     * 输入框按回车触发登录功能
     * @throws IOException 由login()引起
     * @triggerId usernameField, passwordField
     * @triggerType ENTER pressed
     */
    public void doLogin() throws IOException {
        this.login();
    }
}
