package eqlee.ctm.api.apply;

import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.entity.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Author qf
 * @Date 2019/9/28
 * @Version 1.0
 */
@Api("报名Api--9090:apply")
@Slf4j
@RestController
@RequestMapping("/v1/app/apply")
public class ApplyApiController {

    @ApiOperation(value = "申请报名--9090:apply", notes = "申请报名--9090:apply")
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
    public ResultVo insertApply() {
        return null;
    }

    @ApiOperation(value = "可报名线路", notes = "可报名线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutDate", value = "出发日期", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineNameOrRegion", value = "线路名称或区域", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/listPageApply")
    public Object listPageApply () {

        return null;
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
    public Object listPageDoAplly () {

        return null;
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
    public Object toListPageDoAplly () {

        return null;
    }

    @ApiOperation(value = "查询同一公司的所有分页数据（同行的报名记录）", notes = "查询同一公司的所有分页数据（同行的报名记录）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutTime", value = "出行时间", required = false, dataType = "String", paramType = "path"),
    })
    @GetMapping("/listPageDoApplyCompany")
    public Object listPageDoApply2Company () {

        return null;
    }

    @ApiOperation(value = "根据ID查询一条报名记录", notes = "根据ID查询一条报名记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "报名Id", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/queryApply")
    public Object queryById (@RequestParam("Id") Long Id) {

        return null;
    }

    @ApiOperation(value = "我的报名记录", notes = "我的报名记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "OutTime", value = "出行时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名模糊查询", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/page2MeApply")
    public Object page2MeApply () {

        return null;
    }


    @ApiOperation(value = "分页查询月结的信息", notes = "分页查询月结的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出行时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "type", value = "类型(0-查询全部 1-未付款 2-已付款)", required = false, dataType = "int", paramType = "path")
    })
    @GetMapping("/queryMonthApply")
    public Object queryMonthApply () {
        return null;
    }

    @ApiOperation(value = "修改月结付款状态", notes = "修改月结付款状态")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/updateMonthType")
    public ResultVo updateMonthType () {

        return null;
    }
}
