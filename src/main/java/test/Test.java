package test;

import com.juliy.ims.utils.DocCodeUtil;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author JuLiy
 * @date 2022/9/29 17:21
 */
public class Test extends Application {

    @Override
    public void start(Stage stage) {
        System.out.println(DocCodeUtil.getOrderCode(1L));
    }

    public static void main(String[] args) {
        launch();
    }
}
