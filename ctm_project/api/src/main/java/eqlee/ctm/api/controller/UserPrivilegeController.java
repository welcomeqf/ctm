package eqlee.ctm.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.anntation.IgnoreResponseAdvice;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.entity.UserMenu;
import eqlee.ctm.api.entity.query.PrivilegeDetailedQuery;
import eqlee.ctm.api.entity.query.PrivilegeQuery;
import eqlee.ctm.api.entity.query.PrivilegeWithQuery;
import eqlee.ctm.api.entity.vo.ResultResposeVo;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
@Api("权限Api--9093:api")
@Slf4j
@RestController
@RequestMapping("/v1/app/api/privilege")
public class UserPrivilegeController {

    @Value("${api.userIp}")
    private String Ip;

    @Value("${api.port}")
    private String port;

    private final String ip = "localhost";

    private final String path = "ctm_user";

    @Autowired
    private HttpClientUtils apiService;

    @ApiOperation(value = "增加权限-- 9093:api", notes = "增加权限-- 9093:api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "menuList", value = "所有菜单集合", required = true, dataType = "List", paramType = "path")
    })
    @PostMapping("/insertPrivilege")
    @CrossOrigin
    @CheckToken
    @IgnoreResponseAdvice
    public Object insertPrivilege(@RequestBody PrivilegeQuery query) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");

        PrivilegeDetailedQuery query1 = new PrivilegeDetailedQuery();
        query1.setAppId(encode);
        query1.setRoleId(query.getRoleId());

        List<PrivilegeWithQuery> list = new ArrayList<>();
        for (UserMenu menu : query.getMenuList()) {
            PrivilegeWithQuery withQuery = new PrivilegeWithQuery();
            withQuery.setMenuId(menu.getMenuId());
            list.add(withQuery);
        }

        query1.setMenuList(list);

        String url = "http://" + ip +":" + port + "/" + path + "/v1/app/privilege/insertPrivilege";

        String s = JSONObject.toJSONString(query1);
        HttpResult httpResult = apiService.doPost(url, s);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getMsg());
        }

        return JSONObject.parse(httpResult.getBody());
    }
}
