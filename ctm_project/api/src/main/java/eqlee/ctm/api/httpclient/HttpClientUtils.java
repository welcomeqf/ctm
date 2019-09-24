package eqlee.ctm.api.httpclient;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author qf
 * @Date 2019/9/21
 * @Version 1.0
 */
@Component
public class HttpClientUtils {

    private CloseableHttpClient httpClient;

    public HttpClientUtils() {
        //打开浏览器
        httpClient = HttpClients.createDefault();
    }

    /**
     * GET请求
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public HttpResult doGet(String url, Map<String, Object> map) throws Exception {

        // 声明URIBuilder
        URIBuilder uriBuilder = new URIBuilder(url);

        // 判断参数map是否为非空
        if (map != null) {
            // 遍历参数
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                // 设置参数
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        // 2 创建httpGet对象，相当于设置url请求地址
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        // 3 使用HttpClient执行httpGet，相当于按回车，发起请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            HttpResult httpResult = new HttpResult();
            httpResult.setCode(404);
            httpResult.setBody("请求失败");
            return httpResult;
        }

        // 4 解析结果，封装返回对象httpResult，相当于显示相应的结果
        HttpResult httpResult = new HttpResult();
        // 解析数据封装HttpResult
        if (response.getEntity() != null) {
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setBody(EntityUtils.toString(response.getEntity(),"UTF-8"));

        } else {
            httpResult.setCode(response.getStatusLine().getStatusCode());
        }

        // 返回
        return httpResult;
    }


    /**
     * POST请求
     * @param url
     * @param jsonInfo
     * @return
     * @throws Exception
     */
    public HttpResult doPost(String url, String jsonInfo) throws Exception {
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
//        httpPost.setHeader("token","lpck");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(jsonInfo, charSet);
        httpPost.setEntity(entity);

        // 使用HttpClient发起请求，返回response
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            HttpResult httpResult = new HttpResult();
            httpResult.setCode(404);
            httpResult.setBody("请求失败");
            return httpResult;
        }

        // 解析response封装返回对象httpResult
        HttpResult httpResult = new HttpResult();
        // 解析数据封装HttpResult
        if (response.getEntity() != null) {
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setBody(EntityUtils.toString(response.getEntity(),charSet));

        } else {
            httpResult.setCode(response.getStatusLine().getStatusCode());
        }

        // 返回结果
        return httpResult;
    }


    /**
     * PUT请求
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public HttpResult doPut(String url, Map<String, Object> map) throws Exception {
        // 声明httpPost请求
        HttpPut httpPut = new HttpPut(url);

        // 判断map不为空
        if (map != null) {
            // 声明存放参数的List集合
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            // 遍历map，设置参数到list中
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }

            // 创建form表单对象
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");

            // 把表单对象设置到httpPost中
            httpPut.setEntity(formEntity);
        }

        // 使用HttpClient发起请求，返回response
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPut);
        } catch (IOException e) {
            HttpResult httpResult = new HttpResult();
            httpResult.setCode(404);
            httpResult.setBody("请求失败");
            return httpResult;
        }

        // 解析response封装返回对象httpResult
        HttpResult httpResult = new HttpResult();
        // 解析数据封装HttpResult
        if (response.getEntity() != null) {
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setBody(EntityUtils.toString(response.getEntity(),"UTF-8"));

        } else {
            httpResult.setCode(response.getStatusLine().getStatusCode());
        }

        // 返回结果
        return httpResult;
    }

    /**
     * Delete请求
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public HttpResult doDelete(String url, Map<String, Object> map) throws Exception {

        // 声明URIBuilder
        URIBuilder uriBuilder = new URIBuilder(url);

        // 判断参数map是否为非空
        if (map != null) {
            // 遍历参数
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                // 设置参数
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        // 2 创建httpGet对象，相当于设置url请求地址
        HttpDelete httpDelete = new HttpDelete(uriBuilder.build());

        // 3 使用HttpClient执行httpGet，相当于按回车，发起请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpDelete);
        } catch (IOException e) {
            HttpResult httpResult = new HttpResult();
            httpResult.setCode(404);
            httpResult.setBody("请求失败");
            return httpResult;

        }

        // 4 解析结果，封装返回对象httpResult，相当于显示相应的结果
        HttpResult httpResult = new HttpResult();
        // 解析数据封装HttpResult
        if (response.getEntity() != null) {
            httpResult.setCode(response.getStatusLine().getStatusCode());
            httpResult.setBody(EntityUtils.toString(response.getEntity(),"UTF-8"));

        } else {
            httpResult.setCode(response.getStatusLine().getStatusCode());
        }
        // 返回
        return httpResult;
    }

}
