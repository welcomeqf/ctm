package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/29
 * @Version 1.0
 */
@Data
public class ApplyPayResultQuery {

    /**
     * 订单号
     */
    private String applyNo;

    /**
     * 商品描述
     */
    private String productName;

    /**
     * true说明有openId,只需要传一个code=null
     * false说明没有openId,需要重新授权
     */
    private Boolean auto;
}
