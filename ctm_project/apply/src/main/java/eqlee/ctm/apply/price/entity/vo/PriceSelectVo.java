package eqlee.ctm.apply.price.entity.vo;

import lombok.Data;

/**
 * @Author Claire
 * @Date 2019/9/29 0029
 * @Version 1.0
 */
@Data
public class PriceSelectVo {

    private Long id;

    private String outDate;

    private String lineName;

    private Double adultPrice;

    private Double oldPrice;

    private Double babyPrice;

    private Double childPrice;
}
