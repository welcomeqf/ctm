package eqlee.ctm.report.statisticline.service.impl;


import com.yq.utils.DateUtil;
import eqlee.ctm.report.statisticline.dao.StatisticLineMapper;
import eqlee.ctm.report.statisticline.entity.vo.*;
import eqlee.ctm.report.statisticline.service.IStatisticLineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * 金额统计
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<PriceCountVo> selectPriceByTime(String startTime, String endTime) {
        LocalDate startDate = DateUtil.parseDate(startTime);
        LocalDate endDate = DateUtil.parseDate(endTime);

        return statisticLineMapper.selectPriceByTime(startDate,endDate);

    }


    /**
     * 人数统计
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<PersonCountVo> selectCountByTime(String startTime, String endTime) {
        LocalDate startDate = DateUtil.parseDate(startTime);
        LocalDate endDate = DateUtil.parseDate(endTime);

        return statisticLineMapper.selectCountByTime(startDate,endDate);
    }

    /**
     * 报名人数和金额报表
     * @param year
     * @return
     */
    @Override
    public List<StatisticApplyVo> StatisticsApplyDataByTime(String year) {

        List<StatisticApplyVo> list = new ArrayList<>();
        List<QueryStatisticApplyVo> queryStatisticApplyVos = statisticLineMapper.StatisticsApplyDataByTime(year);
        if(queryStatisticApplyVos != null && !queryStatisticApplyVos.isEmpty()){
            for(QueryStatisticApplyVo vo : queryStatisticApplyVos){
                List<String> months = list.stream().map(StatisticApplyVo::getStatisticsMonth).collect(Collectors.toList());
                if(!months.contains(vo.getStatisticsMonth())){
                    StatisticApplyVo vm = new StatisticApplyVo();
                    List<QueryStatisticApplyVo> qlist = queryStatisticApplyVos.stream().filter(item -> item.getStatisticsMonth().equals(vo.getStatisticsMonth())).collect(Collectors.toList());
                    vm.setStatisticsMonth(vo.getStatisticsMonth());
                    vm.setStatisticsList(qlist);
                    list.add(vm);
                }
            }
        }
        return list;
    }

    /**
     * 报名人数和金额报表
     * @param year
     * @return
     */
    @Override
    public List<QueryStatisticApplyVo> StatisticsEcxApplyDataByTime(String year) {

        return statisticLineMapper.StatisticsApplyDataByTime(year);
    }


    /**
     * 利润支出人数总金额统计
     * @param year
     * @return
     */
    @Override
    public List<StatisticOrderVo> StatisticsOrderDataByTime(String year) {

        List<StatisticOrderVo> list = new ArrayList<>();
        List<QueryStatisticOrderVo> queryStatisticOrderVos = statisticLineMapper.StatisticsOrderDataByTime(year);
        if(queryStatisticOrderVos != null && !queryStatisticOrderVos.isEmpty()){
            for(QueryStatisticOrderVo vo : queryStatisticOrderVos){
                List<String> months = list.stream().map(StatisticOrderVo::getStatisticsMonth).collect(Collectors.toList());
                if(!months.contains(vo.getStatisticsMonth())){
                    StatisticOrderVo vm = new StatisticOrderVo();
                    List<QueryStatisticOrderVo> qlist = queryStatisticOrderVos.stream().filter(item -> item.getStatisticsMonth().equals(vo.getStatisticsMonth())).collect(Collectors.toList());
                    vm.setStatisticsMonth(vo.getStatisticsMonth());
                    vm.setStatisticsList(qlist);
                    list.add(vm);
                }
            }
        }
        return list;
    }



    /**
     * 利润支出人数总金额统计
     * @param year
     * @return
     */
    @Override
    public List<QueryStatisticOrderVo> StatisticsEcxOrderDataByTime(String year) {

        return statisticLineMapper.StatisticsOrderDataByTime(year);
    }
}
