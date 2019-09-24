package eqlee.ctm.user.vilidata;

import com.yq.utils.Base64Util;
import com.yq.utils.CryptoUtils;

/**
 * @Author qf
 * @Date 2019/9/21
 * @Version 1.0
 */
public class DataUtils {

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
