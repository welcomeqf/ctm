package eqlee.ctm.api.pay.controller;


import com.alibaba.fastjson.JSONObject;
import com.yq.anntation.IgnoreResponseAdvice;
import com.yq.constanct.CodeType;
import com.yq.data.Result;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import com.yq.jwt.contain.LocalUser;
import com.yq.jwt.entity.UserLoginQuery;
import com.yq.jwt.islogin.CheckToken;
import com.yq.utils.IdGenerator;
import com.yq.utils.StringUtils;
import eqlee.ctm.api.code.entity.PayInfo;
import eqlee.ctm.api.code.entity.query.PayInfoQuery;
import eqlee.ctm.api.code.service.IPayInfoService;
import eqlee.ctm.api.pay.entity.Pay;
import eqlee.ctm.api.pay.entity.PayResult;
import eqlee.ctm.api.pay.entity.PayToken;
import eqlee.ctm.api.pay.entity.bo.PayInfoBo;
import eqlee.ctm.api.pay.entity.query.ResultQuery;
import eqlee.ctm.api.pay.entity.vo.PayTokenVo;
import eqlee.ctm.api.pay.entity.vo.ResultTokenVo;
import eqlee.ctm.api.pay.entity.vo.sucFailVo;
import eqlee.ctm.api.pay.service.IPayService;
import eqlee.ctm.api.pay.vilidata.PayData;
import eqlee.ctm.api.pay.vilidata.PayTokenUtils;
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

    @Autowired
    private IPayInfoService payInfoService;

    @Autowired
    private LocalUser localUser;

    @Autowired
    private PayTokenUtils payTokenUtils;

    IdGenerator idGenerator = new IdGenerator();

    private final String URL = "http://pay.wapi.eqlee.com";


    @ApiOperation(value = "微信扫码支付", notes = "微信扫码支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payOrderSerialNumber", value = "订单号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "Money", value = "金钱", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "productName", value = "商品描述", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/getPcPay")
    @CrossOrigin
    @IgnoreResponseAdvice
    public Object pay(@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                      @RequestParam("Money") Double Money,
                      @RequestParam("productName") String productName) throws Exception{
        String callbackUrl = "http://ctm.wapi.eqlee.com/api/v1/pay/sucFail";
        String url = URL + "/v1/WeChatPay/GetQRCode?payOrderSerialNumber=" + payOrderSerialNumber +"&Money=" +Money
                + "&productName=" + productName + "&callbackUrl=" +callbackUrl;

        String token = payTokenUtils.getMapToken();
        String tokenString = "Bearer " +token;

        HttpResult httpResult = apiService.get(url, tokenString);
        if (httpResult == null) {
           throw new ApplicationException(CodeType.SERVICE_ERROR, "无数据返回");
        }

        return JSONObject.parse(httpResult.getBody());
    }


    /**
     * 微信支付完成调用此接口
     * @param vo
     * @return
     */
    @PostMapping("/sucFail")
    @CrossOrigin
    @IgnoreResponseAdvice
    public Result sucFail (@RequestBody sucFailVo vo) {
        Pay pay = new Pay();
        if ("SUCCESS".equals(vo.getMessage())) {
            //支付成功
            pay.setId(idGenerator.getNumberId());
            pay.setApplyNo(vo.getPayOrderSerialNumber());
            pay.setPayStatu(1);
            pay.setPayDate(LocalDateTime.now());
            pay.setThirdPartyNumber(vo.getThirdPayOrderId());
            pay.setRemark(vo.getMessage());
            pay.setPayType(0);
            PayResult result = payService.insertPayInfo(pay);
            //修改报名表支付状态
            payService.updateApplyPayStatus(vo.getPayOrderSerialNumber(),0);
            pay.setId(idGenerator.getNumberId());
            pay.setApplyNo(vo.getPayOrderSerialNumber());
            pay.setPayStatu(2);
            pay.setPayDate(LocalDateTime.now());
            pay.setThirdPartyNumber(vo.getThirdPayOrderId());
            pay.setRemark(vo.getMessage());
            pay.setPayType(0);
            payService.insertPayInfo(pay);
            return Result.SUCCESS(result);
        }
        //支付失败
        PayResult applyInfo = payService.deleteApplyInfo(vo.getPayOrderSerialNumber());
        return Result.SUCCESS(applyInfo);
    }

    /**
     * 支付宝支付完成调用此接口
     * @param vo
     * @return
     */
    @PostMapping("/sucAliFail")
    @CrossOrigin
    @IgnoreResponseAdvice
    public Result sucAliFail (@RequestBody sucFailVo vo) {
        Pay pay = new Pay();
        if ("SUCCESS".equals(vo.getMessage())) {
            //支付成功
            pay.setId(idGenerator.getNumberId());
            pay.setApplyNo(vo.getPayOrderSerialNumber());
            pay.setPayStatu(1);
            pay.setPayDate(LocalDateTime.now());
            pay.setThirdPartyNumber(vo.getThirdPayOrderId());
            pay.setPayType(1);
            pay.setRemark(vo.getMessage());
            PayResult result = payService.insertPayInfo(pay);
            //修改报名表支付状态
            payService.updateApplyPayStatus(vo.getPayOrderSerialNumber(),1);
            return Result.SUCCESS(result);
        }
        //支付失败
        PayResult applyInfo = payService.deleteApplyInfo(vo.getPayOrderSerialNumber());
        pay.setId(idGenerator.getNumberId());
        pay.setApplyNo(vo.getPayOrderSerialNumber());
        pay.setPayStatu(2);
        pay.setPayDate(LocalDateTime.now());
        pay.setThirdPartyNumber(vo.getThirdPayOrderId());
        pay.setPayType(1);
        pay.setRemark(vo.getMessage());
        PayResult result = payService.insertPayInfo(pay);
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
    @CheckToken
    @IgnoreResponseAdvice
    public Object pay(@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                      @RequestParam("Money") Double Money,
                      @RequestParam("productName") String productName,
                      @RequestParam("code") String code) throws Exception{
        String callbackUrl = "http://ctm.wapi.eqlee.com/api/v1/pay/sucFail";
        UserLoginQuery user = localUser.getUser("用户信息");
        String openId = null;

        if (StringUtils.isBlank(code)) {
            //查询数据库中的openId
            PayInfoQuery query = payInfoService.queryPayInfo(user.getId());
            openId = query.getOpenId();
        }

        if (StringUtils.isNotBlank(code)) {
            //装配支付信息
            Boolean pay = payInfoService.queryPay(user.getId());

            if (pay) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"请传入空的code");
            }

            //获取openId
            openId = PayData.getOpenId(code);

            log.error("openId:" +openId);
            if (StringUtils.isBlank(openId)) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "请重新授权");
            }

            PayInfo payInfo = new PayInfo();
            payInfo.setId(idGenerator.getNumberId());
            payInfo.setType(0);
            payInfo.setCode(code);
            payInfo.setOpenId(openId);
            payInfo.setUserId(user.getId());
            payInfo.setCreateUserId(user.getId());
            payInfo.setUpdateUserId(user.getId());
            Integer integer = payInfoService.insertPayInfo(payInfo);

            if (integer <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"code存入失败");
            }
        }

        String url = URL + "/v1/WeChatPay/GetJsApiPayInfo?payOrderSerialNumber=" + payOrderSerialNumber +"&Money=" +Money
                + "&productName=" + productName + "&callbackUrl=" +callbackUrl + "&openId=" +openId;

        //获取token
        String token = payTokenUtils.getMapToken();
        String tokenString = "Bearer " +token;

        HttpResult httpResult = apiService.get(url, tokenString);
        if (httpResult == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"无返回数据");
        }
        return JSONObject.parse(httpResult.getBody());
    }


    @ApiOperation(value = "支付完成查询接口", notes = "支付完成查询接口")
    @ApiImplicitParam(name = "applyNo", value = "订单号", required = true, dataType = "String", paramType = "path")
    @GetMapping("/queryPayResult")
    @IgnoreResponseAdvice
    @CrossOrigin
    public Object queryPayResult(@RequestParam("applyNo") String applyNo) throws Exception{

        //查询支付结果
        String url = URL + "/v1/WeChatPay/QueryOrderState?payOrderSerialNumber=" + applyNo;

        //获取token
        String token = payTokenUtils.getMapToken();
        String tokenString = "Bearer " +token;

        HttpResult httpResult = apiService.get(url, tokenString);

        if (httpResult == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"无返回数据");
        }

        if (httpResult.getCode() == 200) {
            ResultQuery query = payService.queryPayResult(applyNo);
            return Result.SUCCESS(query);
        }

        return JSONObject.parse(httpResult.getBody());
    }

    @ApiOperation(value = "微信退款", notes = "微信退款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payOrderSerialNumber", value = "订单号", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "totalFee", value = "订单总金额", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "refundFee", value = "退款金额", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/refund")
    @IgnoreResponseAdvice
    @CrossOrigin
    public Object refund (@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                          @RequestParam("totalFee") Double totalFee,
                          @RequestParam("refundFee") Double refundFee) throws Exception{

        String url = URL + "/v1/WeChatPay/WechatRefund?payOrderSerialNumber=" + payOrderSerialNumber +"&totalFee=" +totalFee
                + "&refundFee=" +refundFee;

        //获取token
        String token = payTokenUtils.getMapToken();
        String tokenString = "Bearer " +token;

        HttpResult httpResult = apiService.get(url, tokenString);

        if (httpResult == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"无返回数据");
        }

        return JSONObject.parse(httpResult.getBody());
    }



    @GetMapping("/insertPayInfo")
    @IgnoreResponseAdvice
    @CrossOrigin
    @CheckToken
    public void insertPayInfo (@RequestBody PayInfoBo vo){
        Pay pay = new Pay();
        pay.setId(idGenerator.getNumberId());
        pay.setApplyNo(vo.getPayOrderSerialNumber());
        pay.setPayStatu(vo.getPayStatus());
        pay.setPayDate(LocalDateTime.now());
        pay.setThirdPartyNumber(vo.getThirdPayOrderId());
        pay.setRemark(vo.getMessage());
        pay.setPayType(vo.getPayType());
        payService.insertPayInfo(pay);
    }


}
