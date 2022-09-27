package com.juliy.ims.common;

import com.juliy.ims.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * stage管理类
 * @author JuLiy
 * @date 2022/9/27 10:03
 */
public class StageManager {
    /** 存储已创建的stage，key为对应的页面名称 */
    public static Map<String, Stage> STAGE = new HashMap<>();
    /** 存储已创建的controller，key为对应的页面名称 */
    public static Map<String, Object> CONTROLLER = new HashMap<>();

    /**
     * 创建新的stage
     * @param fxmlName    fxml文件名,不需要后缀
     * @param title       页面的标题
     * @param isResizable 页面是否可以缩放
     * @return 创建好的stage对象，未显示
     * @throws IOException 由FXMLLoader.load()引起
     */
    public static Stage createStage(String fxmlName, String title, boolean isResizable) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlName + ".fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.setResizable(isResizable);
        STAGE.put(fxmlName, stage);
        CONTROLLER.put(fxmlName, fxmlLoader.getController());
        return stage;
    }

    /**
     * 设置primaryStage
     * @param primaryStage Application.start()中提供的初始stage
     * @throws IOException 由FXMLLoader.load()引起
     */
    public static void initPrimaryStage(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("login");
        primaryStage.setResizable(false);
        STAGE.put("login", primaryStage);
        CONTROLLER.put("login", fxmlLoader.getController());
    }
}
