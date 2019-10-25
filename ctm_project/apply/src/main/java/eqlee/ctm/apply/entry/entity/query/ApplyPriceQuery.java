package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/15
 * @Version 1.0
 */
@Data
public class ApplyPriceQuery {

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

    /**
     * 成年人数
     */
    private Integer adultNumber;

    /**
     * 幼儿人数
     */
    private Integer babyNumber;

    /**
     * 老人人数
     */
    private Integer oldNumber;

    /**
     * 小孩人数
     */
    private Integer childNumber;

}
