package eqlee.ctm.api.controller;

import com.alibaba.fastjson.JSONObject;
import eqlee.ctm.api.entity.vo.UserVo;
import eqlee.ctm.api.httpclient.HttpClientUtils;
import eqlee.ctm.api.httpclient.HttpResult;
import eqlee.ctm.api.vilidate.BuildVo;
import eqlee.ctm.api.vilidate.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/9/21
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/api")
public class ApiController {

    @Autowired
    private HttpClientUtils apiService;

    private final Integer Status = 200;


    @GetMapping("/get")
    @CrossOrigin
    public Object getResult(@RequestParam("AppId") String AppId) throws Exception{
        String dcodeing = DataUtils.getDcodeing(AppId);
        String url = "http://192.168.124.1:10000/build/query?AppId=" +dcodeing;

        Map<String, Object> map = new HashMap<>();
        HttpResult httpResult = apiService.doGet(url,map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        Object parse = JSONObject.parse(httpResult.getBody());
        System.out.println(parse);
        return parse;

    }

    @PostMapping("/insert")
    public Object insert() throws Exception{
        BuildVo buildVo = new BuildVo();
        buildVo.setName("小明");
        String s1 = DataUtils.getDcodeing("D433KFHWXYDFRZ4ZRVYGLYZHVM");
        buildVo.setAppId(s1);
        buildVo.setParentId(0);
        buildVo.setType("2");
        String url = "http://192.168.124.1:10000/build/insertBuild";
        String s = JSONObject.toJSONString(buildVo);
        HttpResult httpResult = apiService.doPost(url, s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        Object parse = JSONObject.parse(httpResult.getBody());
        return parse;
    }


    @PostMapping("/register")
    @CrossOrigin
    public Object get() throws Exception{
        String url = "http://192.168.0.22:8001/v1/app/user/register";

        UserVo userVo = new UserVo();
        userVo.setUserName("guest");
        userVo.setPassword("guest");
        userVo.setPhone("13873854679");
        userVo.setRoleName("运营人员");
        userVo.setName("小明");
        userVo.setCompanyId(623843335580155904L);
        String s = JSONObject.toJSONString(userVo);
        HttpResult httpResult = apiService.doPost(url,s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        Object parse = JSONObject.parse(httpResult.getBody());
        System.out.println(parse);
        return parse;

    }


    @GetMapping("/getLogin")
    @CrossOrigin
    public Object getLogin(@RequestParam("userName") String userName, @RequestParam("password") String password) throws Exception{
        String url = "http://192.168.0.22:8001/v1/app/user/login?AppId=RSA&userName=" +userName +"&password=" +password;

        Map<String, Object> map = new HashMap<>();
        HttpResult httpResult = apiService.doGet(url,map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        Object parse = JSONObject.parse(httpResult.getBody());
        System.out.println(parse);
        return parse;

    }

}
