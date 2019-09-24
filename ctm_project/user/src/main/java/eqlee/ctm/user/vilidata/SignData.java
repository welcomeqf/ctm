package eqlee.ctm.user.vilidata;


import com.yq.utils.SignUtils;
import eqlee.ctm.user.entity.vo.ResultSignVo;

import java.security.KeyPair;
import java.security.Signature;

/**
 * @Author qf
 * @Date 2019/9/22
 * @Version 1.0
 */
public class SignData {

    public static Boolean getResult(String AppId, String user) throws Exception{
        //私钥
        KeyPair keypair = SignUtils.getKeypair(AppId);
        //签名算法
        Signature mySig = Signature.getInstance("MD5WithRSA");
        //公钥
        byte[] bytes = SignUtils.getpublicByKeypair(mySig, keypair, user.getBytes());
        return SignUtils.decryptBypublic(mySig, keypair, user, bytes);
    }


    /**
     * 返回签名结果
     * @param AppId
     * @param user
     * @return
     * @throws Exception
     */
    public static ResultSignVo getSign(String AppId, String user) throws Exception{
        KeyPair keypair = SignUtils.getKeypair(AppId);
        //签名算法
        Signature mySig = Signature.getInstance("MD5WithRSA");
        //公钥
        byte[] bytes = SignUtils.getpublicByKeypair(mySig, keypair, user.getBytes());
        ResultSignVo signVo = new ResultSignVo();
        signVo.setMySig(mySig.toString());
        signVo.setBytes(bytes.toString());
        signVo.setKeypair(keypair.toString());
        return signVo;
    }
}
