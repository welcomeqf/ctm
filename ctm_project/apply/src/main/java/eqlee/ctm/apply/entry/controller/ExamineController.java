package eqlee.ctm.apply.entry.controller;

import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.entry.entity.vo.ExamineAddInfoVo;
import eqlee.ctm.apply.entry.entity.vo.ExamineVo;
import eqlee.ctm.apply.entry.service.IExamineService;
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
 * @Date 2019/9/20
 * @Version 1.0
 */
@Api
@Slf4j
@RestController
@RequestMapping("/v1/app/examine")
public class ExamineController {

    @Autowired
    private IExamineService examineService;

    @ApiOperation(value = "同行提交取消报名表的审核记录", notes = "同行取消修改报名表的审核记录")
    @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/insertCancelExamine")
    @CrossOrigin
    @CheckToken
    public ResultVo cancelExamine(@RequestBody ExamineAddInfoVo infoVo) {
        if (infoVo.getApplyId() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        examineService.CancelExamine(infoVo.getApplyId());

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
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
    public ResultVo insertUpdateExamine(@RequestBody ExamineVo vo) {
        if (vo.getApplyId() == null || StringUtils.isBlank(vo.getConnectName()) ||
            StringUtils.isBlank(vo.getConnectTel()) || StringUtils.isBlank(vo.getPlace())) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        examineService.UpdateApplyExamine(vo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "通过取消报名表的审核（运营操作）", notes = "通过取消报名表的审核（运营操作）")
    @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/adoptCancelExamine")
    @CrossOrigin
    @CheckToken
    public ResultVo adoptCancelExamine(@RequestBody ExamineAddInfoVo infoVo) {
        if (infoVo.getApplyId() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        examineService.AdoptCancelExamine(infoVo.getApplyId());

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "通过修改报名表记录的审核（运营操作）", notes = "通过修改报名表记录的审核（运营操作）")
    @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/adoptUpdateExamine")
    @CrossOrigin
    @CheckToken
    public ResultVo adoptUpdateExamine(@RequestBody ExamineAddInfoVo infoVo) {
        if (infoVo.getApplyId() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        examineService.AdoptUpdateExamine(infoVo.getApplyId());

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "不通过报名审核（运营操作）", notes = "不通过报名审核（运营操作）")
    @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/notAdoptExamine")
    @CrossOrigin
    @CheckToken
    public ResultVo notAdoptExamine(@RequestParam("applyId") Long applyId) {
        if (applyId == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        examineService.NotAdoptExamine(applyId);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "通过报名表的审核（运营操作）", notes = "通过报名表的审核（运营操作）")
    @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/doptExamine")
    @CrossOrigin
    @CheckToken
    public ResultVo doptExamine(@RequestParam("applyId") Long applyId) {
        if (applyId == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        examineService.doptExamine(applyId);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }
}
