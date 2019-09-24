package eqlee.ctm.api.vilidate;

import com.yq.utils.Base64Util;
import com.yq.utils.CryptoUtils;
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
    public static ErrorResult getError () {
        ErrorResult result = new ErrorResult();
        result.setMsg("调用Api出错,请重试");
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
}
