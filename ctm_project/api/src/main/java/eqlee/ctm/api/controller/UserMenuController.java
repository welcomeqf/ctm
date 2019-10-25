package eqlee.ctm.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.PrivilegeMenuQuery;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.entity.query.UserPrivilegeQuery;
import eqlee.ctm.api.entity.vo.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
@Api("用户权限菜单--9093:api")
@Slf4j
@RestController
@RequestMapping("/v1/app/api/menu")
public class UserMenuController {

    /**
     * Ip路径
     */
    @Value("${api.userIp}")
    private String Ip;

    @Value("${api.port}")
    private String port;

    private final String path = "ctm_user";

    @Autowired
    private HttpClientUtils apiService;

    @Autowired
    private LocalUser localUser;

    private final Integer Status = 200;


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

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            String msg = DataUtils.getMsg(vo.getMsg());
            return DataUtils.getError(msg);
        }
        return JSONObject.parse(httpResult.getBody());

    }

    @ApiOperation(value = "查看权限", notes = "查看权限")
    @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryAll")
    @CheckToken
    @CrossOrigin
    public Object queryAllParentMenu(@RequestParam("roleId") Long roleId) throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/menu/queryAll?&AppId=" +encode + "&roleId=" +roleId;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            String msg = DataUtils.getMsg(vo.getMsg());
            return DataUtils.getError(msg);
        }
        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "首页返回所有权限", notes = "首页返回所有权限")
    @GetMapping("/query")
    @CrossOrigin
    @CheckToken
    public Object queryAllMenu(){

        UserLoginQuery users = localUser.getUser("用户信息");
        return users.getMenuList();

    }


    @ApiOperation(value = "查看父权限", notes = "查看父权限")
    @GetMapping("/queryAllParent")
    @CheckToken
    @CrossOrigin
    public Object queryAllParent() throws Exception{
        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/menu/queryAllParent?&AppId=" +encode;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo.getCode() != 0) {
            String msg = DataUtils.getMsg(vo.getMsg());
            return DataUtils.getError(msg);
        }
        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "返回子用户的权限", notes = "返回子用户的权限")
    @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryZiAll")
    @CrossOrigin
    @CheckToken
    public Object queryPrivilege(Long roleId) throws Exception{
        UserLoginQuery users = localUser.getUser("用户信息");
        List<UserPrivilegeQuery> list = new ArrayList<>();
        for (PrivilegeMenuQuery menuQuery : users.getMenuList()) {
            UserPrivilegeQuery query = new UserPrivilegeQuery();
            query.setAction(menuQuery.getAction());
            query.setIconClass(menuQuery.getIconClass());
            query.setIconColor(menuQuery.getIconColor());
            query.setMenuName(menuQuery.getMenuName());
            query.setStart(true);
            list.add(query);
        }

        String encode = DataUtils.getEncodeing("RSA");
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/menu/queryZiAll";

        MenuZiVo vo = new MenuZiVo();
        vo.setAppId(encode);
        vo.setList(list);
        vo.setRoleId(roleId);

        String s = JSONObject.toJSONString(vo);
        HttpResult httpResult = apiService.doPost(url, s);

        ResultResposeVo vo1 = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (vo1.getCode() != 0) {
            String msg = DataUtils.getMsg(vo1.getMsg());
            return DataUtils.getError(msg);
        }
        return JSONObject.parse(httpResult.getBody());

    }

}
