package eqlee.ctm.api.vilidate;


import com.yq.httpclient.ErrorResult;
import com.yq.utils.Base64Util;
import com.yq.utils.CryptoUtils;

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
        result.setCode(401);
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
        String MSG = "请求失败";
        if (MSG.equals(result)) {
            return "服务正在启动，请稍候";
        }



        if (result == null) {
            return "未知错误";
        }
        return result;
    }
}
