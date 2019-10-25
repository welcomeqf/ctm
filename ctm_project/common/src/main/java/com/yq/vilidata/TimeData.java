package com.yq.vilidata;


import com.yq.utils.DateUtil;
import com.yq.utils.StringUtils;
import com.yq.vilidata.query.TimeQuery;

import java.time.LocalDate;

/**
 * 报表的参数时间工具类
 * @Author qf
 * @Date 2019/8/29
 * @Version 1.0
 */
public class TimeData {

    public static TimeQuery getParam (String startTime, String endTime) {

        TimeQuery timeQuery = new TimeQuery();
        if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
            //开始时间和结束时间都为空
            LocalDate startDate = LocalDate.now().minusMonths(1);
            LocalDate endDate = LocalDate.now();
            startTime = DateUtil.formatDate(startDate);
            endTime = DateUtil.formatDate(endDate);
            timeQuery.setStartTime(startTime);
            timeQuery.setEndTime(endTime);
        } else if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            //开始时间为空，结束时间不为空
            LocalDate localDate = DateUtil.parseDate(endTime);
            LocalDate startDate = localDate.minusMonths(1);
            startTime = DateUtil.formatDate(startDate);
            timeQuery.setStartTime(startTime);
            timeQuery.setEndTime(endTime);
        } else if (StringUtils.isBlank(endTime) && StringUtils.isNotBlank(startTime)){
            //结束时间为空，开始时间不为空
            LocalDate endDate = LocalDate.now();
            endTime = DateUtil.formatDate(endDate);
            timeQuery.setStartTime(startTime);
            timeQuery.setEndTime(endTime);
        } else {
            timeQuery.setStartTime(startTime);
            timeQuery.setEndTime(endTime);
        }
        return timeQuery;
    }
}
