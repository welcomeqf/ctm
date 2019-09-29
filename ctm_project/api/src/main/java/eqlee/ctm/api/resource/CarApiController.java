package eqlee.ctm.api.resource;

import com.alibaba.fastjson.JSONObject;
import eqlee.ctm.api.httpclient.HttpClientUtils;
import eqlee.ctm.api.httpclient.HttpResult;
import eqlee.ctm.api.jwt.islogin.CheckToken;
import eqlee.ctm.api.resource.Vo.CarVo;
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
@Api("车辆Api")
@RestController
@RequestMapping("/v1/app/car")
public class CarApiController {

    @Value("${api.userIp}")
    private String Ip;

    @Value("${resourceApi.port}")
    private String port;

    @Value("${resourceApi.path}")
    private String path;

    @Autowired
    private HttpClientUtils apiService;

    private final Integer Status = 200;

    @ApiOperation(value = "车辆列表",notes = "车辆列表展示（分页）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "页面大小", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping("/queryCarPage")
    @CrossOrigin
    public Object queryAllCar(@RequestParam("current") Integer current,
                                 @RequestParam("size") Integer size) throws Exception{
        String url = "http://" + Ip +":" + port  + "/v1/app/resource/car/queryCarPage?current=" +current + "&size=" + size;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "车辆删除",notes = "车辆删除")
    @DeleteMapping("/{Id}")
    @ApiImplicitParam(name = "Id",value = "车辆Id",required = true,dataType = "Long",paramType = "path")
    @CrossOrigin
    public Object deleteCar(@PathVariable("Id") Long Id) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/resource/car/deleteCar?Id=" + Id;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doDelete(url,map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }
        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "车辆增加",notes = "车辆增加")
    @PostMapping("/addCar")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carName", value = "车辆名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "carNo", value = "车牌号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "statu", value = "状态", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "isStop", value = "是否启用", required = true, dataType = "boolean", paramType = "path")
    })
    public Object addCar(@RequestBody CarVo carVo) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/resource/car/addCar";

        String s = JSONObject.toJSONString(carVo);
        HttpResult httpResult = apiService.doPost(url, s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "车辆修改",notes = "车辆修改")
    @PostMapping("/updateCar")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Id", value = "车辆Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "carName", value = "车辆名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "carNo", value = "车辆号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "statu", value = "状态", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "isStop", value = "是否启用", required = true, dataType = "boolean", paramType = "path")
    })
    public Object updateCar(Long Id,@RequestBody CarVo carVo) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/resource/car/updateCar";

        String s = JSONObject.toJSONString(carVo);
        HttpResult httpResult = apiService.doPut(url,s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "查询车辆修改页首页",notes = "查询车辆修改页首页")
    @GetMapping("/CarDetail/{Id}")
    @ApiImplicitParam(name = "Id", value = "车辆Id", required = true, dataType = "Long", paramType = "path")
    @CrossOrigin
    public Object updateCarIndex(@PathVariable("Id") Long Id) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/resource/car/updateCarDetail?Id=" +Id;

        Map<String,Object> map = new HashMap<>();
        HttpResult httpResult = apiService.doGet(url,map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError();
        }

        return JSONObject.parse(httpResult.getBody());
    }

}
