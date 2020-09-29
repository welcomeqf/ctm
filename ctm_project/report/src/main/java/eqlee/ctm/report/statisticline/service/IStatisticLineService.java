package eqlee.ctm.report.statisticline.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import eqlee.ctm.report.statisticline.entity.vo.*;

import java.util.List;
import java.util.Map;

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

    /**
     * 报名人数金额统计 导出
     * @param year
     * @return
     */
    List<QueryStatisticApplyVo> StatisticsEcxApplyDataByTime(String year);

    /**
     * 利润支出人数总金额统计
     * @param year
     * @return
     */
    List<StatisticOrderVo> StatisticsOrderDataByTime(String year);


    /**
     * 利润支出人数总金额统计 导出
     * @param year
     * @return
     */
    List<QueryStatisticOrderVo> StatisticsEcxOrderDataByTime(String year);

    /**
     * 分页查询所有财务审核
     * @param page
     * @param guideName
     * @param orderNo
     * @return
     */
    Map<String,Object> StatisticsOrderDataByTimeDetail(Page<OrderDetailResultQuery> page, String guideName, String orderNo, String year, String month, String cityName);
}
