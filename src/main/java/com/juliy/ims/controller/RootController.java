package com.juliy.ims.controller;

import com.juliy.ims.common.Context;

/**
 * 根控制器类，所有控制器类继承此类
 * @author JuLiy
 * @date 2022/9/29 12:07
 */
public class RootController {
    /** 初始化时将当前Controller实例存至Context中 */
    public RootController() {
        Context.controllerMap.put(this.getClass().getSimpleName(), this);
    }
}
