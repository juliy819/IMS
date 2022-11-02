package com.juliy.ims.common;

import com.juliy.ims.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * 常用操作类
 * @author JuLiy
 * @date 2022/9/30 16:50
 */
public class Operation {
    /**
     * 创建新窗口
     * @param fxmlName     要加载的fxml文件名(无需后缀)
     * @param title        窗口标题
     * @param isResizeable 窗口是否可缩放
     * @return 创建好的窗口对象
     * @throws IOException 由FXMLLoader.load()抛出
     */
    public Stage createStage(String fxmlName, String title, boolean isResizeable) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("view/" + fxmlName + ".fxml")));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image("images/logo.png"));
        stage.setResizable(isResizeable);
        Context.getStageMap().put(fxmlName, stage);
        return stage;
    }

    /**
     * 加载页面
     * @param fxmlName 要加载的fxml文件名(需设置为fx:root)
     * @param basePane 加载位置
     * @throws IOException 由FXMLLoader.load()抛出
     */
    public void loadPage(String fxmlName, Pane basePane) throws IOException {
        basePane.getChildren().clear();
        FXMLLoader innerLoader = new FXMLLoader(App.class.getResource("view/" + fxmlName + ".fxml"));
        innerLoader.setRoot(basePane);
        innerLoader.load();
    }

    /**
     * 页面跳转
     * @param currentStageName 当前窗口名
     * @param targetStageName  目标窗口名
     */
    public void jump(String currentStageName, String targetStageName) {
        Context.getStageMap().get(currentStageName).close();
        Context.getStageMap().get(targetStageName).show();
    }

    /**
     * 弹窗
     * @param alertType 弹窗类型
     * @param title     弹窗标题
     * @param text      弹窗内容
     */
    public void showAlert(Alert.AlertType alertType, String title, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * 创建弹窗对象，用于实现输入弹窗、确认弹窗等
     * @param alertType 弹窗类型
     * @param title     弹窗标题
     * @param text      弹窗内容
     * @return 未显示的弹窗对象
     */
    public Alert createAlert(Alert.AlertType alertType, String title, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        return alert;
    }
}
