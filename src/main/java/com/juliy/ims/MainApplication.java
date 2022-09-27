package com.juliy.ims;

import com.juliy.ims.common.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author JuLiy
 * @date 2022/9/27 10:10
 */
public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        StageManager.initPrimaryStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
