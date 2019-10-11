package eqlee.ctm.resource.statisticline.controller;

import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.resource.statisticline.entity.vo.StatisticLineVo;
import eqlee.ctm.resource.statisticline.entity.vo.StatisticNumVo;
import eqlee.ctm.resource.statisticline.service.IStatisticLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/29 0029
 * @Version 1.0
 */
@Api("线路统计报表")
@RestController
@Slf4j
@RequestMapping("/v1/app/statisticline")
public class StatisticLineController {

    @Autowired
    private IStatisticLineService statisticLineService;


    @ApiOperation(value = "线路报表", notes = "线路报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/statisticLineQuery")
    @CrossOrigin
    @CheckToken
    public List<StatisticLineVo> statisticQuery(@RequestParam("startTime") String startTime,
                                                @RequestParam("endTime") String endTime) {
        return statisticLineService.getstatisticQuery(startTime,endTime);
    }



    @ApiOperation(value = "报名人数报表", notes = "报名人数报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/statisticCountQuery")
    @CrossOrigin
    @CheckToken
    public List<StatisticNumVo> statisticCountQuery(@RequestParam("startTime") String startTime,
                                                    @RequestParam("endTime") String endTime) {
        return statisticLineService.getstatisticCountQuery(startTime,endTime);
    }

}
