package eqlee.ctm.report.statisticline.service.impl;


import com.yq.utils.DateUtil;
import eqlee.ctm.report.statisticline.dao.StatisticLineMapper;
import eqlee.ctm.report.statisticline.entity.vo.PersonCountVo;
import eqlee.ctm.report.statisticline.entity.vo.PriceCountVo;
import eqlee.ctm.report.statisticline.entity.vo.StatisticApplyVo;
import eqlee.ctm.report.statisticline.service.IStatisticLineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
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

        return statisticLineMapper.StatisticsApplyDataByTime(year);
    }
}
