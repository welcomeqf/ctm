package com.yq.utils;


import com.alibaba.fastjson.JSONObject;
import com.yq.constanct.CodeType;
import com.yq.entity.send.*;
import com.yq.entity.send.ctmuser.*;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 发短信推送工具类 以及各工程相互调取工具类 若有修改 请使用Ctrl + 鼠标左键查看方法引用 对应更新
 * @author xub
 * @date 2020年7月28日11:37:31
 * @vesion 1.0
 **/
@Slf4j
@Component
public class SendUtils {

   public SendUtils() {
   }
   @Autowired
   private HttpClientUtils apiService;

   private final String URL = "http://sms.wapi.eqlee.com/v1";

   //短信
   private final String AppId = "1596012833";

   private final String Secret = "189f011dada246429842deb65b31e9fd";

   //微信公众号信息
   private final String wx_appid = "wxd1a8a6b0eed550f4";
   private final String wx_secret = "597f021bdcd903d03b9ef73035cbaa42";

   //微信公众号信息 test
   private final String wx_appid1 = "wx12c72482508c5c52";
   private final String wx_secret1 = "6daa8b189c2bc17f2da90ac3fc62c9d2";

   // 获取access_token的接口地址（GET） 限2000（次/天）
   public final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
   // 发送模板消息
   public final String SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send";

   /**
    * Ctm_user
    */
   private final String user_url = "http://localhost:9092/ctm_user";

   private final String user_url1 = "http://localhost:8001";

   private final String accessKey = "20191115111837644858794286944256";

   private final String accessKeySecret = "78421a143252433783afd3af8ba1d2f4";

   /**
    * 用户模块路径
    */


   /**
    * 创建一个容器
    */
   Map<String,Object> user_map = new ConcurrentHashMap<>();

   /**
    * 创建容器
    */
   Map<String,Object> map = new ConcurrentHashMap<>();

   /**
    * 创建容器
    */
   Map<String,Object> wx_map = new ConcurrentHashMap<>();

   /**
    * 获取token
    * @return
    * @throws Exception
    */
   public AuthResult getUserToken () throws Exception{

      String url = user_url +"/v1/auth/loginAuth";

      AuthLoginParamVo payToken = new AuthLoginParamVo();
      payToken.setAccessKey(accessKey);
      payToken.setAccessKeySecret(accessKeySecret);

      String s = JSONObject.toJSONString(payToken);
      HttpResult httpResult = apiService.doPost(url,s);

      TokenWithVo vo = JSONObject.parseObject(httpResult.getBody(), TokenWithVo.class);

      AuthResult data = vo.getData();

      user_map.put("token",vo.getData().getToken());
      user_map.put("exp",vo.getData().getExpTime());

      return data;
   }

   /**
    * 取ctm的token
    * @return
    * @throws Exception
    */
   public String getuserMapToken () throws Exception {

      //先去容器取出token判断是否过期
      Long exp = (Long) map.get("exp");

      //如果值为空
      if (exp == null) {
         AuthResult userToken = getUserToken();
         return userToken.getToken();
      }

      Date date = new Date();
      Date erpDate = new Date(exp);
      //判断token时间是否过期
      if (erpDate.before(date)) {
         //token已过期
         AuthResult userToken = getUserToken();
         return userToken.getToken();
      }

      String token = (String) map.get("token");
      return token;
   }


   /**
    * 获取系统指定微信公众号推送人
    * type = 1 接收导游选人通知(需根据城市过滤) 2 接收报名审核通知  3 接收交账审核通知 4 接收同行审核通知 5 审核结果通知同行city字段对应同行账号
    * @return
    */
   public String queryNotifyAdminInfo (String city,Integer type){
      //路径
      String url = user_url + "/v1/app/user/queryGuideByCity?city=" + city + "&type=" + type;
      HttpResult httpResult = null;

      try {
         //获得token
         String userToken = getuserMapToken();
         String token = "Bearer " + userToken;
         httpResult = apiService.get(url,token);
      }catch (Exception e) {
         e.printStackTrace();
      }

      SendResultVo result = JSONObject.parseObject(httpResult.getBody(), SendResultVo.class);
      if (result.getCode() != 0) {
         throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
      }

      return JSONObject.toJSONString(result.getData());
   }

   /**  若有修改 更新 main 工程
    * 同行公司审核通过同步添加同行用户
    * 登录账号：信柏商务CompanyName，初始密码：注册手机号后6位 角色:同行
    * @return
    */
   public String AddPeerUserInfo (String account, Long companyId, String userName, String phone, List<CityBo> city){
      //路径
      String url = user_url + "/v1/app/user/register";
      HttpResult httpResult = null;

      try {
         //组装用户信息
         UserVo userVo = new UserVo();
         userVo.setCompanyId(companyId);
         userVo.setUserName(account);
         userVo.setName(userName);
         userVo.setCompanyName(account);
         userVo.setRoleName("同行");
         userVo.setPhone(phone);
         userVo.setCity(city);
         userVo.setType(0);
         //用户密码
         String psw = "123456";
         if(phone.length()>=6){
            psw = phone.substring(phone.length()- 6,phone.length());
         }
         userVo.setPassword(psw);

         String s = JSONObject.toJSONString(userVo);

         //获得token
         String userToken = getuserMapToken();
         String token = "Bearer " + userToken;
         httpResult = apiService.post(url, s,token);
      }catch (Exception e) {
         e.printStackTrace();
      }

      SendResultVo result = JSONObject.parseObject(httpResult.getBody(), SendResultVo.class);
      if (result.getCode() != 0) {
         throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
      }

      return JSONObject.toJSONString(result.getData());
   }

   /**
    * 请求短信token
    * @return
    * @throws Exception
    */
   private String getSendToken() throws Exception{

      String url = URL +"/Token/GetToken";

      SendToken sendsToken = new SendToken();
      sendsToken.setAppId(AppId);
      sendsToken.setAppSecret(Secret);

      String s = JSONObject.toJSONString(sendsToken);
      HttpResult httpResult = apiService.doPost(url, s);

      SendTokenVo vo = JSONObject.parseObject(httpResult.getBody(), SendTokenVo.class);

      ResultTokenVo result = vo.getResult();

      map.put("send_token",result.getToken());
      map.put("send_ext",result.getExpires());
      return result.getToken();
   }


   private String getMapToken() throws Exception {

      Long ext = (Long) map.get("send_ext");

      //如果过期时间过期或存的token为空则重新请求，若未过期则用存的token
      if (ext == null) {
         return getSendToken();
      }

      Long now = Instant.now().getEpochSecond();

      if (now < ext) {
         //未过期
         String result = (String) map.get("send_token");
         return result;
      }

      return getSendToken();
   }

   public Boolean send(String mobile,String content) throws Exception {
      Boolean flag = true;
      String url = URL +"/Sms/SendSms";
      //获得token
      String userToken = getMapToken();
      String token = "Bearer " + userToken;
      SendModel sendModel = new SendModel();
      sendModel.setMobile(mobile);
      sendModel.setContent(content);
      String s = JSONObject.toJSONString(sendModel);
      HttpResult httpResult = apiService.post(url, s,token);
      ResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
      if (vo.getStatusCode() != 200) {
         //throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,"短信发送失败！");
         flag = false;
      }
      if(vo.getResult().getError()){
         //throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,vo.getResult().getMsg());
         flag = false;
      }
      return flag;
   }



   /**
    * 获取微信Access_token
    * @return
    * @throws Exception
    */
   private String getWechatToken() throws Exception{

      String url = ACCESS_TOKEN_URL +"?grant_type=client_credential&appid=" + wx_appid + "&secret=" + wx_secret + "";

      String returnData = getReturnData(url);

      JSONObject jsonObject;
      try {
         jsonObject = JSONObject.parseObject(returnData);
         String access_token = jsonObject.getString("access_token");
         String expires_in = jsonObject.getString("expires_in");
         wx_map.put("wx_token",access_token);
         wx_map.put("wx_ext",expires_in);
         return access_token;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return "";
   }

   public static String getReturnData(String urlString) {
      String res = "";
      try {
         java.net.URL url = new URL(urlString);
         java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url
                 .openConnection();
         conn.connect();
         java.io.BufferedReader in = new java.io.BufferedReader(
                 new java.io.InputStreamReader(conn.getInputStream(),
                         "UTF-8"));
         String line;
         while ((line = in.readLine()) != null) {
            res += line;
         }
         in.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return res;
   }


   public String getwxMapToken() throws Exception {

      Long ext = (Long) map.get("wx_ext");

      //如果过期时间过期或存的token为空则重新请求，若未过期则用存的token
      if (ext == null) {
         return getWechatToken();
      }

      Long now = Instant.now().getEpochSecond();

      if (now < ext) {
         //未过期
         String result = (String) map.get("wx_token");
         return result;
      }

      return getWechatToken();
   }

   public JSONObject getOpenId (String code) {

      if(code != null){
         String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                 + "appid=" + wx_appid
                 + "&secret=" + wx_secret
                 + "&code=" + code + "&grant_type=authorization_code";

         String returnData = getReturnData(url);

         JSONObject jsonObject;
         try {
            jsonObject = JSONObject.parseObject(returnData);
            //String openid = jsonObject.getString("openid");
            return jsonObject;
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return null;
   }

   /*
   * 通过openid获取用户基本信息
   */
   public JSONObject getUserInfoByOpenId (String openid,String token) throws Exception {

      JSONObject jsonObject;
      //获得token
      if(openid != null){
         String url = "https://api.weixin.qq.com/sns/userinfo?"
                 + "access_token=" + token
                 + "&openid=" + openid
                 + "&lang=zh_CN";

         String returnData = getReturnData(url);

         try {
            jsonObject = JSONObject.parseObject(returnData);
            return jsonObject;
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return null;
   }

   /*
   * 报名审核成功，通知导游选人成团(通知导游)
   * 模板ID：ANUyH-uwrSH_ELkIXiDbyqt9J3VxX5lJrOb_Yg3rYi4
   * 有新的报名记录，请前往系统选择人员！
      活动标题：珠海一日游
      报名人：李四
      报名电话：13800138000
      请尽快处理！
   */
   public Boolean pushGuideSelect(String openId,String lineName,String applyName,String applyPhone,String outDate) throws Exception {
      Boolean flag = true;
      //获得token
      String token = getwxMapToken();
      String url = SEND_MESSAGE +"?access_token=" + token;
      Map<String, Object> pushData = new HashMap<String, Object>();
      pushData.put( "touser", openId);
      pushData.put( "template_id", "ANUyH-uwrSH_ELkIXiDbyqt9J3VxX5lJrOb_Yg3rYi4");
      pushData.put( "url", "http://510766.com/roadList");
      //模板数据
      Map<String, WechatPushTemplate> templateMap = new HashMap<String, WechatPushTemplate>();
      WechatPushTemplate first =  new WechatPushTemplate();
      first.setValue("有新的报名记录，请前往系统选择人员！(出行日期:" + outDate + ")");
      first.setColor("#173177");
      templateMap.put("first",first);

      WechatPushTemplate keyword1 =  new WechatPushTemplate();
      keyword1.setValue(lineName);
      keyword1.setColor("#173177");
      templateMap.put("keyword1",keyword1);

      WechatPushTemplate keyword2 =  new WechatPushTemplate();
      keyword2.setValue(applyName);
      keyword2.setColor("#173177");
      templateMap.put("keyword2",keyword2);

      WechatPushTemplate keyword3 =  new WechatPushTemplate();
      keyword3.setValue(applyPhone);
      keyword3.setColor("#173177");
      templateMap.put("keyword3",keyword3);

      WechatPushTemplate remark =  new WechatPushTemplate();
      remark.setValue("请尽快处理！");
      remark.setColor("#173177");
      templateMap.put("remark",remark);

      pushData.put("data",templateMap);

      String s = JSONObject.toJSONString(pushData);
      HttpResult httpResult = apiService.post(url, s,"");
      WechatResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),WechatResultResposeVo.class);
      if(vo.getErrcode() != 0){
         flag = false;
      }
      return flag;
   }

   /*
      同行注册待审核提醒(通知管理人员)
      标题：入驻待审核通知
      模板ID：LMTG0_p1UqTVNBY8JKXFh7686FqlHwcyik81X04r6RA
      您有同行注册了，记得去审核哦！！
      入驻申请客户：珠海四海商务有限公司
      对接人：王二小
      对接人电话：13800138000
      请尽快处理！
      http://510766.com/clEdit?id=公司id
  */
   public Boolean pushCompanyExamManage(String openId,String companyName,String contactName,String contactPhone,Long companyId) throws Exception {
      Boolean flag = true;
      //获得token
      String token = getwxMapToken();
      String url = SEND_MESSAGE +"?access_token=" + token;
      Map<String, Object> pushData = new HashMap<String, Object>();
      pushData.put( "touser", openId);
      pushData.put( "template_id", "LMTG0_p1UqTVNBY8JKXFh7686FqlHwcyik81X04r6RA");
      pushData.put( "url", "http://510766.com/clEdit" + "?id=" + companyId);
      //模板数据
      Map<String, WechatPushTemplate> templateMap = new HashMap<String, WechatPushTemplate>();
      WechatPushTemplate first =  new WechatPushTemplate();
      first.setValue("您有同行注册了，记得去审核哦！！");
      first.setColor("#173177");
      templateMap.put("first",first);

      WechatPushTemplate keyword1 =  new WechatPushTemplate();
      keyword1.setValue(companyName);
      keyword1.setColor("#173177");
      templateMap.put("keyword1",keyword1);

      WechatPushTemplate keyword2 =  new WechatPushTemplate();
      keyword2.setValue(contactName);
      keyword2.setColor("#173177");
      templateMap.put("keyword2",keyword2);

      WechatPushTemplate keyword3 =  new WechatPushTemplate();
      keyword3.setValue(contactPhone);
      keyword3.setColor("#173177");
      templateMap.put("keyword3",keyword3);

      WechatPushTemplate remark =  new WechatPushTemplate();
      remark.setValue("请尽快处理！");
      remark.setColor("#173177");
      templateMap.put("remark",remark);

      pushData.put("data",templateMap);

      String s = JSONObject.toJSONString(pushData);
      HttpResult httpResult = apiService.post(url, s,"");
      WechatResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),WechatResultResposeVo.class);
      if(vo.getErrcode() != 0){
         flag = false;
      }
      return flag;
   }

   /* 报名待审核提醒(通知管理人员) 需要按城市过滤 包括取消报名审核
      标题：报名审核提醒
      模板ID：HVNVtZhFbuXzjy-ioFFb28M0NP91XuiRimyyCQuD7_c
      test模板id:lC3toe5zPaK3iEm5eIfziFPV5vbzbJdTWF4Qb0PRZ9U
      您有新的报名待审核信息，记得去审核哦！！
      姓名：张三
      时间：2020-07-25 18:23:01
      请尽快处理！
      http://510766.com/examEdit?id=applyno&type=0&status=0
  */
   public Boolean pushApplyExamManage(String openId,String contactName,String applyDate,String applyNo) throws Exception {
      Boolean flag = true;
      //获得token
      String token = getwxMapToken();
      String url = SEND_MESSAGE +"?access_token=" + token;
      Map<String, Object> pushData = new HashMap<String, Object>();
      pushData.put( "touser", openId);
      pushData.put( "template_id", "HVNVtZhFbuXzjy-ioFFb28M0NP91XuiRimyyCQuD7_c");
      pushData.put( "url", "http://510766.com/examEdit" + "?id=" + applyNo + "&type=0&status=0");
      //模板数据
      Map<String, WechatPushTemplate> templateMap = new HashMap<String, WechatPushTemplate>();
      WechatPushTemplate first =  new WechatPushTemplate();
      first.setValue("您有新的报名待审核信息，记得去审核哦！！");
      first.setColor("#173177");
      templateMap.put("first",first);

      WechatPushTemplate keyword1 =  new WechatPushTemplate();
      keyword1.setValue(contactName);
      keyword1.setColor("#173177");
      templateMap.put("keyword1",keyword1);

      WechatPushTemplate keyword2 =  new WechatPushTemplate();
      keyword2.setValue(applyDate);
      keyword2.setColor("#173177");
      templateMap.put("keyword2",keyword2);


      WechatPushTemplate remark =  new WechatPushTemplate();
      remark.setValue("请尽快处理！");
      remark.setColor("#173177");
      templateMap.put("remark",remark);

      pushData.put("data",templateMap);

      String s = JSONObject.toJSONString(pushData);
      HttpResult httpResult = apiService.post(url, s,"");
      WechatResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),WechatResultResposeVo.class);
      if(vo.getErrcode() != 0){
         flag = false;
      }
      return flag;
   }


   /*
      报名审核结果通知(通知同行)
      标题：审核结果通知
      模板ID：05bbdJxMv4rzSCzSMYyHuhtPWfi290SogGB4MYadzyQ
      亲，您提交的申请已处理。
      姓名：张三 13800138000
      审核结果：已通过
      审核时间：2020-07-25
      感谢您的使用，谢谢！！
  */
   public Boolean pushApplyExamPeer(String openId,String contactName,String contactPhone,String examDate,String examResult) throws Exception {
      Boolean flag = true;
      //获得token
      String token = getwxMapToken();
      String url = SEND_MESSAGE +"?access_token=" + token;
      Map<String, Object> pushData = new HashMap<String, Object>();
      pushData.put( "touser", openId);
      pushData.put( "template_id", "05bbdJxMv4rzSCzSMYyHuhtPWfi290SogGB4MYadzyQ");
      pushData.put( "url", "http://510766.com/signedList");
      //模板数据
      Map<String, WechatPushTemplate> templateMap = new HashMap<String, WechatPushTemplate>();
      WechatPushTemplate first =  new WechatPushTemplate();
      first.setValue("亲，您提交的申请已处理。");
      first.setColor("#173177");
      templateMap.put("first",first);

      WechatPushTemplate keyword1 =  new WechatPushTemplate();
      keyword1.setValue(contactName + " " + contactPhone);
      keyword1.setColor("#173177");
      templateMap.put("keyword1",keyword1);

      WechatPushTemplate keyword2 =  new WechatPushTemplate();
      keyword2.setValue(examResult);
      keyword2.setColor("#173177");
      templateMap.put("keyword2",keyword2);

      WechatPushTemplate keyword3 =  new WechatPushTemplate();
      keyword3.setValue(examDate);
      keyword3.setColor("#173177");
      templateMap.put("keyword3",keyword3);

      WechatPushTemplate remark =  new WechatPushTemplate();
      remark.setValue("感谢您的使用，谢谢！！");
      remark.setColor("#173177");
      templateMap.put("remark",remark);

      pushData.put("data",templateMap);

      String s = JSONObject.toJSONString(pushData);
      HttpResult httpResult = apiService.post(url, s,"");
      WechatResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),WechatResultResposeVo.class);
      if(vo.getErrcode() != 0){
         flag = false;
      }
      return flag;
   }


   /*导游交账审核提醒(通知管理员)
   * 标题： 订单待审核提醒
   * 模板ID：RTcvxc6svIUdaB52_-QQA1UGpe5mjhH-y6B-uLS5zic
   * 您有新的交账订单待审核！
      订单号：123456789
      提交时间：2020-07-22
      提交人：林洁
      请尽快处理！
      http://510766.com/checkEdit?id=订单id&value=订单状态默认0
   */
   public Boolean pushBillExamManage(String openId,String orderNo,String orderDate,String guideName,Long orderId) throws Exception {
      Boolean flag = true;
      //获得token
      String token = getwxMapToken();
      String url = SEND_MESSAGE +"?access_token=" + token;
      Map<String, Object> pushData = new HashMap<String, Object>();
      pushData.put( "touser", openId);
      pushData.put( "template_id", "RTcvxc6svIUdaB52_-QQA1UGpe5mjhH-y6B-uLS5zic");
      pushData.put( "url", "http://510766.com/checkEdit?id=" + orderId + "&value=0");
      //模板数据
      Map<String, WechatPushTemplate> templateMap = new HashMap<String, WechatPushTemplate>();
      WechatPushTemplate first =  new WechatPushTemplate();
      first.setValue("您有新的交账订单待审核！");
      first.setColor("#173177");
      templateMap.put("first",first);

      WechatPushTemplate keyword1 =  new WechatPushTemplate();
      keyword1.setValue(orderNo);
      keyword1.setColor("#173177");
      templateMap.put("keyword1",keyword1);

      WechatPushTemplate keyword2 =  new WechatPushTemplate();
      keyword2.setValue(orderDate);
      keyword2.setColor("#173177");
      templateMap.put("keyword2",keyword2);

      WechatPushTemplate keyword3 =  new WechatPushTemplate();
      keyword3.setValue(guideName);
      keyword3.setColor("#173177");
      templateMap.put("keyword3",keyword3);

      WechatPushTemplate remark =  new WechatPushTemplate();
      remark.setValue("请尽快处理！");
      remark.setColor("#173177");
      templateMap.put("remark",remark);

      pushData.put("data",templateMap);

      String s = JSONObject.toJSONString(pushData);
      HttpResult httpResult = apiService.post(url, s,"");
      WechatResultResposeVo vo = JSONObject.parseObject(httpResult.getBody(),WechatResultResposeVo.class);
      if(vo.getErrcode() != 0){
         flag = false;
      }
      return flag;
   }

}
