package eqlee.ctm.api.pay.entity.query;

import lombok.Data;



/**
 * @Author qf
 * @Date 2019/10/29
 * @Version 1.0
 */
@Data
public class PayResultQuery {

    /**
     * id
     */
    private Long id;

    /**
     * 报名单号
     */
    private String applyNo;

    /**
     * 第三方流水号
     */
    private String thirdPartyNumber;

    /**
     * 支付状态（0--未支付  1--支付成功  2--支付失败）
     */
    private Integer payStatus;

    /**
     * 支付时间
     */
    private String payDate;

}
