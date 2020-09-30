package eqlee.ctm.api.code.controller;

import com.yq.anntation.IgnoreResponseAdvice;
import com.yq.constanct.CodeType;
import com.yq.data.Result;
import com.yq.exception.ApplicationException;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.code.service.IPayInfoService;
import eqlee.ctm.api.entity.vo.ResultVo;
import eqlee.ctm.api.pay.entity.query.ResultQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author qf
 * @Date 2019/10/31
 * @Version 1.0
 */
@Slf4j
@Api("支付code控制层")
@RestController
@RequestMapping("/v1/code")
public class CodeController {

    @Autowired
    private LocalUser localUser;

    @Autowired
    private IPayInfoService payInfoService;

    @ApiOperation(value = "删除该用户的支付授权信息", notes = "删除该用户的支付授权信息")
    @GetMapping("/deletePayInfo")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Result deletePayInfo(){
        UserLoginQuery user = localUser.getUser();

        Integer integer = payInfoService.deletePayInfo(user.getId());
        ResultVo vo = new ResultVo();
        if (integer <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "操作失败");
        }
        vo.setMsg("ok");
        return Result.SUCCESS(vo);
    }
}
