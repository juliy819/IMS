package com.juliy.ims.my_components;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;

/**
 * 自定义表格cell
 * @author JuLiy
 * @date 2022/11/9 22:48
 */
public class MyTableCell<S, T> extends TableCell<S, T> {
    private final BorderPane pane = new BorderPane();

    private final Label label = new Label();

    public MyTableCell() {
        pane.setCenter(label);

        Tooltip tip = new Tooltip();
        tip.textProperty().bind(label.textProperty());
        this.setTooltip(tip);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            label.setText(item.toString());
            setGraphic(pane);
        } else {
            setGraphic(null);
            setText(null);
        }
    }
}
