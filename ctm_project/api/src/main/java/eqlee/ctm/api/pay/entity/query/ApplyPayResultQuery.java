package eqlee.ctm.api.pay.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/29
 * @Version 1.0
 */
@Data
public class ApplyPayResultQuery {

    /**
     * 支付金额
     */
    private Double money;

    /**
     * 报名支付状态
     */
    private Boolean applyStatus;

    /*
    * 微信公众号推送使用字段
    */
    private String City;
    private String ContactName;
}
