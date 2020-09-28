package eqlee.ctm.report.statisticline.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/14
 * @Version 1.0
 */
@Data
public class StatisticOrderVo {

    /**
     * 统计的月份
     */
    private String StatisticsMonth;

    /**
     * 统计的人数 金额 按城市
     */
    private List<QueryStatisticOrderVo> StatisticsList;

}


