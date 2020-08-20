package eqlee.ctm.report.statisticline.dao;


import eqlee.ctm.report.statisticline.entity.vo.PersonCountVo;
import eqlee.ctm.report.statisticline.entity.vo.PriceCountVo;
import eqlee.ctm.report.statisticline.entity.vo.QueryStatisticApplyVo;
import eqlee.ctm.report.statisticline.entity.vo.StatisticApplyVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/29 0029
 * @Version 1.0
 */
@Component
public interface StatisticLineMapper{

    /**
     * 统计每天的金额
     * @param startDate
     * @param endDate
     * @return
     */
    List<PriceCountVo> selectPriceByTime(@Param("startTime") LocalDate startDate,
                                         @Param("endTime") LocalDate endDate);





    /**
     * 统计每天的人数
     * @param startDate
     * @param endDate
     * @return
     */
    List<PersonCountVo> selectCountByTime(@Param("startTime") LocalDate startDate,
                                          @Param("endTime") LocalDate endDate);


    /**
     * 报名人数和金额报表
     * @param year
     * @return
     */
    List<QueryStatisticApplyVo> StatisticsApplyDataByTime(@Param("year") String year);



}
