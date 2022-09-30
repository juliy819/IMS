package com.juliy.ims.controller;

import com.juliy.ims.common.Context;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 货品列表界面控制器
 * @author JuLiy
 * @date 2022/9/29 16:20
 */
public class ProductListController extends RootController {

    @FXML
    public ImageView drawerCtrlImageView;
    @FXML
    public ScrollPane scrollPane;

    private MainController mainController;

    @FXML
    private void initialize() {
        drawerCtrlImageView.setImage(new Image("images/left arrow.png"));
        mainController = (MainController) Context.controllerMap.get(MainController.class.getSimpleName());
        System.out.println(MainController.class.getSimpleName());
    }

    /** 控制功能菜单的收起和展开 */
    @FXML
    public void drawerControl() {
        mainController.drawerControl(drawerCtrlImageView);
    }
}