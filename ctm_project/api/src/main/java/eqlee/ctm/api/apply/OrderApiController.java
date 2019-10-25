package eqlee.ctm.api.apply;


import eqlee.ctm.api.entity.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * @Author qf
 * @Date 2019/10/8
 * @Version 1.0
 */
@Api("订单Api--9090:apply")
@Slf4j
@RestController
@RequestMapping("/v1/app/apply/orders")
public class OrderApiController {

    @ApiOperation(value = "导游选人确认",notes = "导游选人确认")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "list", value = "已选人信息Id集合", required = true, dataType = "list", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "id数组", required = true, dataType = "Long", paramType = "path")
    })
    @PostMapping("/saveOrders")
    public ResultVo saveApplyed () {

        return null;
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
    public Object updateApply () {

        return null;
    }

    @ApiOperation(value = "排车",notes = "排车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "carNumber", value = "车牌号", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/saveCar")
    public ResultVo saveCar () {

        return null;
    }


    @ApiOperation(value = "接收到换人申请的列表",notes = "接收到换人申请的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/sureChangeIndex")
    public Object sureChangeIndex() {

        return null;
    }


    @ApiOperation(value = "换人",notes = "换人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "choisedList", value = "游客信息列表（以下都是列表里的数据）", required = true, dataType = "List<Choised>", paramType = "path"),
            @ApiImplicitParam(name = "orderId", value = "订单Id数组", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "(1--同意换人  2--不同意换人)", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/sureChoised")
    public ResultVo sureChoised () {

        return null;
    }



    @ApiOperation(value = "展示换人申请结果的列表",notes = "展示换人申请结果的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "类型(1-我推给别人的(默认),2-别人推给我的)", required = false, dataType = "int", paramType = "path")
    })
    @GetMapping("/denyChoisedIndex")
    public Object denyChoisedindex () {
        return null;
    }




    @ApiOperation(value = "收入统计",notes = "收入统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/incomeCount")
    public Object IncomeCount () {
        return null;
    }




    @ApiOperation(value = "未付款人展示页",notes = "未付款人展示页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("/unpaidIndex")
    public Object unpaidIndex () {
        return null;
    }




    @ApiOperation(value = "展示导游选人后的所有线路以及日期",notes = "展示导游选人后的所有线路以及日期")
    @GetMapping("/queryLineAndTime")
    public Object queryLineAndTime () {
        return null;
    }
}
