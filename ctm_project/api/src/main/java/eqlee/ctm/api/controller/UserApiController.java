package eqlee.ctm.api.controller;

import com.alibaba.fastjson.JSONObject;
import eqlee.ctm.api.entity.vo.UserVo;
import eqlee.ctm.api.httpclient.HttpClientUtils;
import eqlee.ctm.api.httpclient.HttpResult;
import eqlee.ctm.api.vilidate.DataUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
@Slf4j
@Api("用户Api")
@RestController
@RequestMapping("/v1/app/api/user")
public class UserApiController {

    @Value("${api.userIp}")
    private String Ip;

    @Value("${api.port}")
    private String port;

    @Autowired
    private HttpClientUtils apiService;

    private final Integer Status = 200;

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
    public Object register(@RequestBody UserVo userVo) throws Exception{
        String url = "http://" + Ip +":" + port + "/v1/app/user/register";

        String s = JSONObject.toJSONString(userVo);
        HttpResult httpResult = apiService.doPost(url, s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "登录", notes ="登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "AppId", value = "签名Id", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/login")
    @CrossOrigin
    public Object login(@RequestParam("userName") String userName, @RequestParam("password") String password) throws Exception {
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/v1/app/user/login?userName=" +userName + "&AppId=" +encode + "&password=" +password;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "注销", notes = "注销")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/deleteUser")
    @CrossOrigin
    public Object deleteUser(@RequestParam("userName") String userName) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/v1/app/user/deleteUser?userName=" +userName + "&AppId=" +encode;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "退出账号", notes = "退出账号")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/exitUser")
    @CrossOrigin
    public Object exitUser(@RequestParam("userName") String userName) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/v1/app/user/exitUser?userName=" +userName + "&AppId=" +encode;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "冻结账户", notes = "冻结账户")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/stopUser")
    @CrossOrigin
    public Object stopUser(@RequestParam("userName") String userName) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/v1/app/user/stopUser?userName=" +userName + "&AppId=" +encode;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "解冻账户", notes = "解冻账户")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/toStopUser")
    @CrossOrigin
    public Object toStopUser(@RequestParam("userName") String userName) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/v1/app/user/toStopUser?userName=" +userName + "&AppId=" +encode;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "子用户注册", notes = "子用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "companyId", value = "公司Id", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/downRegister")
    @CrossOrigin
    public Object downRegister(@RequestBody UserVo userVo) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        userVo.setAppId(encode);
        String url = "http://" + Ip +":" + port + "/v1/app/user/downRegister";

        String s = JSONObject.toJSONString(userVo);
        HttpResult httpResult = apiService.doPost(url, s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());

    }

    @ApiOperation(value = "分页查询用户信息", notes = "分页查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/pageListUser")
    @CrossOrigin
    public Object pageListUser(@RequestParam("current") Integer current, @RequestParam("size") Integer size) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/v1/app/user/pageListUser?current=" +current + "&AppId=" +encode + "&size=" + size;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
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
    public Object queryPageUserByName(@RequestParam("current") Integer current,
                                               @RequestParam("size") Integer size,
                                               @RequestParam("userName") String userName,
                                               @RequestParam("roleName") String roleName) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/v1/app/user/queryPageUserByName?current=" +current + "&AppId=" +encode + "&size=" + size
                + "&userName=" + userName + "&roleName=" +roleName;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "对用户名模糊查询加分页查询", notes = "对用户名模糊查询加分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "userName", value = "用户名或用户名一部分", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryUserByName")
    @CrossOrigin
    public Object queryUserByName(@RequestParam("current") Integer current,
                                           @RequestParam("size") Integer size,
                                           @RequestParam("userName") String userName) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/v1/app/user/queryUserByName?current=" +current + "&AppId=" +encode + "&size=" + size
                + "&userName=" + userName;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }
}
