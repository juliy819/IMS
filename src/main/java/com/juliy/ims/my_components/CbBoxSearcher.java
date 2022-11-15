package com.juliy.ims.my_components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.scene.control.TextField;

import java.util.concurrent.TimeUnit;

/** 内容变化时，在对应下拉框中进行模糊搜索 */
public class CbBoxSearcher implements ChangeListener<String> {
    MyComboBox cbBox;
    ObservableList<CcbBoxModel> list;
    TextField tf;
    ObservableList<CcbBoxModel> emptyList = FXCollections.observableArrayList(new CcbBoxModel("搜索中..."));

    boolean isSearching = false;

    public CbBoxSearcher(TextField tf, MyComboBox cbBox) {
        this.tf = tf;
        this.cbBox = cbBox;
        this.list = cbBox.getItems();
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        cbBox.setItems(emptyList);
        cbBox.show();

        if (!isSearching) {
            Task<String> task = new Task<>() {
                @Override
                protected String call() throws Exception {
                    isSearching = true;
                    TimeUnit.SECONDS.sleep(1);
                    isSearching = false;
                    return tf.getText();
                }
            };
            task.valueProperty().addListener((ob, ov, nv) -> {
                FilteredList<CcbBoxModel> newList = list.filtered(s -> s.getValue().contains(nv));
                cbBox.hide();
                cbBox.setVisibleRowCount(newList.size());
                cbBox.setItems(newList);
                cbBox.show();
            });
            Thread t = new Thread(task);
            t.start();
        }
    }
}
