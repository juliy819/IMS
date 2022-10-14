package com.juliy.ims.common;

/**
 * 自定义异常类
 * @author JuLiy
 * @date 2022/10/10 18:32
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
