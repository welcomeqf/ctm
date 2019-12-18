package eqlee.ctm.api.img.vilidata;

import com.alibaba.fastjson.JSONObject;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import eqlee.ctm.api.img.entity.query.ImgQuery;
import eqlee.ctm.api.img.entity.vo.ImgTokenVo;
import eqlee.ctm.api.img.entity.vo.ImgVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qf
 * @date 2019/11/27
 * @vesion 1.0
 **/
@Slf4j
@Component
public class ImgTokenData {


   @Autowired
   private HttpClientUtils apiService;

   private final String accessKey = "652913462813390001";

   private final String accessKeySecret = "1b595227845a2337af24f52ca1ccbdxo1";

   private final String img_Url = "http://img.eqlee.com";

   /**
    * 创建图片token容器
    */
   Map<String,Object> map = new ConcurrentHashMap<>();

   public String getImgToken () throws Exception {

      String url = img_Url +"/v1/AccessToken";

      ImgTokenVo vo = new ImgTokenVo();

      vo.setAccessKey(accessKey);
      vo.setAccessKeySecret(accessKeySecret);

      String s = JSONObject.toJSONString(vo);
      HttpResult httpResult = apiService.doPost(url, s);

      ImgVo result = JSONObject.parseObject(httpResult.getBody(), ImgVo.class);

      ImgQuery data = result.getData();

      map.put("img_token",data.getToken());
      map.put("img_exp",data.getExpiresAt());
      return data.getToken();
   }


   public String getImgMapToken () throws Exception {
      //先去容器取出token判断是否过期
      Long exp = (Long) map.get("img_exp");

      //如果值为空
      if (exp == null) {
         return getImgToken();
      }

      Date now = new Date();
      Date erpDate = new Date(exp);
      //判断token时间是否过期
      if (now.after(erpDate)) {
         //token已过期
         return getImgToken();
      }

      String token = (String) map.get("img_token");
      return token;
   }

}
