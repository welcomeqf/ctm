package eqlee.ctm.api.pay.controller;


import com.alibaba.fastjson.JSONObject;
import com.yq.data.Result;
import com.yq.jwt.islogin.CheckToken;
import eqlee.ctm.api.httpclient.HttpClientUtils;
import eqlee.ctm.api.httpclient.HttpResult;
import eqlee.ctm.api.pay.entity.Pay;
import eqlee.ctm.api.pay.entity.PayResult;
import eqlee.ctm.api.pay.entity.PayToken;
import eqlee.ctm.api.pay.entity.query.ResultQuery;
import eqlee.ctm.api.pay.entity.vo.ResultTokenVo;
import eqlee.ctm.api.pay.entity.vo.TokenVo;
import eqlee.ctm.api.pay.entity.vo.sucFailVo;
import eqlee.ctm.api.pay.service.IPayService;
import eqlee.ctm.api.pay.vilidata.PayData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * @Author qf
 * @Date 2019/10/28
 * @Version 1.0
 */
@Api("支付")
@Slf4j
@RestController
@RequestMapping("/v1/pay")
public class PayController {

    @Autowired
    private HttpClientUtils apiService;

    @Autowired
    private IPayService payService;

    private final String URL = "http://192.168.0.86:8122";

    /**
     * 获取token
     * @return
     * @throws Exception
     */
    public Object getPayToken () throws Exception{
        String AccessKey = "157231318100010";
        String AccessKeySecret = "6c3222a87a3f46afad471622f9fc5228";

        String url = URL +"/v1/Token/Token";

        PayToken payToken = new PayToken();
        payToken.setAccessKey(AccessKey);
        payToken.setAccessKeySecret(AccessKeySecret);

        String s = JSONObject.toJSONString(payToken);
        HttpResult httpResult = apiService.doPost(url, s);

        TokenVo vo = JSONObject.parseObject(httpResult.getBody(), TokenVo.class);

        ResultTokenVo result = vo.getResult();
        return result.getToken();
    }



    @ApiOperation(value = "微信扫码支付", notes = "微信扫码支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payOrderSerialNumber", value = "订单号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Money", value = "金钱", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "productName", value = "商品描述", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/getPcPay")
    @CrossOrigin
    public Object pay(@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                      @RequestParam("Money") Double Money,
                      @RequestParam("productName") String productName) throws Exception{
        String callbackUrl = "http://ctm.wapi.eqlee.com/ctm_api/v1/pay/sucFail";
        String url = URL + "/v1/WeChatPay/GetQRCode?payOrderSerialNumber=" + payOrderSerialNumber +"&Money=" +Money
                + "&productName=" + productName + "&callbackUrl=" +callbackUrl;

        Object token = getPayToken();

        String tokenString = "Bearer " +token;

        HttpResult httpResult = apiService.get(url, tokenString);

        return JSONObject.parse(httpResult.getBody());
    }


    /**
     * 支付完成调用此接口
     * @param vo
     * @return
     */
    @PostMapping("/sucFail")
    @CrossOrigin
    public Result sucFail (@RequestBody sucFailVo vo) {
        Pay pay = new Pay();
        if ("SUCCESS".equals(vo.getMessage())) {
            //支付成功
            pay.setApplyNo(vo.getPayOrderSerialNumber());
            pay.setPayStatu(1);
            pay.setPayDate(LocalDateTime.now());
            pay.setThirdPartyNumber(vo.getThirdPayOrderId());
            PayResult result = payService.insertPayInfo(pay);
            //修改报名表支付状态
            payService.updateApplyPayStatus(vo.getPayOrderSerialNumber());
            return Result.SUCCESS(result);
        }
        //支付失败
        pay.setApplyNo(vo.getPayOrderSerialNumber());
        pay.setPayStatu(2);
        pay.setPayDate(LocalDateTime.now());
        pay.setThirdPartyNumber(vo.getThirdPayOrderId());
        payService.insertPayInfo(pay);
        PayResult applyInfo = payService.deleteApplyInfo(vo.getPayOrderSerialNumber());
        return Result.SUCCESS(applyInfo);
    }


    @ApiOperation(value = "微信JSAPI支付", notes = "微信JSAPI支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payOrderSerialNumber", value = "订单号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Money", value = "金钱", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "productName", value = "商品描述", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "code", value = "微信个人信息code", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/getAppPay")
    @CrossOrigin
    public Object pay(@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                      @RequestParam("Money") Double Money,
                      @RequestParam("productName") String productName,
                      @RequestParam("code") String code) throws Exception{
        String callbackUrl = "http://ctm.wapi.eqlee.com/ctm_api/v1/pay/sucFail";

        //获取openId
        String openId = PayData.getOpenId(code);
        String url = URL + "/v1/WeChatPay/GetJsApiPayInfo?payOrderSerialNumber=" + payOrderSerialNumber +"&Money=" +Money
                + "&productName=" + productName + "&callbackUrl=" +callbackUrl + "&openId=" +openId;

        //获取token
        Object token = getPayToken();
        String tokenString = "Bearer " +token;

        HttpResult httpResult = apiService.get(url, tokenString);

        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "支付完成查询接口", notes = "支付完成查询接口")
    @ApiImplicitParam(name = "applyNo", value = "订单号", required = true, dataType = "String", paramType = "path")
    @GetMapping("/queryPayResult")
    @CrossOrigin
    public Result queryPayResult(@RequestParam("applyNo") String applyNo){
        ResultQuery query = payService.queryPayResult(applyNo);
        return Result.SUCCESS(query);
    }

}
