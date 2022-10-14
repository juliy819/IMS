package com.juliy.ims.controller;

import com.juliy.ims.common.Context;
import com.juliy.ims.common.CustomException;
import com.juliy.ims.service.LoginService;
import com.juliy.ims.service.impl.LoginServiceImpl;
import com.leewyatt.rxcontrols.controls.RXPasswordField;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * 登录页面控制器
 * @author JuLiy
 * @date 2022/9/26 22:49
 */
public class LoginController extends RootController {

    public RXTextField txtUsername;
    public RXPasswordField txtPassword;
    private final LoginService loginService = new LoginServiceImpl();

    SimpleStringProperty username = new SimpleStringProperty();
    SimpleStringProperty password = new SimpleStringProperty();

    public void initialize() {
        username.bindBidirectional(txtUsername.textProperty());
        password.bindBidirectional(txtPassword.textProperty());

    }

    /**
     * 登录功能
     * @触发组件 txtUsername, txtPassword
     * @触发事件 回车
     */
    @FXML
    public void login() {
        try {
            loginService.loginCheck(username.get(), password.get());
            Context.operation.showAlert(Alert.AlertType.INFORMATION, "登录消息", "登录成功！");
        } catch (CustomException e) {
            Context.operation.showAlert(Alert.AlertType.WARNING, "登录消息", e.getMessage());
            return;
        }

        try {
            Context.operation.createStage("main", "main", true);
            Context.operation.jump("login", "main");
        } catch (Exception e) {
            e.printStackTrace();
            Context.operation.showAlert(Alert.AlertType.ERROR, "系统错误", "页面加载失败");
        }
    }

    /**
     * 清空用户名输入框内容
     * @触发组件 txtUsername.button
     * @触发事件 点击
     */
    @FXML
    public void clearUsernameInput() {
        username.set("");
    }
}
