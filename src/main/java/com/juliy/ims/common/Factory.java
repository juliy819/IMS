package com.juliy.ims.common;

import com.juliy.ims.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * 工厂类
 * @author JuLiy
 * @date 2022/9/29 12:07
 */
public class Factory {
    /**
     * 创建新窗口
     * @param title    窗口标题
     * @param width    窗口宽度
     * @param height   窗口高度
     * @param fxmlName 要加载的fxml文件名(无需后缀)
     * @return 创建好的窗口对象
     * @throws IOException 由FXMLLoader.load()抛出
     */
    public Stage createStage(String title, int width, int height, String fxmlName) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource(fxmlName + ".fxml")));
        Stage stage = new Stage();
        stage.setScene(new Scene(root, width, height));
        stage.setTitle(title);
        stage.setResizable(false);
        //设置stage关闭时的操作
        stage.setOnCloseRequest(windowEvent -> {
            //避免stage关闭后应用程序仍在后台运行
            System.exit(0);
        });
        Context.stageManager.addStage(fxmlName, stage);
        return stage;
    }
}
