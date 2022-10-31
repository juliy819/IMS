package test;

import com.juliy.ims.common.Context;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author JuLiy
 * @date 2022/9/29 17:21
 */
public class Test extends Application {
    private final int WEAK_INDEX = -1;
    private final int STRONG_INDEX = -2;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage = Context.operation.createStage("test", "test", true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
