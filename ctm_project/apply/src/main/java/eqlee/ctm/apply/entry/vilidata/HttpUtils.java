package eqlee.ctm.apply.entry.vilidata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yq.constanct.CodeType;
import com.yq.exception.ApplicationException;
import com.yq.httpclient.HttpClientUtils;
import com.yq.httpclient.HttpResult;
import eqlee.ctm.apply.entry.entity.bo.MsgUpdateAllBo;
import eqlee.ctm.apply.entry.entity.query.ExaRefundQuery;
import eqlee.ctm.apply.entry.entity.query.ResultRefundQuery;
import eqlee.ctm.apply.entry.entity.query.UserQuery;
import eqlee.ctm.apply.entry.entity.vo.*;
import eqlee.ctm.apply.message.entity.vo.MsgAddVo;
import eqlee.ctm.apply.message.entity.vo.MsgUpdateVo;
import eqlee.ctm.apply.message.entity.vo.MsgVo;
import eqlee.ctm.apply.sxpay.entity.PayInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.spring.web.json.Json;

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

    /**
     * 主api路径
     */
    private final String api_url = "http://localhost:9093/ctm_api";

    @Autowired
    private TokenData tokenData;

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
     * 支付宝退款
     * @param payOrderSerialNumber
     * @param refundReason
     * @param refundFee
     * @param token
     * @return
     */
    public ResultRefundQuery aliRefund (String payOrderSerialNumber, String refundReason, Double refundFee, String token){

        String url = URL + "/v1/Alipay/AliRefund?payOrderSerialNumber=" + payOrderSerialNumber +"&RefundReason=" +refundReason
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


    /**
     * 查询用户信息
     * @param id
     */
    public String queryUserInfo (Long id) throws Exception{
        //路径
        String url = user_url + "/v1/app/user/getUserById?id=" + id;
        HttpResult httpResult;

        //获得token
        String userToken;

        ResultResposeVo result;
        userToken = tokenData.getMapToken();
        String token = "Bearer " + userToken;
        httpResult = apiService.get(url,token);
        result = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);

        if (result.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
        }
        return JSON.toJSONString(result.getData());
    }


    /**
     * 查询所有管理员的id集合
     * @return
     */
    public Object queryAllAdminInfo (){
        //路径
        String url = user_url + "/v1/app/user/queryAllAdminId";
        HttpResult httpResult = null;

        try {
            //获得token
            String userToken = tokenData.getMapToken();
            String token = "Bearer " + userToken;
            httpResult = apiService.get(url,token);
        }catch (Exception e) {
            e.printStackTrace();
        }

        ResultResposeVo result = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (result.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
        }

        return result.getData();
    }



    /**
     * 增加支付信息
     * @return
     */
    public Object insertPayInfo (PayInfo info){
        //路径
        String url = api_url + "/v1/pay/insertPayInfo";
        HttpResult httpResult = null;

        String s = JSONObject.toJSONString(info);

        try {
            //获得token
            String userToken = tokenData.getMapToken();
            String token = "Bearer " + userToken;
            httpResult = apiService.post(url,s,token);
        }catch (Exception e) {
            e.printStackTrace();
        }

        ResultResposeVo result = JSONObject.parseObject(httpResult.getBody(),ResultResposeVo.class);
        if (result.getCode() != 0) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND,result.getMsg());
        }

        return result.getData();
    }

}
