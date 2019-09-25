package eqlee.ctm.apply.orders.controller;

import com.yq.constanct.CodeType;
import eqlee.ctm.apply.exception.ApplicationException;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.orders.entity.Vo.OrdersVo;
import eqlee.ctm.apply.orders.service.IOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
@Api("订单")
@Slf4j
@RestController
@RequestMapping("/v1/app/apply/orders")
public class OrdersController {

    @Autowired
    IOrdersService ordersService;



    @ApiOperation(value = "导游选人确认",notes = "导游选人确认")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyVoList", value = "已选人信息", required = true, dataType = "List<ApplyVo>", paramType = "path"),
            //ApplyNo里面的字段信息
            @ApiImplicitParam(name = "LineId", value = "线路Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "ContactName", value = "联系人姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ContactTel", value = "联系方式", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Place", value = "接送地", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),

    })
    @GetMapping("saveApplyed")
    public ResultVo saveApplyed(List<OrdersVo> applyVoList){
        if(applyVoList.size() == 0){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        ordersService.saveApply(applyVoList);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

}
