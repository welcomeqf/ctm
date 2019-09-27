package eqlee.ctm.api.controller;

import com.alibaba.fastjson.JSONObject;
import eqlee.ctm.api.entity.query.PrivilegeQuery;
import eqlee.ctm.api.entity.vo.ResultVo;
import eqlee.ctm.api.httpclient.HttpClientUtils;
import eqlee.ctm.api.httpclient.HttpResult;
import eqlee.ctm.api.jwt.islogin.CheckToken;
import eqlee.ctm.api.vilidate.DataUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
@Api("权限Api")
@Slf4j
@RestController
@RequestMapping("/v1/app/api/privilege")
public class UserPrivilegeController {

    @Value("${api.userIp}")
    private String Ip;

    @Value("${api.port}")
    private String port;

    @Value("${api.path}")
    private String path;

    @Autowired
    private HttpClientUtils apiService;

    private final Integer Status = 200;

    @ApiOperation(value = "增加权限", notes = "增加权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "menuList", value = "所有菜单集合", required = true, dataType = "List", paramType = "path")
    })
    @PostMapping("/insertPrivilege")
    @CrossOrigin
    @CheckToken
    public Object insertPrivilege(@RequestBody PrivilegeQuery query) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        query.setAppId(encode);
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/user/privilege/insertPrivilege";

        String s = JSONObject.toJSONString(query);
        HttpResult httpResult = apiService.doPost(url, s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());
    }
}
