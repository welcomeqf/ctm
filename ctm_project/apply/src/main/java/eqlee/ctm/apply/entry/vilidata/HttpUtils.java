package eqlee.ctm.apply.entry.vilidata;

import com.alibaba.fastjson.JSONObject;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import eqlee.ctm.apply.entry.entity.bo.MsgUpdateAllBo;
import eqlee.ctm.apply.entry.entity.query.ExaRefundQuery;
import eqlee.ctm.apply.entry.entity.query.ResultRefundQuery;
import eqlee.ctm.apply.entry.entity.vo.*;
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

    /**
     * ctm的路径
     */
    private final String address = "localhost";

    private final String port = "9097";

    private final String path = "ctm_msg";

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


    /**
     * 增加消息记录
     * @param vo
     */
    public void addMsg (MsgVo vo) {

    //路径
     String url = "http://" + address + ":" + port + "/" + path + "/v1/msg/insertMsg";

        String s = JSONObject.toJSONString(vo);
        HttpResult httpResult = null;

        try {
            httpResult = apiService.doPost(url, s);
        }catch (Exception e) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "调用出错");
        }

        ResultResposeVo result = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (result.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
        }
    }



    /**
     * 批量增加同一角色下所有用户的消息记录
     * @param msgAddVo
     */
    public void addAllMsg (MsgAddVo msgAddVo) {

        //路径
        String url = "http://" + address + ":" + port + "/" + path + "/v1/msg/addAllMsg";

        String s = JSONObject.toJSONString(msgAddVo);
        HttpResult httpResult;

        try {
            httpResult = apiService.doPost(url, s);
        }catch (Exception e) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "调用出错");
        }

        ResultResposeVo result = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (result.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
        }
    }


    /**
     * 修改查看状态
     * @param id
     */
    public void updateMsg (Integer id) throws Exception{
        //路径
        String url = "http://" + address + ":" + port + "/" + path + "/v1/msg/updateMsg";

        MsgUpdateVo vo = new MsgUpdateVo();
        vo.setId(id);

        String s = JSONObject.toJSONString(vo);
        HttpResult httpResult = apiService.doPost(url, s);

        ResultResposeVo result = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (result.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
        }
    }


    /**
     * 查询未读消息条数
     * @param toId
     * @param msgType
     */
    public Object queryCount (Long toId, Integer msgType, String msg){
        //路径
        String url = "http://" + address + ":" + port + "/" + path + "/v1/msg/queryCount?toId=" + toId +
              "&msgType=" +msgType + "&msg=" +msg;

        HttpResult httpResult;
        try {
            httpResult = apiService.doGet(url,null);
        }catch (Exception e) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "调用出错");
        }

        ResultResposeVo result = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (result.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
        }

        return result.getData();
    }


    /**
     * 查询未读消息
     * @param toId
     * @param msgType
     */
    public Object listMsgInfo (Long toId, Integer msgType) throws Exception{
        //路径
        String url = "http://" + address + ":" + port + "/" + path + "/v1/msg/listMsgInfo?toId=" + toId + "&msgType=" +msgType;

        HttpResult httpResult = apiService.doGet(url,null);

        ResultResposeVo result = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (result.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
        }

        return result.getData();
    }


    /**
     * 修改当前用户所有未读消息状态
     * @param id
     */
    public void updateUserAllMsg (Long id) {
        //路径
        String url = "http://" + address + ":" + port + "/" + path + "/v1/msg/updateUserAllMsg";

        MsgUpdateAllBo vo = new MsgUpdateAllBo();
        vo.setToId(id);

        String s = JSONObject.toJSONString(vo);
        HttpResult httpResult;

        try {
            httpResult = apiService.doPost(url, s);
        }catch (Exception e) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "调用出错");
        }

        ResultResposeVo result = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (result.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
        }
    }

}
