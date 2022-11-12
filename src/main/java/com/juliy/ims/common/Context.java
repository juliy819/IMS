package com.juliy.ims.common;

import javafx.application.HostServices;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文类，单例模式
 * @author JuLiy
 * @date 2022/9/29 12:07
 */
public class Context {
    private static Context context;
    /** 存储控制器，key为对应的控制器名称 例:login */
    private final HashMap<String, Object> controllerMap = new HashMap<>();
    /** 存储已创建的窗口，key为对应的窗口名称 例:login */
    private final HashMap<String, Stage> stageMap = new HashMap<>();
    /** Application类中获取的host对象 */
    private HostServices host;

    private Context() {}

    /**
     * 获取Context类实例
     * @return 返回本类的实例；若不存在，则先创建
     */
    public static Context getContext() {
        if (context == null) {
            context = new Context();
        }
        return context;
    }

    /**
     * 获取controller集合
     * @return controller集合
     */
    public Map<String, Object> getControllerMap() {
        return controllerMap;
    }

    /**
     * 获取stage集合
     * @return stage集合
     */
    public Map<String, Stage> getStageMap() {
        return stageMap;
    }

    /**
     * 获取host
     * @return host
     */
    public HostServices getHost() {
        return host;
    }

    /** 设置host */
    public void setHost(HostServices host) {
        this.host = host;
    }
}
