package eqlee.ctm.api.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import eqlee.ctm.api.pay.entity.bo.ParamObj;
import eqlee.ctm.api.pay.entity.bo.PayResultBo;
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
 * @date 2019/12/24
 * @vesion 1.0
 **/
@Slf4j
@Api
@RestController
@RequestMapping("/v1/ali")
public class AliSxController {


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
         @ApiImplicitParam(name = "date", value = "每月的第一天", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "returnUrl", value = "支付完成同步请求页面", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "type", value = "0-续费授信 1-结算授信金额", required = true, dataType = "String", paramType = "path")
   })
   @GetMapping("/aliPcPay")
   @CrossOrigin
   public Object getAliPcPay(@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                             @RequestParam("Money") Double Money,
                             @RequestParam("productName") String productName,
                             @RequestParam("Body") String Body,
                             @RequestParam("returnUrl") String returnUrl,
                             @RequestParam("type") Integer type,
                             @RequestParam("date") String date,
                             @RequestParam("companyName") String companyName) throws Exception{
      String callbackUrl = null;
      if (type == 0) {
         //续费
         callbackUrl = "http://ctm.wapi.eqlee.com/apply/sx/sucAliFail";
      }

      if (type == 1) {
         //结算月结
         callbackUrl = "http://ctm.wapi.eqlee.com/apply/sx/sucAliFail";
      }

      String url = URL + "/v1/Alipay/GetTradeQrCodePayPayModel?payOrderSerialNumber=" + payOrderSerialNumber +"&Money=" +Money
            + "&productName=" + productName + "&callbackUrl=" +callbackUrl + "&Body=" + Body + "&returnUrl=" +returnUrl + "&selfParameter=" +date  +companyName;

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
         @ApiImplicitParam(name = "date", value = "每月的第一天", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "returnUrl", value = "支付完成同步请求页面", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "type", value = "0-续费授信 1-结算授信金额", required = true, dataType = "String", paramType = "path")
   })
   @GetMapping("/aliPay")
   @CrossOrigin
   public Object pay(@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                     @RequestParam("Money") Double Money,
                     @RequestParam("productName") String productName,
                     @RequestParam("Body") String Body,
                     @RequestParam("returnUrl") String returnUrl,
                     @RequestParam("type") Integer type,
                     @RequestParam("date") String date,
                     @RequestParam("companyName") String companyName) throws Exception{
      String callbackUrl = null;
      if (type == 0) {
         //续费
         callbackUrl = "http://ctm.wapi.eqlee.com/apply/sx/sucAliFail";
      }

      if (type == 1) {
         //结算月结
         callbackUrl = "http://ctm.wapi.eqlee.com/apply/sx/sucAliFail";
      }

      String url = URL + "/v1/Alipay/GetTradeWapPayModel?payOrderSerialNumber=" + payOrderSerialNumber +"&Money=" +Money
            + "&productName=" + productName + "&callbackUrl=" +callbackUrl + "&Body=" +Body + "&returnUrl=" +returnUrl + "&selfParameter=" +date  +companyName;

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
