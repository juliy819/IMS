package com.juliy.ims.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
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

import java.util.stream.Collectors;

/**
 * @author JuLiy
 * @date 2022/10/3 21:24
 */
public class TestController {

    @FXML
    public AnchorPane basePane;

    @FXML
    public TextField field;

    @FXML
    public TextField listViewSearch;

    @FXML
    public JFXListView<CheckCbBoxModel> listView;

    @FXML
    TitledPane titledPane;

    ObservableList<CheckCbBoxModel> obList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        obList.addAll(new CheckCbBoxModel("不选择"),
                      new CheckCbBoxModel("a"),
                      new CheckCbBoxModel("aa"),
                      new CheckCbBoxModel("aaa"),
                      new CheckCbBoxModel("a1"),
                      new CheckCbBoxModel("a12"),
                      new CheckCbBoxModel("a123"));

        listView.setItems(obList);

        listView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<CheckCbBoxModel> call(ListView<CheckCbBoxModel> param) {
                return new ListCell<>() {
                    private final JFXCheckBox cb = new JFXCheckBox();
                    private BooleanProperty booleanProperty;

                    {
                        cb.setOnAction(actionEvent -> {
                            getListView().getSelectionModel().select(getItem());
                            getListView().getSelectionModel().clearSelection();
                            String selected = param.getItems()
                                    .stream()
                                    .filter(CheckCbBoxModel::isSelected)
                                    .map(CheckCbBoxModel::getValue)
                                    .sorted()
                                    .map(i -> i + "")
                                    .collect(Collectors.joining(","));
                            titledPane.setText(selected);
                        });
                    }

                    @Override
                    protected void updateItem(CheckCbBoxModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {

                            if (booleanProperty != null) {
                                cb.selectedProperty().unbindBidirectional(booleanProperty);
                            }
                            booleanProperty = item.selectedProperty();
                            cb.selectedProperty().bindBidirectional(booleanProperty);
                            setGraphic(cb);
                            setText(item.getValue() + "");
                        } else {
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        listViewSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                listView.setItems(null);
                return;
            }
            FilteredList<CheckCbBoxModel> newList = obList.filtered(s -> s.getValue().contains(newValue));
            listView.setItems(newList);
            listView.refresh();
        });


        JFXComboBox<CheckCbBoxModel> cbBox = new JFXComboBox<>() {
            @Override
            protected Skin<?> createDefaultSkin() {
                ComboBoxListViewSkin<CheckCbBoxModel> skin = new ComboBoxListViewSkin<>(this);
                skin.setHideOnClick(false);
                return skin;
            }
        };
        cbBox.setLabelFloat(true);
        cbBox.setPromptText("test");
        basePane.getChildren().add(cbBox);

        cbBox.setLayoutX(293);
        cbBox.setLayoutY(150);
        cbBox.setPrefWidth(150);
        cbBox.setItems(obList);
        cbBox.setPlaceholder(new Label("没找到"));

        cbBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<CheckCbBoxModel> call(ListView<CheckCbBoxModel> checkCbBoxModelListView) {
                return new ListCell<>() {
                    private final CheckBox cb = new CheckBox();
                    private BooleanProperty booleanProperty;

                    {
                        cb.setOnAction(actionEvent -> {
                            getListView().getSelectionModel().select(getItem());
                            getListView().getSelectionModel().clearSelection();
                        });
                    }

                    @Override
                    protected void updateItem(CheckCbBoxModel checkCbBoxModel, boolean empty) {
                        super.updateItem(checkCbBoxModel, empty);
                        if (!empty) {
                            if (booleanProperty != null) {
                                cb.selectedProperty().unbindBidirectional(booleanProperty);
                            }
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
            }
        });

        field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                cbBox.setItems(null);
                return;
            }
            System.out.println("'" + newValue + "'");
            FilteredList<CheckCbBoxModel> newList = obList.filtered(s -> s.getValue().contains(newValue));
            cbBox.setItems(newList);
            cbBox.hide();
            cbBox.show();
        });
    }
}
