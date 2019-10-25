package eqlee.ctm.api.apply;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yq.constanct.CodeType;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.apply.vo.PriceVo;
import eqlee.ctm.api.entity.vo.ResultVo;
import eqlee.ctm.api.httpclient.HttpClientUtils;
import eqlee.ctm.api.httpclient.HttpResult;
import eqlee.ctm.api.vilidate.DataUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.portable.ApplicationException;
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
@Api("价格Api--9090:apply")
@RestController
@RequestMapping("/v1/app/price")
public class PriceApiController {

    @Value("${api.userIp}")
    private String Ip;

    @Value("${applyApi.port}")
    private String port;

    @Value("${applyApi.path}")
    private String path;

    @Autowired
    private HttpClientUtils apiService;

    private final Integer Status = 200;

    @ApiOperation(value = "价格设定--9090:apply", notes = "价格设定--9090:apply")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "adultPrice", value = "成年价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "oldPrice", value = "老人价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "babyPrice", value = "幼儿价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "childPrice", value = "小孩价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "remark", value = "备注", required = false, dataType = "String", paramType = "path")
    })
    @PostMapping("/insertPrice")
    @CrossOrigin
    public Object insertPrice(@RequestBody PriceVo priceVo) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/apply/price/insertPrice";

        String s = JSONObject.toJSONString(priceVo);
        HttpResult httpResult = apiService.doPost(url, s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError(httpResult.getBody());
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "批量价格修改", notes = "批量价格修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "lineName", value = "线路名", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "adultPrice", value = "成年价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "oldPrice", value = "老人价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "babyPrice", value = "幼儿价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "childPrice", value = "小孩价格", required = true, dataType = "double", paramType = "path"),
    })
    @PostMapping("/batchUpdatePrice")
    @CrossOrigin
    public Object batchUpdatePrice(PriceVo priceVo) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/apply/price/batchUpdatePrice";

        String s = JSONObject.toJSONString(priceVo);
        HttpResult httpResult = apiService.doPut(url,s);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError(httpResult.getBody());
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "由时间和线路对价格进行查询", notes = "由时间和线路对价格进行查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "OutDate", value = "出发时间", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名称", required = false, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "页面大小", required = true, dataType = "Long", paramType = "path"),
    })
    @GetMapping("/queryPricePageByFilter")
    @CrossOrigin
    public Object queryPricePageByFilter(@RequestParam("OutDate") String OutDate,
                                                @RequestParam("LineName") String LineName,
                                                @RequestParam("size") Integer size,
                                                @RequestParam("current") Integer current) throws Exception{
        String url = "http://" + Ip +":" + port + "/" + path + "/v1/app/apply/price/queryPricePageByFilter?OutDate=" + OutDate
                + "&LineName=" +LineName + "&size=" +size + "&current=" +current;

        Map<String,Object> map = new HashMap<>();

        HttpResult httpResult = apiService.doGet(url, map);

        if (httpResult.getCode() != Status) {
            return DataUtils.getError(httpResult.getBody());
        }
        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "查询一条价格记录", notes = "查询一条价格记录")
    @ApiImplicitParam(name = "Id", value = "Id", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/queryPriceById")
    public Object queryPriceById (@RequestParam("Id") Long Id) {

        return null;
    }


    @ApiOperation(value = "修改一条价格记录", notes = "修改一条价格记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "adultPrice", value = "成年价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "oldPrice", value = "老人价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "babyPrice", value = "幼儿价格", required = true, dataType = "double", paramType = "path"),
            @ApiImplicitParam(name = "childPrice", value = "小孩价格", required = true, dataType = "double", paramType = "path")
    })
    @PostMapping("/updateOnePrice")
    public ResultVo updateOnePrice() {

        return null;
    }

    @ApiOperation(value = "根据时间和线路查询一条价格记录", notes = "根据时间和线路查询一条价格记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "OutDate", value = "出发时间", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "LineName", value = "线路名", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/queryPrice")
    public Object queryPrice () {

        return null;
    }


    @ApiOperation(value = "删除价格", notes = "删除价格")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "long", paramType = "path")
    @GetMapping("/deletePriceById")
    public ResultVo deletePriceById () {

        return null;
    }

}
