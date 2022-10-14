package com.juliy.ims;

import com.juliy.ims.common.Context;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author JuLiy
 * @date 2022/9/27 10:10
 */
public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage = Context.operation.createStage("login", "", true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
