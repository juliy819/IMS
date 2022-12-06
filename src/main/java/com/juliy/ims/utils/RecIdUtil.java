package com.juliy.ims.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 单据编号生成类
 * @author JuLiy
 * @date 2022/10/1 18:09
 * @生成规则 1位单号类型+17位时间戳+14位(用户id加密&随机数)
 */
public class RecIdUtil {

    private RecIdUtil() {}

    /** 生成不带类别标头的编码 */
    private static String getCode() {
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    public static String getEntryId() {
        return Head.ENTRY.getCode() + getCode();
    }

    public static String getOutId() {
        return Head.OUT.getCode() + getCode();
    }

    public static String getAllocationCode() {
        return Head.ALLOCATION.getCode() + getCode();
    }


    enum Head {
        ENTRY("E"),
        OUT("O"),
        ALLOCATION("A");

        private final String code;

        Head(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }

}
