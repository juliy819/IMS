package com.juliy.ims.controller;

import com.jfoenix.controls.JFXDrawer;
import com.juliy.ims.common.Context;
import javafx.fxml.FXML;

/**
 * @author JuLiy
 * @date 2022/9/29 16:20
 */
public class ProductListController extends RootController {

    @FXML
    public void drawerControl() {
        MainController mainController = (MainController) Context.controllerMap.get(MainController.class.getSimpleName());
        JFXDrawer drawer = mainController.drawer;
        if (drawer.isClosed()) {
            drawer.setPrefWidth(200);
            drawer.open();
        } else if (drawer.isClosing()) {
            drawer.setPrefWidth(200);
            drawer.open();
        } else if (drawer.isOpened()) {
            drawer.close();
        } else if (drawer.isOpening()) {
            drawer.close();
        }
    }
}