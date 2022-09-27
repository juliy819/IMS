package com.juliy.ims.controller;

import com.juliy.ims.common.StageManager;
import com.juliy.ims.entity.User;
import com.leewyatt.rxcontrols.controls.RXPasswordField;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author JuLiy
 * @date 2022/9/26 22:49
 */
public class LoginController {

    public RXTextField usernameField;
    public RXPasswordField passwordField;
    public AnchorPane login;

    public void login() throws IOException {
        User user = new User("admin", "123");
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
        if (!username.equals(user.getUsername()) || !password.equals(user.getPassword())) {
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

    public void doLogin(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            this.login();
        }
    }
}
