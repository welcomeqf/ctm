package eqlee.ctm.apply.orders.entity.bo;


import lombok.Data;


/**
 * @Author qf
 * @Date 2019/10/11
 * @Version 1.0
 */
@Data
public class ChoisedBo {

    /**
     *订单详情id
     */
    private Long orderId;

    private String outDate;

    private String lineName;

    /**
     * 1--同意换人  2--不同意换人
     */
    private Integer type;

}
