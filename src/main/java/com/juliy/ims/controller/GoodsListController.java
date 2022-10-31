package com.juliy.ims.controller;

import com.juliy.ims.common.Context;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 货品列表界面控制器
 * @author JuLiy
 * @date 2022/9/29 16:20
 */
public class GoodsListController extends RootController {

    @FXML
    public ImageView drawerCtrlImageView;
    @FXML
    public ImageView lastPageImageView;
    @FXML
    public ImageView nextPageImageView;
    @FXML
    public TextField pageJumpField;
    @FXML
    public Label totalPageLabel;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public Pane testPane;

    private MainController mainController;

    Pattern pattern = Pattern.compile("[0-9]*");

    @FXML
    private void initialize() {
        mainController = (MainController) Context.controllerMap.get(MainController.class.getSimpleName());
        initImage();
        pageJumpFieldInputCheck();
    }

    /** 给组件设置图片 */
    private void initImage() {
        drawerCtrlImageView.setImage(new Image("images/left arrow.png"));
        lastPageImageView.setImage(new Image("images/left arrow.png"));
        nextPageImageView.setImage(new Image("images/right arrow.png"));
    }

    private void pageJumpFieldInputCheck() {
        totalPageLabel.setText("/20");
        pageJumpField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            //使用backspace将输入框清空时，newValue值为空
            // 此时不进行其余操作，不然parseInt(String)会报错，它不能将空String转化为int
            if ("".equals(newValue)) {
                return;
            }
            //若输入的不是数字，将值置为oldValue，即不更新输入框内容
            Matcher isNumber = pattern.matcher(newValue);
            if (!isNumber.matches()) {
                pageJumpField.setText(oldValue);
                return;
            }

            int targetPage = Integer.parseInt(newValue);
            int totalPage = Integer.parseInt(totalPageLabel.getText().substring(1));
            if (targetPage < 1 || targetPage > totalPage) {
                pageJumpField.setText(oldValue);
            }
        });
    }

    /**
     * 控制功能菜单的收起和展开
     * @触发组件 drawerCtrlImageView
     * @触发事件 点击
     */
    @FXML
    public void drawerControl() {
        mainController.drawerControl(drawerCtrlImageView);
    }

    /**
     * 表格跳转至指定页
     * @触发组件 pageJumpField
     * @触发事件 回车
     */
    @FXML
    public void jumpPage() {
        String page = pageJumpField.getText();
        if ("".equals(page)) {
            Context.operation.showAlert(Alert.AlertType.ERROR, "错误", "页数不能为空！");
            return;
        }
        int targetPage = Integer.parseInt(page);
        System.out.println("表格跳转至:" + targetPage + "页");
    }

    /**
     * 表格跳转至上一页
     * @触发组件 lastPageImageView
     * @触发事件 点击
     */
    @FXML
    public void lastPage() {
        System.out.println("lastPage");
    }

    /**
     * 表格跳转至下一页
     * @触发组件 nextPageImageView
     * @触发事件 点击
     */
    @FXML
    public void nextPage() {
        System.out.println("nextPage");
    }
}