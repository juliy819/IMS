package com.juliy.ims.exception;

/**
 * @author JuLiy
 * @date 2022/12/7 14:02
 */
public class DBFileException extends RuntimeException {
    public DBFileException() {
        super("数据库文件设置有误，请检查数据库设置文件");
    }
}
