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
import eqlee.ctm.api.pay.entity.PayToken;
import eqlee.ctm.api.pay.entity.bo.PayResultBo;
import eqlee.ctm.api.pay.entity.query.ResultQuery;
import eqlee.ctm.api.pay.entity.vo.PayTokenVo;
import eqlee.ctm.api.pay.entity.vo.ResultTokenVo;
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

/**
 * @author qf
 * @date 2019/11/25
 * @vesion 1.0
 **/
@Api("支付宝支付")
@Slf4j
@RestController
@RequestMapping("/v1/aliPay")
public class AliPayController {

   @Autowired
   private HttpClientUtils apiService;

   @Autowired
   private PayTokenUtils tokenUtils;


   private final String URL = "http://pay.wapi.eqlee.com";


   @ApiOperation(value = "支付宝扫码支付", notes = "支付宝扫码支付")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "payOrderSerialNumber", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "Money", value = "金钱", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "productName", value = "产品名称标题", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "Body", value = "商品描述", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "returnUrl", value = "支付完成同步请求页面", required = true, dataType = "String", paramType = "path"),
   })
   @GetMapping("/getAliPcPay")
   @CrossOrigin
   public Object getAliPcPay(@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                     @RequestParam("Money") Double Money,
                     @RequestParam("productName") String productName,
                     @RequestParam("Body") String Body,
                     @RequestParam("returnUrl") String returnUrl) throws Exception{
      String callbackUrl = "http://ctm.wapi.eqlee.com/api/v1/pay/sucAliFail";
      String url = URL + "/v1/Alipay/GetTradeQrCodePayPayModel?payOrderSerialNumber=" + payOrderSerialNumber +"&Money=" +Money
            + "&productName=" + productName + "&callbackUrl=" +callbackUrl + "&Body=" + Body + "&returnUrl=" +returnUrl;

      String token = tokenUtils.getMapToken();
      String tokenString = "Bearer " +token;

      HttpResult httpResult = apiService.get(url, tokenString);
      if (httpResult == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "无数据返回");
      }

      if (httpResult.getCode() != 200) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "调用出错");
      }

      PayResultBo bo = JSONObject.parseObject(httpResult.getBody(), PayResultBo.class);

      if (bo.getResult().getError()) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, bo.getResult().getMsg());
      }

      if (bo.getResult().getData() == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "支付还未开通");
      }

      return bo.getResult().getData();
   }


   @ApiOperation(value = "获取支付方式", notes = "获取支付方式")
   @GetMapping("/getPayType")
   @CrossOrigin
   public Object getPayType() throws Exception{
      String url = URL + "/v1/WeChatPay/GetAuthPayWay";

      String token = tokenUtils.getMapToken();
      String tokenString = "Bearer " +token;

      HttpResult httpResult = apiService.get(url, tokenString);
      if (httpResult == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "无数据返回");
      }

      if (httpResult.getCode() != 200) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "调用出错");
      }

      PayResultBo bo = JSONObject.parseObject(httpResult.getBody(), PayResultBo.class);

      if (bo.getResult().getError()) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, bo.getResult().getMsg());
      }

      if (bo.getResult().getData() == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "支付还未开通");
      }

      return bo.getResult().getData();
   }

   @ApiOperation(value = "支付宝手机网站支付", notes = "支付宝手机网站支付")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "payOrderSerialNumber", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "Money", value = "金钱", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "productName", value = "商品标题", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "Body", value = "商品描述", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "returnUrl", value = "支付完成同步请求页面", required = true, dataType = "String", paramType = "path")
   })
   @GetMapping("/getAliPay")
   @CrossOrigin
   public Object pay(@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                     @RequestParam("Money") Double Money,
                     @RequestParam("productName") String productName,
                     @RequestParam("Body") String Body,
                     @RequestParam("returnUrl") String returnUrl) throws Exception{
      String callbackUrl = "http://ctm.wapi.eqlee.com/api/v1/pay/sucAliFail";

      String url = URL + "/v1/Alipay/GetTradeWapPayModel?payOrderSerialNumber=" + payOrderSerialNumber +"&Money=" +Money
            + "&productName=" + productName + "&callbackUrl=" +callbackUrl + "&Body=" +Body + "&returnUrl=" +returnUrl;

      //获取token
      String token = tokenUtils.getMapToken();
      String tokenString = "Bearer " +token;

      HttpResult httpResult = apiService.get(url, tokenString);
      if (httpResult == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"无返回数据");
      }
      if (httpResult.getCode() != 200) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "调用出错");
      }

      PayResultBo bo = JSONObject.parseObject(httpResult.getBody(), PayResultBo.class);

      if (bo.getResult().getError()) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, bo.getResult().getMsg());
      }

      if (bo.getResult().getData() == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "支付还未开通");
      }

      return bo.getResult().getData();
   }


   @ApiOperation(value = "支付宝支付完成查询接口", notes = "支付宝支付完成查询接口")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "applyNo", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "queryType", value = "查询订单支付或退款状态(0--支付状态 1--退款状态)", required = true, dataType = "int", paramType = "path")
   })
   @GetMapping("/queryAliPayResult")
   @CrossOrigin
   public Object queryAliPayResult(@RequestParam("applyNo") String applyNo,
                                @RequestParam("queryType") Integer queryType) throws Exception{

      //查询支付结果
      String url = URL + "/v1/WeChatPay/QueryOrderState?payOrderSerialNumber=" + applyNo + "&queryType=" +queryType;

      //获取token
      String token = tokenUtils.getMapToken();
      String tokenString = "Bearer " +token;

      HttpResult httpResult = apiService.get(url, tokenString);

      if (httpResult == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"无返回数据");
      }
      if (httpResult.getCode() != 200) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "调用出错");
      }

      PayResultBo bo = JSONObject.parseObject(httpResult.getBody(), PayResultBo.class);

      if (bo.getResult().getError()) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, bo.getResult().getMsg());
      }

      if (bo.getResult().getData() == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "支付还未开通");
      }

      return bo.getResult().getData();
   }


   @ApiOperation(value = "支付宝退款", notes = "支付宝退款")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "payOrderSerialNumber", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "RefundReason", value = "退款原因", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "refundFee", value = "退款金额", required = true, dataType = "double", paramType = "path")
   })
   @GetMapping("/aliRefund")
   @CrossOrigin
   public Object refund (@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                         @RequestParam("RefundReason") String RefundReason,
                         @RequestParam("refundFee") Double refundFee) throws Exception{

      String url = URL + "/v1/Alipay/AliRefund?payOrderSerialNumber=" + payOrderSerialNumber +"&RefundReason=" +RefundReason
            + "&refundFee=" +refundFee;

      //获取token
      String token = tokenUtils.getMapToken();
      String tokenString = "Bearer " +token;

      HttpResult httpResult = apiService.get(url, tokenString);

      if (httpResult == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"无返回数据");
      }

      if (httpResult.getCode() != 200) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "调用出错");
      }

      PayResultBo bo = JSONObject.parseObject(httpResult.getBody(), PayResultBo.class);

      if (bo.getResult().getError()) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, bo.getResult().getMsg());
      }

      if (bo.getResult().getData() == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "支付还未开通");
      }

      return bo.getResult().getData();
   }

}
