package eqlee.ctm.apply.orders.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.apply.orders.entity.query.OrderDetailedQuery;
import eqlee.ctm.apply.orders.service.IOrdersDetailedService;
import eqlee.ctm.apply.orders.service.IOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/10/17
 * @Version 1.0
 */
@Slf4j
@Api("订单详情API")
@RestController
@RequestMapping("/v1/app/orderDetailed")
public class OrderDetailedController {


    @Autowired
    private IOrdersDetailedService ordersDetailedService;

    @Autowired
    private IOrdersService ordersService;


    @ApiOperation(value = "查询导游人员表",notes = "查询导游人员表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出行日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "payType", value = "支付类型(现结,月结,面收)", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/pageOrderDetailed2Type")
    @CrossOrigin
    @CheckToken
    public Map<String,Object> pageOrderDetailed2Type (@RequestParam("payType") String payType,
                                                            @RequestParam("lineName") String lineName,
                                                            @RequestParam("outDate") String outDate,
                                                            @RequestParam("current") Integer current,
                                                            @RequestParam("size") Integer size) {

        String carNumber = ordersService.getCarNumber(lineName, outDate);
        Page<OrderDetailedQuery> page = new Page<>(current,size);
        Page<OrderDetailedQuery> page1 = ordersDetailedService.pageOrderDetailed2Type(page, payType, lineName, outDate);
        Map<String,Object> map = new HashMap<>();
        map.put("carNo",carNumber);
        map.put("data",page1);
        return map;
    }
}
