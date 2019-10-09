package eqlee.ctm.finance.financeexpress.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import eqlee.ctm.finance.financeexpress.entity.AllCountVo;
import eqlee.ctm.finance.financeexpress.entity.CountVo;
import eqlee.ctm.finance.financeexpress.entity.FinanceExpressCountVo;
import eqlee.ctm.finance.financeexpress.entity.CashCountVo;
import eqlee.ctm.finance.financeexpress.service.IFinanceExpressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Claire
 * @Date 2019/10/8 0008
 * @Version 1.0
 */
@RestController
@Slf4j
@Api("财务展示Api")
@RequestMapping("/v1/app/financeezpress")
public class FinanceExpressController {

    @Autowired
    private IFinanceExpressService financeExpressService;


    @ApiOperation(value = "财务现结结算列表", notes = "财务现结结算列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "time", value = "时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = false, dataType = "String", paramType = "path"),
    })
    @GetMapping("/queryfinancecount")
    @CrossOrigin
    public Page<CashCountVo> queryfinancecount(@RequestParam("current") Integer current,
                                               @RequestParam("size") Integer size,
                                               @RequestParam("time") String time,
                                               @RequestParam("companyName") String companyName) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR, "当前页或者页面大小为空");
        }
        return financeExpressService.queryfinancecount(current, size, time, companyName);
    }



    @ApiOperation(value = "财务代收账单展示页",notes = "财务代收账单展示页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "time", value = "时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "guideName", value = "导游姓名", required = false, dataType = "String", paramType = "path"),
    })
    @GetMapping("/queryfinancecollection")
    @CrossOrigin
    public Page<FinanceExpressCountVo> queryfinancecollection(@RequestParam("current") Integer current,
                                                              @RequestParam("size") Integer size,
                                                              @RequestParam("time") String time,
                                                              @RequestParam("guideName") String guideName){
        if(current == null||size == null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"当前页或者页面大小为空");
        }
        return financeExpressService.queryfinancecollection(current,size,time,guideName);
    }



    @ApiOperation(value = "财务结算列表", notes = "财务结算列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "time", value = "时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = false, dataType = "String", paramType = "path"),
    })
    @GetMapping("/getfinancecount")
    @CrossOrigin
    public Page<CountVo> getfinancecount(@RequestParam("current") Integer current,
                                         @RequestParam("size") Integer size,
                                         @RequestParam("time") String time,
                                         @RequestParam("companyName") String companyName) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR, "当前页或者页面大小为空");
        }
        return financeExpressService.getfinancecount(current, size, time, companyName);
    }


    @ApiOperation(value = "财务总账单列表", notes = "财务总账单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "time", value = "时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = false, dataType = "String", paramType = "path"),
    })
    @GetMapping("/getAllfinancecount")
    @CrossOrigin
    public Page<AllCountVo> getAllfinancecount(@RequestParam("current") Integer current,
                                               @RequestParam("size") Integer size,
                                               @RequestParam("time") String time,
                                               @RequestParam("companyName") String companyName) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR, "当前页或者页面大小为空");
        }
        return financeExpressService.getAllfinancecount(current, size, time, companyName);
    }

}

