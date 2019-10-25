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
 * @Date 2019/10/17
 * @Version 1.0
 */
@Slf4j
@Api("订单详情API")
@RestController
@RequestMapping("/v1/app/orderDetailed")
public class OrderDetailedApiController {


    @ApiOperation(value = "查询导游人员表--9090:apply",notes = "查询导游人员表--9090:apply")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出行日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "payType", value = "支付类型(现结,月结,面收)", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/pageOrderDetailed2Type")
    public Object pageOrderDetailed2Type () {

        return null;
    }
}
