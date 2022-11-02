package com.juliy.ims.common;

import javafx.application.HostServices;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文类，用于管理stage、controller等
 * @author JuLiy
 * @date 2022/9/29 12:07
 */
public class Context {

    /** 常用操作 */
    public static final Operation OPERATION = new Operation();
    /** 存储已创建的控制器，key为对应的控制器名称 */
    private static final Map<String, Object> CONTROLLER_MAP = new HashMap<>();
    /** 存储已创建的窗口，key为对应的窗口名称 */
    private static final Map<String, Stage> STAGE_MAP = new HashMap<>();

    /** Application类中获取的host对象 */
    private static HostServices host;

    private Context() {}

    /**
     * 获取controller集合
     * @return controller集合
     */
    public static Map<String, Object> getControllerMap() {
        return CONTROLLER_MAP;
    }

    /**
     * 获取stage集合
     * @return stage集合
     */
    public static Map<String, Stage> getStageMap() {
        return STAGE_MAP;
    }

    /**
     * 获取host
     * @return host
     */
    public static HostServices getHost() {
        return host;
    }

    /** 设置host */
    public static void setHost(HostServices host) {
        Context.host = host;
    }
}
