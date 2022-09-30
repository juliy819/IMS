package com.juliy.ims.common;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文类，用于管理stage、controller等
 * @author JuLiy
 * @date 2022/9/29 12:07
 */
public class Context {
    public static Operation operation = new Operation();

    /** 存储已创建的控制器，key为对应的控制器名称 */
    public static Map<String, Object> controllerMap = new HashMap<>();

    /** 存储已创建的窗口，key为对应的窗口名称 */
    public static Map<String, Stage> stageMap = new HashMap<>();
}
