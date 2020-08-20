package eqlee.ctm.report.statisticline.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/10/14
 * @Version 1.0
 */
@Data
public class StatisticApplyVo {

    /**
     * 统计的月份
     */
    private String StatisticsMonth;

    /**
     * 统计的人数 金额 按城市
     */
    private List<QueryStatisticApplyVo> StatisticsList;

}

//@Data
//class StatisticSubVo {
//
//    /**
//     * 城市
//     */
//    private String city;
//
//
//
//    /**
//     * 总人数
//     */
//    private Integer allPersonCount;
//
//    /**
//     * 总金额
//     */
//    private Double allPrice;
//}
