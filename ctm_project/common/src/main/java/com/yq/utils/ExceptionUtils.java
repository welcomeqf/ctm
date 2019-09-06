package com.yq.utils;

/**
 * @Author qf
 * @Date 2019/8/31
 * @Version 1.0
 */
public class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static String getMassage(Exception e) {
        String msg = String.format("%s at %s", e, e.getStackTrace()[0]);
        return msg;
    }
}
