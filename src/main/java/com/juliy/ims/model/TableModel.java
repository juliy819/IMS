package com.juliy.ims.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

/**
 * 翻页表格模型
 * @author JuLiy
 * @date 2022/11/19 11:56
 */
public class TableModel<T> {
    private final TableView<T> table;
    private final ComboBox<String> cbbPageSize;
    private final StringProperty curPage = new SimpleStringProperty("1");
    private final StringProperty totalPage = new SimpleStringProperty("1");
    private final IntegerProperty pageSize = new SimpleIntegerProperty(20);
    private final ChangeListener<String> curPageLimit = (ob, ov, nv) -> {
        //清空输入框时，不进行其余操作
        if ("".equals(nv)) {
            return;
        }
        //若输入的不是数字，将值置为oldValue，即不更新输入框内容
        if (!nv.matches("\\d*")) {
            setCurPage(Integer.parseInt(ov));
            return;
        }
        int target = Integer.parseInt(nv);
        int total = getTotalPage();
        if (target < 1 || target > total) {
            setCurPage(Integer.parseInt(ov));
        }
    };

    /**
     * @param table       表格
     * @param cbbPageSize 行数选择下拉框
     */
    public TableModel(TableView<T> table, ComboBox<String> cbbPageSize) {
        this.table = table;
        this.cbbPageSize = cbbPageSize;
        initCbbPageSize();
    }

    /** 初始化行数下拉框 */
    private void initCbbPageSize() {
        cbbPageSize.getItems().addAll("20条/页", "50条/页", "100条/页");
        cbbPageSize.getSelectionModel().select(0);
        cbbPageSize.getSelectionModel()
                .selectedItemProperty()
                .addListener((ob, ov, nv) -> {
                    setCurPage(1);
                    setPageSize(Integer.parseInt(nv.split("条")[0]));
                });
    }

    /** 跳转至表格指定页数 */
    public void jumpPage() {
        if ("".equals(curPage.get())) {
            setCurPage(1);
        }
    }

    /** 跳转至表格上一页 */
    public void toLastPage() {
        //当前为第一页则无法跳转至上一页
        if (Integer.parseInt(curPage.get()) == 1) {
            return;
        }
        setCurPage(getCurPage() - 1);
    }

    /** 跳转至表格下一页 */
    public void toNextPage() {
        //当前页为最后一页则无法跳转至下一页
        if (curPage.get().equals(totalPage.get())) {
            return;
        }
        setCurPage(getCurPage() + 1);
    }

    /**
     * 更新表格页面
     * @param itemList 要显示的列表
     */
    public void updateTableView(ObservableList<T> itemList) {
        table.setItems(itemList);
        table.refresh();
    }

    /**
     * 更新表格总页数
     * @param totalCount 表格记录总数量，为便于本类无参调用，设计为可变参数，外部调用传一个参数即可
     */
    public void updateTotalPage(int... totalCount) {
        int size = pageSize.get();
        int pageNum = (int) Math.ceil((double) totalCount[0] / size);
        totalPage.set(String.valueOf(pageNum));
    }

    public int getCurPage() {
        return Integer.parseInt(curPage.get());
    }

    public void setCurPage(int curPage) {
        this.curPage.set(String.valueOf(curPage));
    }

    public StringProperty curPageProperty() {
        return curPage;
    }

    public int getTotalPage() {
        return Integer.parseInt(totalPage.get());
    }

    public StringProperty totalPageProperty() {
        return totalPage;
    }

    public int getPageSize() {
        return pageSize.get();
    }

    public void setPageSize(int pageSize) {
        this.pageSize.set(pageSize);
    }

    public IntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public ChangeListener<String> getCurPageLimit() {
        return curPageLimit;
    }
}
