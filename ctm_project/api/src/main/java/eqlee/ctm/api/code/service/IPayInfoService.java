package eqlee.ctm.api.code.service;

import eqlee.ctm.api.code.entity.PayInfo;
import eqlee.ctm.api.code.entity.query.PayInfoQuery;

/**
 * @Author qf
 * @Date 2019/10/30
 * @Version 1.0
 */
public interface IPayInfoService {

    /**
     * 增加支付信息（code以及openId）
     * @param payInfo
     * @return
     */
    Integer insertPayInfo (PayInfo payInfo);

    /**
     * 查询该用户的支付信息
     * @param userId
     * @return
     */
    PayInfoQuery queryPayInfo (Long userId);

    /**
     * 查询该用户是否有该记录
     * @param userId
     * @return
     */
    Boolean queryPay (Long userId);

    /**
     * 删除该用户的支付授权信息
     * @param id
     * @return
     */
    Integer deletePayInfo (Long id);
}
