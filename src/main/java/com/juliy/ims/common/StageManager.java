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
 * @author JuLiy
 * @date 2022/9/27 10:03
 */
public class StageManager {
    public static Map<String, Stage> STAGE = new HashMap<>();
    public static Map<String, Object> CONTROLLER = new HashMap<>();

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
