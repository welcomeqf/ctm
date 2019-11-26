package eqlee.ctm.resource.company.vilidata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import eqlee.ctm.resource.company.entity.vo.ResultResposeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author qf
 * @Date 2019/11/4
 * @Version 1.0
 */
@Component
public class HttpUtils {


    @Autowired
    private HttpClientUtils apiService;

    /**
     * 用户模块路径
     */
    private final String user_url;

    @Autowired
    private TokenData tokenData;

    public HttpUtils() {
        user_url = "http://localhost:9092/ctm_user";
    }


    /**
     * 删除该公司下所以用户和角色
     * @param id
     */
    public void deleteUserAndRole (Long id) throws Exception{
        //路径
        String url = user_url + "/v1/app/user/deleteAllUserRole?id=" + id;
        HttpResult httpResult;

        //获得token
        String userToken;

        ResultResposeVo result;
        userToken = tokenData.getUserToken();
        String token = "Bearer " + userToken;
        httpResult = apiService.get(url,token);
        result = JSONObject.parseObject(httpResult.getBody(), ResultResposeVo.class);

        if (result.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
        }
    }



}
