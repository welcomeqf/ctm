package eqlee.ctm.apply.entry.controller;

import com.yq.constanct.CodeType;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.entry.entity.vo.ExamineAddInfoVo;
import eqlee.ctm.apply.entry.entity.vo.ExamineVo;
import eqlee.ctm.apply.entry.service.IExamineService;
import eqlee.ctm.apply.exception.ApplicationException;
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
@RequestMapping("/v1/app/apply/examine")
public class ExamineController {

    @Autowired
    private IExamineService examineService;

    @ApiOperation(value = "增加一条取消报名记录审核的记录", notes = "增加一条取消报名记录审核的记录")
    @ApiImplicitParam(name = "ApplyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/insertCancelExamine")
    @CrossOrigin
    public ResultVo CancelExamine(@RequestBody ExamineAddInfoVo infoVo) {
        if (infoVo.getApplyId() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        examineService.CancelExamine(infoVo.getApplyId());

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "增加一条修改报名表审核的记录", notes = "增加一条修改报名表审核的记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ApplyId", value = "报名Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "ConnectName", value = "联系人", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "ConnectTel", value = "联系电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Place", value = "接送地点", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/insertUpdateExamine")
    @CrossOrigin
    public ResultVo CancelExamine(@RequestBody ExamineVo vo) {
        if (vo.getApplyId() == null || StringUtils.isBlank(vo.getConnectName()) ||
            StringUtils.isBlank(vo.getConnectTel()) || StringUtils.isBlank(vo.getPlace())) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        examineService.UpdateApplyExamine(vo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "通过取消报名表的审核", notes = "通过取消报名表的审核")
    @ApiImplicitParam(name = "ApplyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/AdoptCancelExamine")
    @CrossOrigin
    public ResultVo AdoptCancelExamine(@RequestBody ExamineAddInfoVo infoVo) {
        if (infoVo.getApplyId() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        examineService.AdoptCancelExamine(infoVo.getApplyId());

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "通过修改报名表记录的审核", notes = "通过修改报名表记录的审核")
    @ApiImplicitParam(name = "ApplyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/AdoptUpdateExamine")
    @CrossOrigin
    public ResultVo AdoptUpdateExamine(@RequestBody ExamineAddInfoVo infoVo) {
        if (infoVo.getApplyId() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        examineService.AdoptUpdateExamine(infoVo.getApplyId());

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "不通过取消报名表的审核", notes = "不通过取消报名表的审核")
    @ApiImplicitParam(name = "ApplyId", value = "报名Id", required = true, dataType = "Long", paramType = "path")
    @PostMapping("/NotAdoptExamine")
    @CrossOrigin
    public ResultVo NotAdoptExamine(@RequestBody ExamineAddInfoVo infoVo) {
        if (infoVo.getApplyId() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        examineService.NotAdoptExamine(infoVo.getApplyId());

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }
}
