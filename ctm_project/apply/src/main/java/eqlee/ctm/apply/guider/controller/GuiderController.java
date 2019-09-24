package eqlee.ctm.apply.guider.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import eqlee.ctm.apply.exception.ApplicationException;
import eqlee.ctm.apply.guider.entity.vo.GuiderVo;
import eqlee.ctm.apply.guider.service.IGuiderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Claire
 * @Date 2019/9/23 0023
 * @Version 1.0
 */
@Api("选人组团")
@Slf4j
@RestController
@RequestMapping("/v1/app/apply/guider")
public class GuiderController {

    @Autowired
    private IGuiderService guiderService;

    @ApiOperation(value = "导游首页",notes = "导游首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Long", paramType = "path")
    })
    @GetMapping("guiderindex")
    @CrossOrigin
    public Page<GuiderVo> guiderindex(Integer current,Integer size){
        if(current == null || size == null){
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        return guiderService.guiderIndex(current,size);
    }

}
