package eqlee.ctm.api.resource;

import com.alibaba.fastjson.JSONObject;
import eqlee.ctm.api.entity.vo.ResultVo;
import eqlee.ctm.api.httpclient.HttpClientUtils;
import eqlee.ctm.api.httpclient.HttpResult;
import eqlee.ctm.api.resource.Vo.CompanyVo;
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
@Api("公司Api")
@RestController
@RequestMapping("/v1/app/api/company")
public class CompanyApiController {

    @Value("${api.userIp}")
    private String Ip;

    @Value("${resourceApi.port}")
    private String port;

    @Value("${resourceApi.path}")
    private String path;

    @Autowired
    private HttpClientUtils apiService;

    private final Integer Status = 200;

    @ApiOperation(value = "同行列表", notes = "同行列表展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "页面大小", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping("/queryCompany")
    @CrossOrigin
    public Object queryCompany(@RequestParam("current") Integer current, @RequestParam("size") Integer size) throws Exception{

        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/resource/company/queryCompany?current=" +current + "&size=" + size;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "同行信息修改", notes = "同行信息修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "CompanyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "StartDate", value = "合同开始时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endDate", value = "合同结束时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "PayMethod", value = "支付方式", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Stopped", value = "状态", required = true, dataType = "Boolen", paramType = "path")
    })
    @PutMapping("/updateCompany")
    @CrossOrigin
    public Object updateCompany (@RequestBody CompanyVo companyVo) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/resource/company/updateCompany";

        String s = JSONObject.toJSONString(companyVo);
        HttpResult httpResult = apiService.doPut(url,s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "同行信息删除", notes = "同行信息删除")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/deleteCompany")
    @CrossOrigin
    public Object deleteCompany (@RequestParam("Id") Long Id) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/resource/company/deleteCompany?Id=" +Id;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doDelete(url,map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "同行列表", notes = "由公司名查询的公司列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CompanyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "页面大小", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping("/queryCompanyByName")
    @CrossOrigin
    public Object queryCompanyByCompanyName (@RequestParam("size") Integer size,@RequestParam("CompanyName") String CompanyName,
                                                    @RequestParam("current") Integer current) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/resource/company/queryCompanyByName?current=" +current +
                "&size=" + size + "CompanyName=" +CompanyName;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "添加", notes = "添加同行信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CompanyName", value = "公司名称", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "StartDate", value = "合同开始时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endDate", value = "合同结束时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "PayMethod", value = "支付方式", required = true, dataType = "String", paramType = "path"),
    })
    @PostMapping("/addCompany")
    @CrossOrigin
    public Object addCompany (@RequestBody CompanyVo companyVo) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/resource/company/addCompany";

        String s = JSONObject.toJSONString(companyVo);
        HttpResult httpResult = apiService.doPost(url, s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "同行状态", notes = "同行状态修改")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @PutMapping("/UpdateCompanyStop")
    @CrossOrigin
    public Object UpdateCompanyStop (@RequestParam("Id") Long Id) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/resource/company/UpdateCompanyStop?Id=" + Id;

        HttpResult httpResult = apiService.doPut(url, null);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "同行信息修改首页", notes = "同行信息修改首页")
    @ApiImplicitParam(name = "Id", value = "公司Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/updateCompanyIndex")
    @CrossOrigin
    public Object updateCompanyIndex (@RequestParam("Id") Long Id) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/resource/company/updateCompanyIndex?Id=" + Id;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

}
