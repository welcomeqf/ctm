package eqlee.ctm.api.pay.service;

import eqlee.ctm.api.pay.entity.Pay;
import eqlee.ctm.api.pay.entity.PayResult;
import eqlee.ctm.api.pay.entity.query.PayResultQuery;
import eqlee.ctm.api.pay.entity.query.ResultQuery;

/**
 * @Author qf
 * @Date 2019/10/28
 * @Version 1.0
 */
public interface IPayService {

    /**
     * 增加支付信息
     * @param pay
     * @return
     */
    PayResult insertPayInfo (Pay pay);

    /**
     * 修改报名表支付状态
     * @param applyNo
     * @param payInfo
     */
    void updateApplyPayStatus (String applyNo, Integer payInfo);

    /**
     * 支付失败删除该报名记录并删除审核记录
     * @param applyNo
     * @return
     */
    PayResult deleteApplyInfo (String applyNo);

    /**
     * 查询支付结果
     * @param applyNo
     * @return
     */
    ResultQuery queryPayResult (String applyNo);
}
