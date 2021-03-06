package eqlee.ctm.apply.entry.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.entry.entity.bo.ApplyCompanyInfo;
import eqlee.ctm.apply.entry.entity.bo.ApplyCountBo;
import eqlee.ctm.apply.entry.entity.bo.ApplyCountCaiBo;
import eqlee.ctm.apply.entry.entity.bo.ApplyDoExaInfo;
import eqlee.ctm.apply.entry.entity.query.*;
import eqlee.ctm.apply.entry.entity.vo.ApplyCountVo;
import eqlee.ctm.apply.entry.entity.vo.ApplySeacherVo;
import eqlee.ctm.apply.entry.entity.vo.ApplyVo;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.entry.service.IExamineService;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/9/19
 * @Version 1.0
 */
@Api
@Slf4j
@RestController
@RequestMapping("/v1/app/apply")
public class ApplyController {

    @Autowired
    private IApplyService applyService;

    @Autowired
    private IExamineService examineService;

    @Autowired
    private LocalUser localUser;


    @ApiOperation(value = "申请报名", notes = "申请报名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "outDate", value = "出行日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "contactName", value = "联系人名字", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "contactTel", value = "联系电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "place", value = "接送地点", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "adultNumber", value = "成年人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "babyNumber", value = "幼儿人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "oldNumber", value = "老人人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "childNumber", value = "小孩人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "companyUserId", value = "同行id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "payType", value = "支付类型(月结,现结,面收)", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "marketAllPrice", value = "门市价", required = true, dataType = "double", paramType = "path")
    })
    @PostMapping("/insertApply")
    @CrossOrigin
    @CheckToken
    public ApplyPayResultQuery insertApply(@RequestBody ApplyVo applyVo) {
        if (StringUtils.isBlank(applyVo.getOutDate()) || StringUtils.isBlank(applyVo.getContactName()) ||
            StringUtils.isBlank(applyVo.getContactTel())  || StringUtils.isBlank(applyVo.getPlaceAddress()) ||
            StringUtils.isBlank(applyVo.getLineName()) || applyVo.getAdultNumber() == null ||
            applyVo.getBabyNumber() == null || applyVo.getOldNumber() == null || applyVo.getType() == null ||
            applyVo.getChildNumber() == null || StringUtils.isBlank(applyVo.getPayType()) || applyVo.getMarketAllPrice() == null
        || applyVo.getAdultPrice() == null || applyVo.getBabyPrice() == null || applyVo.getChildPrice() == null || applyVo.getOldPrice() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"申请报名表参数不能为空");
        }
        if (applyVo.getAdultNumber() < 0 || applyVo.getBabyNumber() < 0 || applyVo.getOldNumber() < 0
        || applyVo.getChildNumber() < 0) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"人数个数不能为负数");
        }

        if (applyVo.getType() != 0 && applyVo.getType() != 1 && applyVo.getType() != 2) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "传入type有误");
        }

        if (applyVo.getType() == 0 || applyVo.getType() == 2) {
            //报名或包团
            return applyService.insertApply(applyVo);
        }

        //补录
        return applyService.insertWithApply(applyVo);
    }


    @ApiOperation(value = "可报名线路", notes = "可报名线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineNameOrRegion", value = "线路名称或区域", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "lineName", value = "线路名称", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/listPageApply")
    @CrossOrigin
    @CheckToken
    public Page<ApplyQuery> listPageApply(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                          @RequestParam("OutDate") String OutDate, @RequestParam("LineNameOrRegion") String LineNameOrRegion,
                                          @RequestParam("lineName") String lineName) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        Page<ApplyQuery> page = new Page<>(current,size);
        return applyService.listPage2Apply(page,OutDate,LineNameOrRegion,lineName);
    }

    @ApiOperation(value = "运营审核未(已)审核的报名记录（可根据出发日期和线路模糊查询）", notes = "运营审核未审核的报名记录（可根据出发日期和线路模糊查询）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "类型(null-全部,0-报名审核,1-取消报名的审核)", required = false, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "applyDate", value = "报名时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "exaStatus", value = "审核状态(0-未审核  1-已审核)", required = false, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutDateEnd", value = "出发时间截止", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/listPageDoAplly")
    @CrossOrigin
    @CheckToken
    public Page<ApplyDoExaQuery> listPageDoAplly(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                               @RequestParam("OutDate") String OutDate, @RequestParam("LineName") Long LineName,
                                               @RequestParam("type") Integer type,@RequestParam("applyDate") String applyDate,
                                               @RequestParam("exaStatus") Integer exaStatus,@RequestParam("OutDateEnd") String OutDateEnd)  throws Exception {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        Page<ApplyDoExaQuery> page = new Page<>(current,size);

        return applyService.listPageDo2Apply(page,OutDate,LineName,type,applyDate,exaStatus,OutDateEnd);
    }


    @ApiOperation(value = "查询同一公司的所有分页数据（同行的报名记录）", notes = "查询同一公司的所有分页数据（同行的报名记录）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutTime", value = "出行时间", required = false, dataType = "String", paramType = "path"),
    })
    @GetMapping("/listPageDoApplyCompany")
    @CrossOrigin
    @CheckToken
    public Page<ApplyCompanyQuery> listPageDoApply2Company(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                                           @RequestParam("OutTime") String OutTime) {
        if (current == null || size == null || StringUtils.isBlank(OutTime)) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        UserLoginQuery user = localUser.getUser();
        Company company = applyService.queryCompany(user.getCompanyId());
        Page<ApplyCompanyQuery> page = new Page<>(current,size);
        return applyService.listPageDoApply2Company(page,OutTime,company.getCompanyName());

    }


    @ApiOperation(value = "根据ID查询一条报名记录", notes = "根据ID查询一条报名记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "报名Id", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/queryApply")
    @CrossOrigin
    @CheckToken
    public ApplySeacherVo queryById (@RequestParam("Id") Long Id) {
        return applyService.queryById(Id);
    }


    @ApiOperation(value = "我的报名记录", notes = "我的报名记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutTime", value = "出行时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "applyTime", value = "报名时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名模糊查询", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "roadName", value = "线路名", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "(0-已取消 1-未取消)", required = false, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "todayType", value = "(1-今天已报名 2-今天出行)", required = false, dataType = "int", paramType = "path")
    })
    @GetMapping("/page2MeApply")
    @CrossOrigin
    @CheckToken
    public Page<ApplyCompanyQuery> page2MeApply(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                                @RequestParam("OutTime") String OutTime, @RequestParam("LineName") String LineName,
                                                @RequestParam("applyTime") String applyTime,@RequestParam("type") Integer type,
                                                @RequestParam("companyUserId") Long companyUserId,@RequestParam("todayType") Integer todayType,
                                                @RequestParam("roadName") String roadName) {

        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        Page<ApplyCompanyQuery> page = new Page<>(current,size);
        UserLoginQuery user = localUser.getUser();

        String admin = "运营人员";
        String admin1 = "超级管理员";
        String admin2 = "管理员";
        if (admin.equals(user.getRoleName()) || admin1.equals(user.getRoleName()) || admin2.equals(user.getRoleName())) {
            return applyService.pageAdmin2Apply(page,LineName,OutTime,applyTime,type,companyUserId,todayType,roadName);
        }

        return applyService.pageMeApply(page,LineName,OutTime,applyTime,type,todayType,roadName, user);

    }


    @ApiOperation(value = "分页查询月结的信息", notes = "分页查询月结的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出行时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "类型(0-查询全部 1-未付款 2-已付款)", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/queryMonthApply")
    @CrossOrigin
    @CheckToken
    public Page<ApplyMonthQuery> queryMonthApply(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                                @RequestParam("outDate") String outDate, @RequestParam("type") Integer type) {

        if (current == null || size == null || type == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        Page<ApplyMonthQuery> page = new Page<>(current,size);
        return applyService.queryMonth2Apply(page,type,outDate);

    }


    @ApiOperation(value = "修改月结付款状态", notes = "修改月结付款状态")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/updateMonthType")
    @CrossOrigin
    @CheckToken
    public ResultVo updateMonthType(@RequestParam("id") Long id) {

        if (id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        applyService.updateMonthType(id);

        ResultVo vo = new ResultVo();
        vo.setResult("OK");
        return vo;
    }


    @ApiOperation(value = "根据订单号回收报名表", notes = "根据订单号回收报名表")
    @ApiImplicitParam(name = "applyNo", value = "报名单号", required = true, dataType = "String", paramType = "path")
    @GetMapping("/dopApply")
    @CrossOrigin
    @CheckToken
    public ResultVo dopApply(@RequestParam("applyNo") String applyNo) {

        if (StringUtils.isBlank(applyNo)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        applyService.dopApply(applyNo);

        ResultVo vo = new ResultVo();
        vo.setResult("OK");
        return vo;
    }


    @ApiOperation(value = "同行报名记录统计", notes = "同行报名记录统计")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "time", value = "查询年份", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "companyId", value = "同行id", required = false, dataType = "Long", paramType = "path"),
          @ApiImplicitParam(name = "caiType", value = "(0-未确认 1-已确认)", required = false, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "payType", value = "(0-现结 2-月结)", required = false, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "type", value = "(0-未支付 1-已支付)", required = false, dataType = "int", paramType = "path")
    })
    @GetMapping("/pageResultCountList")
    @CrossOrigin
    @CheckToken
    public Map<String,Object> pageResultCountList(@RequestParam("current") Integer current,
                                                  @RequestParam("size") Integer size,
                                                  @RequestParam("time") String time,
                                                  @RequestParam("type") Integer type,
                                                  @RequestParam("caiType") Integer caiType,
                                                  @RequestParam("payType") Integer payType,
                                                  @RequestParam("companyId") Long companyId,
                                                  @RequestParam("month") String month) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        UserLoginQuery user = localUser.getUser();
        Page<ApplyResultCountQuery> page = new Page<>(current,size);


        String admin = "运营人员";
        String admin1 = "超级管理员";
        String admin2 = "管理员";
        HashMap<String,Object> map = new HashMap<>();
        if (admin.equals(user.getRoleName()) || admin1.equals(user.getRoleName()) || admin2.equals(user.getRoleName())) {
            Page<ApplyResultCountQuery> data = applyService.pageResultAdminCountList(page, time, type, caiType, payType, companyId,month);
            map.put("data",data);
            map.put("count",null);
            return map;
        }
        //得到分页的具体数据
        Page<ApplyResultCountQuery> data = applyService.pageResult2CountList(page, time, type , caiType, payType, user);
        //查询统计的数据
        ApplyCountBo applyCountBo = applyService.queryApplyCount();
        List<ApplyCountBo> list = new ArrayList<>();
        list.add(applyCountBo);
        map.put("count",list);
        map.put("data",data);
        return map;
    }


    @ApiOperation(value = "返回待付款的支付信息", notes = "返回待付款的支付信息")
    @ApiImplicitParam(name = "applyNo", value = "报名单号", required = true, dataType = "String", paramType = "path")
    @GetMapping("/queryPayInfo")
    @CrossOrigin
    @CheckToken
    public ApplyPayResultQuery queryPayInfo(@RequestParam("applyNo") String applyNo) {

        if (StringUtils.isBlank(applyNo)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        return applyService.queryPayInfo(applyNo);
    }


    @ApiOperation(value = "报名审核的详情", notes = "报名审核的详情")
    @ApiImplicitParam(name = "applyNo", value = "报名单号", required = true, dataType = "String", paramType = "path")
    @GetMapping("/queryApplyDoExaInfo")
    @CrossOrigin
    @CheckToken
    public ApplyDoExaInfo queryApplyDoExaInfo(@RequestParam("applyNo") String applyNo) {

        if (StringUtils.isBlank(applyNo)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        return applyService.queryApplyDoExaInfo(applyNo);
    }


    @ApiOperation(value = "查询统计的详情", notes = "查询统计的详情")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "payType", value = "0-现结 2-月结", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "outDate", value = "出行年月", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "companyName", value = "公司", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryCountInfo")
    @CrossOrigin
    @CheckToken
    public Page<ApplyCountVo> queryCountInfo(@RequestParam("current") Integer current,
                                             @RequestParam("size") Integer size,
                                             @RequestParam("payType") Integer payType,
                                             @RequestParam("outDate") String outDate,
                                             @RequestParam("companyName") String companyName,
                                             @RequestParam("lineName") String lineName) {

        if (StringUtils.isBlank(outDate) || StringUtils.isBlank(companyName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        Page<ApplyCountVo> page = new Page<>(current,size);
        return applyService.queryCountInfo(page,outDate,companyName,lineName,payType);
    }



    @ApiOperation(value = "财务月结统计", notes = "财务月结统计")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "time", value = "查询年份", required = false, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "companyId", value = "同行id", required = false, dataType = "Long", paramType = "path"),
          @ApiImplicitParam(name = "caiType", value = "(0-未确认 1-已确认)", required = false, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "payType", value = "(0-现结 2-月结)", required = false, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "type", value = "(0-未支付 1-已支付)", required = false, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "month", value = "查询月份", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryCaiMonthCount")
    @CrossOrigin
    @CheckToken
    public Page<ApplyResultCountQuery> queryCaiMonthCount(@RequestParam("current") Integer current,
                                                  @RequestParam("size") Integer size,
                                                  @RequestParam("time") String time,
                                                  @RequestParam("type") Integer type,
                                                  @RequestParam("caiType") Integer caiType,
                                                  @RequestParam("payType") Integer payType,
                                                  @RequestParam("companyId") Long companyId,
                                                  @RequestParam("month") String month) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        Page<ApplyResultCountQuery> page = new Page<>(current, size);


        return applyService.pageResultAdminCountList(page, time, type, caiType, payType, companyId,month);
    }



    @ApiOperation(value = "财务月结统计详情", notes = "财务月结统计详情")
    @ApiImplicitParams({
          @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "payType", value = "(0-现结 2-月结)", required = true, dataType = "int", paramType = "path"),
          @ApiImplicitParam(name = "outDate", value = "出行年月", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "companyName", value = "公司", required = true, dataType = "String", paramType = "path"),
          @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryCountInfo2")
    @CrossOrigin
    @CheckToken
    public Map<String,Object> queryCountInfo2(@RequestParam("current") Integer current,
                                                 @RequestParam("size") Integer size,
                                                 @RequestParam("payType") Integer payType,
                                                 @RequestParam("outDate") String outDate,
                                                 @RequestParam("companyName") String companyName,
                                                 @RequestParam("lineName") String lineName,
                                                 @RequestParam("id") Long id) {

        if (StringUtils.isBlank(outDate) || StringUtils.isBlank(companyName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        Page<ApplyCountVo> page = new Page<>(current,size);
        Page<ApplyCountCaiBo> boPage = applyService.queryCountInfo2(page, outDate, companyName, lineName, payType);

        ApplyCompanyInfo info = applyService.queryInfo(id);
        Map<String,Object> map = new HashMap<>();
        map.put("page",boPage);
        map.put("count",info);

        return map;
    }


    @ApiOperation(value = "财务确认", notes = "财务确认")
    @ApiImplicitParams({
                @ApiImplicitParam(name = "outDate", value = "每月第一天", required = true, dataType = "String", paramType = "path"),
                @ApiImplicitParam(name = "companyName", value = "公司", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/upCaiMonthCount")
    @CrossOrigin
    @CheckToken
    public ResultVo upCaiMonthCount(@RequestParam("outDate") String outDate,
                                    @RequestParam("companyName") String companyName) {

        if (StringUtils.isBlank(outDate) || StringUtils.isBlank(companyName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
         applyService.upCaiMonthCount(outDate,companyName);

        ResultVo vo = new ResultVo();
        vo.setResult("ok");
        return vo;
    }



    @GetMapping("/queryCount")
    @CrossOrigin
    @CheckToken
    public ApplyExaCountQuery queryCount() {
        return applyService.queryCount();
    }

    @ApiOperation(value = "导游未选的单数统计", notes = "导游未选的单数统计")
    @GetMapping("/guiderCount")
    @CrossOrigin
    @CheckToken
    public ApplyExaCountQuery guiderCount() {
        return applyService.guiderCount();
    }

    @ApiOperation(value = "报名未审核统计", notes = "报名未审核统计")
    @GetMapping("/queryExamineCount")
    @CrossOrigin
    @CheckToken
    public ApplyExaCountQuery queryExamineCount() {

        return applyService.queryExamineCount2();
//        return applyService.queryExamineCount();
    }

    @ApiOperation(value = "获取报名审核记录信息", notes = "获取报名审核记录信息")
    @ApiImplicitParam(name = "applyId", value = "报名id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryApplyExamRecord")
    @CrossOrigin
    @CheckToken
    public List<ApplyExamRecord> queryApplyExamRecord(@RequestParam("applyId") Long applyId) throws Exception {

        if (applyId == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        return examineService.queryExamRecord(applyId);
    }

}
