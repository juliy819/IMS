package com.juliy.ims.controller;

import com.juliy.ims.entity.Company;
import com.juliy.ims.entity.Warehouse;
import com.juliy.ims.entity.model.RecordDO;
import com.juliy.ims.model.VariableTableModel;
import com.juliy.ims.service.CreateRecordService;
import com.juliy.ims.service.impl.CreateRecordServiceImpl;
import com.juliy.ims.utils.RecIdUtil;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 入库单页面控制器
 * @author JuLiy
 * @date 2022/12/2 15:02
 */
public class EntryRecordController {

    public static final StringConverter<Company> companyConverter = new StringConverter<>() {

        @Override
        public String toString(Company object) {
            if (object == null) {
                return null;
            }
            return object.getCompanyName();
        }

        @Override
        public Company fromString(String string) {
            return null;
        }
    };
    public static final StringConverter<Warehouse> whsConverter = new StringConverter<>() {

        @Override
        public String toString(Warehouse object) {
            if (object == null) {
                return null;
            }
            return object.getWhsName();
        }

        @Override
        public Warehouse fromString(String string) {
            return null;
        }
    };
    private static final Logger log = Logger.getLogger(EntryRecordController.class);
    private static final String RECEIPT_TYPE = "采购入库";
    private final CreateRecordService service = new CreateRecordServiceImpl();
    private String receiptId;
    private VariableTableModel tableModel;
    @FXML
    private ComboBox<Company> cbbCompany;
    @FXML
    private ComboBox<Warehouse> cbbWhs;
    @FXML
    private TableView<RecordDO> table;
    @FXML
    private TableColumn<String, Integer> tcDelete;
    @FXML
    private TableColumn<RecordDO, Integer> tcGoodsId;
    @FXML
    private TableColumn<RecordDO, String> tcGoodsType;
    @FXML
    private TableColumn<RecordDO, String> tcGoodsName;
    @FXML
    private TableColumn<RecordDO, String> tcGoodsSpec;
    @FXML
    private TableColumn<RecordDO, BigDecimal> tcPurPrice;
    @FXML
    private TableColumn<RecordDO, BigDecimal> tcAmount;
    @FXML
    private TableColumn<RecordDO, Integer> tcEntryQty;
    @FXML
    private RXTextField tfReceiptId;
    @FXML
    private RXTextField tfReceiptType;
    @FXML
    private RXTextField tfTotalAmount;
    @FXML
    private Text txtCompanyError;
    @FXML
    private Text txtWhsError;
    @FXML
    private Text txtTableError;
    @FXML
    private Text txtSuccess;

    @FXML
    private void initialize() {
        tableModel = new VariableTableModel(table, service, 0);

        tfTotalAmount.textProperty().bind(tableModel.totalAmountProperty());

        cbbCompany.setConverter(companyConverter);
        cbbWhs.setConverter(whsConverter);

        cbbCompany.getItems().addAll(service.getSupplierList());
        cbbWhs.getItems().addAll(service.getWarehouseList());

        RecordDO rec = new RecordDO();
        table.getItems().add(rec);

        tcGoodsId.setCellValueFactory(new PropertyValueFactory<>("goodsId"));
        tcGoodsName.setCellValueFactory(new PropertyValueFactory<>("goodsName"));
        tcGoodsType.setCellValueFactory(new PropertyValueFactory<>("goodsType"));
        tcGoodsSpec.setCellValueFactory(new PropertyValueFactory<>("goodsSpec"));
        tcPurPrice.setCellValueFactory(new PropertyValueFactory<>("refPurPrice"));
        tcEntryQty.setCellValueFactory(new PropertyValueFactory<>("entryQty"));
        tcAmount.setCellValueFactory(new PropertyValueFactory<>("entryAmt"));

        tcGoodsId.setCellFactory(param -> tableModel.getCbbTableCell());

        tcEntryQty.setCellFactory(param -> tableModel.getTfTableCell());

        tcDelete.setCellFactory(param -> tableModel.getDeleteTableCell());

    }

    /**
     * 添加新记录
     * @触发组件 btnAddNewRecord
     * @触发事件 鼠标点击
     */
    @FXML
    void addNewRecord() {
        table.getItems().add(new RecordDO());
        //每次加30的话每加两次会有一次显示出右边的滚动条，比较难看，因此一次加31，一次加29
        int size = table.getItems().size() % 2 == 0 ? 31 : 29;
        table.setPrefHeight(table.getPrefHeight() + size);
        table.refresh();
    }

    /**
     * 生成入库单
     * @触发组件 btnCreateNewEntryRecord
     * @触发事件 鼠标点击
     */
    @FXML
    void createNewEntryRecord() {
        clearPrompt();

        if (!inputCheck()) {
            return;
        }

        service.addEntryReceipt(generateReceipt());
        txtSuccess.setText("创建成功! 单号:" + receiptId);
        txtSuccess.setVisible(true);
        log.info("创建入库单:" + receiptId);

        clearInput();
    }

    private boolean inputCheck() {
        if (cbbCompany.getSelectionModel().isEmpty()) {
            txtCompanyError.setText("未选择供应商!");
            txtCompanyError.setVisible(true);
            return false;
        }
        if (cbbWhs.getSelectionModel().isEmpty()) {
            txtWhsError.setText("未选择仓库!");
            txtWhsError.setVisible(true);
            return false;
        }
        if ("0".equals(tfTotalAmount.getText())) {
            txtTableError.setText("请至少添加一项入库货品!");
            txtTableError.setVisible(true);
            return false;
        }
        return true;
    }

    List<RecordDO> generateReceipt() {
        receiptId = RecIdUtil.getEntryId();
        List<RecordDO> recList = new ArrayList<>();

        table.getItems().forEach(rec -> {
            //金额为0表示该行未设置好，不进行设置
            if (rec.getEntryAmt().compareTo(new BigDecimal(0)) != 0) {
                rec.setReceiptType(RECEIPT_TYPE);
                rec.setReceiptId(receiptId);
                rec.setCompanyId(cbbCompany.getSelectionModel().getSelectedItem().getCompanyId());
                rec.setWhsId(cbbWhs.getSelectionModel().getSelectedItem().getWhsId());
                rec.setOutQty(0);
                rec.setOutAmt(new BigDecimal(0));

                recList.add(rec);
            }
        });

        return recList;
    }

    /** 清除提示框 */
    private void clearPrompt() {
        List.of(
                txtCompanyError,
                txtWhsError,
                txtTableError,
                txtSuccess
        ).forEach(txt -> txt.setVisible(false));
    }

    /** 清空输入 */
    private void clearInput() {
        cbbCompany.getSelectionModel().clearSelection();
        cbbWhs.getSelectionModel().clearSelection();
        table.getItems().clear();
        table.getItems().add(new RecordDO());
        table.setPrefHeight(60);
        tableModel.updateTotalAmount();
    }
}
