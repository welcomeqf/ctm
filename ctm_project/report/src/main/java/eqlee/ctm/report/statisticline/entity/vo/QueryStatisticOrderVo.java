package eqlee.ctm.report.statisticline.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/14
 * @Version 1.0
 */
@Data
public class QueryStatisticOrderVo {

    /**
     * 统计的月份
     */
    private String StatisticsMonth;

    /**
     * 城市
     */
    private String city;

    /**
     * 总人数
     */
    private Integer allPersonCount;

    /**
     * 总金额
     */
    private Double allPrice;

    /**
     * 总支出
     */
    private Double outPrice;

    /**
     * 总利润
     */
    private Double profitPrice;


}


