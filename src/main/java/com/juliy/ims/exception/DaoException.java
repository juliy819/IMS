package com.juliy.ims.exception;

/**
 * 数据库操作异常
 * @author JuLiy
 * @date 2022/11/23 11:28
 */
public class DaoException extends RuntimeException {
    public DaoException(String message) {
        super(message);
    }
}
