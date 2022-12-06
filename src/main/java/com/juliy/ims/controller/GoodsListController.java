package com.juliy.ims.controller;

import com.juliy.ims.entity.Goods;
import com.juliy.ims.model.CheckCbbUnitModel;
import com.juliy.ims.model.PageTableModel;
import com.juliy.ims.model.SearchCbBoxModel;
import com.juliy.ims.my_components.MyComboBox;
import com.juliy.ims.my_components.MyTableCell;
import com.juliy.ims.service.GoodsListService;
import com.juliy.ims.service.impl.GoodsListServiceImpl;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 货品列表页面控制器
 * @author JuLiy
 * @date 2022/9/29 16:20
 */
public class GoodsListController extends RootController {
    final SimpleStringProperty totalGoodsNum = new SimpleStringProperty();
    private final Logger log = Logger.getLogger(GoodsListController.class);
    private final GoodsListService service = new GoodsListServiceImpl();
    private final MyComboBox cbbGoodsType = new MyComboBox();
    private final MyComboBox cbbGoodsId = new MyComboBox();
    private final MyComboBox cbbGoodsName = new MyComboBox();
    private final MyComboBox cbbGoodsSpec = new MyComboBox();
    private PageTableModel<Goods> tableModel;
    private SearchCbBoxModel goodsTypeModel;
    private SearchCbBoxModel goodsIdModel;
    private SearchCbBoxModel goodsNameModel;
    private SearchCbBoxModel goodsSpecModel;
    @FXML
    private AnchorPane paneGoodsType;
    @FXML
    private AnchorPane paneGoodsId;
    @FXML
    private AnchorPane paneGoodsName;
    @FXML
    private AnchorPane paneGoodsSpec;
    @FXML
    private ComboBox<String> cbbPageSize;
    @FXML
    private PieChart pieGoodsType;
    @FXML
    private TableView<Goods> tableGoods;
    @FXML
    private TableColumn<Goods, String> tcComment;
    @FXML
    private TableColumn<Goods, Integer> tcId;
    @FXML
    private TableColumn<Goods, Integer> tcMaxQty;
    @FXML
    private TableColumn<Goods, Integer> tcMinQty;
    @FXML
    private TableColumn<Goods, String> tcName;
    @FXML
    private TableColumn<Goods, BigDecimal> tcPurPrice;
    @FXML
    private TableColumn<Goods, BigDecimal> tcSellPrice;
    @FXML
    private TableColumn<Goods, String> tcSpec;
    @FXML
    private TableColumn<Goods, String> tcType;
    @FXML
    private TableColumn<Goods, String> tcUnit;
    @FXML
    private TextField tfCurPage;
    @FXML
    private RXTextField tfGoodsId;
    @FXML
    private RXTextField tfGoodsName;
    @FXML
    private RXTextField tfGoodsSpec;
    @FXML
    private RXTextField tfGoodsType;
    @FXML
    private Text txtTotalGoods;
    @FXML
    private Text txtTotalPage;
    @FXML
    private BorderPane paneChart;
    @FXML
    private VBox paneTable;
    @FXML
    private AnchorPane paneContent;
    @FXML
    private StackPane paneFull;

    @FXML
    private void initialize() {
        initModel();
        initComponents();
        initData();

        log.info("货品列表页面加载完成");
    }

    /** 初始化model，并将model的属性与组件进行绑定 */
    private void initModel() {
        tableModel = new PageTableModel<>(tableGoods, cbbPageSize);
        goodsTypeModel = new SearchCbBoxModel(tfGoodsType, cbbGoodsType);
        goodsIdModel = new SearchCbBoxModel(tfGoodsId, cbbGoodsId);
        goodsNameModel = new SearchCbBoxModel(tfGoodsName, cbbGoodsName);
        goodsSpecModel = new SearchCbBoxModel(tfGoodsSpec, cbbGoodsSpec);

        txtTotalGoods.textProperty().bind(totalGoodsNum);
        tfCurPage.textProperty().bindBidirectional(tableModel.curPageProperty());
        txtTotalPage.textProperty().bind(tableModel.totalPageProperty());
    }

    /** 初始化各组件，如设置位置、监听等 */
    private void initComponents() {
        addCustomComponents();
        initTableColumn();

        goodsTypeModel.setOnCbBoxHidden(event -> filterGoods());
        goodsIdModel.setOnCbBoxHidden(event -> filterGoods());
        goodsNameModel.setOnCbBoxHidden(event -> filterGoods());
        goodsSpecModel.setOnCbBoxHidden(event -> filterGoods());

        tfCurPage.textProperty().addListener(tableModel.getCurPageLimit());
        tableModel.pageSizeProperty().addListener((ob, ov, nv) -> filterGoods());
    }

    /** 添加自定义组件 */
    private void addCustomComponents() {
        paneGoodsType.getChildren().add(cbbGoodsType);
        paneGoodsId.getChildren().add(cbbGoodsId);
        paneGoodsName.getChildren().add(cbbGoodsName);
        paneGoodsSpec.getChildren().add(cbbGoodsSpec);
    }

    /** 初始化表格列 */
    private void initTableColumn() {
        Map.of(tcType, "goodsTypeName",
               tcId, "goodsId",
               tcName, "goodsName",
               tcSpec, "goodsSpec",
               tcUnit, "goodsUnit",
               tcPurPrice, "refPurPrice",
               tcSellPrice, "refSellPrice",
               tcMaxQty, "maxQty",
               tcMinQty, "minQty",
               tcComment, "goodsComment"
        ).forEach((tc, value) -> {
            tc.setCellValueFactory(new PropertyValueFactory<>(value));
            tc.setCellFactory(param -> new MyTableCell<>());
        });
    }

    /** 将从数据库获取的数据加载到各个组件中 */
    private void initData() {
        filterGoods();

        List<Goods> goodsList = service.getAllGoods();

        cbbGoodsType.getItems().addAll(goodsList.stream().map(Goods::getGoodsTypeName).distinct().map(CheckCbbUnitModel::new).toList());
        cbbGoodsId.getItems().addAll(goodsList.stream().map(Goods::getGoodsId).map(String::valueOf).map(CheckCbbUnitModel::new).toList());
        cbbGoodsName.getItems().addAll(goodsList.stream().map(Goods::getGoodsName).map(CheckCbbUnitModel::new).toList());
        cbbGoodsSpec.getItems().addAll(goodsList.stream().map(Goods::getGoodsSpec).distinct().map(CheckCbbUnitModel::new).toList());

        totalGoodsNum.set(String.valueOf(service.getTotalCount()));
        initChart(goodsList);
    }

    /** 初始化图表 */
    private void initChart(List<Goods> goodsList) {
        //统计每个货品类别对应的货品数量
        HashMap<String, Integer> typeMap = new HashMap<>();
        cbbGoodsType.getItems().forEach(model -> typeMap.put(model.getValue(), 0));
        goodsList.forEach(goods -> {
            Integer num = typeMap.get(goods.getGoodsTypeName());
            num++;
            typeMap.put(goods.getGoodsTypeName(), num);
        });

        //将数据添加进图表中
        ObservableList<PieChart.Data> pieList = pieGoodsType.getData();
        double total = Double.parseDouble(totalGoodsNum.get());
        typeMap.forEach((typeName, num) -> {
            double percentage = num / total;
            String percentageStr = String.format("%.2f", percentage * 100) + "%";
            PieChart.Data data = new PieChart.Data(typeName + "-" + percentageStr, percentage);
            //要先添加进图表中才能调用getNode方法
            pieList.add(data);
            Tooltip tip = new Tooltip(typeName + "-" + percentageStr);
            Tooltip.install(data.getNode(), tip);
        });

        //设置图表过小时不显示标签，变大后再显示  300是选的大概值
        pieGoodsType.widthProperty()
                .addListener((ob, ov, nv) -> pieGoodsType.setLabelsVisible(nv.intValue() >= 300));
    }

    /**
     * 表格跳转至指定页
     * @触发组件 tfPageJump
     * @触发事件 回车
     */
    @FXML
    void jumpPage() {
        tableModel.jumpPage();
        updateTableView();
    }

    /**
     * 表格跳转至上一页
     * @触发组件 btnLastPage
     * @触发事件 点击
     */
    @FXML
    void toLastPage() {
        tableModel.toLastPage();
        updateTableView();
    }

    /**
     * 表格跳转至下一页
     * @触发组件 btnNextPage
     * @触发事件 点击
     */
    @FXML
    void toNextPage() {
        tableModel.toNextPage();
        updateTableView();
    }

    /**
     * 设置组件全屏显示
     * @param event 点击事件
     * @触发组件 btnTableFullScreen, btnChartFullScreen
     * @触发事件 点击
     */
    @FXML
    void setFullScreen(ActionEvent event) {
        Node btn = (Node) event.getSource();
        Node pane = btn.getParent().getParent();
        if (!paneFull.getChildren().contains(pane)) {
            paneFull.getChildren().add(pane);
        } else {
            paneContent.getChildren().add(pane);
        }
    }

    private void filterGoods() {
        String typeStr = goodsTypeModel.getButtonCellText();
        String idStr = goodsIdModel.getButtonCellText();
        String nameStr = goodsNameModel.getButtonCellText();
        String specStr = goodsSpecModel.getButtonCellText();
        tableModel.updateTotalPage(service.getFilterCount(typeStr, idStr, nameStr, specStr));
        tableModel.setCurPage(1);
        updateTableView();
    }

    /** 更新表格内容 */
    private void updateTableView() {
        String typeStr = goodsTypeModel.getButtonCellText();
        String idStr = goodsIdModel.getButtonCellText();
        String nameStr = goodsNameModel.getButtonCellText();
        String specStr = goodsSpecModel.getButtonCellText();
        tableModel.updateTableView(service.filterGoodsByPage(typeStr, idStr, nameStr, specStr, tableModel.getCurPage(), tableModel.getPageSize()));
    }
}