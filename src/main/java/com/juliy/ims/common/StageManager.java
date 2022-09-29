package com.juliy.ims.common;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * 窗口管理类
 * @author JuLiy
 * @date 2022/9/27 10:03
 */
public class StageManager {
    /** 存储已创建的窗口，key为对应的页面名称 */
    private final Map<String, Stage> stageMap = new HashMap<>();

    /**
     * 添加新建的窗口
     * @param name  窗口名
     * @param stage 窗口对象
     */
    public void addStage(String name, Stage stage) {
        stageMap.put(name, stage);
    }

    /**
     * 获取指定窗口
     * @param name 窗口名
     * @return 窗口对象
     */
    public Stage getStage(String name) {
        return stageMap.get(name);
    }

    /**
     * 关闭指定窗口
     * @param name 窗口名
     */
    public void closeStage(String name) {
        stageMap.get(name).close();
    }

    /**
     * 删除指定窗口
     * @param name 窗口名
     */
    public void releaseStage(String name) {
        stageMap.remove(name);
    }

    public void jump(String currentStageName, String targetStageName) {
        stageMap.get(currentStageName).close();
        stageMap.get(targetStageName).show();
    }
}
