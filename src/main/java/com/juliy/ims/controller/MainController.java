package com.juliy.ims.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * index页面控制器
 * @author JuLiy
 * @date 2022/9/26 23:09
 */
public class MainController extends RootController {
    public AnchorPane functionAnchorPane;
    public SplitPane splitPane;
    public TreeView<String> functionTreeView;

    public String[] functionList = {
            "报表统计", "货品列表", "当前库存查询", "出入库流水账", "收发存汇总", "库存预警分析",
            "基础设置", "新增仓库", "新增货品类别", "新增货品", "新增供应商", "新增客户",
            "入库管理", "采购入库单", "退料入库单", "生产入库单", "销售退货入库单", "其他入库单",
            "出库管理", "采购退货出库单", "领料出库单", "销售出库单", "其他出库单",
            "库存管理", "库存调拨单", "库存盘点单", "库存调拨记录", "库存盘点记录"};

    @FXML
    private void initialize() {
        initTreeView();
    }

    private void initTreeView() {
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

        functionTreeView.setRoot(root);
        functionTreeView.setShowRoot(false);
    }

    public void stowLeftPane(ActionEvent actionEvent) {
        double[] dividerPositions = splitPane.getDividerPositions();
        System.out.println(Arrays.toString(dividerPositions));
        if (dividerPositions[0] > 0.01) {
            splitPane.setDividerPosition(0, 0);
        } else {
            splitPane.setDividerPosition(0, 0.3);
        }
    }
}
