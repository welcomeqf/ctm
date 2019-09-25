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
            @ApiImplicitParam(name = "applyVoList", value = "已选人信息", required = true, dataType = "List<ApplyVo>", paramType = "path")
    })
    @GetMapping("saveApplyed")
    public ResultVo saveApplyed(){
       /* if(applyVoList.size() == 0){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }*/
        List<OrdersVo> applyVoList = new ArrayList<OrdersVo>();
        OrdersVo ordersVo = new OrdersVo();
        ordersVo.setContactName("王八蛋");
        ordersVo.setContactTel("10000");
        ordersVo.setLineId(623921375563743232L);
        ordersVo.setOutDate("1905-07-01");
        ordersVo.setLineName("长隆线");
        ordersVo.setPlace("唐家湾");
        ordersVo.setRegion("香洲区");
        applyVoList.add(ordersVo);
        ordersService.saveApply(applyVoList);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

}
