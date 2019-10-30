package eqlee.ctm.apply.entry.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.entry.entity.Apply;
import eqlee.ctm.apply.entry.entity.query.*;
import eqlee.ctm.apply.entry.entity.vo.ApplySeacherVo;
import eqlee.ctm.apply.entry.entity.vo.ApplyVo;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @ApiImplicitParam(name = "payType", value = "支付类型(月结,现结,面收)", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/insertApply")
    @CrossOrigin
    @CheckToken
    public ApplyPayResultQuery insertApply(@RequestBody ApplyVo applyVo) {
        if (StringUtils.isBlank(applyVo.getOutDate()) || StringUtils.isBlank(applyVo.getContactName()) ||
            StringUtils.isBlank(applyVo.getContactTel()) || StringUtils.isBlank(applyVo.getPlace()) ||
            StringUtils.isBlank(applyVo.getLineName()) || applyVo.getAdultNumber() == null ||
            applyVo.getBabyNumber() == null || applyVo.getOldNumber() == null ||
            applyVo.getChildNumber() == null || StringUtils.isBlank(applyVo.getPayType())) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"申请报名表参数不能为空");
        }
        if (applyVo.getAdultNumber() < 0 || applyVo.getBabyNumber() < 0 || applyVo.getOldNumber() < 0
        || applyVo.getChildNumber() < 0) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"人数个数不能为负数");
        }
        UserLoginQuery user = localUser.getUser("用户信息");
        applyVo.setCompanyUser(user.getAccount());
        applyVo.setCompanyNameId(user.getCompanyId());
        applyVo.setCreateUserId(user.getId());
        applyVo.setUpdateUserId(user.getId());

        return applyService.insertApply(applyVo);
    }


    @ApiOperation(value = "可报名线路", notes = "可报名线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineNameOrRegion", value = "线路名称或区域", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/listPageApply")
    @CrossOrigin
    @CheckToken
    public Page<ApplyQuery> listPageApply(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                          @RequestParam("OutDate") String OutDate, @RequestParam("LineNameOrRegion") String LineNameOrRegion) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        Page<ApplyQuery> page = new Page<>(current,size);
        return applyService.listPageApply(page,OutDate,LineNameOrRegion);
    }

    @ApiOperation(value = "运营审核未审核的报名记录（可根据出发日期和线路模糊查询）", notes = "运营审核未审核的报名记录（可根据出发日期和线路模糊查询）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ApplyType", value = "类型(报名审核,取消报名的审核)", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/listPageDoAplly")
    @CrossOrigin
    @CheckToken
    public Page<ApplyDoExaQuery> listPageDoAplly(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                              @RequestParam("OutDate") String OutDate, @RequestParam("LineName") String LineName,
                                               @RequestParam("ApplyType") String ApplyType) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        Page<ApplyDoExaQuery> page = new Page<>(current,size);

        return applyService.listPageDoApply(page,OutDate,LineName,ApplyType);
    }



    @ApiOperation(value = "运营审核已审核的报名记录（可根据出发日期和线路模糊查询）", notes = "运营审核已审核的报名记录（可根据出发日期和线路模糊查询）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ApplyType", value = "类型（报名审核,取消报名的审核）", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/toListPageDoAplly")
    @CrossOrigin
    @CheckToken
    public Page<ApplyDoExaQuery> toListPageDoAplly(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                              @RequestParam("OutDate") String OutDate, @RequestParam("LineName") String LineName,
                                              @RequestParam("ApplyType") String ApplyType) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }


        Page<ApplyDoExaQuery> page = new Page<>(current,size);

        return applyService.toListPageDoApply(page,OutDate,LineName,ApplyType);
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
        UserLoginQuery user = localUser.getUser("用户信息");
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
            @ApiImplicitParam(name = "LineName", value = "线路名模糊查询", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/page2MeApply")
    @CrossOrigin
    @CheckToken
    public Page<ApplyCompanyQuery> page2MeApply(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                             @RequestParam("OutTime") String OutTime, @RequestParam("LineName") String LineName) {

        Page<ApplyCompanyQuery> page = new Page<>(current,size);
        return applyService.page2MeApply(page,OutTime,LineName);

    }
}
