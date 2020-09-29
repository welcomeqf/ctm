package eqlee.ctm.report.statisticline.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import com.yq.vilidata.TimeData;
import com.yq.vilidata.query.TimeQuery;
import eqlee.ctm.report.statisticline.entity.vo.*;
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
import java.util.Map;

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


    @ApiOperation(value = "金额统计报表", notes = "金额统计报表（暂停使用）")
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



    @ApiOperation(value = "报名人数报表", notes = "报名人数报表（暂停使用）")
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

    @ApiOperation(value = "报名人数和金额报表二合一", notes = "报名人数和金额报表")
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


    @ApiOperation(value = "每月利润统计", notes = "按年月对报名收入及出团开支进行汇总，统计每月的总报名人数，收入，支出，利润项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/StatisticsOrderDataByTime")
    @CrossOrigin
    @CheckToken
    public List<QueryStatisticOrderVo> StatisticsOrderDataByTime(@RequestParam("year") String year) {
        if(StringUtils.isEmpty(year)){
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR) + "";
        }
        return statisticLineService.StatisticsEcxOrderDataByTime(year);
    }

    @ApiOperation(value = "每月利润统计明细", notes = "按年月对报名收入及出团开支进行汇总，统计每月的总报名人数，收入，支出，利润项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号/线路名称", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "guideName", value = "导游名字", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "cityName", value = "城市(多个城市以英文逗号隔开)", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "year", value = "年份", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "month", value = "月份", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "当页条数", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("/StatisticsOrderDataByTimeDetail")
    @CrossOrigin
    @CheckToken
    public Map<String,Object> StatisticsOrderDataByTimeDetail(@RequestParam("current") Integer current,
                                                              @RequestParam("size") Integer size,
                                                              @RequestParam("guideName") String guideName,
                                                              @RequestParam("orderNo") String orderNo,
                                                              @RequestParam("cityName") String cityName,
                                                              @RequestParam("year") String year,
                                                              @RequestParam("month") String month) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        Page<OrderDetailResultQuery> page = new Page<>(current,size);
        if(StringUtils.isEmpty(year)){
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR) + "";
        }
        return statisticLineService.StatisticsOrderDataByTimeDetail(page,guideName,orderNo,year,month,cityName);
    }

}
