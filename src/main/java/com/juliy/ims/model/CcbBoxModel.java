package com.juliy.ims.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 下拉列表框中的复选框单元格模型
 * @author JuLiy
 * @date 2022/10/11 8:51
 */
public class CcbBoxModel {
    private final StringProperty value = new SimpleStringProperty();
    private final BooleanProperty selected = new SimpleBooleanProperty();

    public CcbBoxModel(String value) {
        setValue(value);
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public StringProperty valueProperty() {
        return value;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}
