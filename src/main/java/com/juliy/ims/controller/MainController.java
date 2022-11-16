package com.juliy.ims.controller;

import com.juliy.ims.utils.CommonUtil;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.juliy.ims.common.Context.getContext;

/**
 * 主界面控制器
 * @author JuLiy
 * @date 2022/9/26 23:09
 */
public class MainController extends RootController {

    private static final Logger log = Logger.getLogger(MainController.class);
    double offsetX;
    double offsetY;
    TranslateTransition hideSidePaneTransition;
    TranslateTransition showSidePaneTransition;
    @FXML
    private VBox paneSide;
    @FXML
    private AnchorPane paneMain;
    @FXML
    private TreeView<String> treeFunctions;
    @FXML
    private StackPane paneCarousel;
    @FXML
    private ToggleButton btnSideCtrl;
    @FXML
    private Text txtPageName;

    @FXML
    private void initialize() {
        initTreeView();
        initPage();
        initAnimation();

        btnSideCtrl.setTooltip(new Tooltip("收起"));
    }

    /** 初始化动画 */
    private void initAnimation() {
        hideSidePaneTransition = new TranslateTransition(Duration.millis(300), paneSide);
        hideSidePaneTransition.setByX(0);
        hideSidePaneTransition.setToX(-210);
        hideSidePaneTransition.setOnFinished(event -> {
            paneSide.setVisible(false);
            AnchorPane.setLeftAnchor(paneMain, 0.0);
        });

        showSidePaneTransition = new TranslateTransition(Duration.millis(300), paneSide);
        showSidePaneTransition.setByX(-210);
        showSidePaneTransition.setToX(0);
    }

    /** 初始化左侧功能菜单数据 */
    private void initTreeView() {
        TreeItem<String> root = new TreeItem<>();
        TreeItem<String> reportStat = new TreeItem<>("报表统计");
        TreeItem<String> basicSettings = new TreeItem<>("基础设置");
        TreeItem<String> entryMgt = new TreeItem<>("入库管理");
        TreeItem<String> outMgt = new TreeItem<>("出库管理");
        TreeItem<String> invMgt = new TreeItem<>("库存管理");

        Image rootImg = new Image("images/功能.png");
        Image reportStatImg = new Image("images/数据.png");
        Image basicSettingsImg = new Image("images/新增.png");
        Image invMgtImg = new Image("images/表单.png");
        setItems(root, rootImg, reportStat, basicSettings, entryMgt, outMgt, invMgt);
        setItems(reportStat, reportStatImg, "货品列表", "当前库存查询", "出入库流水账", "收发存汇总", "库存预警分析");
        setItems(basicSettings, basicSettingsImg, "新增仓库", "新增货品类别", "新增货品", "新增供应商", "新增客户");
        setItems(entryMgt, invMgtImg, "采购入库单", "退料入库单", "生产入库单", "销售退货入库单", "其他入库单");
        setItems(outMgt, invMgtImg, "采购退货出库单", "领料出库单", "销售出库单", "其他出库单");
        setItems(invMgt, invMgtImg, "库存调拨单", "库存盘点单", "库存调拨记录", "库存盘点记录");

        treeFunctions.setRoot(root);
        reportStat.setExpanded(true);
    }

    /** 初始化页面 */
    private void initPage() {
        AnchorPane goodsList = new AnchorPane();
        AnchorPane invQuery = new AnchorPane();
        AnchorPane blankPage = new AnchorPane();
        //提前加载好页面，切换时再加载会有延迟
        try {
            CommonUtil.loadPage("goodsList", goodsList);
            CommonUtil.loadPage("invQuery", invQuery);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            CommonUtil.showAlert(Alert.AlertType.ERROR, "错误", "页面加载出现问题");
        }
        paneCarousel.getChildren().add(goodsList);

        treeFunctions.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> {
            if (nv.getChildren().isEmpty()) {
                //若不是根节点，则设置页面标题并加载页面内容
                txtPageName.setText(nv.getValue());
                switch (nv.getValue()) {
                    case "货品列表" ->
                            paneCarousel.getChildren().set(0, goodsList);
                    case "当前库存查询" ->
                            paneCarousel.getChildren().set(0, invQuery);
                    default -> paneCarousel.getChildren().set(0, blankPage);
                }
            }
        });
        treeFunctions.getSelectionModel().select(1);
    }

    /**
     * 设置treeItem子节点
     * @param rootItem   根节点
     * @param img        子节点的图片
     * @param childNames 子节点内容
     */
    private void setItems(TreeItem<String> rootItem, Image img, String... childNames) {
        for (String childName : childNames) {
            TreeItem<String> childItem = new TreeItem<>(childName);
            childItem.setGraphic(new ImageView(img));
            rootItem.getChildren().add(childItem);
        }
    }

    /**
     * 设置treeItem子节点<br>
     * 由于本方法只有唯一的用处，
     * 可以保证不会出现堆污染，
     * 因此使用@SafeVarargs注解
     * @param rootItem   根节点
     * @param img        子节点图片
     * @param childItems 子节点
     */
    @SafeVarargs
    private void setItems(TreeItem<String> rootItem, Image img, TreeItem<String>... childItems) {
        for (TreeItem<String> childItem : childItems) {
            childItem.setGraphic(new ImageView(img));
            rootItem.getChildren().add(childItem);
        }
    }

    /**
     * 窗口拖动
     * @param mouseEvent 鼠标事件
     * @触发组件 topBar
     * @触发事件 鼠标拖动
     */
    @FXML
    void dragWindow(MouseEvent mouseEvent) {
        Stage stage = getContext().getStageMap().get("main");
        stage.setX(mouseEvent.getScreenX() - offsetX);
        stage.setY(mouseEvent.getScreenY() - offsetY);
    }

    /**
     * 获取鼠标在窗口内的坐标
     * @param mouseEvent 鼠标事件
     * @触发组件 topBar
     * @触发事件 鼠标按下
     */
    @FXML
    void getOffset(MouseEvent mouseEvent) {
        offsetX = mouseEvent.getSceneX();
        offsetY = mouseEvent.getSceneY();
    }

    /**
     * 窗口最小化
     * @触发组件 btnMinimize
     * @触发事件 鼠标点击
     */
    @FXML
    void minimizeWindow() {
        Stage stage = getContext().getStageMap().get("main");
        stage.setIconified(true);
    }

    /**
     * 窗口最大化
     * @触发组件 btnMaximize
     * @触发事件 鼠标点击
     */
    @FXML
    void maximizeWindow() {
        Stage stage = getContext().getStageMap().get("main");
        stage.setFullScreenExitHint("");
        stage.setFullScreen(!stage.isFullScreen());
    }

    /**
     * 关闭窗口
     * @触发组件 btnExit
     * @触发事件 鼠标点击
     */
    @FXML
    void exitWindow() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * 控制菜单的显示和隐藏
     * @触发组件 btnSideCtrl
     * @触发事件 鼠标点击
     */
    @FXML
    void controlSidePane() {
        if (paneSide.isVisible()) {
            btnSideCtrl.getTooltip().setText("展开");
            hideSidePaneTransition.play();
        } else {
            btnSideCtrl.getTooltip().setText("收起");
            paneSide.setVisible(true);
            AnchorPane.setLeftAnchor(paneMain, 200.0);
            showSidePaneTransition.play();
        }
    }
}
