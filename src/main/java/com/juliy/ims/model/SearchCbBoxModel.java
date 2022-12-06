package com.juliy.ims.model;

import com.juliy.ims.my_components.MyComboBox;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.concurrent.TimeUnit;

/**
 * 带搜索功能下拉框模型
 * @author JuLiy
 * @date 2022/11/19 12:59
 */
public class SearchCbBoxModel {
    private final RXTextField textField;
    private final MyComboBox comboBox;
    private final ObservableList<CheckCbbUnitModel> list;
    private final ObservableList<CheckCbbUnitModel> emptyList = FXCollections.observableArrayList(new CheckCbbUnitModel("搜索中..."));
    private boolean isSearching = false;

    public SearchCbBoxModel(RXTextField textField, MyComboBox comboBox) {
        this.textField = textField;
        this.comboBox = comboBox;
        list = comboBox.getItems();
        init();
    }

    private void init() {
        textField.setOnClickButton(event -> textField.clear());
        //输入框内容变化时在对应下拉框中进行模糊搜索
        textField.textProperty().addListener(new SearchListener());
    }

    /** 设置下拉框列表收起后的操作事件 */
    public void setOnCbBoxHidden(EventHandler<Event> eventHandler) {
        comboBox.setOnHidden(eventHandler);
    }

    /** 获取首单元格的的内容 */
    public String getButtonCellText() {
        return comboBox.getButtonCell().getText();
    }

    private class SearchListener implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            //输入框变化时，先显示搜索中
            comboBox.setItems(emptyList);
            comboBox.show();

            //若在持续的输入内容，则每隔1秒进行一次搜索，以免输一个字符便搜索一次
            //这里设计的不太行，期望应该是输入间隔大于1s时再进行搜索，但在多线程和javafx多任务这块学的不好
            if (!isSearching) {
                Task<String> task = new Task<>() {
                    @Override
                    protected String call() throws Exception {
                        isSearching = true;
                        TimeUnit.SECONDS.sleep(1);
                        isSearching = false;
                        return textField.getText();
                    }
                };
                task.valueProperty().addListener((ob, ov, nv) -> {
                    FilteredList<CheckCbbUnitModel> newList = list.filtered(s -> s.getValue().contains(nv));
                    comboBox.hide();
                    comboBox.setVisibleRowCount(Math.min(newList.size(), 10));
                    comboBox.setItems(newList);
                    comboBox.show();
                });
                Thread t = new Thread(task);
                t.start();
            }
        }
    }
}
