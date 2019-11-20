package eqlee.ctm.api.vilidate;

import com.alibaba.fastjson.JSONObject;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import eqlee.ctm.api.entity.query.AuthResult;
import eqlee.ctm.api.entity.vo.AuthLoginParamVo;
import eqlee.ctm.api.pay.entity.vo.ResultTokenVo;
import eqlee.ctm.api.pay.entity.vo.TokenVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

   /**
    * Ctm
    */
   private final String accessKey = "20191115111837644858794286944256";

   private final String accessKeySecret = "78421a143252433783afd3af8ba1d2f4";

   /**
    * 获取token
    * @return
    * @throws Exception
    */
   public String getUserToken () throws Exception{

      String url = URL +"/v1/auth/loginAuth";

      AuthLoginParamVo payToken = new AuthLoginParamVo();
      payToken.setAccessKey(accessKey);
      payToken.setAccessKeySecret(accessKeySecret);

      String s = JSONObject.toJSONString(payToken);
      HttpResult httpResult = apiService.doPost(url,s);

      TokenVo vo = JSONObject.parseObject(httpResult.getBody(), TokenVo.class);

      AuthResult data = vo.getData();
      return data.getToken();
   }
}
