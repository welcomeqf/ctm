package eqlee.ctm.apply.entry.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.entry.entity.query.ApplyCompanyQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyDoQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyQuery;
import eqlee.ctm.apply.entry.entity.query.ApplyUpdateInfo;
import eqlee.ctm.apply.entry.entity.vo.ApplyVo;
import eqlee.ctm.apply.entry.service.IApplyService;
import eqlee.ctm.apply.exception.ApplicationException;
import eqlee.ctm.apply.line.entity.vo.LineVo;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "申请报名", notes = "申请报名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "OutDate", value = "出行日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "CompanyName", value = "同行公司名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "CompanyUser", value = "同行代表人", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ContactName", value = "联系人名字", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ContactTel", value = "联系电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Place", value = "接送地点", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AdultNumber", value = "成年人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "BabyNumber", value = "幼儿人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OldNumber", value = "老人人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "ChildNumber", value = "小孩人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "PayType", value = "支付类型", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/insertApply")
    @CrossOrigin
    public ResultVo insertApply(@RequestBody ApplyVo applyVo) {
        if (StringUtils.isBlank(applyVo.getOutDate()) || StringUtils.isBlank(applyVo.getCompanyName()) ||
            StringUtils.isBlank(applyVo.getCompanyUser()) || StringUtils.isBlank(applyVo.getContactName()) ||
            StringUtils.isBlank(applyVo.getContactTel()) || StringUtils.isBlank(applyVo.getPlace()) ||
            StringUtils.isBlank(applyVo.getRegion()) || StringUtils.isBlank(applyVo.getLineName()) ||
            applyVo.getAdultNumber() == null || applyVo.getBabyNumber() == null || applyVo.getOldNumber() == null ||
            applyVo.getChildNumber() == null || StringUtils.isBlank(applyVo.getPayType())) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"申请报名表参数不能为空");
        }
        applyService.insertApply(applyVo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "展示报名首页", notes = "展示报名首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineNameOrRegion", value = "线路名称或区域", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/listPageApply")
    @CrossOrigin
    public Page<ApplyQuery> listPageApply(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                          @RequestParam("OutDate") String OutDate, @RequestParam("LineNameOrRegion") String LineNameOrRegion) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        Page<ApplyQuery> page = new Page<>(current,size);
        return applyService.listPageApply(page,OutDate,LineNameOrRegion);
    }

    @ApiOperation(value = "查询所有分页数据（可根据订单号和线路模糊查询）", notes = "查询所有分页数据（可根据订单号和线路模糊查询）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "ApplyNo", value = "订单号", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/listPageDoAplly")
    @CrossOrigin
    public Page<ApplyDoQuery> listPageDoAplly(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                    @RequestParam("ApplyNo") String ApplyNo, @RequestParam("LineName") String LineName) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        Page<ApplyDoQuery> page = new Page<>(current,size);

        return applyService.listPageDoApply(page,ApplyNo,LineName);
    }

    @ApiOperation(value = "查询同一公司的所有分页数据（可根据时间进行条件查询）", notes = "查询同一公司的所有分页数据（可根据时间进行条件查询）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "ApplyNo", value = "订单号", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/listPageDoApplyCompany")
    @CrossOrigin
    public Page<ApplyCompanyQuery> listPageDoApply2Company(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
                                                           @RequestParam("OutTime") String OutTime, @RequestParam("CompanyName") String CompanyName) {
        if (current == null || size == null || StringUtils.isBlank(CompanyName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        Page<ApplyCompanyQuery> page = new Page<>(current,size);
        return applyService.listPageDoApply2Company(page,OutTime,CompanyName);

    }
}
