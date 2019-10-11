package eqlee.ctm.api.vilidate;


import com.alibaba.fastjson.JSONObject;
import com.yq.utils.Base64Util;
import com.yq.utils.CryptoUtils;
import eqlee.ctm.api.entity.vo.ResoultJosnVo;
import eqlee.ctm.api.httpclient.ErrorResult;

/**
 * @Author qf
 * @Date 2019/9/21
 * @Version 1.0
 */
public class DataUtils {

    /**
     * 调用Api返回错误结果
     * @return
     */
    public static ErrorResult getError (String msg) {
        ErrorResult result = new ErrorResult();
        result.setMsg(msg);
        result.setCode(400);
        return result;
    }

    /**
     * 对AppId加码
     * @param AppId
     * @return
     */
    public static String getEncodeing(String AppId) {
        String s = Base64Util.encodeString(AppId);
        String encrypt = CryptoUtils.encrypt(s);
        return encrypt;
    }


    /**
     * 对AppId解码
     * @param encodeString
     * @return
     */
    public static String getDcodeing(String encodeString) {
        String decode = CryptoUtils.decrypt(encodeString);
        String decodeString = Base64Util.decodeString(decode);
        return decodeString;
    }

    /**
     * 返回具体错误
     * @param result
     * @return
     */
    public static String getMsg (String result) {
        ResoultJosnVo parse = JSONObject.parseObject(result, ResoultJosnVo.class);
        return parse.getMsg();
    }
}
