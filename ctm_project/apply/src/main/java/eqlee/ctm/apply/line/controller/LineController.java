package eqlee.ctm.apply.line.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.exception.ApplicationException;
import eqlee.ctm.apply.jwt.islogin.CheckToken;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.entity.query.LinePageQuery;
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
            @ApiImplicitParam(name = "TravelSituation", value = "出游情况（几日游）", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "MaxNumber", value = "最大人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "MinNumber", value = "最小人数", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/insertLine")
    @CrossOrigin
    @CheckToken
    public ResultVo insertLine(@RequestBody LineVo lineVo) {
        if (StringUtils.isBlank(lineVo.getLineName()) || StringUtils.isBlank(lineVo.getInformation())
        || StringUtils.isBlank(lineVo.getRegion()) || lineVo.getTravelSituation() == null) {
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
            @ApiImplicitParam(name = "TravelSituation", value = "出游情况（几日游）", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "Stopped", value = "是否停用(false-正常 1-禁用true)", required = true, dataType = "Boolean", paramType = "path"),
            @ApiImplicitParam(name = "MaxNumber", value = "最大人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "MinNumber", value = "最小人数", required = true, dataType = "int", paramType = "path")
    })
    @PutMapping("/updateLine")
    @CrossOrigin
    @CheckToken
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

    @ApiOperation(value = "分页查询所有线路", notes = "分页查询所有线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/listLine")
    @CrossOrigin
    @CheckToken
    public Page<Line> listLine(@RequestParam("current") Integer current,
                                        @RequestParam("size") Integer size) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"线路分页查询线路失败");
        }
        LinePageQuery pageQuery = new LinePageQuery();
        pageQuery.setCurrent(current);
        pageQuery.setSize(size);
        return lineService.listPageLine(pageQuery);
    }

    @ApiOperation(value = "停用线路", notes = "停用线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "String", paramType = "path")
    @PutMapping("/stopLine")
    @CrossOrigin
    @CheckToken
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
    @PutMapping("/startLine")
    @CrossOrigin
    @CheckToken
    public ResultVo startLine(@RequestParam("Id") Long Id) {
        if (Id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        lineService.updateNormal(Id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "根据Id查询一条线路", notes = "根据Id查询一条线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "String", paramType = "path")
    @GetMapping("/queryLineById")
    @CrossOrigin
    @CheckToken
    public Line queryLineById(@RequestParam("Id") Long Id) {
        if (Id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        return lineService.queryOneLine(Id);
    }

}
