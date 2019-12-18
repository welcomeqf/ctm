package eqlee.ctm.apply.guider.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.StringUtils;
import eqlee.ctm.apply.guider.entity.vo.ApplyVo;
import eqlee.ctm.apply.guider.entity.vo.GuiderVo;
import eqlee.ctm.apply.guider.service.IGuiderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Author Claire
 * @Date 2019/9/23 0023
 * @Version 1.0
 */
@Api("选人组团")
@Slf4j
@RestController
@RequestMapping("/v1/app/guider")
public class GuiderController {

    @Autowired
    private IGuiderService guiderService;

    @ApiOperation(value = "导游首页",notes = "导游首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "outDate", value = "出发时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "region", value = "区域", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/guiderIndex")
    @CrossOrigin
    @CheckToken
    public Page<GuiderVo> guiderIndex(@RequestParam("current") Integer current,
                                      @RequestParam("size") Integer size,
                                      @RequestParam("outDate") String outDate,
                                      @RequestParam("lineName") String lineName,
                                      @RequestParam("region") String region){
        if(current == null || size == null){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        Page<GuiderVo> page = new Page<>(current,size);
        return guiderService.guiderIndex(page,outDate,lineName,region);
    }




}
