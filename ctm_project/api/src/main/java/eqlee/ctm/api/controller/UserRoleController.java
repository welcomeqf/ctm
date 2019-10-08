package eqlee.ctm.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.entity.vo.UserRoleVo;
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
@Api("用户角色Api")
@RestController
@RequestMapping("/v1/app/api/role")
public class UserRoleController {

    @Value("${api.userIp}")
    private String Ip;

    @Value("${api.port}")
    private String port;

    @Value("${api.path}")
    private String path;

    @Autowired
    private HttpClientUtils apiService;

    private final Integer Status = 200;

    @ApiOperation(value = "增加角色", notes = "增加角色")
    @ApiImplicitParam(name = "RoleName", value = "角色名称", required = true, dataType = "String", paramType = "path")
    @PostMapping("/insertRole")
    @CrossOrigin
    @CheckToken
    public Object addRole(@RequestBody UserRoleVo roleVo) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        roleVo.setAppId(encode);
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/role/addRole";

        String s = JSONObject.toJSONString(roleVo);
        HttpResult httpResult = apiService.doPost(url, s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "int", paramType = "path")
    @DeleteMapping("/{Id}")
    @CrossOrigin
    @CheckToken
    public Object deleteRole(@PathVariable("Id") Long Id) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/role/deleteRole/" +Id + "/" +encode;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "查询角色", notes = "查询角色")
    @GetMapping("/RoleInfo")
    @CrossOrigin
    @CheckToken
    public Object getRole() throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/role/RoleInfo?AppId=" +encode;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
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
    public Object queryPageRole(@RequestParam("current") Integer current, @RequestParam("size") Integer size) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/" + path +  "/v1/app/role/queryPageRole?AppId=" +encode + "&current=" +current + "&size=" +size;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }


}
