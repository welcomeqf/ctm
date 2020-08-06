package eqlee.ctm.api.pay.vilidata;

import com.alibaba.fastjson.JSONObject;

import java.net.URL;

/**
 * @Author qf
 * @Date 2019/10/28
 * @Version 1.0
 */
public class PayData {

    public static String getOpenId (String code) {

//        String appId = "wxee961914859e2eab";
        String appId = "wxd1a8a6b0eed550f4";
//        String secret = "106342221c129aee134951ea037cd571";
        String secret = "597f021bdcd903d03b9ef73035cbaa42";

        //test微信公众号信息
        String appId1 = "wx12c72482508c5c52";
        String secret1 = "6daa8b189c2bc17f2da90ac3fc62c9d2";


        if(code != null){
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                    + "appid=" + appId
                    + "&secret=" + secret
                    + "&code=" + code + "&grant_type=authorization_code";

            String returnData = getReturnData(url);

            JSONObject jsonObject;
            try {
                jsonObject = JSONObject.parseObject(returnData);
                String openid = jsonObject.getString("openid");
                return openid;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return"";
    }


    public static String getReturnData(String urlString) {
        String res = "";
        try {
            URL url = new URL(urlString);
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
}
