package com.juliy.ims.controller;

import com.juliy.ims.entity.GoodsType;
import com.juliy.ims.service.AddNewService;
import com.juliy.ims.service.impl.AddNewServiceImpl;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import static com.juliy.ims.utils.CommonUtil.isInputEmpty;
import static com.juliy.ims.utils.CommonUtil.isLengthExceed;

/**
 * 添加货品类别页面控制器
 * @author JuLiy
 * @date 2022/11/21 14:43
 */
public class AddGoodsTypeController {

    private static final Logger log = Logger.getLogger(AddGoodsTypeController.class);
    private final AddNewService service = new AddNewServiceImpl();
    private final StringProperty typeName = new SimpleStringProperty("");
    @FXML
    private RXTextField tfGoodsTypeName;
    @FXML
    private Text txtTypeNameError;
    @FXML
    private Text txtSuccess;

    @FXML
    private void initialize() {
        tfGoodsTypeName.textProperty().bindBidirectional(typeName);
        tfGoodsTypeName.setOnClickButton(event -> tfGoodsTypeName.clear());
        tfGoodsTypeName.setOnMouseClicked(event -> clearPrompt());
    }

    /**
     * 添加物品类别
     * @触发组件 btnAddNewGoodsType
     * @触发事件 鼠标点击
     */
    @FXML
    void addNewGoodsType() {
        clearPrompt();

        if (!inputCheck()) {
            return;
        }

        service.addNew(createGoodsType());
        txtSuccess.setText("'" + typeName.get() + "' 添加成功！");
        txtSuccess.setVisible(true);
        log.info("添加物品类别:'" + typeName.get() + "'");
    }

    /** 根据输入内容创建货品类别对象 */
    private GoodsType createGoodsType() {
        GoodsType goodsType = new GoodsType();
        goodsType.setGoodsTypeName(typeName.get());
        return goodsType;
    }

    /**
     * 输入检查
     * @return 输入正确返回true，输入有误返回false
     */
    private boolean inputCheck() {
        //检查输入是否为空
        if (isInputEmpty(typeName.get(), txtTypeNameError)) {
            return false;
        }

        //检查输入是否合法
        if (isLengthExceed(typeName.get(), 8, txtTypeNameError)) {
            return false;
        }

        //检查仓库名称是否已存在
        if (service.isNameExist(typeName.get(), "t_goods_type")) {
            txtTypeNameError.setText("类别名称已存在！");
            txtTypeNameError.setVisible(true);
            return false;
        }

        return true;
    }

    /** 清空提示消息 */
    private void clearPrompt() {
        txtTypeNameError.setVisible(false);
        txtSuccess.setVisible(false);
    }
}
