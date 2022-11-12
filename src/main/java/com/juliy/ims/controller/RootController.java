package com.juliy.ims.controller;

import static com.juliy.ims.common.Context.getContext;

/**
 * 根控制器类，所有控制器类继承此类
 * @author JuLiy
 * @date 2022/9/29 12:07
 */
public class RootController {
    /** 初始化时将当前Controller实例存至Context中 */
    public RootController() {
        //简化Controller名称，例：LoginController->login
        String simpleName = this.getClass().getSimpleName().replace("Controller", "").toLowerCase();
        getContext().getControllerMap().put(simpleName, this);
    }
}
