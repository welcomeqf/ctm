package eqlee.ctm.resource.statisticline.service;

import com.yq.IBaseMapper.IBaseMapper;
import eqlee.ctm.resource.statisticline.entity.vo.StatisticLineVo;
import eqlee.ctm.resource.statisticline.entity.vo.StatisticNumVo;

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
    List<StatisticLineVo> getstatisticQuery(String startTime, String endTime);


    /**
     * 总人数报表
     * @param startTime
     * @param endTime
     * @return
     */
    List<StatisticNumVo> getstatisticCountQuery(String startTime, String endTime);
}
