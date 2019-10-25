package eqlee.ctm.apply.entry.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.entry.entity.Examine;
import eqlee.ctm.apply.entry.entity.vo.ExamineAddInfoVo;
import eqlee.ctm.apply.entry.entity.vo.ExamineInfoVo;
import eqlee.ctm.apply.entry.entity.vo.ExamineUpdateInfoVo;
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

    @ApiOperation(value = "通过或拒绝-取消报名表的审核", notes = "通过或拒绝-取消报名表的审核）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "通过（1）或取消（2）", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/adoptCancelExamine")
    @CrossOrigin
    @CheckToken
    public ResultVo adoptCancelExamine(@RequestBody ExamineAddInfoVo infoVo) {
        if (infoVo.getApplyId() == null || infoVo.getStatus() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        if (infoVo.getStatus() != 1 && infoVo.getStatus() != 2) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"status传入有误");
        }

        if (infoVo.getStatus() == 1) {
            examineService.AdoptCancelExamine(infoVo.getApplyId());
        }
        if (infoVo.getStatus() == 2) {
            examineService.NotAdoptCancelExamine(infoVo.getApplyId());
        }

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "通过或拒绝报名审核", notes = "通过或拒绝报名审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "报名Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "通过（1）或取消（2）", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/notAdoptExamine")
    @CrossOrigin
    @CheckToken
    public ResultVo notAdoptExamine(@RequestBody ExamineAddInfoVo infoVo) {
        if (infoVo.getApplyId() == null || infoVo.getStatus() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        if (infoVo.getStatus() != 1 && infoVo.getStatus() != 2) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"status传入有误");
        }

        if (infoVo.getStatus() == 1) {
            examineService.doptExamine(infoVo.getApplyId());
        }

        if (infoVo.getStatus() == 2) {
            examineService.NotAdoptExamine(infoVo.getApplyId());
        }

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "展示修改报名表记录", notes = "展示修改报名表记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/listUpdateInfo")
    @CrossOrigin
    @CheckToken
    public Page<ExamineUpdateInfoVo> listUpdateInfo(@RequestParam("current") Integer current,
                                        @RequestParam("size") Integer size) {
       Page<ExamineUpdateInfoVo> page = new Page<>(current,size);
        return examineService.listUpdateInfo(page);
    }


    @ApiOperation(value = "展示修改记录详情", notes = "展示修改记录详情")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryUpdateInfo")
    @CrossOrigin
    @CheckToken
    public ExamineInfoVo queryUpdateInfo(@RequestParam("Id") Long Id) {
        return examineService.queryUpdateInfo(Id);
    }
}
