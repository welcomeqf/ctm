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
        if(code != null){
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                    + "appid=wxee961914859e2eab"
                    + "&secret=106342221c129aee134951ea037cd571"
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
