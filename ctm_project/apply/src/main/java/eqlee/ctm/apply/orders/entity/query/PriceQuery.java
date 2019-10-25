package eqlee.ctm.apply.orders.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/19
 * @Version 1.0
 */
@Data
public class PriceQuery {

    /**
     * 成年价格
     */
    private Double adultPrice;

    /**
     * 老人价格
     */
    private Double oldPrice;

    /**
     * 幼儿价格
     */
    private Double babyPrice;

    /**
     * 小孩价格
     */
    private Double childPrice;
}
