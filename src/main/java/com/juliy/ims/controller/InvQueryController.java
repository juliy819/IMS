package com.juliy.ims.controller;

import com.juliy.ims.entity.table_unit.InvDO;
import com.juliy.ims.model.CheckCbbUnitModel;
import com.juliy.ims.model.PageTableModel;
import com.juliy.ims.model.SearchCbBoxModel;
import com.juliy.ims.my_components.MyComboBox;
import com.juliy.ims.my_components.MyTableCell;
import com.juliy.ims.service.InvQueryService;
import com.juliy.ims.service.impl.InvQueryServiceImpl;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * 库存查询页面控制器
 * @author JuLiy
 * @date 2022/9/30 16:21
 */
public class InvQueryController {
    private final Logger log = Logger.getLogger(InvQueryController.class);
    private final InvQueryService service = new InvQueryServiceImpl();
    private final MyComboBox cbbWhsName = new MyComboBox();
    private final MyComboBox cbbGoodsId = new MyComboBox();
    private final MyComboBox cbbGoodsType = new MyComboBox();
    private final MyComboBox cbbGoodsName = new MyComboBox();
    private final MyComboBox cbbGoodsSpec = new MyComboBox();
    private PageTableModel<InvDO> tableModel;
    private SearchCbBoxModel whsNameModel;
    private SearchCbBoxModel goodsIdModel;
    private SearchCbBoxModel goodsTypeModel;
    private SearchCbBoxModel goodsNameModel;
    private SearchCbBoxModel goodsSpecModel;

    @FXML
    private ComboBox<String> cbbPageSize;
    @FXML
    private AnchorPane paneContent;
    @FXML
    private StackPane paneFull;
    @FXML
    private AnchorPane paneWhsName;
    @FXML
    private AnchorPane paneGoodsId;
    @FXML
    private AnchorPane paneGoodsName;
    @FXML
    private AnchorPane paneGoodsSpec;
    @FXML
    private AnchorPane paneGoodsType;
    @FXML
    private VBox paneTable;
    @FXML
    private TableView<InvDO> tableInv;
    @FXML
    private TableColumn<InvDO, String> tcWhsName;
    @FXML
    private TableColumn<InvDO, Integer> tcId;
    @FXML
    private TableColumn<InvDO, String> tcType;
    @FXML
    private TableColumn<InvDO, String> tcName;
    @FXML
    private TableColumn<InvDO, String> tcSpec;
    @FXML
    private TableColumn<InvDO, String> tcUnit;
    @FXML
    private TableColumn<InvDO, Integer> tcQty;
    @FXML
    private TextField tfCurPage;
    @FXML
    private RXTextField tfWhsName;
    @FXML
    private RXTextField tfGoodsId;
    @FXML
    private RXTextField tfGoodsType;
    @FXML
    private RXTextField tfGoodsName;
    @FXML
    private RXTextField tfGoodsSpec;
    @FXML
    private Text txtTotalPage;

    @FXML
    private void initialize() {
        initModel();
        initComponents();
        initData();

        log.info("库存查询页面加载完成");
    }

    /** 初始化model，并将model的属性与组件进行绑定 */
    private void initModel() {
        tableModel = new PageTableModel<>(tableInv, cbbPageSize);
        whsNameModel = new SearchCbBoxModel(tfWhsName, cbbWhsName);
        goodsIdModel = new SearchCbBoxModel(tfGoodsId, cbbGoodsId);
        goodsTypeModel = new SearchCbBoxModel(tfGoodsType, cbbGoodsType);
        goodsNameModel = new SearchCbBoxModel(tfGoodsName, cbbGoodsName);
        goodsSpecModel = new SearchCbBoxModel(tfGoodsSpec, cbbGoodsSpec);

        tfCurPage.textProperty().bindBidirectional(tableModel.curPageProperty());
        txtTotalPage.textProperty().bind(tableModel.totalPageProperty());
    }

    /** 初始化组件 */
    private void initComponents() {
        addCustomComponents();
        initTableColumn();

        //收起下拉框后刷新
        whsNameModel.setOnCbBoxHidden(event -> filterInv());
        goodsIdModel.setOnCbBoxHidden(event -> filterInv());
        goodsTypeModel.setOnCbBoxHidden(event -> filterInv());
        goodsNameModel.setOnCbBoxHidden(event -> filterInv());
        goodsSpecModel.setOnCbBoxHidden(event -> filterInv());
        //设置输入框限制
        tfCurPage.textProperty().addListener(tableModel.getCurPageLimit());
        //更改表格条数后刷新
        tableModel.pageSizeProperty().addListener((ob, ov, nv) -> filterInv());
    }

    /** 添加自定义组件 */
    private void addCustomComponents() {
        paneWhsName.getChildren().add(cbbWhsName);
        paneGoodsId.getChildren().add(cbbGoodsId);
        paneGoodsType.getChildren().add(cbbGoodsType);
        paneGoodsName.getChildren().add(cbbGoodsName);
        paneGoodsSpec.getChildren().add(cbbGoodsSpec);
    }

    /** 初始化表格列 */
    private void initTableColumn() {
        Map.of(tcWhsName, "whsName",
               tcId, "goodsId",
               tcType, "goodsTypeName",
               tcName, "goodsName",
               tcSpec, "goodsSpec",
               tcUnit, "goodsUnit",
               tcQty, "goodsQty"
        ).forEach((tc, value) -> {
            tc.setCellValueFactory(new PropertyValueFactory<>(value));
            tc.setCellFactory(param -> new MyTableCell<>());
        });
    }

    /** 加载数据 */
    private void initData() {
        filterInv();

        List<InvDO> invList = service.getAllInv();

        cbbWhsName.getItems().addAll(invList.stream().map(InvDO::getWhsName).distinct().sorted().map(CheckCbbUnitModel::new).toList());
        cbbGoodsId.getItems().addAll(invList.stream().map(InvDO::getGoodsId).distinct().sorted().map(String::valueOf).map(CheckCbbUnitModel::new).toList());
        cbbGoodsType.getItems().addAll(invList.stream().map(InvDO::getGoodsTypeName).distinct().map(CheckCbbUnitModel::new).toList());
        cbbGoodsName.getItems().addAll(invList.stream().map(InvDO::getGoodsName).distinct().map(CheckCbbUnitModel::new).toList());
        cbbGoodsSpec.getItems().addAll(invList.stream().map(InvDO::getGoodsSpec).distinct().map(CheckCbbUnitModel::new).toList());
    }


    /**
     * 表格跳转至指定页
     * @触发组件 tfPageJump
     * @触发事件 回车
     */
    @FXML
    void jumpPage(ActionEvent event) {
        tableModel.jumpPage();
        updateTableView();
    }

    /**
     * 设置组件全屏显示
     * @param event 点击事件
     * @触发组件 btnTableFullScreen
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

    private void filterInv() {
        String whsNameStr = cbbWhsName.getButtonCell().getText();
        String idStr = cbbGoodsId.getButtonCell().getText();
        String typeStr = cbbGoodsType.getButtonCell().getText();
        String nameStr = cbbGoodsName.getButtonCell().getText();
        String specStr = cbbGoodsSpec.getButtonCell().getText();
        tableModel.updateTotalPage(service.getFilterCount(whsNameStr, idStr, typeStr, nameStr, specStr));
        tableModel.setCurPage(1);
        updateTableView();
    }

    /** 更新表格内容 */
    private void updateTableView() {
        String whsNameStr = whsNameModel.getButtonCellText();
        String typeStr = goodsTypeModel.getButtonCellText();
        String idStr = goodsIdModel.getButtonCellText();
        String nameStr = goodsNameModel.getButtonCellText();
        String specStr = goodsSpecModel.getButtonCellText();
        tableModel.updateTableView(service.filterInvByPage(whsNameStr, idStr, typeStr, nameStr, specStr, tableModel.getCurPage(), tableModel.getPageSize()));
    }
}