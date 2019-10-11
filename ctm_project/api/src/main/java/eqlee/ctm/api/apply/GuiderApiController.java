package eqlee.ctm.api.apply;

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
 * @Date 2019/9/28
 * @Version 1.0
 */
@Api("导游Api")
@Slf4j
@RestController
@RequestMapping("/v1/app/guider")
public class GuiderApiController {


    @ApiOperation(value = "导游首页",notes = "导游首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/guiderIndex")
    public Object guiderIndex () {

        return null;
    }


    @ApiOperation(value = "导游选人时看到的报名表",notes = "导游选人时看到的报名表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出发时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("/applyIndex")
    public Object applyIndex () {

        return null;
    }


}
