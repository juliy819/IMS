package com.juliy.ims.controller;

import com.juliy.ims.entity.Goods;
import com.juliy.ims.my_components.CbBoxSearcher;
import com.juliy.ims.my_components.CcbBoxModel;
import com.juliy.ims.my_components.MyComboBox;
import com.juliy.ims.my_components.MyTableCell;
import com.juliy.ims.service.GoodsListService;
import com.juliy.ims.service.impl.GoodsListServiceImpl;
import com.juliy.ims.utils.CommonUtil;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 货品列表页面控制器
 * @author JuLiy
 * @date 2022/9/29 16:20
 */
public class GoodsListController extends RootController {

    final SimpleStringProperty totalGoodsNum = new SimpleStringProperty();
    final SimpleStringProperty curPage = new SimpleStringProperty();
    final SimpleStringProperty totalPage = new SimpleStringProperty();
    final SimpleIntegerProperty pageSize = new SimpleIntegerProperty(20);

    private final Logger log = Logger.getLogger(GoodsListController.class);
    private final GoodsListService service = new GoodsListServiceImpl();
    private final MyComboBox cbbGoodsType = new MyComboBox();
    private final MyComboBox cbbGoodsId = new MyComboBox();
    private final MyComboBox cbbGoodsName = new MyComboBox();
    private final MyComboBox cbbGoodsSpec = new MyComboBox();
    ObservableList<CcbBoxModel> goodsTypeList;
    ObservableList<CcbBoxModel> goodsIdList;
    ObservableList<CcbBoxModel> goodsNameList;
    ObservableList<CcbBoxModel> goodsSpecList;
    ObservableList<Goods> showList;
    ObservableList<Goods> goodsList;
    @FXML
    private AnchorPane paneGoodsType;
    @FXML
    private AnchorPane paneGoodsId;
    @FXML
    private AnchorPane paneGoodsName;
    @FXML
    private AnchorPane paneGoodsSpec;
    @FXML
    private ComboBox<String> cbbPageRule;
    @FXML
    private PieChart pieGoodsType;
    @FXML
    private TableView<Goods> tableGoodsList;
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
    private void initialize() {
        bindProperties();
        initComponents();
        initData();
        log.info("货品列表页面加载完成");
    }

    /** 将各property与对应的输入框内容绑定 */
    private void bindProperties() {
        goodsTypeList = cbbGoodsType.getItems();
        goodsIdList = cbbGoodsId.getItems();
        goodsNameList = cbbGoodsName.getItems();
        goodsSpecList = cbbGoodsSpec.getItems();
        showList = tableGoodsList.getItems();

        txtTotalGoods.textProperty().bind(totalGoodsNum);
        txtTotalPage.textProperty().bind(totalPage);
        curPage.bindBidirectional(tfCurPage.textProperty());
    }

    /** 初始化各组件，如设置位置、监听等 */
    private void initComponents() {
        addCustomComponents();
        initTextField();
        initTableColumn();

        //初始化行数下拉框
        cbbPageRule.getItems().addAll("20条/页", "50条/页", "100条/页");
        cbbPageRule.getSelectionModel().select(0);
        cbbPageRule.getSelectionModel()
                .selectedItemProperty()
                .addListener((ob, ov, nv) -> {
                    pageSize.set(Integer.parseInt(nv.split("条")[0]));
                    curPage.set("1");
                    updateTableView();
                    updateFilteredGoodsNum();
                });

        cbbGoodsType.setOnHidden(event -> filterGoods());
        cbbGoodsId.setOnHidden(event -> filterGoods());
        cbbGoodsName.setOnHidden(event -> filterGoods());
        cbbGoodsSpec.setOnHidden(event -> filterGoods());
    }

    /** 添加自定义组件 */
    private void addCustomComponents() {
        paneGoodsType.getChildren().add(cbbGoodsType);
        paneGoodsId.getChildren().add(cbbGoodsId);
        paneGoodsName.getChildren().add(cbbGoodsName);
        paneGoodsSpec.getChildren().add(cbbGoodsSpec);
    }

    /** 初始化输入框 */
    private void initTextField() {
        Map.of(tfGoodsType, cbbGoodsType,
               tfGoodsId, cbbGoodsId,
               tfGoodsName, cbbGoodsName,
               tfGoodsSpec, cbbGoodsSpec
        ).forEach((tf, cbb) -> {
            //点击输入框右侧按钮清空文本
            tf.setOnClickButton(event -> tf.clear());
            //输入框内容变化时在对应下拉框中进行模糊搜索
            tf.textProperty().addListener(new CbBoxSearcher(tf, cbb));
        });

        tfCurPage.textProperty().addListener((ob, ov, nv) -> {
            //清空输入框时，不进行其余操作
            if ("".equals(nv)) {
                return;
            }
            //若输入的不是数字，将值置为oldValue，即不更新输入框内容
            if (!nv.matches("\\d*")) {
                curPage.set(ov);
                return;
            }
            int target = Integer.parseInt(nv);
            int total = Integer.parseInt(totalPage.get());
            if (target < 1 || target > total) {
                curPage.set(ov);
            }
        });


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
        totalGoodsNum.set(String.valueOf(goodsList.size()));

        goodsTypeList.addAll(goodsList.stream().map(Goods::getGoodsTypeName).distinct().map(CcbBoxModel::new).toList());
        goodsIdList.addAll(goodsList.stream().map(Goods::getGoodsId).map(String::valueOf).map(CcbBoxModel::new).toList());
        goodsNameList.addAll(goodsList.stream().map(Goods::getGoodsName).map(CcbBoxModel::new).toList());
        goodsSpecList.addAll(goodsList.stream().map(Goods::getGoodsSpec).distinct().map(CcbBoxModel::new).toList());
    }

    /**
     * 表格跳转至指定页
     * @触发组件 tfPageJump
     * @触发事件 回车
     */
    @FXML
    public void jumpPage() {
        if ("".equals(curPage.get())) {
            CommonUtil.showAlert(Alert.AlertType.ERROR, "错误", "页数不能为空！");
            return;
        }
        updateTableView();
    }

    /**
     * 表格跳转至上一页
     * @触发组件 btnLastPage
     * @触发事件 点击
     */
    @FXML
    public void toLastPage() {
        if (Integer.parseInt(curPage.get()) == 1) {
            return;
        }
        curPage.set(String.valueOf(Integer.parseInt(curPage.get()) - 1));
        updateTableView();
    }

    /**
     * 表格跳转至下一页
     * @触发组件 btnNextPage
     * @触发事件 点击
     */
    @FXML
    public void toNextPage() {
        if (curPage.get().equals(totalPage.get())) {
            return;
        }
        curPage.set(String.valueOf(Integer.parseInt(curPage.get()) + 1));
        updateTableView();
    }

    /** 更新表格显示的页面 */
    private void updateTableView() {
        int targetPage = Integer.parseInt(curPage.get());
        long skipNum = (long) (targetPage - 1) * pageSize.get();
        showList.clear();
        showList.addAll(goodsList.stream().skip(skipNum).limit(pageSize.get()).toList());
    }

    private void updateFilteredGoodsNum() {
        int totalCount = goodsList.size();
        int size = pageSize.get();
        int pageNum = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        totalPage.set(String.valueOf(pageNum));
    }

    private void filterGoods() {
        String typeStr = cbbGoodsType.getButtonCell().getText();
        String idStr = cbbGoodsId.getButtonCell().getText();
        String nameStr = cbbGoodsName.getButtonCell().getText();
        String specStr = cbbGoodsSpec.getButtonCell().getText();
        goodsList = service.getGoodsList(typeStr, idStr, nameStr, specStr);
        curPage.set("1");
        updateTableView();
        updateFilteredGoodsNum();
    }
}