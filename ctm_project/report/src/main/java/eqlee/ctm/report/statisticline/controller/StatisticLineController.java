package eqlee.ctm.report.statisticline.controller;

import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import com.yq.vilidata.TimeData;
import com.yq.vilidata.query.TimeQuery;
import eqlee.ctm.report.statisticline.entity.vo.PersonCountVo;
import eqlee.ctm.report.statisticline.entity.vo.PriceCountVo;
import eqlee.ctm.report.statisticline.entity.vo.StatisticApplyVo;
import eqlee.ctm.report.statisticline.service.IStatisticLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/29 0029
 * @Version 1.0
 */
@Api("统计报表")
@RestController
@Slf4j
@RequestMapping("/v1/app/statistic")
public class StatisticLineController {

    @Autowired
    private IStatisticLineService statisticLineService;


    @ApiOperation(value = "金额统计报表", notes = "金额统计报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/selectPriceByTime")
    @CrossOrigin
    @CheckToken
    public List<PriceCountVo> statisticQuery(@RequestParam("startTime") String startTime,
                                             @RequestParam("endTime") String endTime) {
        TimeQuery query = TimeData.getParam(startTime, endTime);
        return statisticLineService.selectPriceByTime(query.getStartTime(),query.getEndTime());
    }



    @ApiOperation(value = "报名人数报表", notes = "报名人数报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/selectCountByTime")
    @CrossOrigin
    @CheckToken
    public List<PersonCountVo> selectCountByTime(@RequestParam("startTime") String startTime,
                                                 @RequestParam("endTime") String endTime) {

        TimeQuery query = TimeData.getParam(startTime, endTime);
        return statisticLineService.selectCountByTime(query.getStartTime(),query.getEndTime());
    }

    @ApiOperation(value = "报名人数和金额报表", notes = "报名人数和金额报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/StatisticsApplyDataByTime")
    @CrossOrigin
    @CheckToken
    public List<StatisticApplyVo> StatisticsApplyDataByTime(@RequestParam("year") String year) {
        if(StringUtils.isEmpty(year)){
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR) + "";
        }
        return statisticLineService.StatisticsApplyDataByTime(year);
    }

}
