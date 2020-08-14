package eqlee.ctm.report.statisticline.service;


import eqlee.ctm.report.statisticline.entity.vo.PersonCountVo;
import eqlee.ctm.report.statisticline.entity.vo.PriceCountVo;
import eqlee.ctm.report.statisticline.entity.vo.StatisticApplyVo;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/29 0029
 * @Version 1.0
 */
public interface IStatisticLineService {

    /**
     * 总金额报表
     * @param startTime
     * @param endTime
     * @return
     */
    List<PriceCountVo> selectPriceByTime(String startTime, String endTime);


    /**
     * 总人数报表
     * @param startTime
     * @param endTime
     * @return
     */
    List<PersonCountVo> selectCountByTime(String startTime, String endTime);

    /**
     * 报名人数金额统计
     * @param year
     * @return
     */
    List<StatisticApplyVo> StatisticsApplyDataByTime(String year);
}
