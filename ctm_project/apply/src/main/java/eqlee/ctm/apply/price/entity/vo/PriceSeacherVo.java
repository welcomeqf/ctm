package eqlee.ctm.apply.price.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/10
 * @Version 1.0
 */
@Data
public class PriceSeacherVo {

    private String outDate;

    private String lineName;

    private Double adultPrice;

    private Double oldPrice;

    private Double babyPrice;

    private Double childPrice;
}
