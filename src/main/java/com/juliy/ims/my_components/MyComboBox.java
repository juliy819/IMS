package com.juliy.ims.my_components;

import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.stream.Collectors;

/**
 * 自定义下拉框
 * @author JuLiy
 * @date 2022/11/8 22:06
 */
public class MyComboBox extends ComboBox<CcbBoxModel> {

    /** 原数据集合，用于buttonCell查询选择状态显示 */
    private final ObservableList<CcbBoxModel> list;

    public MyComboBox() {
        list = getItems();

        AnchorPane.setLeftAnchor(this, 10.0);
        AnchorPane.setRightAnchor(this, 10.0);
        AnchorPane.setBottomAnchor(this, 10.0);
        this.setCursor(Cursor.HAND);
        this.setPlaceholder(new Text("未找到"));
        //设置自定义cell
        this.setCellFactory(param -> new MyListCell());

        this.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(CcbBoxModel ccbBoxModel, boolean empty) {
                super.updateItem(ccbBoxModel, empty);
                String selected = list.stream()
                        .filter(CcbBoxModel::isSelected)
                        .map(CcbBoxModel::getValue)
                        .collect(Collectors.joining(","));
                setText(selected);
            }
        });

        Tooltip tip = new Tooltip();
        tip.setWrapText(true);
        tip.textProperty().bind(this.getButtonCell().textProperty());
        this.setTooltip(tip);
    }

    //实现点击内部区域后不会隐藏显示
    @Override
    protected Skin<?> createDefaultSkin() {
        ComboBoxListViewSkin<CcbBoxModel> skin = new ComboBoxListViewSkin<>(this);
        skin.setHideOnClick(false);
        return skin;
    }

    static class MyListCell extends ListCell<CcbBoxModel> {

        private final BorderPane cbPane = new BorderPane();

        private final BorderPane noCbPane = new BorderPane(new Label("搜索中..."));

        private final CheckBox checkBox = new CheckBox();

        private final Label cbLabel = new Label();

        private BooleanProperty booleanProperty;


        public MyListCell() {
            cbPane.setLeft(checkBox);
            cbPane.setCenter(cbLabel);

            //点击复选框也要触发cell的选择事件，不然不会更新buttonCell的内容
            checkBox.setOnAction(event -> {
                getListView().getSelectionModel().select(getIndex());
                getListView().getSelectionModel().clearSelection();
            });
            //点击整个cell区域都会触发复选框的选择事件
            this.setOnMouseClicked(event -> {
                checkBox.setSelected(!checkBox.isSelected());
                getListView().getSelectionModel().clearSelection();
            });

            Tooltip tip = new Tooltip();
            tip.textProperty().bind(cbLabel.textProperty());
            this.setTooltip(tip);

            this.setCursor(Cursor.HAND);
        }

        @Override
        public void updateItem(CcbBoxModel item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if ("搜索中...".equals(item.getValue())) {
                    setGraphic(noCbPane);
                    return;
                }
                if (booleanProperty != null) {
                    checkBox.selectedProperty().unbindBidirectional(booleanProperty);
                }
                booleanProperty = item.selectedProperty();
                checkBox.selectedProperty().bindBidirectional(booleanProperty);
                cbLabel.setText(item.getValue());
                setGraphic(cbPane);
            } else {
                setGraphic(null);
                setText(null);
            }
        }
    }

}
