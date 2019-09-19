package eqlee.ctm.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import eqlee.ctm.user.entity.User;
import eqlee.ctm.user.entity.query.UserLoginQuery;
import eqlee.ctm.user.entity.query.UserQuery;
import eqlee.ctm.user.entity.vo.ResultVo;
import eqlee.ctm.user.entity.vo.UserVo;
import eqlee.ctm.user.exception.ApplicationException;
import eqlee.ctm.user.service.IUserService;
import com.yq.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public UserLoginQuery login(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
        }
        return userService.login(userName, password);

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


    @ApiOperation(value = "子用户注册", notes = "子用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "companyName", value = "公司名", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/downRegister")
    @CrossOrigin
    public ResultVo downRegister(@RequestBody UserVo userVo) {
        if (StringUtils.isBlank(userVo.getUserName()) || StringUtils.isBlank(userVo.getPassword())
                || StringUtils.isBlank(userVo.getName()) || StringUtils.isBlank(userVo.getPhone()) ||
                StringUtils.isBlank(userVo.getRoleName()) || StringUtils.isBlank(userVo.getCompanyName())) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        userService.dowmRegister(userVo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "分页查询用户信息", notes = "分页查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/pageListUser")
    @CrossOrigin
    public Page<UserQuery> pageListUser(@RequestParam("current") Integer current,
                                        @RequestParam("size") Integer size) {
       if (current == null || size == null) {
           throw new ApplicationException(CodeType.PARAM_ERROR,"分页查询用户参数不能为空");
       }
       Page<UserQuery> page = new Page<>(current,size);
        return userService.queryAllUserByPage(page);

    }

    /**
     * 该接口作为用户模块以及子用户设置的共同接口
     * @param current
     * @param size
     * @param userName
     * @param roleName
     * @return
     */
    @ApiOperation(value = "对用户名模糊以及角色帅选查询分页用户信息", notes = "对用户名模糊以及角色帅选查询分页用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "userName", value = "用户名或用户名一部分", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryPageUserByName")
    @CrossOrigin
    public Page<UserQuery> queryPageUserByName(@RequestParam("current") Integer current,
                                               @RequestParam("size") Integer size,
                                               @RequestParam("userName") String userName,
                                               @RequestParam("roleName") String roleName) {
        if (current == null || size == null || StringUtils.isBlank(userName) || StringUtils.isBlank(roleName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"分页查询用户参数不能为空");
        }
        Page<UserQuery> page = new Page<>(current,size);
        return userService.queryPageUserByName(page,userName,roleName);

    }

    @ApiOperation(value = "模糊查询加分页", notes = "模糊查询加分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "userName", value = "用户名或用户名一部分", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryUserByName")
    @CrossOrigin
    public Page<UserQuery> queryUserByName(@RequestParam("current") Integer current,
                                               @RequestParam("size") Integer size,
                                               @RequestParam("userName") String userName) {
        if (current == null || size == null || StringUtils.isBlank(userName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"模糊加分页查询用户参数不能为空");
        }
        Page<UserQuery> page = new Page<>(current,size);
        return userService.queryUserByName(page,userName);

    }

}
