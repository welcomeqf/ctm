package eqlee.ctm.apply.sxpay.token.vilidata;


import com.alibaba.fastjson.JSONObject;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import eqlee.ctm.apply.entry.vilidata.ResultTokenVo;
import eqlee.ctm.apply.sxpay.token.entity.PayToken;
import eqlee.ctm.apply.sxpay.token.entity.PayVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付token工具类
 * @author qf
 * @date 2019/11/26
 * @vesion 1.0
 **/
@Slf4j
@Component
public class PayTokenUtils {


   @Autowired
   private HttpClientUtils apiService;

   private final String URL = "http://pay.wapi.eqlee.com";

   private final String AccessKey = "157222853200009";

   private final String AccessKeySecret = "74c172912d9b4631bc72e567c27b2ece";

   /**
    * 创建容器
    */
   Map<String,Object> map = new ConcurrentHashMap<>();

   /**
    * 请求token
    * @return
    * @throws Exception
    */
   public String getPayToken () throws Exception{

      String url = URL +"/v1/Token/Token";

      PayToken payToken = new PayToken();
      payToken.setAccessKey(AccessKey);
      payToken.setAccessKeySecret(AccessKeySecret);

      String s = JSONObject.toJSONString(payToken);
      HttpResult httpResult = apiService.doPost(url, s);

      PayVo vo = JSONObject.parseObject(httpResult.getBody(), PayVo.class);

      ResultTokenVo result = vo.getResult();

      map.put("pay_token",result.getToken());
      map.put("pay_ext",result.getExpires());
      return result.getToken();
   }


   public String getMapToken() throws Exception {

      Long ext = (Long) map.get("pay_ext");

      //如果过期时间过期或存的token为空则重新请求，若未过期则用存的token
      if (ext == null) {
         return getPayToken();
      }

      Long now = Instant.now().getEpochSecond();

      if (now < ext) {
         //未过期
         String result = (String) map.get("pay_token");
         return result;
      }

      return getPayToken();
   }

}
