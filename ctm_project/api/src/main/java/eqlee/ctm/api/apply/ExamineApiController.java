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
@Api("报名审核Api")
@Slf4j
@RestController
@RequestMapping("/v1/app/examine")
public class ExamineApiController {

    @ApiOperation(value = "同行提交取消报名表的审核记录", notes = "同行取消修改报名表的审核记录")
    @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/insertCancelExamine")
    public Object cancelExamine () {

        return null;
    }

    @ApiOperation(value = "同行提交修改报名表的审核记录", notes = "同行提交修改报名表的审核记录")
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

    @ApiOperation(value = "通过取消报名表的审核（运营操作）", notes = "通过取消报名表的审核（运营操作）")
    @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/adoptCancelExamine")
    public Object adoptCancelExamine() {

        return null;
    }

    @ApiOperation(value = "通过修改报名表记录的审核（运营操作）", notes = "通过修改报名表记录的审核（运营操作）")
    @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/adoptUpdateExamine")
    public ResultVo adoptUpdateExamine () {

        return null;
    }


    @ApiOperation(value = "不通过取消或修改报名表的审核（运营操作）", notes = "不通过取消或修改报名表的审核（运营操作）")
    @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/notAdoptExamine")
    public ResultVo notAdoptExamine () {

        return null;
    }
}
