package test;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.skins.JFXComboBoxListViewSkin;
import com.juliy.ims.model.CcbBoxModel;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    public JFXListView<CcbBoxModel> listView;

    @FXML
    TitledPane titledPane;

    ObservableList<CcbBoxModel> obList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        obList.addAll(new CcbBoxModel("不选择"),
                      new CcbBoxModel("a"),
                      new CcbBoxModel("aa"),
                      new CcbBoxModel("aaa"),
                      new CcbBoxModel("a1"),
                      new CcbBoxModel("a12"),
                      new CcbBoxModel("a123"));

        listView.setItems(obList);

        listView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<CcbBoxModel> call(ListView<CcbBoxModel> param) {
                return new ListCell<>() {
                    private final JFXCheckBox cb = new JFXCheckBox();
                    private BooleanProperty booleanProperty;

                    {
                        cb.setOnAction(actionEvent -> {
                            getListView().getSelectionModel().select(getItem());
                            getListView().getSelectionModel().clearSelection();
                            String selected = param.getItems()
                                    .stream()
                                    .filter(CcbBoxModel::isSelected)
                                    .map(CcbBoxModel::getValue)
                                    .sorted()
                                    .map(i -> i + "")
                                    .collect(Collectors.joining(","));
                            titledPane.setText(selected);
                        });
                    }

                    @Override
                    protected void updateItem(CcbBoxModel item, boolean empty) {
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
            FilteredList<CcbBoxModel> newList = obList.filtered(s -> s.getValue().contains(newValue));
            listView.setItems(newList);
            listView.refresh();
        });


        JFXComboBox<CcbBoxModel> cbBox = new JFXComboBox<>() {
            @Override
            protected Skin<?> createDefaultSkin() {
                JFXComboBoxListViewSkin<CcbBoxModel> skin = new JFXComboBoxListViewSkin<>(this);
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
            public ListCell<CcbBoxModel> call(ListView<CcbBoxModel> checkCbBoxModelListView) {
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
                    protected void updateItem(CcbBoxModel ccbBoxModel, boolean empty) {
                        super.updateItem(ccbBoxModel, empty);
                        if (!empty) {
                            if (booleanProperty != null) {
                                cb.selectedProperty().unbindBidirectional(booleanProperty);
                            }
                            booleanProperty = ccbBoxModel.selectedProperty();
                            cb.selectedProperty().bindBidirectional(booleanProperty);
                            setGraphic(cb);
                            setText(ccbBoxModel.getValue() + "");
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
            protected void updateItem(CcbBoxModel ccbBoxModel, boolean empty) {
                super.updateItem(ccbBoxModel, empty);
                String selected = cbBox.getItems()
                        .stream()
                        .filter(CcbBoxModel::isSelected)
                        .map(CcbBoxModel::getValue).sorted()
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
            FilteredList<CcbBoxModel> newList = obList.filtered(s -> s.getValue().contains(newValue));
            cbBox.setItems(newList);
            cbBox.hide();
            cbBox.show();
        });
    }
}
