package com.juliy.ims.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.juliy.ims.common.Context;
import com.juliy.ims.common.CustomException;
import com.juliy.ims.service.LoginService;
import com.juliy.ims.service.impl.LoginServiceImpl;
import com.leewyatt.rxcontrols.controls.RXPasswordField;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 登录页面控制器
 * @author JuLiy
 * @date 2022/9/26 22:49
 */
public class LoginController extends RootController {

    @FXML
    public RXTextField tfUsername;
    @FXML
    public RXPasswordField tfPassword;
    @FXML
    public RXTextField tfCaptcha;
    @FXML
    public JFXCheckBox cbRemember;

    private double offsetX, offsetY;

    private final LoginService loginService = new LoginServiceImpl();

    private final Properties infoProps = new Properties();
    private static final Logger log = Logger.getLogger(LoginController.class);

    SimpleStringProperty username = new SimpleStringProperty();
    SimpleStringProperty password = new SimpleStringProperty();
    SimpleStringProperty captcha = new SimpleStringProperty();
    SimpleBooleanProperty remember = new SimpleBooleanProperty();

    public void initialize() {
        //将各property与对应的输入框内容绑定
        username.bindBidirectional(tfUsername.textProperty());
        password.bindBidirectional(tfPassword.textProperty());
        captcha.bindBidirectional(tfCaptcha.textProperty());
        remember.bindBidirectional(cbRemember.selectedProperty());

        //读取配置文件
        try {
            InputStream in = new FileInputStream("src/main/resources/info.properties");
            infoProps.load(in);
        } catch (IOException e) {
            String msg = "读取配置文件失败，请检查文件位置";
            log.error(msg);
            Context.operation.createAlert(Alert.AlertType.ERROR, "错误", msg);
        }

        //若上次勾选了记住密码，则自动填入上次登录账号信息
        remember.set(Boolean.parseBoolean(infoProps.getProperty("remember")));
        if (remember.get()) {
            username.set(infoProps.getProperty("loginUsername"));
            password.set(infoProps.getProperty("loginPassword"));
        }
    }

    /**
     * 登录功能
     * @触发组件 tfUsername, tfPassword, btnLogin
     * @触发事件 输入框回车、点击按钮
     */
    @FXML
    public void login() {
        //验证账号密码，通过抛出异常来判断是否为空、是否错误
        try {
            loginService.loginCheck(username.get(), password.get());
        } catch (CustomException e) {
            log.warn(e.getMessage());
            Context.operation.showAlert(Alert.AlertType.WARNING, "登录消息", e.getMessage());
            return;
        }

        Context.operation.showAlert(Alert.AlertType.INFORMATION, "登录消息", "登录成功！");
        //判断是否要记录账号密码
        if (remember.get()) {
            infoProps.setProperty("loginUsername", username.get());
            infoProps.setProperty("loginPassword", password.get());
        }
        infoProps.setProperty("remember", String.valueOf(remember.get()));
        //保存配置文件
        try {
            infoProps.store(new FileWriter("src/main/resources/info.properties"), null);
        } catch (IOException e) {
            log.error("写入info.properties文件失败，请确认文件位置是否正确");
        }

        //跳转到登录页面
        try {
            Context.operation.createStage("main", "main", true);
            Context.operation.jump("login", "main");
        } catch (Exception e) {
            log.fatal(e);
            Context.operation.showAlert(Alert.AlertType.ERROR, "系统错误", "页面加载失败");
        }
    }

    /**
     * 清空用户名输入框内容
     * @触发组件 tfUsername.button
     * @触发事件 点击
     */
    @FXML
    public void clearUsername() {
        username.set("");
    }

    /**
     * 清空验证码输入框内容
     * @触发组件 tfCaptcha.button
     * @触发事件 点击
     */
    @FXML
    public void clearCaptcha() {
        captcha.set("");
    }

    /**
     * 窗口拖动
     * @param mouseEvent 鼠标事件
     * @触发组件 topBar
     * @触发事件 鼠标拖动
     */
    @FXML
    public void dragWindow(MouseEvent mouseEvent) {
        Stage stage = Context.stageMap.get("login");
        stage.setX(mouseEvent.getScreenX() - offsetX);
        stage.setY(mouseEvent.getScreenY() - offsetY);
    }

    /**
     * 获取鼠标在窗口内的坐标
     * @param mouseEvent 鼠标事件
     * @触发组件 topBar
     * @触发事件 鼠标按下
     */
    @FXML
    public void getOffset(MouseEvent mouseEvent) {
        offsetX = mouseEvent.getSceneX();
        offsetY = mouseEvent.getSceneY();
    }

    /**
     * 关闭窗口
     * @触发组件 btnExit
     * @触发事件 鼠标点击
     */
    @FXML
    public void exitWindow() {
        Platform.exit();
        System.exit(0);
    }
}