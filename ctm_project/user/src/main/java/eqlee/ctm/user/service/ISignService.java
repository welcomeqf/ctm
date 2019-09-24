package eqlee.ctm.user.service;

import eqlee.ctm.user.entity.Sign;

/**
 * 签名认证的service
 * @Author qf
 * @Date 2019/9/23
 * @Version 1.0
 */
public interface ISignService {

    /**
     * 增加一条签名记录
     * @param sign
     */
    void insertSign(Sign sign);

    /**
     * 通过AppId查询该条记录
     * @param AppId
     * @return
     */
    Sign queryOne(String AppId);

}
