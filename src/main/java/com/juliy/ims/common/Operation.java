package com.juliy.ims.common;

import com.juliy.ims.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource(fxmlName + ".fxml")));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.setResizable(isResizeable);
        Context.stageMap.put(fxmlName, stage);
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
        FXMLLoader innerLoader = new FXMLLoader(MainApplication.class.getResource(fxmlName + ".fxml"));
        innerLoader.setRoot(basePane);
        innerLoader.load();
    }

    /**
     * 页面跳转
     * @param currentStageName 当前窗口名
     * @param targetStageName  目标窗口名
     */
    public void jump(String currentStageName, String targetStageName) {
        Context.stageMap.get(currentStageName).close();
        Context.stageMap.get(targetStageName).show();
    }
}
