package com.juliy.ims.controller;

import com.juliy.ims.entity.Company;
import com.juliy.ims.service.AddNewService;
import com.juliy.ims.service.impl.AddNewServiceImpl;
import com.juliy.ims.utils.CommonUtil;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

import static com.juliy.ims.utils.CommonUtil.*;

/**
 * 添加供应商页面控制器
 * @author JuLiy
 * @date 2022/11/21 14:44
 */
public class AddCompanyController {

    private static final Logger log = Logger.getLogger(AddCompanyController.class);
    private final AddNewService service = new AddNewServiceImpl();
    private final StringProperty companyName = new SimpleStringProperty("");
    private final StringProperty add = new SimpleStringProperty("");
    private final StringProperty contactName = new SimpleStringProperty("");
    private final StringProperty phone = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty post = new SimpleStringProperty("");
    private final StringProperty bankName = new SimpleStringProperty("");
    private final StringProperty bankAcct = new SimpleStringProperty("");
    private final StringProperty taxId = new SimpleStringProperty("");
    private final StringProperty comment = new SimpleStringProperty("");

    @FXML
    private ComboBox<String> cbbType;
    @FXML
    private TextArea taComment;
    @FXML
    private RXTextField tfAdd;
    @FXML
    private RXTextField tfBankAcct;
    @FXML
    private RXTextField tfBankName;
    @FXML
    private RXTextField tfContactName;
    @FXML
    private RXTextField tfEmail;
    @FXML
    private RXTextField tfPhone;
    @FXML
    private RXTextField tfPost;
    @FXML
    private RXTextField tfCompanyName;
    @FXML
    private RXTextField tfTaxId;
    @FXML
    private Text txtAddError;
    @FXML
    private Text txtBankAcctError;
    @FXML
    private Text txtBankNameError;
    @FXML
    private Text txtContactNameError;
    @FXML
    private Text txtEmailError;
    @FXML
    private Text txtPhoneError;
    @FXML
    private Text txtPostError;
    @FXML
    private Text txtSuccess;
    @FXML
    private Text txtCompanyNameError;
    @FXML
    private Text txtTaxIdError;
    @FXML
    private Text txtTypeError;

    @FXML
    private void initialize() {
        //绑定
        Map.of(tfCompanyName, companyName,
               tfAdd, add,
               tfContactName, contactName,
               tfPhone, phone,
               tfEmail, email,
               tfPost, post,
               tfBankName, bankName,
               tfBankAcct, bankAcct,
               tfTaxId, taxId,
               taComment, comment
        ).forEach((textInput, stringProperty) ->
                          textInput.textProperty().bindBidirectional(stringProperty));

        //设置输入框
        Map.of(tfCompanyName, txtCompanyNameError,
               tfContactName, txtContactNameError,
               tfPhone, txtPhoneError
        ).forEach(CommonUtil::initEmptyPromptTextField);

        List.of(tfAdd, tfEmail, tfPost, tfBankName, tfBankAcct, tfTaxId)
                .forEach(tf -> tf.setOnClickButton(event -> tf.clear()));

        cbbType.getItems().addAll("供应商", "客户");
    }

    /**
     * 添加新公司
     */
    @FXML
    void addNewCompany() {
        clearPrompt();

        if (!inputCheck()) {
            return;
        }

        service.addNew(createCompany());
        txtSuccess.setText("'" + companyName.get() + "' 添加成功！");
        txtSuccess.setVisible(true);
        log.info("添加" + cbbType.getSelectionModel().getSelectedItem() + ":'" + companyName.get() + "'");
    }

    /**
     * 输入检查
     * @return 输入正确返回true，输入有误返回false
     */
    private boolean inputCheck() {
        if (cbbType.getSelectionModel().isEmpty()) {
            txtTypeError.setText("请选择公司类别！");
            txtTypeError.setVisible(true);
            return false;
        }

        //检查输入是否为空
        if (isInputEmpty(companyName.get(), txtCompanyNameError) ||
                isInputEmpty(contactName.get(), txtContactNameError) ||
                isInputEmpty(phone.get(), txtPhoneError)) {
            return false;
        }

        //检查输入是否合法
        if (isLengthExceed(companyName.get(), 32, txtCompanyNameError) ||
                isLengthExceed(add.get(), 32, txtAddError) ||
                isLengthExceed(contactName.get(), 4, txtContactNameError) ||
                isLengthExceed(phone.get(), 16, txtPhoneError) ||
                isLengthExceed(email.get(), 32, txtEmailError) ||
                isLengthExceed(post.get(), 16, txtPostError) ||
                isLengthExceed(bankName.get(), 16, txtBankNameError) ||
                isLengthExceed(bankAcct.get(), 20, txtBankAcctError) ||
                (!"".equals(email.get()) && isFormatIllegal(email.get(), "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", txtEmailError)) ||
                (!"".equals(bankAcct.get()) && isFormatIllegal(bankAcct.get(), "^\\d{20}$", txtBankAcctError)) ||
                (!"".equals(taxId.get()) && isFormatIllegal(taxId.get(), "^\\d{20}$", txtTaxIdError))) {
            log.info(123);
            return false;
        }

        //检查公司名称是否已存在
        if (service.isNameExist(companyName.get(), "t_company")) {
            txtCompanyNameError.setText("公司名称已存在！");
            txtCompanyNameError.setVisible(true);
            return false;
        }

        return true;
    }

    /** 根据输入内容创建对象 */
    private Company createCompany() {
        Company company = new Company();
        company.setCompanyType(cbbType.getSelectionModel().getSelectedItem());
        company.setCompanyName(companyName.get());
        company.setCompanyAdd(add.get());
        company.setContactName(contactName.get());
        company.setContactPhone(phone.get());
        company.setContactEmail(email.get());
        company.setContactPost(post.get());
        company.setBankName(bankName.get());
        company.setBankAcct(bankAcct.get());
        company.setTaxId(taxId.get());
        company.setCompanyComment(comment.get());

        return company;
    }

    /** 清空提示消息 */
    private void clearPrompt() {
        List.of(txtTypeError,
                txtCompanyNameError,
                txtAddError,
                txtContactNameError,
                txtPhoneError,
                txtEmailError,
                txtPostError,
                txtBankNameError,
                txtBankAcctError,
                txtTaxIdError,
                txtSuccess
        ).forEach(text -> text.setVisible(false));
    }
}
