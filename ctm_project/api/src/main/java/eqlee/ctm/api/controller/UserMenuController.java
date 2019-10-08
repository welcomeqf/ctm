package eqlee.ctm.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.entity.vo.MenuVo;
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
@Api("用户权限菜单")
@Slf4j
@RestController
@RequestMapping("/v1/app/api/menu")
public class UserMenuController {

    @Value("${api.userIp}")
    private String Ip;

    @Value("${api.port}")
    private String port;

    @Value("{api.path}")
    private String path;

    @Autowired
    private HttpClientUtils apiService;

    private final Integer Status = 200;

    @ApiOperation(value = "增加菜单", notes = "增加菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "MenuName", value = "菜单名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Parent", value = "父级ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "Action", value = "链接地址", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "IconClass", value = "图标", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/insertMenu")
    @CrossOrigin
    @CheckToken
    public Object addMenu(@RequestBody MenuVo vo) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        vo.setAppId(encode);
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/menu/addMenu";

        String s = JSONObject.toJSONString(vo);
        HttpResult httpResult = apiService.doPost(url, s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "查询所有权限", notes = "查询所有权限")
    @ApiImplicitParam(name = "Id", value = "Id", required = false, dataType = "Long", paramType = "path")
    @GetMapping("/queryAllMenu")
    @CrossOrigin
    @CheckToken
    public Object queryMenu(@RequestParam("Id") Long Id) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/menu/queryMenu?Id=" +Id + "&AppId=" +encode;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());

    }
}
