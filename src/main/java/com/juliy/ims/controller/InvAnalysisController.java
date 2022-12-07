package com.juliy.ims.controller;

import com.juliy.ims.entity.Goods;
import com.juliy.ims.entity.table_unit.InvAnalysisDO;
import com.juliy.ims.model.CheckCbbUnitModel;
import com.juliy.ims.model.PageTableModel;
import com.juliy.ims.model.SearchCbBoxModel;
import com.juliy.ims.my_components.MyComboBox;
import com.juliy.ims.my_components.MyTableCell;
import com.juliy.ims.service.AnalysisService;
import com.juliy.ims.service.impl.AnalysisServiceImpl;
import com.leewyatt.rxcontrols.controls.RXTextField;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 库存分析页面控制器
 * @author JuLiy
 * @date 2022/12/6 20:27
 */
public class InvAnalysisController {

    private final Logger log = Logger.getLogger(InvAnalysisController.class);
    private final AnalysisService service = new AnalysisServiceImpl();
    private final MyComboBox cbbGoodsId = new MyComboBox();
    private final MyComboBox cbbGoodsType = new MyComboBox();
    private final MyComboBox cbbGoodsName = new MyComboBox();
    private final MyComboBox cbbGoodsSpec = new MyComboBox();
    private PageTableModel<InvAnalysisDO> overTableModel;
    private PageTableModel<InvAnalysisDO> underTableModel;
    private SearchCbBoxModel goodsIdModel;
    private SearchCbBoxModel goodsTypeModel;
    private SearchCbBoxModel goodsNameModel;
    private SearchCbBoxModel goodsSpecModel;
    private ExecutorService threadPool;

    @FXML
    private ComboBox<String> cbbOverPageSize;
    @FXML
    private ComboBox<String> cbbUnderPageSize;
    @FXML
    private AnchorPane paneContent;
    @FXML
    private StackPane paneFull;
    @FXML
    private AnchorPane paneGoodsId;
    @FXML
    private AnchorPane paneGoodsName;
    @FXML
    private AnchorPane paneGoodsSpec;
    @FXML
    private AnchorPane paneGoodsType;
    @FXML
    private TableView<InvAnalysisDO> tableOver;
    @FXML
    private TableView<InvAnalysisDO> tableUnder;
    @FXML
    private TableColumn<InvAnalysisDO, Integer> tcMaxQty;
    @FXML
    private TableColumn<InvAnalysisDO, Integer> tcMinQty;
    @FXML
    private TableColumn<InvAnalysisDO, Integer> tcOverCurQty;
    @FXML
    private TableColumn<InvAnalysisDO, Integer> tcOverId;
    @FXML
    private TableColumn<InvAnalysisDO, String> tcOverName;
    @FXML
    private TableColumn<InvAnalysisDO, Integer> tcOverQty;
    @FXML
    private TableColumn<InvAnalysisDO, String> tcOverSpec;
    @FXML
    private TableColumn<InvAnalysisDO, String> tcOverType;
    @FXML
    private TableColumn<InvAnalysisDO, String> tcOverUnit;
    @FXML
    private TableColumn<InvAnalysisDO, Integer> tcUnderCurQty;
    @FXML
    private TableColumn<InvAnalysisDO, Integer> tcUnderId;
    @FXML
    private TableColumn<InvAnalysisDO, String> tcUnderName;
    @FXML
    private TableColumn<InvAnalysisDO, Integer> tcUnderQty;
    @FXML
    private TableColumn<InvAnalysisDO, String> tcUnderSpec;
    @FXML
    private TableColumn<InvAnalysisDO, String> tcUnderType;
    @FXML
    private TableColumn<InvAnalysisDO, String> tcUnderUnit;
    @FXML
    private RXTextField tfGoodsId;
    @FXML
    private RXTextField tfGoodsName;
    @FXML
    private RXTextField tfGoodsSpec;
    @FXML
    private RXTextField tfGoodsType;
    @FXML
    private TextField tfOverCurPage;
    @FXML
    private TextField tfUnderCurPage;
    @FXML
    private Text txtOverTotalPage;
    @FXML
    private Text txtUnderTotalPage;

    @FXML
    private void initialize() {
        initThreadPool();
        initModel();
        initComponents();
        initData();

        log.info("库存预警分析页面加载完成");
    }

    /** 初始化线程池 */
    private void initThreadPool() {
        ThreadFactory threadFactory = Thread::new;
        threadPool = new ThreadPoolExecutor(1, 1,
                                            0L, TimeUnit.MICROSECONDS,
                                            new LinkedBlockingQueue<>(1024), threadFactory);
    }

    /** 初始化model，并将model的属性与组件进行绑定 */
    private void initModel() {
        overTableModel = new PageTableModel<>(tableOver, cbbOverPageSize);
        underTableModel = new PageTableModel<>(tableUnder, cbbUnderPageSize);

        tfOverCurPage.textProperty().bindBidirectional(overTableModel.curPageProperty());
        tfUnderCurPage.textProperty().bindBidirectional(underTableModel.curPageProperty());
        txtOverTotalPage.textProperty().bind(overTableModel.totalPageProperty());
        txtUnderTotalPage.textProperty().bind(underTableModel.totalPageProperty());

        goodsIdModel = new SearchCbBoxModel(tfGoodsId, cbbGoodsId);
        goodsTypeModel = new SearchCbBoxModel(tfGoodsType, cbbGoodsType);
        goodsNameModel = new SearchCbBoxModel(tfGoodsName, cbbGoodsName);
        goodsSpecModel = new SearchCbBoxModel(tfGoodsSpec, cbbGoodsSpec);
    }

    /** 初始化组件 */
    private void initComponents() {
        addCustomComponents();
        initTableColumn();
        //收起下拉框后刷新
        goodsIdModel.setOnCbBoxHidden(event -> filter());
        goodsTypeModel.setOnCbBoxHidden(event -> filter());
        goodsNameModel.setOnCbBoxHidden(event -> filter());
        goodsSpecModel.setOnCbBoxHidden(event -> filter());
        //设置输入框限制
        tfOverCurPage.textProperty().addListener(overTableModel.getCurPageLimit());
        tfUnderCurPage.textProperty().addListener(underTableModel.getCurPageLimit());
        //更改表格条数后刷新
        overTableModel.pageSizeProperty().addListener((ob, ov, nv) -> filter());
        underTableModel.pageSizeProperty().addListener((ob, ov, nv) -> filter());
    }

    /** 添加自定义组件 */
    private void addCustomComponents() {
        paneGoodsId.getChildren().add(cbbGoodsId);
        paneGoodsType.getChildren().add(cbbGoodsType);
        paneGoodsName.getChildren().add(cbbGoodsName);
        paneGoodsSpec.getChildren().add(cbbGoodsSpec);
    }

    /** 初始化表格列 */
    private void initTableColumn() {
        Map.of(tcOverId, "id",
               tcOverType, "type",
               tcOverName, "name",
               tcOverSpec, "spec",
               tcOverUnit, "unit",
               tcOverCurQty, "curQty",
               tcMaxQty, "maxQty",
               tcOverQty, "overQty"
        ).forEach((tc, value) -> {
            tc.setCellValueFactory(new PropertyValueFactory<>(value));
            tc.setCellFactory(param -> new MyTableCell<>());
        });
        Map.of(tcUnderId, "id",
               tcUnderType, "type",
               tcUnderName, "name",
               tcUnderSpec, "spec",
               tcUnderUnit, "unit",
               tcUnderCurQty, "curQty",
               tcMinQty, "minQty",
               tcUnderQty, "underQty"
        ).forEach((tc, value) -> {
            tc.setCellValueFactory(new PropertyValueFactory<>(value));
            tc.setCellFactory(param -> new MyTableCell<>());
        });
    }

    /** 加载数据 */
    private void initData() {
        filter();

        List<Goods> invList = service.getAllGoods();

        cbbGoodsId.getItems().addAll(invList.stream().map(Goods::getGoodsId).map(String::valueOf).map(CheckCbbUnitModel::new).toList());
        cbbGoodsType.getItems().addAll(invList.stream().map(Goods::getGoodsTypeName).distinct().map(CheckCbbUnitModel::new).toList());
        cbbGoodsName.getItems().addAll(invList.stream().map(Goods::getGoodsName).map(CheckCbbUnitModel::new).toList());
        cbbGoodsSpec.getItems().addAll(invList.stream().map(Goods::getGoodsSpec).distinct().map(CheckCbbUnitModel::new).toList());
    }


    /**
     * 表格跳转至指定页
     * @触发组件 tfPageJump
     * @触发事件 回车
     */
    @FXML
    void jumpPage(ActionEvent event) {
        Node btn = (Node) event.getSource();
        if ("over".equals(btn.getAccessibleText())) {
            overTableModel.jumpPage();
            updateOverTable();
        } else {
            underTableModel.jumpPage();
            updateUnderTable();
        }
    }

    /**
     * 设置组件全屏显示
     * @param event 点击事件
     * @触发组件 btnTableFullScreen
     * @触发事件 点击
     */
    @FXML
    void setFullScreen(ActionEvent event) {
        Node btn = (Node) event.getSource();
        Node pane = btn.getParent().getParent();
        if (!paneFull.getChildren().contains(pane)) {
            paneFull.getChildren().add(pane);
        } else {
            paneContent.getChildren().add(pane);
        }
    }

    /**
     * 表格跳转至上一页
     * @触发组件 btnLastPage
     * @触发事件 点击
     */
    @FXML
    void toLastPage(ActionEvent event) {
        Node btn = (Node) event.getSource();
        if ("over".equals(btn.getAccessibleText())) {
            overTableModel.toLastPage();
            updateOverTable();
        } else {
            underTableModel.toLastPage();
            updateUnderTable();
        }
    }

    /**
     * 表格跳转至下一页
     * @触发组件 btnNextPage
     * @触发事件 点击
     */
    @FXML
    void toNextPage(ActionEvent event) {
        Node btn = (Node) event.getSource();
        if ("over".equals(btn.getAccessibleText())) {
            overTableModel.toNextPage();
            updateOverTable();
        } else {
            underTableModel.toNextPage();
            updateUnderTable();
        }
    }

    private void filter() {
        String idStr = cbbGoodsId.getButtonCell().getText();
        String typeStr = cbbGoodsType.getButtonCell().getText();
        String nameStr = cbbGoodsName.getButtonCell().getText();
        String specStr = cbbGoodsSpec.getButtonCell().getText();

        tableOver.getItems().clear();
        tableUnder.getItems().clear();
        tableOver.setPlaceholder(new Label("搜索中..."));
        tableUnder.setPlaceholder(new Label("搜索中..."));
        //比较耗时间，新建线程执行
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                overTableModel.updateTotalPage(service.getFilterCount("over", idStr, typeStr, nameStr, specStr));
                underTableModel.updateTotalPage(service.getFilterCount("under", idStr, typeStr, nameStr, specStr));
                overTableModel.setCurPage(1);
                underTableModel.setCurPage(1);
                updateOverTable();
                updateUnderTable();
                return true;
            }
        };

        task.setOnSucceeded(event -> {
            tableOver.setPlaceholder(new Label("未找到"));
            tableUnder.setPlaceholder(new Label("未找到"));
        });

        threadPool.execute(task);
    }

    /** 更新超储表格内容 */
    private void updateOverTable() {
        overTableModel.updateTableView(service.filterByPage("over", overTableModel.getCurPage(), overTableModel.getPageSize()));
    }

    /** 更新短缺表格内容 */
    private void updateUnderTable() {
        underTableModel.updateTableView(service.filterByPage("under", underTableModel.getCurPage(), underTableModel.getPageSize()));
    }

}
