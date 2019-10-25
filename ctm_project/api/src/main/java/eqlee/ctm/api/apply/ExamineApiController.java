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
@Api("报名审核Api--9090:apply")
@Slf4j
@RestController
@RequestMapping("/v1/app/examine")
public class ExamineApiController {

    @ApiOperation(value = "同行提交取消报名表的审核记录--9090:apply", notes = "同行取消修改报名表的审核记录--9090:apply")
    @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/insertCancelExamine")
    public Object cancelExamine () {

        return null;
    }

    @ApiOperation(value = "修改报名表", notes = "修改报名表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "connectName", value = "联系人", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "connectTel", value = "联系电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "place", value = "接送地点", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/insertUpdateExamine")
    @CrossOrigin
    @CheckToken
    public Object insertUpdateExamine () {

        return null;
    }

    @ApiOperation(value = "通过或拒绝-取消报名表的审核", notes = "通过或拒绝-取消报名表的审核）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "通过（1）或取消（2）", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/adoptCancelExamine")
    public Object adoptCancelExamine() {

        return null;
    }

    @ApiOperation(value = "通过或拒绝报名审核", notes = "通过或拒绝报名审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "通过（1）或取消（2）", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/notAdoptExamine")
    public ResultVo doptExamine() {

        return null;
    }


    @ApiOperation(value = "展示修改报名表记录", notes = "展示修改报名表记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/listUpdateInfo")
    public ResultVo listUpdateInfo(@RequestParam("current") Integer current,
                                   @RequestParam("size") Integer size) {

        return null;
    }

    @ApiOperation(value = "展示修改记录详情", notes = "展示修改记录详情")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryUpdateInfo")
    public Object queryUpdateInfo () {

        return null;
    }
}
