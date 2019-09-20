package eqlee.ctm.apply.price.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.exception.ApplicationException;
import eqlee.ctm.apply.line.entity.vo.ResultVo;
import eqlee.ctm.apply.price.entity.query.PriceQuery;
import eqlee.ctm.apply.price.entity.vo.PriceVo;
import eqlee.ctm.apply.price.service.IPriceService;
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
@Api("价格管理Api")
@Slf4j
@RestController
@RequestMapping("/v1/app/apply/price")
public class PriceController {

    @Autowired
    private IPriceService priceService;

    @ApiOperation(value = "价格设定", notes = "价格设定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AdultPrice", value = "成年价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "OldPrice", value = "老人价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "BabyPrice", value = "幼儿价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "ChildPrice", value = "小孩价格", required = true, dataType = "double", paramType = "path"),
    })
    @PostMapping("/insertPrice")
    @CrossOrigin
    public ResultVo insertPrice(@RequestBody PriceVo priceVo) {
        if (StringUtils.isBlank(priceVo.getStartTime()) || StringUtils.isBlank(priceVo.getEndTime())
        || StringUtils.isBlank(priceVo.getLineName()) || priceVo.getAdultPrice() == null
        || priceVo.getOldPrice() == null || priceVo.getBabyPrice() == null || priceVo.getChildPrice() ==null) {
            log.error("price param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"价格设定参数不能为空");
        }

        priceService.insertPrice(priceVo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }



    @ApiOperation(value = "批量价格修改", notes = "批量价格修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AdultPrice", value = "成年价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "OldPrice", value = "老人价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "BabyPrice", value = "幼儿价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "ChildPrice", value = "小孩价格", required = true, dataType = "double", paramType = "path"),
    })
    @PostMapping("/batchUpdatePrice")
    @CrossOrigin
    public ResultVo batchUpdatePrice(PriceVo priceVo) {
        if (StringUtils.isBlank(priceVo.getStartTime()) || StringUtils.isBlank(priceVo.getEndTime())
                || StringUtils.isBlank(priceVo.getLineName()) || priceVo.getAdultPrice() == null
                || priceVo.getOldPrice() == null || priceVo.getBabyPrice() == null || priceVo.getChildPrice() ==null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"价格修改参数不能为空");
        }

        priceService.batchUpdatePrice(priceVo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "由时间和线路对价格进行查询", notes = "由时间和线路对价格进行查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "OutDate", value = "出发时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Long", paramType = "path"),
    })
    @GetMapping("/queryPricePageByFilter")
    @CrossOrigin
    public Page<PriceVo> queryPricePageByFilter(@RequestParam("OutDate") String OutDate,
                                                @RequestParam("LineName") String LineName,
                                                @RequestParam("size") Integer size,
                                                @RequestParam("current") Integer current) {
        PriceQuery priceQuery =new PriceQuery();
        priceQuery.setOutDate(OutDate);
        priceQuery.setLineName(LineName);
        priceQuery.setSize(size);
        priceQuery.setCurrent(current);
        return priceService.queryPricePageByFilter(priceQuery);
    }


}
