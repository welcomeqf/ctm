package eqlee.ctm.api.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.anntation.IgnoreResponseAdvice;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import com.yq.jwt.contain.LocalUser;
import com.yq.utils.IdGenerator;
import eqlee.ctm.api.code.service.IPayInfoService;
import eqlee.ctm.api.pay.entity.PayToken;
import eqlee.ctm.api.pay.entity.vo.PayTokenVo;
import eqlee.ctm.api.pay.entity.vo.ResultTokenVo;
import eqlee.ctm.api.pay.service.IPayService;
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
   private IPayService payService;

   @Autowired
   private IPayInfoService payInfoService;

   @Autowired
   private LocalUser localUser;

   IdGenerator idGenerator = new IdGenerator();

   private final String URL = "http://pay.wapi.eqlee.com";

   private final String AccessKey = "157222853200009";

   private final String AccessKeySecret = "74c172912d9b4631bc72e567c27b2ece";

   /**
    * 获取token
    * @return
    * @throws Exception
    */
   public Object getPayToken () throws Exception{

      String url = URL +"/v1/Token/Token";

      PayToken payToken = new PayToken();
      payToken.setAccessKey(AccessKey);
      payToken.setAccessKeySecret(AccessKeySecret);

      String s = JSONObject.toJSONString(payToken);
      HttpResult httpResult = apiService.doPost(url, s);

      PayTokenVo vo = JSONObject.parseObject(httpResult.getBody(), PayTokenVo.class);

      ResultTokenVo result = vo.getResult();
      return result.getToken();
   }


   @ApiOperation(value = "支付宝扫码支付", notes = "支付宝扫码支付")
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
                     @RequestParam("productName") String productName,
                     @RequestParam("Body") String Body,
                     @RequestParam("returnUrl") String returnUrl) throws Exception{
      String callbackUrl = "http://ctm.wapi.eqlee.com/api/v1/pay/sucFail";
      String url = URL + "/v1/Alipay/GetTradeQrCodePayPayModel?payOrderSerialNumber=" + payOrderSerialNumber +"&Money=" +Money
            + "&productName=" + productName + "&callbackUrl=" +callbackUrl + "&Body=" + Body + "&returnUrl=" +returnUrl;

      Object token = getPayToken();
      String tokenString = "Bearer " +token;

      HttpResult httpResult = apiService.get(url, tokenString);
      if (httpResult == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "无数据返回");
      }

      return JSONObject.parse(httpResult.getBody());
   }
}
