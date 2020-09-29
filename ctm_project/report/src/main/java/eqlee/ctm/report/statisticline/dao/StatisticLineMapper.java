package eqlee.ctm.report.statisticline.dao;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.report.statisticline.entity.vo.*;
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

    /**
     * 利润支出人数总金额统计
     * @param year
     * @return
     */
    List<QueryStatisticOrderVo> StatisticsOrderDataByTime(@Param("year") String year);

    /**
     * 利润支出人数总金额统计 拼条件 明细使用
     * @param year
     * @return
     */
    List<QueryStatisticOrderVo> StatisticsOrderDataByTime2(@Param("guideName") String guideName,
                                                           @Param("orderNo") String orderNo,
                                                           @Param("year") String year,
                                                           @Param("month") String month,
                                                           @Param("cityList") List<String> cityList);

    /**
     * 分页查询所有审核数据
     * @param page
     * @param guideName
     * @param orderNo
     * @return
     */
    Page<OrderDetailResultQuery> StatisticsOrderDataByTimeDetail (Page<OrderDetailResultQuery> page,
                                          @Param("guideName") String guideName,
                                          @Param("orderNo") String orderNo,
                                          @Param("year") String year,
                                          @Param("month") String month,
                                          @Param("cityList") List<String> cityList);

}
