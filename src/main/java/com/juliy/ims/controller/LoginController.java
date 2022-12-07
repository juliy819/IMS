package com.juliy.ims.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.juliy.ims.service.LoginService;
import com.juliy.ims.service.impl.LoginServiceImpl;
import com.juliy.ims.utils.CaptchaUtil;
import com.juliy.ims.utils.CommonUtil;
import com.leewyatt.rxcontrols.controls.RXPasswordField;
import com.leewyatt.rxcontrols.controls.RXTextField;
import com.leewyatt.rxcontrols.controls.RXTranslationButton;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.*;

import static com.juliy.ims.common.Context.getContext;

/**
 * 登录页面控制器
 * @author JuLiy
 * @date 2022/9/26 22:49
 */
public class LoginController {

    private static final Logger log = Logger.getLogger(LoginController.class);
    /** 用户名输入框文本内容 */
    final SimpleStringProperty username = new SimpleStringProperty();
    /** 密码输入框文本内容 */
    final SimpleStringProperty password = new SimpleStringProperty();
    /** 验证码输入框文本内容 */
    final SimpleStringProperty captcha = new SimpleStringProperty();
    /** 记住密码复选框的值 */
    final SimpleBooleanProperty remember = new SimpleBooleanProperty();
    private final LoginService loginService = new LoginServiceImpl();
    /** 线程池 */
    ExecutorService threadPool;
    String captchaCode;
    double offsetX;
    double offsetY;
    @FXML
    private RXTextField tfUsername;
    @FXML
    private RXPasswordField tfPassword;
    @FXML
    private RXTextField tfCaptcha;
    @FXML
    private Region iconUsername;
    @FXML
    private Region iconPassword;
    @FXML
    private Region iconCaptcha;
    @FXML
    private JFXCheckBox cbRemember;
    @FXML
    private RXTranslationButton btnLogin;
    @FXML
    private HBox coverBtnLogin;
    @FXML
    private ImageView ivLoading;
    @FXML
    private ImageView ivCaptcha;
    @FXML
    private Text txtPrompt;


    @FXML
    void initialize() {
        bindProperties();
        loadSettings();
        initComponents();
        initThreadPool();
        generateCaptcha();
    }

    /** 将各property与对应的输入框内容绑定 */
    private void bindProperties() {
        username.bindBidirectional(tfUsername.textProperty());
        password.bindBidirectional(tfPassword.textProperty());
        captcha.bindBidirectional(tfCaptcha.textProperty());
        remember.bindBidirectional(cbRemember.selectedProperty());
    }

    /** 读取保存的设置，并判断是否自动填入用户名和密码 */
    private void loadSettings() {
        //读取配置文件
        try {
            loginService.loadSettings();
        } catch (IOException e) {
            String msg = "读取配置文件失败，请检查文件位置";
            log.error(msg);
            CommonUtil.showAlert(Alert.AlertType.ERROR, "错误", msg);
        }

        //若上次勾选了记住密码，则自动填入上次登录账号信息
        if (loginService.isRemember()) {
            remember.set(true);
            String[] userInfo = loginService.getLoginInfo();
            username.set(userInfo[0]);
            password.set(userInfo[1]);
        }
    }

    /** 设置组件监听 */
    private void initComponents() {
        //有输入框为空时，登录按钮不可点击
        ChangeListener<String> disableLogin = (ob, ov, nv) -> {
            boolean isInputEmpty = "".equals(username.get()) || "".equals(password.get()) || "".equals(captcha.get());
            btnLogin.setDisable(isInputEmpty);
        };
        username.addListener(disableLogin);
        password.addListener(disableLogin);
        captcha.addListener(disableLogin);

        class ChangeIconColor implements ChangeListener<Boolean> {
            final Region icon;

            public ChangeIconColor(String name) {
                switch (name) {
                    case "username" -> icon = iconUsername;
                    case "password" -> icon = iconPassword;
                    default -> icon = iconCaptcha;
                }
            }

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                clearErrorPrompt();
                if (newValue.equals(Boolean.TRUE)) {
                    icon.setStyle("-fx-background-color: #66cccc");
                } else {
                    icon.setStyle("-fx-background-color: gray");
                }
            }
        }
        //输入框获得或失去焦点时，清除错误提示，对应图标变色
        tfUsername.focusedProperty().addListener(new ChangeIconColor("username"));
        tfPassword.focusedProperty().addListener(new ChangeIconColor("password"));
        tfCaptcha.focusedProperty().addListener(new ChangeIconColor("captcha"));
    }

    /** 初始化线程池 */
    private void initThreadPool() {
        ThreadFactory threadFactory = Thread::new;
        threadPool = new ThreadPoolExecutor(2, 2,
                                            0L, TimeUnit.MICROSECONDS,
                                            new LinkedBlockingQueue<>(1024), threadFactory);
    }

    /**
     * 登录功能
     * @触发组件 tfUsername, tfPassword, tfCaptcha, btnLogin
     * @触发事件 输入框回车、鼠标点击按钮
     */
    @FXML
    void login() {
        //用来处理有输入框内容为空时输入回车的情况
        if (btnLogin.isDisable()) {
            return;
        }
        //验证码 不区分大小写
        if (!captcha.get().equalsIgnoreCase(captchaCode)) {
            showErrorPrompt("验证码错误");
            return;
        }
        //验证账号密码是否正确
        if (loginService.loginCheck(username.get(), password.get())) {
            showSuccessPrompt();
        } else {
            showErrorPrompt("用户名或密码错误");
            return;
        }
        //判断是否要保存账号密码
        String loginUsername = "";
        String loginPassword = "";
        if (remember.get()) {
            loginUsername = username.get();
            loginPassword = password.get();
        }
        try {
            loginService.saveLoginInfo(loginUsername, loginPassword, String.valueOf(remember.get()));
        } catch (IOException e) {
            log.error("写入info.properties文件失败，请确认文件位置是否正确");
            CommonUtil.showAlert(Alert.AlertType.ERROR, "错误", "账号信息保存失败");
        }

        //延迟2s后跳转至主页面
        threadPool.execute(new Task<>() {
            @Override
            protected Object call() throws InterruptedException {
                //延迟1s
                TimeUnit.SECONDS.sleep(2);
                return null;
            }

            @Override
            protected void updateValue(Object value) {
                super.updateValue(value);
                //跳转到登录页面
                try {
                    CommonUtil.createStage("main", "main", true);
                    CommonUtil.jump("login", "main");
                } catch (IOException e) {
                    String msg = "主页面加载失败";
                    log.error(msg);
                    CommonUtil.showAlert(Alert.AlertType.ERROR, "系统错误", msg);
                }
            }
        });
    }

    /**
     * 跳转到gitee本项目地址
     * @触发组件 linkSource
     * @触发事件 鼠标点击
     */
    @FXML
    void toGitee() {
        getContext().getHost().showDocument("https://gitee.com/juliyang/IMS");
    }

    /**
     * 清空用户名输入框内容
     * @触发组件 tfUsername.button
     * @触发事件 鼠标点击按钮
     */
    @FXML
    void clearUsername() {
        username.set("");
    }

    /**
     * 清空验证码输入框内容
     * @触发组件 tfCaptcha.button
     * @触发事件 鼠标点击按钮
     */
    @FXML
    void clearCaptcha() {
        captcha.set("");
    }

    /**
     * 窗口拖动
     * @param mouseEvent 鼠标事件
     * @触发组件 topBar
     * @触发事件 鼠标拖动
     */
    @FXML
    void dragWindow(MouseEvent mouseEvent) {
        Stage stage = getContext().getStageMap().get("login");
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
    void getOffset(MouseEvent mouseEvent) {
        offsetX = mouseEvent.getSceneX();
        offsetY = mouseEvent.getSceneY();
    }

    /**
     * 关闭窗口
     * @触发组件 btnExit
     * @触发事件 鼠标点击
     */
    @FXML
    void exitWindow() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * 生成验证码
     * @触发组件 ivCaptcha
     * @触发事件 鼠标点击
     */
    @FXML
    void generateCaptcha() {
        captchaCode = CaptchaUtil.getRandom(4);
        log.info("验证码:" + captchaCode);
        try {
            FileOutputStream out = new FileOutputStream("src/main/resources/images/captcha.png");
            CaptchaUtil.draw(out, captchaCode);
        } catch (IOException e) {
            log.error("验证码生成失败");
            CommonUtil.showAlert(Alert.AlertType.ERROR, "错误", "验证码生成失败");
        }
        ivCaptcha.setImage(new Image("file:src/main/resources/images/captcha.png"));
    }

    /**
     * 显示错误提示
     * @param msg 提示消息
     */
    private void showErrorPrompt(String msg) {
        txtPrompt.setFill(Paint.valueOf("red"));
        txtPrompt.setText(msg);
        txtPrompt.setVisible(true);
    }

    /** 显示登陆成功提示 */
    private void showSuccessPrompt() {
        txtPrompt.setFill(Paint.valueOf("green"));
        txtPrompt.setText("登陆成功，请稍后...");
        txtPrompt.setVisible(true);
        coverBtnLogin.setVisible(true);
        //设置loading旋转动画
        RotateTransition rotation = new RotateTransition(Duration.seconds(1), ivLoading);
        rotation.setFromAngle(0);
        rotation.setToAngle(360);
        rotation.setByAngle(90);
        rotation.setCycleCount(Animation.INDEFINITE);
        rotation.play();
    }

    /** 清除提示 */
    private void clearErrorPrompt() {
        txtPrompt.setVisible(false);
        coverBtnLogin.setVisible(false);
    }
}