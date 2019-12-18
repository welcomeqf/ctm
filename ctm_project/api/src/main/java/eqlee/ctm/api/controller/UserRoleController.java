package eqlee.ctm.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.anntation.IgnoreResponseAdvice;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.entity.query.UserRoleZiQuery;
import eqlee.ctm.api.entity.vo.ResultResposeVo;
import eqlee.ctm.api.entity.vo.RoleUpdateVo;
import eqlee.ctm.api.entity.vo.UserRoleVo;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
@Slf4j
@Api("用户角色Api--9093:api")
@RestController
@RequestMapping("/v1/app/api/role")
public class UserRoleController {

    @Value("${api.userIp}")
    private String Ip;

    @Value("${api.port}")
    private String port;

    private final String ip = "localhost";

    private final String path = "ctm_user";

    @Autowired
    private HttpClientUtils apiService;

    @Autowired
    private LocalUser localUser;

    @Autowired
    private TokenData tokenData;

    @ApiOperation(value = "增加角色-- 9093:api", notes = "增加角色-- 9093:api")
    @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String", paramType = "path")
    @PostMapping("/insertRole")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object addRole(@RequestBody UserRoleVo roleVo) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path +  "/v1/app/role/addRole";

        UserLoginQuery user = localUser.getUser("用户信息");
        UserRoleZiQuery query = new UserRoleZiQuery();
        query.setCompanyId(user.getCompanyId());
        query.setRoleName(roleVo.getRoleName());

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


    @ApiOperation(value = "增加子角色", notes = "增加子角色")
    @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "String", paramType = "path")
    @PostMapping("/addZiRole")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object addZiRole(@RequestBody UserRoleVo roleVo) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path +  "/v1/app/role/addZiRole";

        UserLoginQuery user = localUser.getUser("用户信息");
        UserRoleZiQuery query = new UserRoleZiQuery();
        query.setCreateUserId(user.getId());
        query.setCompanyId(user.getCompanyId());
        query.setRoleName(roleVo.getRoleName());

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


    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "int", paramType = "path")
    @DeleteMapping("/{Id}")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object deleteRole(@PathVariable("Id") Long Id) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/role/" +Id;

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


    @ApiOperation(value = "查询角色", notes = "查询角色")
    @GetMapping("/RoleInfo")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object getRole() throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/role/RoleInfo";

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


    @ApiOperation(value = "分页查询所有角色", notes = "分页查询所有角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/queryPageRole")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object queryPageRole(@RequestParam("current") Integer current, @RequestParam("size") Integer size) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/role/queryPageRole?current=" +current + "&size=" +size;

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


    @ApiOperation(value = "修改角色", notes = "修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "stopped", value = "是否停用", required = true, dataType = "Boolean", paramType = "path")
    })
    @PostMapping("/updateRole")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object updateRole (@RequestBody RoleUpdateVo vo) throws Exception{
        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/role/updateRole";

        String s = JSONObject.toJSONString(vo);
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



    @ApiOperation(value = "分页查询所有子角色", notes = "分页查询所有子角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数", required = true, dataType = "int", paramType = "path")
    })
    @GetMapping("/queryZiPageRole")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object queryZiPageRole(@RequestParam("current") Integer current,
                                  @RequestParam("size") Integer size) throws Exception{

        UserLoginQuery user = localUser.getUser("用户信息");
        Long companyId = user.getCompanyId();

        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/role/queryZiPageRole?current="
              +current + "&size=" +size + "&companyId=" +companyId;

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
