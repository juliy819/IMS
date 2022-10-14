package com.juliy.ims.controller;

import com.juliy.ims.model.CheckCbBoxModel;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;

import java.util.stream.Collectors;

/**
 * @author JuLiy
 * @date 2022/10/3 21:24
 */
public class TestController {

    @FXML
    public AnchorPane basePane;

    public ComboBox<CheckCbBoxModel> cbBox;
    @FXML
    public TextField field;

    @FXML
    public CheckComboBox<String> cCbBox;

    ObservableList<CheckCbBoxModel> obList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        obList.addAll(new CheckCbBoxModel("输入框"),
                      new CheckCbBoxModel("不选择"),
                      new CheckCbBoxModel("aa"),
                      new CheckCbBoxModel("bb"),
                      new CheckCbBoxModel("cc"));

        cbBox = new ComboBox<>() {
            @Override
            protected Skin<?> createDefaultSkin() {
                ComboBoxListViewSkin<CheckCbBoxModel> skin = new ComboBoxListViewSkin<>(this);
                skin.setHideOnClick(false);
                return skin;
            }
        };
        basePane.getChildren().add(cbBox);

        cbBox.setLayoutX(293);
        cbBox.setLayoutY(126);
        cbBox.setPrefWidth(150);
        cbBox.setItems(obList);
        cbBox.setPlaceholder(new Label("没找到"));

        cbBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<CheckCbBoxModel> call(ListView<CheckCbBoxModel> checkCbBoxModelListView) {
                return new ListCell<>() {
                    private CheckBox cb = new CheckBox();
                    private BooleanProperty booleanProperty;

                    {
                        cb.setOnAction(actionEvent -> {
                            getListView().getSelectionModel().select(getItem());
                            System.out.println(1);
                        });
                    }

                    @Override
                    protected void updateItem(CheckCbBoxModel checkCbBoxModel, boolean empty) {
                        super.updateItem(checkCbBoxModel, empty);
                        if (!empty) {
                            if (booleanProperty != null) {
                                cb.selectedProperty().unbindBidirectional(booleanProperty);
                            }
                            System.out.println(2);
                            booleanProperty = checkCbBoxModel.selectedProperty();
                            cb.selectedProperty().bindBidirectional(booleanProperty);
                            setGraphic(cb);
                            setText(checkCbBoxModel.getValue() + "");
                        } else {
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        cbBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(CheckCbBoxModel checkCbBoxModel, boolean empty) {
                super.updateItem(checkCbBoxModel, empty);
                String selected = cbBox.getItems()
                        .stream()
                        .filter(CheckCbBoxModel::isSelected)
                        .map(CheckCbBoxModel::getValue).sorted()
                        .map(i -> i + "")
                        .collect(Collectors.joining(","));
                setText(selected);
                System.out.println(3);
            }
        });

        field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                cbBox.setItems(null);
                return;
            }
            FilteredList<CheckCbBoxModel> newList = obList.filtered(s -> s.getValue().contains(newValue));
            if (newList.isEmpty()) {
                cbBox.setItems(null);
            } else {
                cbBox.setItems(newList);
                cbBox.hide();
                cbBox.show();
            }
        });
    }

    public void cbBox() {
        if (cbBox.getSelectionModel().getSelectedIndex() == 1) {
            //选中“不选择”时清空已选内容
            //若使用cbBox.getSelectionModel().clearSelection()方法会报越界异常，原因未知
            //因此只能先清空原有元素，再重新设置
//            cbBox.setItems(null);
//            cbBox.setItems(obList);
        }
    }
}
