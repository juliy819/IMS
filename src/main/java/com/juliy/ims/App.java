package com.juliy.ims;

import com.juliy.ims.utils.CommonUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import static com.juliy.ims.common.Context.getContext;

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
        getContext().setHost(getHostServices());
        Stage stage = CommonUtil.createStage("login", "login", true);
        stage.show();
    }
}
