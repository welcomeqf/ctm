package eqlee.ctm.apply.line.controller;

import com.yq.constanct.CodeType;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.channle.entity.Channel;
import eqlee.ctm.apply.channle.service.IChannelService;
import eqlee.ctm.apply.exception.ApplicationException;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.entity.query.LineQuery;
import eqlee.ctm.apply.line.entity.vo.LineVo;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.line.service.ILineService;
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
 * @Date 2019/9/18
 * @Version 1.0
 */
@RestController
@Slf4j
@Api("线路设置Api")
@RequestMapping("/v1/app/apply/line")
public class LineController {

    @Autowired
    private ILineService lineService;

    @ApiOperation(value = "增加线路", notes = "增加线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Information", value = "线路简介", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "TravelSituation", value = "出游情况（几日游）", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "MaxNumber", value = "最大人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "MinNumber", value = "最小人数", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/insertLine")
    @CrossOrigin
    public ResultVo insertLine(@RequestBody LineVo lineVo) {
        if (StringUtils.isBlank(lineVo.getLineName()) || StringUtils.isBlank(lineVo.getInformation())
        || StringUtils.isBlank(lineVo.getRegion()) || StringUtils.isBlank(lineVo.getTravelSituation())) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }

        lineService.insertLine(lineVo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "修改线路", notes = "修改线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Information", value = "线路简介", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "TravelSituation", value = "出游情况（几日游）", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Stopped", value = "是否停用(false-正常 1-禁用true)", required = true, dataType = "Boolean", paramType = "path"),
            @ApiImplicitParam(name = "MaxNumber", value = "最大人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "MinNumber", value = "最小人数", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/updateLine")
    @CrossOrigin
    public ResultVo updateLine(@RequestBody Line line) {
        if (line.getId() == null) {
            log.error("update line param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"修改线路参数不能为空");
        }

        lineService.updateLine(line);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "查询所有线路", notes = "查询所有线路")
    @ApiImplicitParam(name = "dateTime", value = "出行日期", required = true, dataType = "String", paramType = "path")
    @GetMapping("/listLine")
    @CrossOrigin
    public List<LineQuery> listLine(@RequestParam("dateTime") String dateTime) {
        return lineService.listLine(dateTime);
    }

    @ApiOperation(value = "停用线路", notes = "停用线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "String", paramType = "path")
    @GetMapping("/stopLine")
    @CrossOrigin
    public ResultVo stopLine(@RequestParam("Id") Long Id) {
        if (Id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        lineService.updateStatus(Id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "启用线路", notes = "启用线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "String", paramType = "path")
    @GetMapping("/startLine")
    @CrossOrigin
    public ResultVo startLine(@RequestParam("Id") Long Id) {
        if (Id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        lineService.updateNormal(Id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

}
