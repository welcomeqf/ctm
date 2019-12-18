package eqlee.ctm.finance.settlement.vilidata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import eqlee.ctm.finance.settlement.vilidata.entity.ResposeVo;
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

    private final String URL = "http://pay.wapi.eqlee.com";

    private final String localUrl = "http://localhost:8001";

    /**
     * ctm的路径
     */
    private final String address = "localhost";

    private final String port = "9097";

    private final String path = "ctm_msg";

    /**
     * 用户模块路径
     */
    private final String user_url = "http://localhost:9092/ctm_user";

    private final String user_url1 = "http://localhost:8001";

    @Autowired
    private TokenData tokenData;


    /**
     * 查询所有管理员的id集合
     * @return
     */
    public Object queryAllAdminInfo (){
        //路径
        String url = user_url + "/v1/app/user/queryAllCaiId";
        HttpResult httpResult = null;

        try {
            //获得token
            String userToken = tokenData.getMapToken();
            String token = "Bearer " + userToken;
            httpResult = apiService.get(url,token);
        }catch (Exception e) {
            e.printStackTrace();
        }

        ResposeVo result = JSONObject.parseObject(httpResult.getBody(), ResposeVo.class);
        if (result.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
        }

        return result.getData();
    }

}
