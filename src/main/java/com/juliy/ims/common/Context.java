package com.juliy.ims.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文类，用于管理stage、controller等
 * @author JuLiy
 * @date 2022/9/29 12:07
 */
public class Context {
    public static StageManager stageManager = new StageManager();
    public static Factory factory = new Factory();
    public static Map<String, Object> controllerMap = new HashMap<>();
}
