package eqlee.ctm.user.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.utils.JwtUtil;
import eqlee.ctm.user.entity.query.*;
import eqlee.ctm.user.entity.vo.ResultVo;
import eqlee.ctm.user.entity.vo.UserUpdatePasswordVo;
import eqlee.ctm.user.entity.vo.UserUpdateVo;
import eqlee.ctm.user.entity.vo.UserVo;
import eqlee.ctm.user.service.IUserService;
import com.yq.utils.StringUtils;
import eqlee.ctm.user.vilidata.DataUtils;
import eqlee.ctm.user.vilidata.SignData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


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
            @ApiImplicitParam(name = "companyId", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    })
    @PostMapping("/register")
    @CrossOrigin
    public ResultVo register(@RequestBody UserVo userVo) {
        if (StringUtils.isBlank(userVo.getUserName()) || StringUtils.isBlank(userVo.getPassword())
        || StringUtils.isBlank(userVo.getName()) || StringUtils.isBlank(userVo.getPhone()) ||
                StringUtils.isBlank(userVo.getRoleName()) || userVo.getCompanyId() == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }

        userService.register(userVo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "登录", notes ="登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/login")
    @CrossOrigin
    public Map<String,Object> login(@RequestParam("userName") String userName, @RequestParam("password") String password,
                                @RequestParam("AppId") String AppId) throws Exception{
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password) || StringUtils.isBlank(AppId)) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        //验证签名
        String dcode = DataUtils.getDcodeing(AppId);
        Boolean result = SignData.getResult(dcode, userName);
        if (!result) {
            throw new ApplicationException(CodeType.AUTHENTICATION_ERROR);
        }
        LoginQuery query = userService.login(userName, password, AppId);

        UserLoginQuery loginQuery = new UserLoginQuery();
        loginQuery.setId(query.getId());
        loginQuery.setTel(query.getTel());
        loginQuery.setRoleName(query.getRoleName());
        loginQuery.setCompanyId(query.getCompanyId());
        loginQuery.setAccount(query.getAccount());
        loginQuery.setCname(query.getCname());
        loginQuery.setMenuList(query.getMenuList());

        //JWT验证
        Map<String,Object> map = new HashMap<>();

        //2592000000
        String token = JwtUtil.createJWT(86400000L, loginQuery);
        map.put("token", token);
        map.put("user", loginQuery);

        return map;

    }

    @ApiOperation(value = "注销", notes = "注销")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/deleteUser/{userName}/{AppId}")
    @CrossOrigin
    public ResultVo deleteUser(@PathVariable("userName") String userName, @PathVariable("AppId")String AppId) {
        if (StringUtils.isBlank(userName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        userService.deleteUser(userName,AppId);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");

        return resultVo;
    }

    @ApiOperation(value = "退出账号", notes = "退出账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/exitUser/{userName}/{AppId}")
    @CrossOrigin
    public ResultVo exitUser(@PathVariable("userName") String userName, @PathVariable("AppId")String AppId) {
        if (StringUtils.isBlank(userName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        userService.exitUser(userName,AppId);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");

        return resultVo;
    }

    @ApiOperation(value = "冻结账户", notes = "冻结账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/stopUser/{userName}/{AppId}")
    @CrossOrigin
    public ResultVo stopUser(@PathVariable("userName") String userName, @PathVariable("AppId")String AppId) {
        if (StringUtils.isBlank(userName)) {
            throw new ApplicationException(CodeType.PARAM_ERROR);
        }
        userService.stopUser(userName,AppId);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");

        return resultVo;
    }

    @ApiOperation(value = "解冻账户", notes = "解冻账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/toStopUser/{userName}/{AppId}")
    @CrossOrigin
    public ResultVo toStopUser(@PathVariable("userName") String userName, @PathVariable("AppId") String AppId) {
        if (StringUtils.isBlank(userName)) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        userService.toStopUser(userName,AppId);

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
            @ApiImplicitParam(name = "roleName", value = "角色名字", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "appId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/downRegister")
    @CrossOrigin
    public ResultVo downRegister(@RequestBody UserZiQuery userVo) {
        if (StringUtils.isBlank(userVo.getUserName()) || StringUtils.isBlank(userVo.getPassword())
                || StringUtils.isBlank(userVo.getName()) || StringUtils.isBlank(userVo.getPhone()) ||
                StringUtils.isBlank(userVo.getAppId()) || userVo.getCompanyId() == null) {
            log.error("param is not null.");
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        userService.dowmRegister(userVo);

        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
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
            @ApiImplicitParam(name = "userName", value = "用户名或用户名一部分", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryPageUserByName")
    @CrossOrigin
    public Page<UserQuery> queryPageUserByName(@RequestParam("current") Integer current,
                                               @RequestParam("size") Integer size,
                                               @RequestParam("userName") String userName,
                                               @RequestParam("roleName") String roleName,
                                               @RequestParam("AppId") String AppId) {
        if (current == null || size == null || StringUtils.isBlank(AppId)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"分页查询用户参数不能为空");
        }
        Page<UserQuery> page = new Page<>(current,size);
        return userService.queryPageUserByName(page,userName,roleName,AppId);

    }

    @ApiOperation(value = "对用户名或角色模糊查询加分页查询", notes = "对用户名或角色模糊查询加分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "userNameOrRole", value = "用户名或角色", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryUserByNameOrRole")
    @CrossOrigin
    public Page<User2Query> queryUserByName(@RequestParam("current") Integer current,
                                           @RequestParam("size") Integer size,
                                           @RequestParam("userNameOrRole") String userNameOrRole,
                                           @RequestParam("AppId") String AppId) {
        if (current == null || size == null || StringUtils.isBlank(AppId)) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"模糊加分页查询用户参数不能为空");
        }
        Page<User2Query> page = new Page<>(current,size);
        return userService.queryUserByName(page,userNameOrRole,AppId);

    }

    @ApiOperation(value = "根据用户名和手机号重置密码", notes = "根据用户名和手机号重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "新密码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "appId", value = "签名Id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "tel", value = "手机号", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/updateUserPassword")
    @CrossOrigin
    public ResultVo updateUserPassword(@RequestBody UserUpdatePasswordVo vo) {
        if (vo.getId() == null || StringUtils.isBlank(vo.getUserName()) || StringUtils.isBlank(vo.getTel()) ||
            StringUtils.isBlank(vo.getPassword()) || StringUtils.isBlank(vo.getAppId())) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
        }
        userService.updateUserPassword(vo.getId(),vo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "cname", value = "姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "appId", value = "签名Id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "tel", value = "手机号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "stopped", value = "是否停用(false--正常  true--停用)", required = true, dataType = "Boolean", paramType = "path")
    })
    @PostMapping("/updateUser")
    @CrossOrigin
    public ResultVo updateUser(@RequestBody UserUpdateVo vo) {
        if (vo.getId() == null || StringUtils.isBlank(vo.getCname()) || StringUtils.isBlank(vo.getTel()) ||
                StringUtils.isBlank(vo.getAppId())) {
            throw new ApplicationException(CodeType.PARAM_ERROR, "参数不能为空");
        }
        userService.updateUser(vo);
        ResultVo resultVo = new ResultVo();
        resultVo.setResult("ok");
        return resultVo;
    }


    @ApiOperation(value = "根据Id查询用户", notes = "根据Id查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "appId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/getUserById")
    @CrossOrigin
    public UserByIdQuery getUserById (@RequestParam("id") Long id, @RequestParam("appId") String appId) {
        if (id == null || appId == null) {
            throw new ApplicationException(CodeType.PARAM_ERROR,"参数不能为空");
        }
        return userService.getUserById(id,appId);
    }

}
