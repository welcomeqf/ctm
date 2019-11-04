package eqlee.ctm.apply.entry.vilidata;

import com.alibaba.fastjson.JSONObject;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import eqlee.ctm.apply.entry.entity.query.ExaRefundQuery;
import eqlee.ctm.apply.entry.entity.query.ResultRefundQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

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

    private final String AccessKey = "157222853200009";

    private final String AccessKeySecret = "74c172912d9b4631bc72e567c27b2ece";

    /**
     * 获取token
     * @return
     * @throws Exception
     */
    public String getPayToken () throws Exception{

        String url = URL +"/v1/Token/Token";

        Token payToken = new Token();
        payToken.setAccessKey(AccessKey);
        payToken.setAccessKeySecret(AccessKeySecret);

        String s = JSONObject.toJSONString(payToken);
        HttpResult httpResult = apiService.doPost(url, s);

        TokenVo vo = JSONObject.parseObject(httpResult.getBody(), TokenVo.class);

        ResultTokenVo result = vo.getResult();
        return result.getToken();
    }

    /**
     * 微信退款
     * @param payOrderSerialNumber
     * @param totalFee
     * @param refundFee
     * @param token
     * @return
     */
    public ResultRefundQuery refund (String payOrderSerialNumber, Double totalFee, Double refundFee, String token){

        String url = URL + "/v1/WeChatPay/WechatRefund?payOrderSerialNumber=" + payOrderSerialNumber +"&totalFee=" +totalFee
                + "&refundFee=" +refundFee;

        HttpResult httpResult = apiService.get(url, token);

        if (httpResult == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"无返回数据");
        }

        ExaRefundQuery query = JSONObject.parseObject(httpResult.getBody(), ExaRefundQuery.class);
        return query.getResult();
    }
}
