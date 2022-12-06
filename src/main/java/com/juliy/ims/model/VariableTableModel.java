package com.juliy.ims.model;

import com.juliy.ims.entity.Goods;
import com.juliy.ims.entity.model.RecordDO;
import com.juliy.ims.service.CreateRecordService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

import java.math.BigDecimal;
import java.util.List;

/**
 * 可变长表格模型
 * @author JuLiy
 * @date 2022/12/4 14:37
 */
public class VariableTableModel {

    /** 0表示入库，1表示出库 */
    final int type;
    private final TableView<RecordDO> table;
    private final List<Integer> goodsIdList;
    private final CreateRecordService service;
    private final StringProperty totalAmount = new SimpleStringProperty("0");


    public VariableTableModel(TableView<RecordDO> table, CreateRecordService service, int type) {
        this.table = table;
        this.service = service;
        this.type = type;
        goodsIdList = service.getGoodsIdList();
    }

    /** 更新总价 */
    public void updateTotalAmount() {
        BigDecimal temp = new BigDecimal("0");
        for (int i = 0; i < table.getItems().size(); i++) {
            RecordDO rec = table.getItems().get(i);

            if (type == 0 && rec.getEntryQty() != null) {
                BigDecimal price = rec.getRefPurPrice();
                BigDecimal qty = new BigDecimal(rec.getEntryQty());
                temp = temp.add(price.multiply(qty));

            } else if (type == 1 && rec.getOutQty() != null) {
                BigDecimal price = rec.getRefSellPrice();
                BigDecimal qty = new BigDecimal(rec.getOutQty());
                temp = temp.add(price.multiply(qty));
            }

        }
        totalAmount.set(String.valueOf(temp));
    }

    public StringProperty totalAmountProperty() {
        return this.totalAmount;
    }

    public CbbTableCell getCbbTableCell() {
        return new CbbTableCell();
    }

    public TfTableCell getTfTableCell() {
        return new TfTableCell();
    }

    public DeleteTableCell getDeleteTableCell() {
        return new DeleteTableCell();
    }


    /** 带复选框的单元格 */
    class CbbTableCell extends TableCell<RecordDO, Integer> {
        final ComboBox<Integer> cbb = new ComboBox<>();
        final BorderPane pane = new BorderPane(cbb);

        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                this.setText(null);
                this.setGraphic(null);
            } else {
                cbb.setPrefWidth(200);
                cbb.getItems().addAll(goodsIdList);
                //每次刷新后选中先前修改的值
                if (table.getItems().get(this.getIndex()).getGoodsId() != null) {
                    cbb.getSelectionModel().select(table.getItems().get(this.getIndex()).getGoodsId());
                }
                //点击时选择对中对应表格行
                cbb.setOnMouseClicked(event -> table.getSelectionModel().select(this.getIndex()));
                //更新数据
                cbb.getSelectionModel().selectedItemProperty().addListener((ob, ov, nv) -> {
                    Goods goods = service.getGoods(nv);
                    table.getItems().get(this.getIndex()).setGoodsId(goods.getGoodsId());
                    table.getItems().get(this.getIndex()).setGoodsName(goods.getGoodsName());
                    table.getItems().get(this.getIndex()).setGoodsType(goods.getGoodsTypeName());
                    table.getItems().get(this.getIndex()).setGoodsSpec(goods.getGoodsSpec());
                    table.getItems().get(this.getIndex()).setRefPurPrice(goods.getRefPurPrice());
                    table.getItems().get(this.getIndex()).setRefPurPrice(goods.getRefPurPrice());
                    table.getItems().get(this.getIndex()).setRefSellPrice(goods.getRefSellPrice());

                    table.refresh();
                });
                this.setGraphic(pane);
            }
        }
    }

    /** 带输入框的单元格 */
    class TfTableCell extends TableCell<RecordDO, Integer> {
        final TextField tf = new TextField();
        final BorderPane pane = new BorderPane(tf);

        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                this.setText(null);
                this.setGraphic(null);
            } else {
                tf.setPrefWidth(200);
                //每次刷新后选中先前修改的值
                if (table.getItems().get(this.getIndex()).getEntryQty() != null) {
                    tf.setText(String.valueOf(table.getItems().get(this.getIndex()).getEntryQty()));
                }
                //点击时选择对中对应表格行
                tf.setOnMouseClicked(event -> table.getSelectionModel().select(this.getIndex()));

                tf.textProperty().addListener((ob, ov, nv) -> {
                    //清空输入框时，不进行其余操作
                    if ("".equals(nv)) {
                        return;
                    }
                    //若输入的不是数字或长度过长，即不更新输入框内容
                    if (!nv.matches("\\d*") || nv.length() > 8) {
                        tf.setText(ov);
                    }
                });

                tf.focusedProperty().addListener((ob, ov, nv) -> {
                    if (nv.equals(Boolean.FALSE) && tf.getText() != null && !"".equals(tf.getText()) && this.getIndex() != -1) {
                        calculateAmount();
                    }
                });

                tf.setOnAction(event -> calculateAmount());

                this.setGraphic(pane);
            }
        }

        /** 计算总价格 */
        private void calculateAmount() {
            BigDecimal purPrice = table.getItems().get(this.getIndex()).getRefPurPrice();
            BigDecimal sellPrice = table.getItems().get(this.getIndex()).getRefSellPrice();
            BigDecimal qty = new BigDecimal(tf.getText());
            table.getItems().get(this.getIndex()).setEntryQty(Integer.valueOf(tf.getText()));
            table.getItems().get(this.getIndex()).setOutQty(Integer.valueOf(tf.getText()));
            table.getItems().get(this.getIndex()).setEntryAmt(purPrice.multiply(qty));
            table.getItems().get(this.getIndex()).setOutAmt(sellPrice.multiply(qty));
            table.refresh();

            updateTotalAmount();
        }
    }

    /** 带删除按钮的单元格 */
    class DeleteTableCell extends TableCell<String, Integer> {
        final Label lb = new Label();
        final BorderPane pane = new BorderPane(lb);
        final Region region = new Region();
        final Button btn = new Button();

        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                this.setText(null);
                this.setGraphic(null);
            } else {
                lb.setText(String.valueOf(this.getIndex() + 1));

                btn.setPrefWidth(30);
                btn.setPrefHeight(30);
                btn.setCursor(Cursor.HAND);
                region.setMaxWidth(10);
                region.setMaxHeight(10);
                btn.setGraphic(region);
                btn.getStyleClass().addAll("btn-common", "btn-delete");

                btn.setOnAction(event -> {
                    table.getItems().remove(this.getIndex());
                    int size = table.getItems().size() % 2 == 0 ? 29 : 31;
                    table.setPrefHeight(table.getPrefHeight() - size);
                    table.refresh();

                    updateTotalAmount();
                });

                this.setGraphic(pane);
                this.setOnMouseEntered(event -> pane.setCenter(btn));
                this.setOnMouseExited(event -> pane.setCenter(lb));
            }
        }
    }

}
