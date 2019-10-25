package eqlee.ctm.api.resource;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author qf
 * @Date 2019/10/11
 * @Version 1.0
 */
@Api("统计报表Api--9091:resource")
@Slf4j
@RestController
@RequestMapping("/v1/app/statistic")
public class StatisticLineApiController {


    @ApiOperation(value = "金额统计报表", notes = "金额统计报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/selectPriceByTime")
    public Object statisticQuery () {

        return null;
    }



    @ApiOperation(value = "报名人数报表", notes = "报名人数报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/selectCountByTime")
    public Object statisticCountQuery () {

        return null;
    }
}
