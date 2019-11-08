package eqlee.ctm.apply.orders.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/17
 * @Version 1.0
 */
@Data
public class OrderDetailedQuery {


    private Long OrderId;

    /**
     * 联系人
     */
    private String ContactName;

    /**
     * 联系电话
     */
    private String ContactTel;

    /**
     * 支付方式（0--现结 1--月结 2--面收）
     */
    private Integer PayType;

    /**
     * 人数
     */
    private Integer AllNumber;

    /**
     * 接送地
     */
    private String Place;

    /**
     * 订单号
     */
    private String OrderNo;
}