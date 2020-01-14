package eqlee.ctm.api.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.yq.anntation.IgnoreResponseAdvice;
import com.yq.constanct.CodeType;
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
import eqlee.ctm.api.pay.entity.bo.ParamObj;
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
 * @date 2019/12/24
 * @vesion 1.0
 **/
@Slf4j
@Api
@RestController
@RequestMapping("/v1/wx")
public class WxSxController {

   @Autowired
   private HttpClientUtils apiService;

   @Autowired
   private IPayService payService;

   @Autowired
   private LocalUser localUser;

   @Autowired
   private IPayInfoService payInfoService;

   @Autowired
   private PayTokenUtils payTokenUtils;

   IdGenerator idGenerator = new IdGenerator();

   private final String URL = "http://pay.wapi.eqlee.com";



   @ApiOperation(value = "授信金额微信扫码支付", notes = "授信金额微信扫码支付")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "payOrderSerialNumber", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "Money", value = "金钱", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "productName", value = "商品描述", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "date", value = "每月的第一天", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "type", value = "0-续费授信 1-结算授信金额", required = true, dataType = "String", paramType = "path")
   })
   @GetMapping("/pcPay")
   @CrossOrigin
   @IgnoreResponseAdvice
   public Object pay(@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                     @RequestParam("Money") Double Money,
                     @RequestParam("productName") String productName,
                     @RequestParam("type") Integer type,
                     @RequestParam("date") String date,
                     @RequestParam("companyName") String companyName) throws Exception{
      String callbackUrl = null;
      if (type == 0) {
         //续费
         callbackUrl = "http://ctm.wapi.eqlee.com/apply/sx/sucFail";
      }

      if (type == 1) {
         //结算月结
         callbackUrl = "http://ctm.wapi.eqlee.com/apply/sx/sucFail";
      }

      String url = URL + "/v1/WeChatPay/GetQRCode?payOrderSerialNumber=" + payOrderSerialNumber +"&Money=" +Money
            + "&productName=" + productName + "&callbackUrl=" +callbackUrl + "&selfParameter=" + date +companyName;

      String token = payTokenUtils.getMapToken();
      String tokenString = "Bearer " +token;

      HttpResult httpResult = apiService.get(url, tokenString);
      if (httpResult == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "无数据返回");
      }

      return JSONObject.parse(httpResult.getBody());
   }


   @ApiOperation(value = "微信JSAPI支付", notes = "微信JSAPI支付")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "payOrderSerialNumber", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "Money", value = "金钱", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "productName", value = "商品描述", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "code", value = "微信个人信息code", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "date", value = "每月的第一天", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "type", value = "0-续费授信 1-结算授信金额", required = true, dataType = "String", paramType = "path")
   })
   @GetMapping("/appPay")
   @CrossOrigin
   @CheckToken
   @IgnoreResponseAdvice
   public Object pay(@RequestParam("payOrderSerialNumber") String payOrderSerialNumber,
                     @RequestParam("Money") Double Money,
                     @RequestParam("productName") String productName,
                     @RequestParam("code") String code,
                     @RequestParam("type") Integer type,
                     @RequestParam("date") String date,
                     @RequestParam("companyName") String companyName) throws Exception{
      String callbackUrl = null;
      if (type == 0) {
         //续费
         callbackUrl = "http://ctm.wapi.eqlee.com/apply/sx/sucFail";
      }

      if (type == 1) {
         //结算月结
         callbackUrl = "http://ctm.wapi.eqlee.com/apply/sx/sucFail";
      }

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
            + "&productName=" + productName + "&callbackUrl=" +callbackUrl + "&openId=" +openId + "&selfParameter=" +date +companyName;

      //获取token
      String token = payTokenUtils.getMapToken();
      String tokenString = "Bearer " +token;

      HttpResult httpResult = apiService.get(url, tokenString);
      if (httpResult == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"无返回数据");
      }
      return JSONObject.parse(httpResult.getBody());
   }



}
