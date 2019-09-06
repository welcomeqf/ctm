package com.yq.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 工具类
 *
 * @Author qf
 * @Date 2019/8/23
 * @Version 1.0
 */
@Component
public class DateUtil {
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public static final String SIMPLE_DATE_FORMAT = "yyyyMMddHHmmss";

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

    private static final DateTimeFormatter simpleDateFormatter = DateTimeFormatter.ofPattern(SIMPLE_DATE_FORMAT);

    public static String formatDateTime(LocalDate date) {
        return dateTimeFormatter.format(date);
    }

    public static String formatDateTime(LocalDateTime date) {
        return dateTimeFormatter.format(date);
    }

    public static String formatSimpleDateTime(LocalDateTime date) {
        return simpleDateFormatter.format(date);
    }

    public static LocalDateTime parseDateTime(String time){ return LocalDateTime.parse(time, dateTimeFormatter); }

    public static String formatDate(LocalDate date) {
        return dateFormatter.format(date);
    }

    public static String formatDate(LocalDateTime date) {
        return dateFormatter.format(date);
    }

    public static LocalDate parseDate(String date){ return LocalDate.parse(date,dateFormatter); }

}
