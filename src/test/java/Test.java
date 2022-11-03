import com.juliy.ims.common.Context;
import com.juliy.ims.utils.DocCodeUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author JuLiy
 * @date 2022/9/29 17:21
 */
public class Test extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Context.OPERATION.createStage("test", "test", true).show();
        System.out.println(DocCodeUtil.getReturnCode(1L));
        System.out.println(DocCodeUtil.getAgainCode(1L));
        System.out.println(DocCodeUtil.getOrderCode(1L));
        System.out.println(DocCodeUtil.getRefundCode(1L));
    }
}
