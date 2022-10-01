package com.juliy.ims.controller;

import com.jfoenix.controls.JFXDrawer;
import com.juliy.ims.common.Context;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 主界面控制器
 * @author JuLiy
 * @date 2022/9/26 23:09
 */
public class MainController extends RootController {

    @FXML
    public JFXDrawer drawer;
    @FXML
    public AnchorPane baseAnchorPane;
    public TreeView<String> treeView;

    public String[] functionList = {
            "报表统计", "货品列表", "当前库存查询", "出入库流水账", "收发存汇总", "库存预警分析",
            "基础设置", "新增仓库", "新增货品类别", "新增货品", "新增供应商", "新增客户",
            "入库管理", "采购入库单", "退料入库单", "生产入库单", "销售退货入库单", "其他入库单",
            "出库管理", "采购退货出库单", "领料出库单", "销售出库单", "其他出库单",
            "库存管理", "库存调拨单", "库存盘点单", "库存调拨记录", "库存盘点记录"};

    @FXML
    private void initialize() throws IOException {
        initTreeView();
        Context.operation.loadPage("product list", baseAnchorPane);
    }

    /** 初始化功能菜单数据 */
    private void initTreeView() {
        treeView = new TreeView<>();

        TreeItem<String> root = new TreeItem<>();
        List<TreeItem<String>> rootItems = new ArrayList<>();
        TreeItem<String> rootItem1 = new TreeItem<>("报表统计");
        TreeItem<String> rootItem2 = new TreeItem<>("基础设置");
        TreeItem<String> rootItem3 = new TreeItem<>("入库管理");
        TreeItem<String> rootItem4 = new TreeItem<>("出库管理");
        TreeItem<String> rootItem5 = new TreeItem<>("库存管理");


        rootItem1.setGraphic(new ImageView(new Image("images/文件夹.png")));
        rootItem2.setGraphic(new ImageView(new Image("images/文件夹.png")));
        rootItem3.setGraphic(new ImageView(new Image("images/文件夹.png")));
        rootItem4.setGraphic(new ImageView(new Image("images/文件夹.png")));
        rootItem5.setGraphic(new ImageView(new Image("images/文件夹.png")));

        rootItems.add(rootItem1);
        rootItems.add(rootItem2);
        rootItems.add(rootItem3);
        rootItems.add(rootItem4);
        rootItems.add(rootItem5);

        root.getChildren().addAll(rootItems);

        treeView.setRoot(root);
        treeView.setShowRoot(false);
        treeView.setStyle("-fx-background-insets:-1.4 0 1 2; -fx-focus-color:transparent");

        drawer.setSidePane(treeView);
        drawer.open();
    }

    @FXML
    private void onDrawerClosed() {
        //菜单关闭后设置宽度为0，以便右边的主菜单扩展为窗口窗口大小
        drawer.setPrefWidth(0);
    }

    /**
     * 控制功能菜单的收起和展开
     * @param drawerCtrlImageView 用的ImageView代替按钮功能，作为形参传入以实现图片的变化
     */
    public void drawerControl(ImageView drawerCtrlImageView) {
        if (drawer.isClosed() || drawer.isClosing()) {
            drawer.setPrefWidth(200);
            drawer.open();
            drawerCtrlImageView.setImage(new Image("images/left arrow.png"));
        } else {
            drawer.close();
            drawerCtrlImageView.setImage(new Image("images/right arrow.png"));
        }
    }

    @FXML
    public void showPage1() throws IOException {
        Context.operation.loadPage("product list", baseAnchorPane);
    }

    @FXML
    public void showPage2() throws IOException {
        Context.operation.loadPage("inventory inquiry", baseAnchorPane);
    }
}
