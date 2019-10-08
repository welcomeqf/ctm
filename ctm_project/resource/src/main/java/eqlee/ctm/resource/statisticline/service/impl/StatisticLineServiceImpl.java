package eqlee.ctm.resource.statisticline.service.impl;

import com.yq.utils.DateUtil;
import com.yq.utils.StringUtils;
import eqlee.ctm.resource.statisticline.dao.StatisticLineMapper;
import eqlee.ctm.resource.statisticline.entity.vo.*;
import eqlee.ctm.resource.statisticline.service.IStatisticLineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/29 0029
 * @Version 1.0
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticLineServiceImpl implements IStatisticLineService {
    @Autowired
    private StatisticLineMapper statisticLineMapper;



    /**
     * 总金额报表
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<StatisticLineVo> getstatisticQuery(String startTime, String endTime) {
        //startTime和endTime都为空
        if (StringUtils.isBlank(startTime)&&StringUtils.isBlank(endTime)){
            LocalDate localDate = LocalDate.now();
            startTime = localDate.toString();
            endTime =localDate.minusMonths(1).toString();
        }
        //如果startTime为空，endTime不为空
        if(StringUtils.isBlank(endTime)&&StringUtils.isNotBlank(startTime)){
            endTime = LocalDate.now().toString();
        }
        List<StatisticLineVo> statisticLineVoList = new ArrayList<StatisticLineVo>();
        List<TimeVo> localDateList = statisticLineMapper.selectOutDateByTime(DateUtil.parseDate(startTime),DateUtil.parseDate(endTime));
        System.out.println(localDateList);
        List<OrdersVo> ordersVoList = statisticLineMapper.selectOrdersByTime(DateUtil.parseDate(startTime),DateUtil.parseDate(endTime));
        for (TimeVo localDate:localDateList) {
            StatisticLineVo statisticLineVo = new StatisticLineVo();
            statisticLineVo.setTime(localDate.getLocalDate().toString());
            statisticLineVo.setSum(0.0);
            for (OrdersVo ordersVo:ordersVoList) {
                if(ordersVo.getOutDate().toString().equals(localDate.getLocalDate().toString())){
                 statisticLineVo.setSum(statisticLineVo.getSum()+ordersVo.getAllPrice());
                }
            }
            statisticLineVo.setSum(statisticLineVo.getSum());
            statisticLineVoList.add(statisticLineVo);
        }
        return statisticLineVoList;
    }



    /**
     * 总人数报表
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<StatisticNumVo> getstatisticCountQuery(String startTime, String endTime) {
        //startTime和endTime都为空
        if (StringUtils.isBlank(startTime)&&StringUtils.isBlank(endTime)){
            LocalDate localDate = LocalDate.now();
            startTime = localDate.toString();
            endTime =localDate.minusMonths(1).toString();
        }
        //如果startTime为空，endTime不为空
        if(StringUtils.isBlank(endTime)&&StringUtils.isNotBlank(startTime)){
            endTime = LocalDate.now().toString();
        }
        List<StatisticNumVo> statisticNumVoList = new ArrayList<StatisticNumVo>();
        List<TimeVo> localDateList = statisticLineMapper.selectOutDateByTime(DateUtil.parseDate(startTime),DateUtil.parseDate(endTime));
        List<OrderDetailedVo> orderDetailedVos = statisticLineMapper.selectOrderDetailedByTime(DateUtil.parseDate(startTime),DateUtil.parseDate(endTime));
        for (TimeVo localDate:localDateList) {
            StatisticNumVo statisticNumVo = new StatisticNumVo();
            statisticNumVo.setTime(localDate.getLocalDate().toString());
            statisticNumVo.setSum(0);
            for (OrderDetailedVo orderDetailedVo:orderDetailedVos) {
                if(orderDetailedVo.getTime().toString().equals(localDate.getLocalDate().toString())){
                    statisticNumVo.setSum(statisticNumVo.getSum()+orderDetailedVo.getAllNumber());
                }
            }
            statisticNumVo.setSum(statisticNumVo.getSum());
            statisticNumVoList.add(statisticNumVo);
        }
        return statisticNumVoList;
    }
}
