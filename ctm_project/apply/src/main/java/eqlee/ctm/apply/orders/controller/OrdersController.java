package eqlee.ctm.apply.orders.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.exception.ApplicationException;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.jwt.contain.LocalUser;
import eqlee.ctm.apply.jwt.entity.UserLoginQuery;
import eqlee.ctm.apply.jwt.islogin.CheckToken;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.orders.entity.Orders;
import eqlee.ctm.apply.orders.entity.Vo.*;
import eqlee.ctm.apply.orders.service.IOrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    @Autowired
    LocalUser localUser = new LocalUser();



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
    @PostMapping("saveApplyed")
    @CrossOrigin
    @CheckToken
    public ResultVo saveApplyed(@RequestBody List<OrdersVo> applyVoList){
        if(applyVoList.size() == 0){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        ordersService.saveApply(applyVoList);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }




    @ApiOperation(value = "导游选人提交展示页",notes = "导游选人提交展示页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "Integer", paramType = "path")

    })
    @GetMapping("ChoisedIndex")
    @CrossOrigin
    @CheckToken
    public Map<String,Object> ChoisedIndex(@RequestParam("Region") String Region,
                                                             @RequestParam("LineName") String LineName,
                                                             @RequestParam("OutDate") String OutDate,
                                                             @RequestParam("current") Integer current,
                                                             @RequestParam("size") Integer size) {
        if(StringUtils.isBlank(Region)||StringUtils.isBlank(LineName)||StringUtils.isBlank(OutDate)){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        Page<OrderIndexVo> page = new Page<OrderIndexVo>();
        page.setSize(size);
        page.setCurrent(current);
        Map<String,Object> map = new HashMap<>();
        String carNumber = ordersService.getCarNumber(LineName,OutDate);
        Page<OrderIndexVo> pageList = ordersService.ChoisedIndex(page,LineName,OutDate);
        map.put("data",pageList);
        map.put("CarNo",carNumber);
        System.out.println(map);
        return map;
    }


    @ApiOperation(value = "导游已选游客",notes = "导游已选游客")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ContactName", value = "姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "Integer", paramType = "path")

    })
    @GetMapping("choisedVisitor")
    @CrossOrigin
    @CheckToken
    public Page<VisitorInformation> choisedVisitor(@RequestParam("ContactName") String ContactName,
                                                    @RequestParam("LineName") String LineName,
                                                    @RequestParam("OutDate") String OutDate,
                                                    @RequestParam("current") Integer current,
                                                    @RequestParam("size") Integer size) {
        if(StringUtils.isBlank(LineName)||StringUtils.isBlank(OutDate)
        ||current == null|| size == null){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        Page<VisitorInformation> page = new Page<VisitorInformation>();
        page.setCurrent(current);
        page.setSize(size);
        return ordersService.choisedVisitor(page,OutDate,ContactName,LineName);

    }



    @ApiOperation(value = "导游换人",notes = "导游换人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyVoList", value = "已选人信息", required = true, dataType = "List<ApplyVo>", paramType = "path"),
            @ApiImplicitParam(name = "Id", value = "更换导游人的Id", required = true, dataType = "Long", paramType = "path"),
            //ApplyNo里面的字段信息
            @ApiImplicitParam(name = "LineId", value = "线路Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "ContactName", value = "联系人姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ContactTel", value = "联系方式", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Place", value = "接送地", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),

    })
    @PutMapping("UpdateApply/{Id}")
    @CrossOrigin
    @CheckToken
    public ResultVo UpdateApply(@RequestBody List<OrderIndexVo> orderIndexVos,
                                 @PathVariable("Id") Long Id){
        if(orderIndexVos.size() == 0|| Id == null){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        ordersService.updateApply(orderIndexVos,Id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "排车",notes = "排车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "CarNumber", value = "车牌号", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("saveCar")
    @CrossOrigin
    @CheckToken
    public ResultVo saveCar(@RequestParam("LineName") String LineName,
                             @RequestParam("OutDate") String OutDate,
                             @RequestParam("CarNumber") String CarNumber){
        if(StringUtils.isBlank(LineName)||StringUtils.isBlank(OutDate)){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        ordersService.save(LineName,OutDate,CarNumber);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }



    @ApiOperation(value = "导游换人消息列表",notes = "导游换人消息列表                                                                   ")
    @GetMapping("sureChangeIndex")
    @CrossOrigin
    @CheckToken
    public List<ChangedVo> sureChangeIndex(){

        return ordersService. waiteChangeIndex();
    }



    @ApiOperation(value = "接受换人",notes = "接受换人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "changedVoList", value = "游客信息表", required = true, dataType = "List<Choised>", paramType = "path"),
            //ChangedVo中信息字段
            @ApiImplicitParam(name = "Id", value = "导游Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "GuideName", value = "导游姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ContactTel", value = "联系电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ContactName", value = "联系人", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("sureChoised")
    @CrossOrigin
    @CheckToken
    public ResultVo sureChoised(@RequestBody List<ChoisedVo> choisedList){
        if(choisedList.size() == 0){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        ordersService.sureChoised(choisedList);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }




    @ApiOperation(value = "拒绝换人",notes = "拒绝换人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "changedVoList", value = "游客信息表", required = true, dataType = "List<Choised>", paramType = "path"),
            //ChangedVo中信息字段
            @ApiImplicitParam(name = "Id", value = "导游Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "GuideName", value = "导游姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ContactTel", value = "联系电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ContactName", value = "联系人", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("denyChoised")
    @CrossOrigin
    @CheckToken
    public ResultVo denyChoised(@RequestBody List<ChoisedVo> choisedList){
        if(choisedList.size() == 0){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        ordersService.denyChoised(choisedList);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }



    @ApiOperation(value = "被拒绝换人消息列表",notes = "被拒绝换人消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("denyChoisedindex")
    @CrossOrigin
    @CheckToken
    public List<ChangedVo> denyChoisedindex(@Param("LineName") String LineName,
                                      @Param("OutDate") String OutDate){
        if(StringUtils.isBlank(LineName)||StringUtils.isBlank(OutDate)){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        return ordersService.denyChoisedindex(LineName,OutDate);
    }




    @ApiOperation(value = "收入统计",notes = "收入统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("IncomeCount")
    @CrossOrigin
    @CheckToken
    public IncomeVo IncomeCount(@Param("LineName") String LineName,
                                @Param("OutDate") String OutDate){
        if(StringUtils.isBlank(LineName)||StringUtils.isBlank(OutDate)){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        return ordersService.IncomeCount(LineName,OutDate);
    }



    @ApiOperation(value = "未付款人展示页",notes = "未付款人展示页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ContactTel", value = "联系方式", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("unpaidIndex")
    @CrossOrigin
    @CheckToken
    public UnpaidInformationVo unpaidIndex(@Param("ContactTel") String ContactTel,
                                                 @Param("LineName") String LineName,
                                                 @Param("OutDate") String OutDate){
        if(StringUtils.isBlank(LineName)||StringUtils.isBlank(OutDate)||StringUtils.isBlank(ContactTel)){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        return ordersService.unpaidInformation(ContactTel,LineName,OutDate);
    }

}
