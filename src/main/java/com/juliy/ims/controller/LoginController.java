package com.juliy.ims.controller;

import com.juliy.ims.common.StageManager;
import com.juliy.ims.service.UserService;
import com.juliy.ims.service.impl.UserServiceImpl;
import com.leewyatt.rxcontrols.controls.RXPasswordField;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 登录页面控制器
 * @author JuLiy
 * @date 2022/9/26 22:49
 */
public class LoginController {

    @FXML
    private RXTextField usernameField;
    @FXML
    private RXPasswordField passwordField;
    private final UserService userService = new UserServiceImpl();

    /**
     * 登录功能
     * @throws IOException 由StageManager.createStage()引起
     */
    public void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if ("".equals(username)) {
            Alert failAlert = new Alert(Alert.AlertType.ERROR);
            failAlert.setTitle("登陆消息");
            failAlert.setHeaderText(null);
            failAlert.setContentText("用户名不能为空!");
            failAlert.showAndWait();
            return;
        }
        if ("".equals(password)) {
            Alert failAlert = new Alert(Alert.AlertType.ERROR);
            failAlert.setTitle("登陆消息");
            failAlert.setHeaderText(null);
            failAlert.setContentText("密码不能为空!");
            failAlert.showAndWait();
            return;
        }
        if (!userService.loginCheck(username, password)) {
            Alert failAlert = new Alert(Alert.AlertType.ERROR);
            failAlert.setTitle("登陆消息");
            failAlert.setHeaderText(null);
            failAlert.setContentText("用户名或密码错误！请重新输入");
            failAlert.showAndWait();
            return;
        }
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("登陆消息");
        successAlert.setHeaderText(null);
        successAlert.setContentText("登录成功！");
        successAlert.showAndWait();

        Stage newStage = StageManager.createStage("index", "index", true);
        Stage thisStage = StageManager.STAGE.get("login");
        IndexController indexController = (IndexController) StageManager.CONTROLLER.get("index");
        indexController.initData(username, password);
        thisStage.close();
        newStage.show();
    }

    /**
     * 监听输入框的回车按钮，触发登录功能
     * @param keyEvent 按键事件
     * @throws IOException 由login()引起
     */
    public void doLogin(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            this.login();
        }
    }
}
