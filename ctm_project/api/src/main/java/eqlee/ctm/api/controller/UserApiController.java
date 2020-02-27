package eqlee.ctm.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yq.anntation.IgnoreResponseAdvice;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.CityJwtBo;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.entity.bo.ParamBo;
import eqlee.ctm.api.entity.query.UserWithQuery;
import eqlee.ctm.api.entity.vo.*;
import eqlee.ctm.api.vilidate.DataUtils;
import eqlee.ctm.api.vilidate.TokenData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
@Slf4j
@Api("用户Api--9093:api")
@RestController
@RequestMapping("/v1/app/api/user")
public class UserApiController {

    @Value("${api.userIp}")
    private String Ip;

    private final String ip = "localhost";

    @Value("${api.port}")
    private String port;

    private final String path = "ctm_user";

    @Autowired
    private LocalUser localUser;

    @Autowired
    private HttpClientUtils apiService;

    @Autowired
    private TokenData tokenData;


    @ApiOperation(value = "注册-- 9093:api", notes = "注册-- 9093:api")
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
    @CheckToken
    @IgnoreResponseAdvice
    public Object register(@RequestBody UserVo userVo) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/register";

        if (userVo.getType() == 1) {
            //本公司
            UserLoginQuery user = localUser.getUser("用户信息");
            userVo.setCompanyId(user.getCompanyId());
        }

        String s = JSONObject.toJSONString(userVo);

        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.post(url, s,token);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "登录", notes ="登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/login")
    @CrossOrigin
    @IgnoreResponseAdvice
    public Object login(@RequestBody UserLoginVo userLoginVo) throws Exception {
        String url = "http://" + ip + ":" + port + "/" + path + "/v1/app/user/login?userName=" + userLoginVo.getUserName() + "&password=" + userLoginVo.getPassword();

        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.get(url,token);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
        }

        String body = httpResult.getBody();
        Object result = JSON.parse(body);
        return result;
    }

    @ApiOperation(value = "注销", notes = "注销")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/deleteUser")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object deleteUser(@RequestParam("id") String id) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/deleteUser/" +id;

        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.get(url,token);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "退出账号", notes = "退出账号")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/exitUser/{userName}")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object exitUser(@PathVariable("userName") String userName) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/exitUser/" +userName;

        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.get(url,token);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "冻结账户", notes = "冻结账户")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/stopUser/{userName}")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object stopUser(@PathVariable("userName") String userName) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/stopUser/" +userName;

        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.get(url,token);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "解冻账户", notes = "解冻账户")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/toStopUser/{userName}")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object toStopUser(@PathVariable("userName") String userName) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/toStopUser/" +userName;

        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.get(url,token);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
        }
        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "子用户注册", notes = "子用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "phone", value = "电话", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/downRegister")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object downRegister(@RequestBody UserVo userVo) throws Exception{
        UserLoginQuery user = localUser.getUser("用户信息");
        UserWithQuery query = new UserWithQuery();

        //装配query
        query.setCompanyId(user.getCompanyId());
        query.setRoleName(userVo.getRoleName());
        query.setName(userVo.getName());
        query.setPassword(userVo.getPassword());
        query.setPhone(userVo.getPhone());
        query.setUserName(userVo.getUserName());
        query.setCity(userVo.getCity());
        query.setCompanyName(userVo.getCompanyName());


        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/downRegister";

        String s = JSONObject.toJSONString(query);
        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.post(url,s,token);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
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
     * @throws Exception
     */
    @ApiOperation(value = "对用户名模糊以及角色帅选查询分页用户信息", notes = "对用户名模糊以及角色帅选查询分页用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "userName", value = "用户名或用户名一部分", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryPageUserByName")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object queryPageUserByName(@RequestParam("current") Integer current,
                                      @RequestParam("size") Integer size,
                                      @RequestParam("userName") String userName,
                                      @RequestParam("roleName") String roleName,
                                      @RequestParam("companyId") Long companyId,
                                      @RequestParam("type") Integer type) throws Exception{
        if (companyId == null) {
            companyId = 1L;
        }

        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/queryPageUserByName?current=" +current + "&size=" + size
                + "&userName=" + userName + "&companyId=" +companyId + "&roleName=" +roleName + "&type=" +type;

        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.get(url,token);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "对用户名或角色模糊查询加分页查询", notes = "对用户名或角色模糊查询加分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "userNameOrRole", value = "用户名或角色", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryUserByNameOrRole")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object queryUserByName(@RequestParam("current") Integer current,
                                           @RequestParam("size") Integer size,
                                           @RequestParam("userNameOrRole") String userNameOrRole) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/queryUserByNameOrRole?current=" +current + "&size=" + size
                + "&userNameOrRole=" + userNameOrRole;

        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.get(url,token);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "根据用户名和原密码重置密码", notes = "根据用户名和原密码重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "password", value = "新密码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "oldPassword", value = "原密码", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/updateUserPassword")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object updateUserPassword(@RequestBody UserPasswordUpVo vo) throws Exception{
        UserUpdatePasswordVo result = new UserUpdatePasswordVo();
        result.setId(vo.getId());
        result.setOldPassword(vo.getOldPassword());
        result.setPassword(vo.getPassword());
        result.setUserName(vo.getUserName());
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/updateUserPassword";

        String jsonString = JSONObject.toJSONString(result);
        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.post(url,jsonString,token);

        ResultResposeVo vo1 = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo1.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo1.getMsg());
        }

        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "cname", value = "姓名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "tel", value = "手机号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "stopped", value = "是否停用(false--正常  true--停用)", required = true, dataType = "Boolean", paramType = "path")
    })
    @PostMapping("/updateUser")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object updateUser(@RequestBody UserUpdateVo vo) throws Exception{
        UserLoginQuery user = localUser.getUser("用户信息");
        UserUpdateInfoVo infoVo = new UserUpdateInfoVo();
        infoVo.setCname(vo.getCname());
        infoVo.setId(vo.getId());
        infoVo.setNewPassword(vo.getNewPassword());
        infoVo.setRoleName(user.getRoleName());
        infoVo.setTel(vo.getTel());
        infoVo.setCity(vo.getCity());
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/updateUser";

        String s = JSONObject.toJSONString(infoVo);
        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.post(url,s,token);

        ResultResposeVo vo1 = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo1.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo1.getMsg());
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "根据Id查询用户", notes = "根据Id查询用户")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/getUserById")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object getUserById (@RequestParam("id") Long id) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/getUserById?id=" +id;

        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.get(url,token);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
        }
        return JSONObject.parse(httpResult.getBody());
    }


    @PostMapping("/queryLocalCityUser")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object queryLocalCityUser() throws Exception{

        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/queryLocalCityUser";

        UserLoginQuery user = localUser.getUser("用户信息");
        ParamBo bo = new ParamBo();
        List<String> list = new ArrayList<>();
        for (CityJwtBo jwtBo : user.getCity()) {
            list.add(jwtBo.getCity());
        }
        bo.setList(list);

        String s = JSONObject.toJSONString(bo);
        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.post(url,s,token);

        ResultResposeVo vo1 = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo1.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo1.getMsg());
        }

        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "查询所有角色下的用户", notes = "查询所有角色下的用户")
    @ApiImplicitParam(name = "roleName", value = "roleName", required = true, dataType = "String", paramType = "path")
    @GetMapping("/queryUserByRole")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object queryUserByRole (@RequestParam("roleName") String roleName) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/user/queryUserByRole?roleName=" +roleName;

        //获得token
        String userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;

        HttpResult httpResult = apiService.get(url,token);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
        }
        return JSONObject.parse(httpResult.getBody());
    }

}
