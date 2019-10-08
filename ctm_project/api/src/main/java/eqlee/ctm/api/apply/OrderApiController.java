package eqlee.ctm.api.apply;

import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.entity.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author qf
 * @Date 2019/10/8
 * @Version 1.0
 */
@Api("订单Api")
@Slf4j
@RestController
@RequestMapping("/v1/app/apply/orders")
public class OrderApiController {

    @ApiOperation(value = "导游选人确认",notes = "导游选人确认")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyVoList", value = "已选人信息", required = true, dataType = "List<ApplyVo>", paramType = "path"),
            //ApplyNo里面的字段信息
            @ApiImplicitParam(name = "lineId", value = "线路Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "contactName", value = "联系人姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "contactTel", value = "联系方式", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "place", value = "接送地", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),

    })
    @PostMapping("/saveOrders")
    public ResultVo saveApplyed () {

        return null;
    }


    @ApiOperation(value = "导游选人提交展示页",notes = "导游选人提交展示页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "Integer", paramType = "path")

    })
    @GetMapping("/choisedIndex")
    public Object choisedIndex () {

        return null;
    }


    @ApiOperation(value = "导游已选游客",notes = "导游已选游客")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ContactName", value = "姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "Integer", paramType = "path")

    })
    @GetMapping("/choisedVisitor")
    public Object choisedVisitor ()  {

        return null;
    }

    @ApiOperation(value = "导游换人",notes = "导游换人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyVoList", value = "已选人信息", required = true, dataType = "List<ApplyVo>", paramType = "path"),
            @ApiImplicitParam(name = "Id", value = "更换导游人的Id", required = true, dataType = "Long", paramType = "path"),
            //ApplyNo里面的字段信息
            @ApiImplicitParam(name = "lineId", value = "线路Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "contactName", value = "联系人姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "contactTel", value = "联系方式", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "place", value = "接送地", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),

    })
    @PutMapping("/updateApply/{Id}")
    public Object updateApply () {

        return null;
    }

    @ApiOperation(value = "排车",notes = "排车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "CarNumber", value = "车牌号", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/saveCar")
    public ResultVo saveCar () {

        return null;
    }


    @ApiOperation(value = "导游换人消息列表",notes = "导游换人消息列表")
    @GetMapping("/sureChangeIndex")
    public Object sureChangeIndex() {

        return null;
    }


    @ApiOperation(value = "接受换人",notes = "接受换人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "changedVoList", value = "游客信息表", required = true, dataType = "List<Choised>", paramType = "path"),
            //ChangedVo中信息字段
            @ApiImplicitParam(name = "id", value = "导游Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "guideName", value = "导游姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "contactTel", value = "联系电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "contactName", value = "联系人", required = true, dataType = "String", paramType = "path")
    })
    @PutMapping("/sureChoised")
    public ResultVo sureChoised () {

        return null;
    }


    @ApiOperation(value = "拒绝换人",notes = "拒绝换人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "changedVoList", value = "游客信息表", required = true, dataType = "List<Choised>", paramType = "path"),
            //ChangedVo中信息字段
            @ApiImplicitParam(name = "id", value = "导游Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "guideName", value = "导游姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "contactTel", value = "联系电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "contactName", value = "联系人", required = true, dataType = "String", paramType = "path")
    })
    @PutMapping("/denyChoised")
    public ResultVo denyChoised () {

        return null;
    }


    @ApiOperation(value = "被拒绝换人消息列表",notes = "被拒绝换人消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/denyChoisedindex")
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
            @ApiImplicitParam(name = "ContactTel", value = "联系方式", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/unpaidIndex")
    public Object unpaidIndex () {

        return null;
    }
}
