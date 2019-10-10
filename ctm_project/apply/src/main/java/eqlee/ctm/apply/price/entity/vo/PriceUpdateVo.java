package eqlee.ctm.apply.price.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/9
 * @Version 1.0
 */
@Data
public class PriceUpdateVo {

    private Long id;

    private Double adultPrice;

    private Double oldPrice;

    private Double babyPrice;

    private Double childPrice;
}
