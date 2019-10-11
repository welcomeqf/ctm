package eqlee.ctm.api.finance.controller;

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
 * @Date 2019/10/9
 * @Version 1.0
 */
@Slf4j
@Api("财务展示APi")
@RestController
@RequestMapping("/v1/app/financeezpress")
public class FinanceExpressApiController {

    @ApiOperation(value = "财务现结结算列表", notes = "财务现结结算列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "time", value = "时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = false, dataType = "String", paramType = "path"),
    })
    @GetMapping("/queryfinancecount")
    public Object queryfinancecount () {

        return null;
    }

    @ApiOperation(value = "财务代收账单展示页",notes = "财务代收账单展示页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "time", value = "时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "guideName", value = "导游姓名", required = false, dataType = "String", paramType = "path"),
    })
    @GetMapping("/queryfinancecollection")
    public Object queryfinancecollection () {

        return null;
    }

    @ApiOperation(value = "财务结算列表", notes = "财务结算列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "time", value = "时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = false, dataType = "String", paramType = "path"),
    })
    @GetMapping("/getfinancecount")
    public Object getfinancecount () {

        return null;
    }

    @ApiOperation(value = "财务总账单列表", notes = "财务总账单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "time", value = "时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = false, dataType = "String", paramType = "path"),
    })
    @GetMapping("/getAllfinancecount")
    public Object getAllfinancecount () {

        return null;
    }


}
