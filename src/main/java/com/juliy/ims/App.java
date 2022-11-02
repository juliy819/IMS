package com.juliy.ims;

import com.juliy.ims.common.Context;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author JuLiy
 * @date 2022/9/27 10:10
 */
public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Context.setHost(getHostServices());
        Context.OPERATION.createStage("login", "", true).show();
    }
}
