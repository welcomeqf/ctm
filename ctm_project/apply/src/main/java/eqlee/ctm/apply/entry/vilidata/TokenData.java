package eqlee.ctm.apply.entry.vilidata;

import com.alibaba.fastjson.JSONObject;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import eqlee.ctm.apply.entry.vilidata.entity.AuthLoginParamVo;
import eqlee.ctm.apply.entry.vilidata.entity.AuthResult;
import eqlee.ctm.apply.entry.vilidata.entity.TokenWithVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qf
 * @date 2019/11/15
 * @vesion 1.0
 **/
@Slf4j
@Component
public class TokenData {

   /**
    * 支付
    */
   private final String payUrl = "http://pay.wapi.eqlee.com";

   private final String AccessKey = "157222853200009";

   private final String AccessKeySecret = "74c172912d9b4631bc72e567c27b2ece";

   @Autowired
   private HttpClientUtils apiService;

   /**
    * Ctm
    */
   private final String ctmUrl = "http://localhost:9092/ctm_user";

   private final String localUrl = "http://localhost:8001";

   private final String accessKey = "20191115111837644858794286944256";

   private final String accessKeySecret = "78421a143252433783afd3af8ba1d2f4";

   /**
    * 创建一个容器
    */
   Map<String,Object> map = new ConcurrentHashMap<>();

   /**
    * 获取token
    * @return
    * @throws Exception
    */
   public AuthResult getUserToken () throws Exception{

      String url = ctmUrl +"/v1/auth/loginAuth";

      AuthLoginParamVo payToken = new AuthLoginParamVo();
      payToken.setAccessKey(accessKey);
      payToken.setAccessKeySecret(accessKeySecret);

      String s = JSONObject.toJSONString(payToken);
      HttpResult httpResult = apiService.doPost(url,s);

      TokenWithVo vo = JSONObject.parseObject(httpResult.getBody(), TokenWithVo.class);

      AuthResult data = vo.getData();

      map.put("token",vo.getData().getToken());
      map.put("exp",vo.getData().getExpTime());

      return data;
   }


   /**
    * 请求支付的token
    * @return
    * @throws Exception
    */
   public String getPayToken () throws Exception{

      String url = payUrl +"/v1/Token/Token";

      Token payToken = new Token();
      payToken.setAccessKey(AccessKey);
      payToken.setAccessKeySecret(AccessKeySecret);

      String s = JSONObject.toJSONString(payToken);
      HttpResult httpResult = apiService.doPost(url, s);

      TokenVo vo = JSONObject.parseObject(httpResult.getBody(), TokenVo.class);

      ResultTokenVo result = vo.getResult();

      map.put("payToken",result.getToken());
      map.put("payExp",result.getExpires());
      return result.getToken();
   }


   /**
    * 取ctm的token
    * @return
    * @throws Exception
    */
   public String getMapToken () throws Exception {

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
    * 取支付token
    * @return
    * @throws Exception
    */
   public String getPayMapToken() throws Exception {

      Long ext = (Long) map.get("payExp");

      //如果过期时间过期或存的token为空则重新请求，若未过期则用存的token
      if (ext == null) {
         return getPayToken();
      }

      Long now = Instant.now().getEpochSecond();

      if (now < ext) {
         //未过期
         String result = (String) map.get("payToken");
         return result;
      }

      return getPayToken();
   }
}
