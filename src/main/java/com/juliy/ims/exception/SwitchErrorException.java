package com.juliy.ims.exception;

/**
 * switch语句选择异常
 * @author JuLiy
 * @date 2022/11/25 20:01
 */
public class SwitchErrorException extends RuntimeException {
    public SwitchErrorException() {
        super("switch语句中无对应的分支");
    }
}
