package com.juliy.ims.controller;

import com.juliy.ims.entity.Goods;
import com.juliy.ims.entity.GoodsType;
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
import javafx.util.StringConverter;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Map;

import static com.juliy.ims.utils.CommonUtil.*;

/**
 * 添加货品页面控制器
 * @author JuLiy
 * @date 2022/11/21 14:43
 */
public class AddGoodsController {

    private static final Logger log = Logger.getLogger(AddGoodsController.class);
    private final AddNewService service = new AddNewServiceImpl();
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty spec = new SimpleStringProperty("");
    private final StringProperty unit = new SimpleStringProperty("");
    private final StringProperty purPrice = new SimpleStringProperty("");
    private final StringProperty sellPrice = new SimpleStringProperty("");
    private final StringProperty maxQty = new SimpleStringProperty("");
    private final StringProperty minQty = new SimpleStringProperty("");
    private final StringProperty comment = new SimpleStringProperty("");
    
    @FXML
    private ComboBox<GoodsType> cbbType;
    @FXML
    private TextArea taComment;
    @FXML
    private RXTextField tfMaxQty;
    @FXML
    private RXTextField tfMinQty;
    @FXML
    private RXTextField tfName;
    @FXML
    private RXTextField tfPurPrice;
    @FXML
    private RXTextField tfSellPrice;
    @FXML
    private RXTextField tfSpec;
    @FXML
    private RXTextField tfUnit;
    @FXML
    private Text txtMaxQtyError;
    @FXML
    private Text txtMinQtyError;
    @FXML
    private Text txtNameError;
    @FXML
    private Text txtPurPriceError;
    @FXML
    private Text txtSellPriceError;
    @FXML
    private Text txtSpecError;
    @FXML
    private Text txtSuccess;
    @FXML
    private Text txtTypeError;
    @FXML
    private Text txtUnitError;

    @FXML
    private void initialize() {
        //绑定
        tfName.textProperty().bindBidirectional(name);
        tfSpec.textProperty().bindBidirectional(spec);
        tfUnit.textProperty().bindBidirectional(unit);
        tfPurPrice.textProperty().bindBidirectional(purPrice);
        tfSellPrice.textProperty().bindBidirectional(sellPrice);
        tfMaxQty.textProperty().bindBidirectional(maxQty);
        tfMinQty.textProperty().bindBidirectional(minQty);
        taComment.textProperty().bindBidirectional(comment);

        Map.of(tfName, txtNameError,
               tfSpec, txtSpecError,
               tfUnit, txtUnitError,
               tfPurPrice, txtPurPriceError,
               tfSellPrice, txtSellPriceError,
               tfMaxQty, txtMaxQtyError,
               tfMinQty, txtMinQtyError)
                .forEach(CommonUtil::initAddTextField);

        //将GoodsType对象转换为String进行显示
        cbbType.setConverter(new StringConverter<>() {
            @Override
            public String toString(GoodsType object) {
                if (object == null) {
                    return null;
                }
                return object.getGoodsTypeId() + "-" + object.getGoodsTypeName();
            }

            @Override
            public GoodsType fromString(String string) {
                return null;
            }
        });
        //获取货品类别数据
        cbbType.getItems().addAll(service.getGoodsTypeList());
    }

    /**
     * 添加货品
     * @触发组件 btnAddNewGoods
     * @触发事件 鼠标点击
     */
    @FXML
    void addNewGoods() {
        clearPrompt();

        if (!inputCheck()) {
            return;
        }

        service.addNew(createGoods());
        txtSuccess.setText("'" + name.get() + "' 添加成功！");
        txtSuccess.setVisible(true);
        log.info("添加货品:'" + name.get() + "'");
    }

    /**
     * 输入检查
     * @return 输入正确返回true，输入有误返回false
     */
    private boolean inputCheck() {

        //检查复选框是否为空
        if (cbbType.getSelectionModel().isEmpty()) {
            txtTypeError.setText("货品类别不能为空！");
            txtNameError.setVisible(true);
            return false;
        }

        //检查输入是否为空
        if (isInputEmpty(name.get(), txtNameError) ||
                isInputEmpty(spec.get(), txtSpecError) ||
                isInputEmpty(unit.get(), txtUnitError) ||
                isInputEmpty(purPrice.get(), txtPurPriceError) ||
                isInputEmpty(sellPrice.get(), txtSellPriceError) ||
                isInputEmpty(maxQty.get(), txtMaxQtyError) ||
                isInputEmpty(minQty.get(), txtMinQtyError)
        ) {
            return false;
        }

        //检查输入是否合法
        if (isLengthExceed(name.get(), 32, txtNameError) ||
                isLengthExceed(spec.get(), 127, txtSpecError) ||
                isLengthExceed(unit.get(), 2, txtSpecError) ||
                isFormatIllegal(purPrice.get(), "^\\d{1,10}\\.\\d$", txtPurPriceError) ||
                isFormatIllegal(sellPrice.get(), "^\\d{1,10}\\.\\d$", txtSellPriceError) ||
                isFormatIllegal(maxQty.get(), "^\\d{1,9}$", txtMaxQtyError) ||
                isFormatIllegal(minQty.get(), "^\\d{1,9}$", txtMinQtyError)) {
            return false;
        }

        //检查货品名称是否已存在
        if (service.isNameExist(name.get(), "t_goods")) {
            txtNameError.setText("货品名称已存在！");
            txtNameError.setVisible(true);
            return false;
        }

        return true;
    }


    /** 根据输入内容创建货品对象 */
    private Goods createGoods() {
        Goods goods = new Goods();
        goods.setGoodsTypeId(cbbType.getSelectionModel().getSelectedItem().getGoodsTypeId());
        goods.setGoodsName(name.get());
        goods.setGoodsSpec(spec.get());
        goods.setGoodsUnit(unit.get());
        goods.setRefPurPrice(new BigDecimal(purPrice.get()));
        goods.setRefSellPrice(new BigDecimal(sellPrice.get()));
        goods.setMaxQty(Integer.parseInt(maxQty.get()));
        goods.setMinQty(Integer.parseInt(minQty.get()));
        goods.setGoodsComment(comment.get());

        return goods;
    }

    /** 清空提示消息 */
    private void clearPrompt() {
        txtTypeError.setVisible(false);
        txtNameError.setVisible(false);
        txtSpecError.setVisible(false);
        txtUnitError.setVisible(false);
        txtPurPriceError.setVisible(false);
        txtSellPriceError.setVisible(false);
        txtMaxQtyError.setVisible(false);
        txtMinQtyError.setVisible(false);
        txtSuccess.setVisible(false);
    }

}
