package test;

import com.juliy.ims.MainApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * @author JuLiy
 * @date 2022/9/29 17:21
 */
public class DrawerTest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("product list.fxml")));
        stage.setScene(new Scene(root));
        stage.setTitle("drawer test");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
