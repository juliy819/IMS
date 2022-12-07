package com.juliy.ims.controller;

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
 * 调拨单页面控制器
 * @author JuLiy
 * @date 2022/12/6 13:51
 */
public class AlocRecordController {

    private static final Logger log = Logger.getLogger(AlocRecordController.class);
    private static final String RECEIPT_TYPE = "调拨";
    private final CreateRecordService service = new CreateRecordServiceImpl();
    private String receiptId;
    private VariableTableModel tableModel;

    @FXML
    private ComboBox<Warehouse> cbbEntryWhs;
    @FXML
    private ComboBox<Warehouse> cbbOutWhs;
    @FXML
    private TableView<RecordDO> table;
    @FXML
    private TableColumn<RecordDO, Integer> tcAlocQty;
    @FXML
    private TableColumn<String, Integer> tcDelete;
    @FXML
    private TableColumn<RecordDO, Integer> tcGoodsId;
    @FXML
    private TableColumn<RecordDO, String> tcGoodsName;
    @FXML
    private TableColumn<RecordDO, String> tcGoodsSpec;
    @FXML
    private TableColumn<RecordDO, String> tcGoodsType;
    @FXML
    private RXTextField tfReceiptId;
    @FXML
    private RXTextField tfReceiptType;
    @FXML
    private Text txtEntryError;
    @FXML
    private Text txtOutError;
    @FXML
    private Text txtSuccess;
    @FXML
    private Text txtTableError;

    @FXML
    private void initialize() {
        tableModel = new VariableTableModel(table, service, 1);
        RecordDO rec = new RecordDO();
        table.getItems().add(rec);

        cbbOutWhs.setConverter(EntryRecordController.whsConverter);
        cbbEntryWhs.setConverter(EntryRecordController.whsConverter);

        cbbOutWhs.getItems().addAll(service.getWarehouseList());
        cbbEntryWhs.getItems().addAll(service.getWarehouseList());

        tcAlocQty.setCellValueFactory(new PropertyValueFactory<>("outQty"));
        tcGoodsId.setCellValueFactory(new PropertyValueFactory<>("goodsId"));
        tcGoodsName.setCellValueFactory(new PropertyValueFactory<>("goodsName"));
        tcGoodsType.setCellValueFactory(new PropertyValueFactory<>("goodsType"));
        tcGoodsSpec.setCellValueFactory(new PropertyValueFactory<>("goodsSpec"));

        tcGoodsId.setCellFactory(param -> tableModel.getCbbTableCell());
        tcAlocQty.setCellFactory(param -> tableModel.getTfTableCell());
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
     * 生成调拨单
     * @触发组件 btnCreateNewAlocRecord
     * @触发事件 鼠标点击
     */
    @FXML
    void createNewAlocRecord() {
        clearPrompt();

        if (!inputCheck()) {
            return;
        }

        service.addALocReceipt(generateReceipt());
        txtSuccess.setText("创建成功! 单号:" + receiptId);
        txtSuccess.setVisible(true);
        log.info("创建调拨单:" + receiptId);

        clearInput();
    }

    /** 输入检查 */
    private boolean inputCheck() {
        if (cbbOutWhs.getSelectionModel().isEmpty()) {
            txtOutError.setText("未选择调出仓库!");
            txtOutError.setVisible(true);
            return false;
        }
        if (cbbEntryWhs.getSelectionModel().isEmpty()) {
            txtEntryError.setText("未选择调入仓库!");
            txtEntryError.setVisible(true);
            return false;
        }
        if (table.getItems().isEmpty()) {
            txtTableError.setText("请至少添加一项出库货品!");
            txtTableError.setVisible(true);
            return false;
        }

        //判断输入的出库数量是否超过所选仓库中的库存量
        int whsId = cbbOutWhs.getSelectionModel().getSelectedItem().getWhsId();
        for (int i = 0; i < table.getItems().size(); i++) {
            RecordDO rec = table.getItems().get(i);
            if (rec.getOutQty() == null) {
                continue;
            }
            int qty = service.getGoodsQty(whsId, rec.getGoodsId());
            if (rec.getOutQty() > qty) {
                txtTableError.setText("'" + rec.getGoodsName() + "'库存数量不足!  当前库存量为:" + qty);
                txtTableError.setVisible(true);
                return false;
            }
        }

        return true;
    }

    /** 根据表格内容生成单据 */
    List<RecordDO> generateReceipt() {
        receiptId = RecIdUtil.getAllocationCode();
        List<RecordDO> recList = new ArrayList<>();

        for (int i = 0; i < table.getItems().size(); i++) {
            RecordDO rec = table.getItems().get(i);
            if (rec.getOutQty() == null) {
                continue;
            }

            rec.setReceiptId(receiptId);
            rec.setCompanyId(-1);
            rec.setEntryAmt(new BigDecimal(0));
            rec.setOutAmt(new BigDecimal(0));

            rec.setReceiptType(RECEIPT_TYPE);
            rec.setCompanyId(cbbEntryWhs.getSelectionModel().getSelectedItem().getWhsId());
            rec.setWhsId(cbbOutWhs.getSelectionModel().getSelectedItem().getWhsId());
            recList.add(rec);
        }

        return recList;
    }


    /** 清除提示框 */
    private void clearPrompt() {
        List.of(
                txtEntryError,
                txtOutError,
                txtTableError,
                txtSuccess
        ).forEach(txt -> txt.setVisible(false));
    }

    /** 清空输入 */
    private void clearInput() {
        cbbEntryWhs.getSelectionModel().clearSelection();
        cbbOutWhs.getSelectionModel().clearSelection();
        table.getItems().clear();
        table.getItems().add(new RecordDO());
        table.setPrefHeight(60);
        tableModel.updateTotalAmount();
    }
}
