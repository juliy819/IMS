package com.juliy.ims.controller;

import com.juliy.ims.entity.table_unit.RecordDO;
import com.juliy.ims.model.CheckCbbUnitModel;
import com.juliy.ims.model.PageTableModel;
import com.juliy.ims.model.SearchCbBoxModel;
import com.juliy.ims.my_components.MyComboBox;
import com.juliy.ims.my_components.MyTableCell;
import com.juliy.ims.service.RecordQueryService;
import com.juliy.ims.service.impl.RecordQueryServiceImpl;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 记录查询页面控制器
 * @author JuLiy
 * @date 2022/12/6 15:15
 */
public class RecordQueryController {

    private final Logger log = Logger.getLogger(RecordQueryController.class);
    private final RecordQueryService service = new RecordQueryServiceImpl();
    private final MyComboBox cbbWhsName = new MyComboBox();
    private final MyComboBox cbbReceiptType = new MyComboBox();
    private final MyComboBox cbbGoodsId = new MyComboBox();
    private final MyComboBox cbbGoodsType = new MyComboBox();
    private PageTableModel<RecordDO> tableModel;
    private SearchCbBoxModel whsNameModel;
    private SearchCbBoxModel receiptTypeModel;
    private SearchCbBoxModel goodsIdModel;
    private SearchCbBoxModel goodsTypeModel;

    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private ComboBox<String> cbbPageSize;
    @FXML
    private AnchorPane paneContent;
    @FXML
    private AnchorPane paneDateRange;
    @FXML
    private StackPane paneFull;
    @FXML
    private AnchorPane paneGoodsId;
    @FXML
    private AnchorPane paneReceiptType;
    @FXML
    private AnchorPane paneGoodsType;
    @FXML
    private VBox paneTable;
    @FXML
    private AnchorPane paneWhsName;
    @FXML
    private TableView<RecordDO> tableRec;
    @FXML
    private TableColumn<RecordDO, Date> tcBizDate;
    @FXML
    private TableColumn<RecordDO, BigDecimal> tcEntryAmt;
    @FXML
    private TableColumn<RecordDO, Integer> tcEntryQty;
    @FXML
    private TableColumn<RecordDO, Integer> tcGoodsId;
    @FXML
    private TableColumn<RecordDO, String> tcGoodsName;
    @FXML
    private TableColumn<RecordDO, String> tcGoodsSpec;
    @FXML
    private TableColumn<RecordDO, String> tcGoodsType;
    @FXML
    private TableColumn<RecordDO, BigDecimal> tcOutAmt;
    @FXML
    private TableColumn<RecordDO, Integer> tcOutQty;
    @FXML
    private TableColumn<RecordDO, String> tcReceiptId;
    @FXML
    private TableColumn<RecordDO, String> tcReceiptType;
    @FXML
    private TableColumn<RecordDO, String> tcWhsName;
    @FXML
    private TextField tfCurPage;
    @FXML
    private RXTextField tfGoodsId;
    @FXML
    private RXTextField tfGoodsType;
    @FXML
    private RXTextField tfRecordType;
    @FXML
    private RXTextField tfWhsName;
    @FXML
    private Text txtTotalPage;


    @FXML
    private void initialize() {
        initModel();
        initComponents();
        initData();

        log.info("记录查询页面加载完成");
    }

    /** 初始化model，并将model的属性与组件进行绑定 */
    private void initModel() {
        tableModel = new PageTableModel<>(tableRec, cbbPageSize);
        whsNameModel = new SearchCbBoxModel(tfWhsName, cbbWhsName);
        receiptTypeModel = new SearchCbBoxModel(tfRecordType, cbbReceiptType);
        goodsIdModel = new SearchCbBoxModel(tfGoodsId, cbbGoodsId);
        goodsTypeModel = new SearchCbBoxModel(tfGoodsType, cbbGoodsType);

        tfCurPage.textProperty().bindBidirectional(tableModel.curPageProperty());
        txtTotalPage.textProperty().bind(tableModel.totalPageProperty());
    }

    /** 初始化组件 */
    private void initComponents() {
        addCustomComponents();
        initTableColumn();
        initDatePicker();

        whsNameModel.setOnCbBoxHidden(event -> filterRec());
        receiptTypeModel.setOnCbBoxHidden(event -> filterRec());
        goodsIdModel.setOnCbBoxHidden(event -> filterRec());
        goodsTypeModel.setOnCbBoxHidden(event -> filterRec());

        tfCurPage.textProperty().addListener(tableModel.getCurPageLimit());
        tableModel.pageSizeProperty().addListener((ob, ov, nv) -> filterRec());
    }

    /** 添加自定义组件 */
    private void addCustomComponents() {
        paneWhsName.getChildren().add(cbbWhsName);
        paneReceiptType.getChildren().add(cbbReceiptType);
        paneGoodsId.getChildren().add(cbbGoodsId);
        paneGoodsType.getChildren().add(cbbGoodsType);
    }

    /** 初始化表格列 */
    private void initTableColumn() {
        // 由于Map.of方法的参数最多为10组键值对，
        // 而不使用这个方法则无法泛化
        // 因此只能进行拆分
        Map.of(tcBizDate, "bizDate",
               tcWhsName, "whsName",
               tcReceiptType, "receiptType",
               tcReceiptId, "receiptId",
               tcGoodsId, "goodsId",
               tcGoodsType, "goodsType",
               tcGoodsName, "goodsName",
               tcGoodsSpec, "goodsSpec"
        ).forEach((tc, value) -> {
            tc.setCellValueFactory(new PropertyValueFactory<>(value));
            tc.setCellFactory(param -> new MyTableCell<>());
        });
        Map.of(tcEntryQty, "entryQty",
               tcEntryAmt, "entryAmt",
               tcOutQty, "outQty",
               tcOutAmt, "outAmt"
        ).forEach((tc, value) -> {
            tc.setCellValueFactory(new PropertyValueFactory<>(value));
            tc.setCellFactory(param -> new MyTableCell<>());
        });
    }

    /** 初始化日期选择器 */
    private void initDatePicker() {
        dpStartDate.setOnHidden(event -> filterRec());
        dpEndDate.setOnHidden(event -> filterRec());
    }

    /** 加载数据 */
    private void initData() {
        filterRec();

        List<RecordDO> recList = service.getAllRec();

        cbbWhsName.getItems().addAll(recList.stream().map(RecordDO::getWhsName).distinct().sorted().map(CheckCbbUnitModel::new).toList());
        cbbReceiptType.getItems().addAll(recList.stream().map(RecordDO::getReceiptType).distinct().sorted().map(CheckCbbUnitModel::new).toList());
        cbbGoodsId.getItems().addAll(recList.stream().map(RecordDO::getGoodsId).distinct().sorted().map(String::valueOf).map(CheckCbbUnitModel::new).toList());
        cbbGoodsType.getItems().addAll(recList.stream().map(RecordDO::getGoodsType).distinct().sorted().map(CheckCbbUnitModel::new).toList());
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

    /** 筛选记录 */
    private void filterRec() {
        String startDate = dpStartDate.getEditor().getText();
        String endDate = dpEndDate.getEditor().getText();
        if ("".equals(startDate)) {
            startDate = "2000/01/01";
        }
        if ("".equals(endDate)) {
            endDate = "3000/01/01";
        }

        tableModel.updateTotalPage(service.getFilterCount(startDate, endDate, getFilterArgs()));
        tableModel.setCurPage(1);
        updateTableView();
    }

    /** 更新表格内容 */
    private void updateTableView() {
        String startDate = dpStartDate.getEditor().getText();
        String endDate = dpEndDate.getEditor().getText();
        if ("".equals(startDate)) {
            startDate = "2000/01/01";
        }
        if ("".equals(endDate)) {
            endDate = "3000/01/01";
        }

        tableModel.updateTableView(service.filterRecByPage(startDate, endDate, getFilterArgs(), tableModel.getCurPage(), tableModel.getPageSize()));
    }

    /** 获取包含筛选条件的记录对象 */
    private RecordDO getFilterArgs() {
        RecordDO rec = new RecordDO();
        rec.setWhsName(cbbWhsName.getButtonCell().getText());
        rec.setReceiptType(cbbReceiptType.getButtonCell().getText());
        if (cbbGoodsId.getButtonCell().getText() == null || "".equals(cbbGoodsId.getButtonCell().getText())) {
            rec.setGoodsId(-1);
        } else {
            rec.setGoodsId(Integer.valueOf(cbbGoodsId.getButtonCell().getText()));
        }
        rec.setGoodsType(cbbGoodsType.getButtonCell().getText());
        return rec;
    }

}
