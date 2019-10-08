package eqlee.ctm.api.apply;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.apply.query.Line;
import eqlee.ctm.api.apply.vo.LineVo;
import eqlee.ctm.api.entity.vo.ResultVo;
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
@Api("线路Api")
@RestController
@RequestMapping("/v1/app/line")
public class LineApiController {

    @Value("${api.userIp}")
    private String Ip;

    @Value("${applyApi.port}")
    private String port;

    @Value("${applyApi.path}")
    private String path;

    @Autowired
    private HttpClientUtils apiService;

    private final Integer Status = 200;

    @ApiOperation(value = "增加线路", notes = "增加线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "information", value = "线路简介", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "travelSituation", value = "出游情况（几日游）", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "maxNumber", value = "最大人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "minNumber", value = "最小人数", required = true, dataType = "int", paramType = "path")
    })
    @PostMapping("/insertLine")
    @CrossOrigin
    public Object insertLine(@RequestBody LineVo lineVo) throws Exception{
        String url = "http://" + Ip +":" + port + "/" +path + "/v1/app/apply/line/insertLine";

        String s = JSONObject.toJSONString(lineVo);
        HttpResult httpResult = apiService.doPost(url, s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "修改线路", notes = "修改线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "information", value = "线路简介", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "region", value = "区域", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "travelSituation", value = "出游情况（几日游）", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "stopped", value = "是否停用(false-正常 1-禁用true)", required = true, dataType = "Boolean", paramType = "path"),
            @ApiImplicitParam(name = "maxNumber", value = "最大人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "minNumber", value = "最小人数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("/updateLine")
    @CrossOrigin
    public Object updateLine(Line line) throws Exception{
        String url = "http://" + Ip +":" + port + "/" +path + "/v1/app/apply/line/updateLine";

        String s = JSONObject.toJSONString(line);
        HttpResult httpResult = apiService.doPut(url,s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "模糊查询分页查询所有线路", notes = "模糊查询分页查询所有线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = false, dataType = "String", paramType = "path")
    })
    @GetMapping("/listLine")
    public Object listLine(@RequestParam("current") Integer current,
                               @RequestParam("size") Integer size) throws Exception{
        String url = "http://" + Ip +":" + port + "/" +path + "/v1/app/apply/line/listLine?current=" + current + "&size=" +size;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "停用线路", notes = "停用线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "String", paramType = "path")
    @GetMapping("/stopLine/{Id}")
    @CrossOrigin
    public Object stopLine(@PathVariable("Id") Long Id) throws Exception{
        String url = "http://" + Ip +":" + port + "/" +path + "/v1/app/apply/line/stopLine?Id=" + Id;

        HttpResult httpResult = apiService.doPut(url,null);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "启用线路", notes = "启用线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "String", paramType = "path")
    @GetMapping("/startLine/{Id}")
    @CrossOrigin
    public Object startLine(@PathVariable("Id") Long Id) throws Exception{
        String url = "http://" + Ip +":" + port + "/" +path + "/v1/app/apply/line/startLine?Id=" + Id;

        HttpResult httpResult = apiService.doPut(url,null);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "根据Id查询一条线路", notes = "根据Id查询一条线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "String", paramType = "path")
    @GetMapping("/queryLineById")
    @CrossOrigin
    public Object queryLineById(@RequestParam("Id") Long Id) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/apply/line/queryLineById?Id=" + Id;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "根据Id删除一条线路", notes = "根据Id删除一条线路")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/deleteLine/{Id}")
    public ResultVo deleteLine(@PathVariable("Id") Long Id) {

        return null;
    }
}