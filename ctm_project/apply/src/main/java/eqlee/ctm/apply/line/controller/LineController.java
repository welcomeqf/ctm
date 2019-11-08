package eqlee.ctm.apply.line.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.line.entity.Line;
import eqlee.ctm.apply.line.entity.query.LineSeacherQuery;
import eqlee.ctm.apply.line.entity.vo.LineUpdateVo;
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
@RequestMapping("/v1/app/line")
public class LineController {

    @Autowired
    private ILineService lineService;

    @ApiOperation(value = "增加线路", notes = "增加线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "information", value = "线路简介", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "travelSituation", value = "出游情况（几日游）", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "maxNumber", value = "最大人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "minNumber", value = "最小人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "city", value = "城市", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "picturePath", value = "图片路径", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/insertLine")
    @CrossOrigin
    @CheckToken
    public ResultVo insertLine(@RequestBody LineVo lineVo) {
        if (StringUtils.isBlank(lineVo.getLineName()) || lineVo.getMinNumber() == null
        || StringUtils.isBlank(lineVo.getRegion()) || lineVo.getTravelSituation() == null) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }

        if (lineVo.getMaxNumber() <= 0) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "最大人数要大于零");
        }

        if (lineVo.getMinNumber() < 0) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "最小人数不能为负数");
        }

        lineService.insertLine(lineVo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "修改线路", notes = "修改线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "information", value = "线路简介", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "travelSituation", value = "出游情况（几日游）", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "stopped", value = "是否停用(false-正常 1-禁用true)", required = true, dataType = "Boolean", paramType = "path"),
            @ApiImplicitParam(name = "maxNumber", value = "最大人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "minNumber", value = "最小人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/updateLine")
    @CrossOrigin
    @CheckToken
    public ResultVo updateLine(@RequestBody LineUpdateVo line) {
        if (line.getId() == null) {
            log.error("update line param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"修改线路参数不能为空");
        }

        lineService.updateLine(line,line.getId());

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "模糊查询分页查询所有线路", notes = "模糊查询分页查询所有线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/listLine")
    @CrossOrigin
    @CheckToken
    public Page<LineSeacherQuery> listLine(@RequestParam("current") Integer current,
                                           @RequestParam("size") Integer size,
                                           @RequestParam("lineName") String lineName) {
        if (current == null || size == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"线路分页查询线路失败");
        }
        Page<LineSeacherQuery> pageQuery = new Page<>(current,size);
        return lineService.listPageLine(pageQuery,lineName);
    }

    @ApiOperation(value = "停用线路", notes = "停用线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/stopLine/{Id}")
    @CrossOrigin
    @CheckToken
    public ResultVo stopLine(@PathVariable("Id") Long Id) {
        if (Id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        lineService.updateStatus(Id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "启用线路", notes = "启用线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/startLine/{Id}")
    @CrossOrigin
    @CheckToken
    public ResultVo startLine(@PathVariable("Id") Long Id) {
        if (Id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        lineService.updateNormal(Id);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "根据Id查询一条线路", notes = "根据Id查询一条线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryLineById")
    @CrossOrigin
    @CheckToken
    public Line queryLineById(@RequestParam("Id") Long Id) {
        if (Id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        return lineService.queryOneLine(Id);
    }


    @ApiOperation(value = "根据Id删除一条线路", notes = "根据Id删除一条线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/deleteLine/{Id}")
    @CrossOrigin
    @CheckToken
    public ResultVo deleteLine(@PathVariable("Id") Long Id) {
        if (Id == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        lineService.deleteLine(Id);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "查询所有线路", notes = "查询所有线路")
    @GetMapping("/listAllLine")
    @CrossOrigin
    @CheckToken
    public List<Line> listAllLine () {
        return lineService.listAllLine();
    }

}
