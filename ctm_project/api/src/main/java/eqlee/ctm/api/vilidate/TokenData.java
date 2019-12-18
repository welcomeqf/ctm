package eqlee.ctm.api.vilidate;

import com.alibaba.fastjson.JSONObject;
import com.yq.constanct.CodeType;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import eqlee.ctm.api.entity.bo.AuthTokenBo;
import eqlee.ctm.api.entity.vo.AuthLoginParamVo;
import eqlee.ctm.api.pay.entity.vo.TokenVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

   @Autowired
   private HttpClientUtils apiService;

   private final String URL = "http://localhost:9092/ctm_user";

   private final String URL1 = "http://localhost:8001";

   /**
    * Ctm
    */
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
   public AuthTokenBo getUserToken () throws Exception{

      String url = URL +"/v1/auth/loginAuth";

      AuthLoginParamVo payToken = new AuthLoginParamVo();
      payToken.setAccessKey(accessKey);
      payToken.setAccessKeySecret(accessKeySecret);

      String s = JSONObject.toJSONString(payToken);
      HttpResult httpResult = apiService.doPost(url,s);

      TokenVo vo = JSONObject.parseObject(httpResult.getBody(), TokenVo.class);

      map.put("ctm_token",vo.getData().getToken());
      map.put("ctm_exp",vo.getData().getExpTime());

      return vo.getData();
   }


   /**
    * 取token
    * @return
    * @throws Exception
    */
   public String getMapToken () throws Exception {

      //先去容器取出token判断是否过期
      Long exp = (Long) map.get("ctm_exp");

      //如果值为空
      if (exp == null) {
         AuthTokenBo userToken = getUserToken();
         return userToken.getToken();
      }

      Date date = new Date();
      Date erpDate = new Date(exp);
      //判断token时间是否过期
      if (erpDate.before(date)) {
         //token已过期
         AuthTokenBo userToken = getUserToken();
         return userToken.getToken();
      }

      String token = (String) map.get("ctm_token");
      return token;
   }

}
