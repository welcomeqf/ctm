package eqlee.ctm.resource.statisticline.dao;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.resource.statisticline.entity.vo.OrderDetailedVo;
import eqlee.ctm.resource.statisticline.entity.vo.OrdersVo;
import eqlee.ctm.resource.statisticline.entity.vo.StatisticLineVo;
import eqlee.ctm.resource.statisticline.entity.vo.TimeVo;
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
     * 查询处于该时间段的所有订单
     * @param startDate
     * @param endDate
     * @return
     */
    List<OrdersVo> selectOrdersByTime(@Param("startTime") LocalDate startDate,
                                      @Param("endTime") LocalDate endDate);





    /**
     * 查询处于该时间段的时间（包括startTime和endTime）
     * @param startDate
     * @param endDate
     * @return
     */
    List<TimeVo> selectOutDateByTime(@Param("startTime") LocalDate startDate,
                                     @Param("endTime") LocalDate endDate);


    /**
     * 查询处于该时间段的时间对应的人数
     * @param startDate
     * @param endDate
     * @return
     */
    List<OrderDetailedVo> selectOrderDetailedByTime(@Param("startTime") LocalDate startDate,
                                                    @Param("endTime") LocalDate endDate);

}
