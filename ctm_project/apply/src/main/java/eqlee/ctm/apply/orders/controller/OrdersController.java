package eqlee.ctm.apply.orders.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.orders.entity.Vo.*;
import eqlee.ctm.apply.orders.entity.bo.CarBo;
import eqlee.ctm.apply.orders.entity.bo.ChoisedBo;
import eqlee.ctm.apply.orders.entity.bo.IdBo;
import eqlee.ctm.apply.orders.entity.query.*;
import eqlee.ctm.apply.orders.service.IOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private IOrdersService ordersService;



    @ApiOperation(value = "导游选人确认",notes = "导游选人确认")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "list", value = "已选人信息Id集合", required = true, dataType = "list", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "id数组", required = true, dataType = "Long", paramType = "path")
    })
    @PostMapping("/saveOrders")
    @CrossOrigin
    @CheckToken
    public LongResultQuery saveApplyed(@RequestBody IdVo vo){
        if(vo.getList().size() == 0){
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        Long orderId = ordersService.saveApply(vo.getList());
        LongResultQuery query = new LongResultQuery();
        query.setId(orderId);
        return query;
    }


    @ApiOperation(value = "导游换人",notes = "导游换人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出发时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "orderIndexVos", value = "已选人信息", required = true, dataType = "List<ApplyVo>", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "更换导游人的Id", required = true, dataType = "Long", paramType = "path"),
            //ApplyNo里面的字段信息
            @ApiImplicitParam(name = "orderId", value = "订单Id数组", required = true, dataType = "Long", paramType = "path")
    })
    @PostMapping("/updateApply")
    @CrossOrigin
    @CheckToken
    public ResultVo updateApply(@RequestBody OrderWithVo vo){
        if(vo.getId() == null || vo.getOrderIndexVos() == null || StringUtils.isBlank(vo.getOutDate())
        || StringUtils.isBlank(vo.getLineName())){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        ordersService.updateApply(vo.getOrderIndexVos(),vo.getId(),vo.getLineName(),vo.getOutDate());
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "排车",notes = "排车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "carNumber", value = "车牌号", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/saveCar")
    @CrossOrigin
    @CheckToken
    public ResultVo saveCar(@RequestBody CarBo bo){
        if(StringUtils.isBlank(bo.getLineName()) || StringUtils.isBlank(bo.getOutDate()) || StringUtils.isBlank(bo.getCarNumber())){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        ordersService.save(bo.getLineName(),bo.getOutDate(),bo.getCarNumber());
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }



    @ApiOperation(value = "接收到换人申请的列表",notes = "接收到换人申请的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/sureChangeIndex")
    @CrossOrigin
    @CheckToken
    public Page<ChangedVo> sureChangeIndex(@RequestParam("current") Integer current, @RequestParam("size") Integer size){
        Page<ChangedVo> page = new Page<>(current,size);
        return ordersService.waiteChangeIndex(page);
    }



    @ApiOperation(value = "换人",notes = "换人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单Id数组", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "(1--同意换人  2--不同意换人)", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/sureChoised")
    @CrossOrigin
    @CheckToken
    public ResultVo sureChoised(@RequestBody ChoisedBo bo){
        if(bo.getType() == null || bo.getOrderId() == null || StringUtils.isBlank(bo.getLineName()) || StringUtils.isBlank(bo.getOutDate())){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }

        if (bo.getType() != 1 && bo.getType() != 2) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"type只能1或2");
        }

        if (bo.getType() == 1) {
            //同意换人
            ordersService.sureChoised(bo.getOrderId(), bo.getOutDate(), bo.getLineName());
        }

        if (bo.getType() == 2) {
            //拒绝换人
            ordersService.denyChoised(bo.getOrderId(), bo.getOutDate(), bo.getLineName());
        }
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }




    @ApiOperation(value = "展示换人申请结果的列表",notes = "展示换人申请结果的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "类型(1-我推给别人的(默认),2-别人推给我的)", required = false, dataType = "int", paramType = "path")
    })
    @GetMapping("/denyChoisedIndex")
    @CrossOrigin
    @CheckToken
    public Page<ChangedQuery> denyChoisedindex(@RequestParam("current") Integer current,
                                               @RequestParam("size") Integer size,
                                               @RequestParam("type") Integer type){
        if(current == null || size == null){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        Page<ChangedQuery> page = new Page<>(current,size);
        return ordersService.denyChoisedindex(page,type);
    }




    @ApiOperation(value = "收入统计",notes = "收入统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/incomeCount")
    @CrossOrigin
    @CheckToken
    public IncomeVo IncomeCount(@RequestParam("LineName") String LineName,
                                @RequestParam("OutDate") String OutDate){
        if(StringUtils.isBlank(LineName)||StringUtils.isBlank(OutDate)){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        return ordersService.IncomeCount(LineName,OutDate);
    }



    @ApiOperation(value = "未付款人展示页",notes = "未付款人展示页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("/unpaidIndex")
    @CrossOrigin
    @CheckToken
    public Page<UnpaidInformationVo> unpaidIndex(@RequestParam("current") Integer current,
                                                 @RequestParam("size") Integer size){
        Page<UnpaidInformationVo> page = new Page<>(current,size);
        return ordersService.unpaidInformation(page);
    }

    @ApiOperation(value = "展示导游选人后的所有线路以及日期",notes = "展示导游选人后的所有线路以及日期")
    @GetMapping("/queryLineAndTime")
    @CrossOrigin
    @CheckToken
    public List<OrderQuery> queryLineAndTime () {
        return ordersService.queryLineAndTime();
    }


    @ApiOperation(value = "查询所有未结算的条数",notes = "查询所有未结算的条数")
    @GetMapping("/queryAllNoCount")
    @CrossOrigin
    @CheckToken
    public OrdersNoCountQuery queryAllNoCount () {
        Integer integer = ordersService.queryAllNoCount();
        OrdersNoCountQuery query = new OrdersNoCountQuery();
        query.setResult(integer);
        return query;
    }


    @ApiOperation(value = "查询所有未结算的具体信息",notes = "查询所有未结算的具体信息")
    @GetMapping("/queryAllNoCountInfo")
    @CrossOrigin
    @CheckToken
    public List<OrdersNoCountInfoQuery> queryAllNoCountInfo () {
        return ordersService.queryAllNoCountInfo();
    }


    @ApiOperation(value = "查询id",notes = "查询id")
    @GetMapping("/queryOrderId")
    @CrossOrigin
    @CheckToken
    public IdBo queryOrderId (@RequestParam("outDate") String outDate,
                              @RequestParam("lineName") String lineName) {

        if (StringUtils.isBlank(outDate) || StringUtils.isBlank(lineName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
        }

        return ordersService.queryOrderId(outDate,lineName);
    }
}
