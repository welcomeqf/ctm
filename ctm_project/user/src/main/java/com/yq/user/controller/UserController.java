package com.yq.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.constanct.CodeType;
import com.yq.user.entity.User;
import com.yq.user.entity.vo.ResultVo;
import com.yq.user.entity.vo.UserVo;
import com.yq.user.exception.ApplicationException;
import com.yq.user.service.IUserService;
import com.yq.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author qf
 * @Date 2019/9/12
 * @Version 1.0
 */
@Slf4j
@Api
@RestController
@RequestMapping("/v1/app/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "companyName", value = "公司名", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/register")
    @CrossOrigin
    public ResultVo register(@RequestBody UserVo userVo) {
        if (StringUtils.isBlank(userVo.getUserName()) || StringUtils.isBlank(userVo.getPassword())
        || StringUtils.isBlank(userVo.getName()) || StringUtils.isBlank(userVo.getPhone()) ||
        StringUtils.isBlank(userVo.getRoleName()) || StringUtils.isBlank(userVo.getCompanyName())) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        userService.register(userVo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "登录", notes ="登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/login")
    @CrossOrigin
    public ResultVo login(@RequestParam("userName") String userName, @RequestParam("password") String password,
                            HttpServletResponse response) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
        }
        User user = userService.login(userName, password);

        ResultVo resultVo = new ResultVo();
        if (user != null) {
            //将登录信息存入cookie中
            Object json = JSONObject.toJSON(user);
            Cookie cookie = new Cookie("login_user",json.toString());
            cookie.setMaxAge(60 * 60 *24 *30);
            cookie.setPath("/");
            response.addCookie(cookie);

            resultVo.setResult("登录成功");
            return resultVo;
        }
        resultVo.setResult("登录失败");
        return resultVo;
    }

    @ApiOperation(value = "注销", notes = "注销")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/deleteUser")
    @CrossOrigin
    public ResultVo deleteUser(@RequestParam("userName") String userName) {
        if (StringUtils.isBlank(userName)) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        userService.deleteUser(userName);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");

        return resultVo;
    }

    @ApiOperation(value = "退出账号", notes = "退出账号")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/exitUser")
    @CrossOrigin
    public ResultVo exitUser(@RequestParam("userName") String userName) {
        if (StringUtils.isBlank(userName)) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        userService.exitUser(userName);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");

        return resultVo;
    }

    @ApiOperation(value = "冻结账户", notes = "冻结账户")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/stopUser")
    @CrossOrigin
    public ResultVo stopUser(@RequestParam("userName") String userName) {
        if (StringUtils.isBlank(userName)) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        userService.stopUser(userName);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");

        return resultVo;
    }

    @ApiOperation(value = "解冻账户", notes = "解冻账户")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/toStopUser")
    @CrossOrigin
    public ResultVo toStopUser(@RequestParam("userName") String userName) {
        if (StringUtils.isBlank(userName)) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        userService.toStopUser(userName);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");

        return resultVo;
    }
}
