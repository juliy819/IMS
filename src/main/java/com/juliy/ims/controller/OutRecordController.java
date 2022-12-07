package com.juliy.ims.controller;

import com.juliy.ims.entity.Company;
import com.juliy.ims.entity.Warehouse;
import com.juliy.ims.entity.table_unit.RecordDO;
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
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 出库单页面控制器
 * @author JuLiy
 * @date 2022/12/5 15:08
 */
public class OutRecordController {
    private static final Logger log = Logger.getLogger(OutRecordController.class);
    private static final String RECEIPT_TYPE = "销售出库";
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
    private TableColumn<RecordDO, BigDecimal> tcSellPrice;
    @FXML
    private TableColumn<RecordDO, BigDecimal> tcAmount;
    @FXML
    private TableColumn<RecordDO, Integer> tcOutQty;
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
        tableModel = new VariableTableModel(table, service, 1);
        RecordDO rec = new RecordDO();
        table.getItems().add(rec);

        tfTotalAmount.textProperty().bind(tableModel.totalAmountProperty());

        cbbCompany.setConverter(EntryRecordController.companyConverter);
        cbbWhs.setConverter(EntryRecordController.whsConverter);

        cbbCompany.getItems().addAll(service.getCustomerList());
        cbbWhs.getItems().addAll(service.getWarehouseList());

        tcGoodsId.setCellValueFactory(new PropertyValueFactory<>("goodsId"));
        tcGoodsName.setCellValueFactory(new PropertyValueFactory<>("goodsName"));
        tcGoodsType.setCellValueFactory(new PropertyValueFactory<>("goodsType"));
        tcGoodsSpec.setCellValueFactory(new PropertyValueFactory<>("goodsSpec"));
        tcSellPrice.setCellValueFactory(new PropertyValueFactory<>("refSellPrice"));
        tcOutQty.setCellValueFactory(new PropertyValueFactory<>("outQty"));
        tcAmount.setCellValueFactory(new PropertyValueFactory<>("outAmt"));

        tcGoodsId.setCellFactory(param -> tableModel.getCbbTableCell());
        tcOutQty.setCellFactory(param -> tableModel.getTfTableCell());
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
     * @触发组件 btnCreateNewOutRecord
     * @触发事件 鼠标点击
     */
    @FXML
    void createNewOutRecord() {
        clearPrompt();

        if (!inputCheck()) {
            return;
        }

        service.addOutReceipt(generateReceipt());
        txtSuccess.setText("创建成功! 单号:" + receiptId);
        txtSuccess.setVisible(true);
        log.info("创建出库单:" + receiptId);

        clearInput();
    }

    /** 输入检查 */
    private boolean inputCheck() {
        if (cbbCompany.getSelectionModel().isEmpty()) {
            txtCompanyError.setText("未选择客户!");
            txtCompanyError.setVisible(true);
            return false;
        }
        if (cbbWhs.getSelectionModel().isEmpty()) {
            txtWhsError.setText("未选择仓库!");
            txtWhsError.setVisible(true);
            return false;
        }
        if ("0".equals(tfTotalAmount.getText())) {
            txtTableError.setText("请至少添加一项出库货品!");
            txtTableError.setVisible(true);
            return false;
        }

        //判断输入的出库数量是否超过所选仓库中的库存量
        int whsId = cbbWhs.getSelectionModel().getSelectedItem().getWhsId();
        String whsName = cbbWhs.getSelectionModel().getSelectedItem().getWhsName();
        for (int i = 0; i < table.getItems().size(); i++) {
            RecordDO rec = table.getItems().get(i);
            int qty = service.getGoodsQty(whsId, rec.getGoodsId());
            if (rec.getOutQty() > qty) {
                txtTableError.setText("'" + rec.getGoodsName() + "'库存数量不足!  当前在" + whsName + "'中的库存量为:" + qty);
                txtTableError.setVisible(true);
                return false;
            }
        }

        return true;
    }

    /** 根据表格内容生成单据 */
    List<RecordDO> generateReceipt() {
        receiptId = RecIdUtil.getOutId();
        List<RecordDO> recList = new ArrayList<>();

        table.getItems().forEach(rec -> {
            //金额为0表示该行未设置好，不进行设置
            if (rec.getOutAmt().compareTo(new BigDecimal(0)) != 0) {
                rec.setReceiptType(RECEIPT_TYPE);
                rec.setReceiptId(receiptId);
                rec.setCompanyId(cbbCompany.getSelectionModel().getSelectedItem().getCompanyId());
                rec.setWhsId(cbbWhs.getSelectionModel().getSelectedItem().getWhsId());
                rec.setEntryQty(0);
                rec.setEntryAmt(new BigDecimal(0));

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
