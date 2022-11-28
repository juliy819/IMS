package com.juliy.ims.utils;

import com.juliy.ims.App;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

import static com.juliy.ims.common.Context.getContext;

/**
 * 常用操作工具类
 * @author JuLiy
 * @date 2022/11/4 8:56
 */
public class CommonUtil {
    private CommonUtil() {}

    /**
     * 创建新窗口
     * @param fxmlName     要加载的fxml文件名(无需后缀)
     * @param title        窗口标题
     * @param isResizeable 窗口是否可缩放
     * @return 创建好的窗口对象
     * @throws IOException 由FXMLLoader.load()抛出
     */
    public static Stage createStage(String fxmlName, String title, boolean isResizeable) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("view/" + fxmlName + ".fxml")));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image("images/logo.png"));
        stage.setResizable(isResizeable);
        getContext().getStageMap().put(fxmlName, stage);
        stage.setOnCloseRequest(event -> getContext().getStageMap().remove(fxmlName));
        return stage;
    }

    /**
     * 加载页面
     * @param fxmlName 要加载的fxml文件名(需设置为fx:root)
     * @param basePane 加载位置
     * @throws IOException 由FXMLLoader.load()抛出
     */
    public static void loadPage(String fxmlName, Pane basePane) throws IOException {
        basePane.getChildren().clear();
        FXMLLoader innerLoader = new FXMLLoader(App.class.getResource("view/" + fxmlName + ".fxml"));
        innerLoader.setRoot(basePane);
        innerLoader.load();
    }

    /**
     * 页面跳转
     * @param currentStageName 当前窗口名
     * @param targetStageName  目标窗口名
     */
    public static void jump(String currentStageName, String targetStageName) {
        getContext().getStageMap().get(currentStageName).close();
        getContext().getStageMap().get(targetStageName).show();
    }

    /**
     * 弹窗
     * @param alertType 弹窗类型
     * @param title     弹窗标题
     * @param text      弹窗内容
     */
    public static void showAlert(Alert.AlertType alertType, String title, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * sql语句拼接<br>
     * 拼接后格式为：(查询的内容)+空格+and+空格
     * @param str  要拆分的关键词字符串，各关键词间以","分隔
     * @param head 语句头;如goods_type_name
     * @param sql  要拼接的sql语句
     */
    public static void spliceSql(String str, String head, StringBuilder sql) {
        sql.append("(");
        String[] arr = str.split(",");
        for (String s : arr) {
            sql.append(head).append(" = ").append("'").append(s).append("'").append(" or ");
        }
        sql.delete(sql.lastIndexOf("or") - 1, sql.length()).append(") and ");
    }

    /**
     * 初始化添加页面中的RXTextField<br>
     * 点击按钮时清空输入框<br>
     * 失去焦点时若文本为空则提示<br>
     * 获取焦点时清除提示
     * @param tf  输入框
     * @param txt 对应的提示文本
     */
    public static void initAddTextField(RXTextField tf, Text txt) {
        //点击按钮清空输入框
        tf.setOnClickButton(event -> tf.clear());
        //失去焦点时，若文本为空则提示;获取焦点时，清除提示
        tf.focusedProperty().addListener((ob, ov, nv) -> {
            if (nv.equals(Boolean.FALSE) && ("".equals(tf.getText()))) {
                txt.setText("内容不能为空！");
                txt.setVisible(true);
            } else {
                txt.setVisible(false);
            }
        });
    }

    /**
     * 判断输入内容是否为空
     * @param str      输入内容
     * @param txtError 对应的错误提示文本
     * @return 为空返回true，不为空返回false
     */
    public static boolean isInputEmpty(String str, Text txtError) {
        if ("".equals(str)) {
            txtError.setText("不能为空！");
            txtError.setVisible(true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断输入内容长度是否超出限制
     * @param str      输入内容
     * @param length   长度
     * @param txtError 对应的错误提示文本
     * @return 超出限制返回true，不超出返回false
     */
    public static boolean isLengthExceed(String str, int length, Text txtError) {
        if (str.length() > length) {
            txtError.setText("长度不能超过" + length + "个字符！");
            txtError.setVisible(true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断输入内容格式是否非法
     * @param str      输入内容
     * @param regex    正则表达式
     * @param txtError 对应的错误提示文本
     * @return 非法返回true，合法返回false
     */
    public static boolean isFormatIllegal(String str, String regex, Text txtError) {
        if (!str.matches(regex)) {
            txtError.setText("格式有误！");
            txtError.setVisible(true);
            return true;
        } else {
            return false;
        }
    }
}
