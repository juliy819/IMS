package com.juliy.ims.controller;

import com.juliy.ims.entity.Warehouse;
import com.juliy.ims.service.AddNewService;
import com.juliy.ims.service.impl.AddNewServiceImpl;
import com.juliy.ims.utils.CommonUtil;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import java.util.Map;

import static com.juliy.ims.utils.CommonUtil.*;

/**
 * 添加仓库页面控制器
 * @author JuLiy
 * @date 2022/11/21 14:43
 */
public class AddWhsController {

    private static final Logger log = Logger.getLogger(AddWhsController.class);
    private final AddNewService service = new AddNewServiceImpl();
    private final StringProperty whsName = new SimpleStringProperty("");
    private final StringProperty add = new SimpleStringProperty("");
    private final StringProperty managerName = new SimpleStringProperty("");
    private final StringProperty phone = new SimpleStringProperty("");
    private final StringProperty comment = new SimpleStringProperty("");


    @FXML
    private RXTextField tfWhsName;
    @FXML
    private RXTextField tfAdd;
    @FXML
    private RXTextField tfManagerName;
    @FXML
    private RXTextField tfPhone;
    @FXML
    private TextArea taComment;
    @FXML
    private Text txtWhsNameError;
    @FXML
    private Text txtAddError;
    @FXML
    private Text txtManagerNameError;
    @FXML
    private Text txtPhoneError;
    @FXML
    private Text txtSuccess;

    @FXML
    private void initialize() {
        //绑定
        tfWhsName.textProperty().bindBidirectional(whsName);
        tfAdd.textProperty().bindBidirectional(add);
        tfManagerName.textProperty().bindBidirectional(managerName);
        tfPhone.textProperty().bindBidirectional(phone);
        taComment.textProperty().bindBidirectional(comment);

        Map.of(tfWhsName, txtWhsNameError,
               tfAdd, txtAddError,
               tfManagerName, txtManagerNameError,
               tfPhone, txtPhoneError)
                .forEach(CommonUtil::initAddTextField);
    }

    /**
     * 添加仓库
     * @触发组件 btnAddNewWarehouse
     * @触发事件 鼠标点击
     */
    @FXML
    void addNewWarehouse() {
        clearPrompt();

        if (!inputCheck()) {
            return;
        }

        service.addNew(createWarehouse());
        txtSuccess.setText("'" + whsName.get() + "' 添加成功！");
        txtSuccess.setVisible(true);
        log.info("添加仓库:'" + whsName.get() + "'");
    }

    /**
     * 输入检查
     * @return 输入正确返回true，输入有误返回false
     */
    private boolean inputCheck() {
        //检查输入是否为空
        if (isInputEmpty(whsName.get(), txtWhsNameError) ||
                isInputEmpty(add.get(), txtAddError) ||
                isInputEmpty(managerName.get(), txtManagerNameError) ||
                isInputEmpty(phone.get(), txtPhoneError)
        ) {
            return false;
        }

        //检查输入是否合法
        if (isLengthExceed(whsName.get(), 5, txtWhsNameError) ||
                isLengthExceed(add.get(), 32, txtAddError) ||
                isLengthExceed(managerName.get(), 4, txtManagerNameError) ||
                isFormatIllegal(phone.get(), "^1\\d{10}$", txtPhoneError)
        ) {
            return false;
        }

        //检查仓库名称是否已存在
        if (service.isNameExist(whsName.get(), "t_warehouse")) {
            txtWhsNameError.setText("仓库名称已存在！");
            txtWhsNameError.setVisible(true);
            return false;
        }

        return true;
    }

    /** 根据输入内容创建仓库对象 */
    private Warehouse createWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setWhsName(whsName.get());
        warehouse.setWhsAdd(add.get());
        warehouse.setManagerName(managerName.get());
        warehouse.setManagerPhone(phone.get());
        warehouse.setWhsComment(comment.get());

        return warehouse;
    }

    /** 清空提示消息 */
    private void clearPrompt() {
        txtWhsNameError.setVisible(false);
        txtAddError.setVisible(false);
        txtManagerNameError.setVisible(false);
        txtPhoneError.setVisible(false);
        txtSuccess.setVisible(false);
    }
}
