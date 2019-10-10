package eqlee.ctm.apply.price.entity.vo;

import lombok.Data;


/**
 * @Author qf
 * @Date 2019/9/18
 * @Version 1.0
 */
@Data
public class PriceVo {

    private String startTime;

    private String endTime;

    private String lineName;

    private Double adultPrice;

    private Double oldPrice;

    private Double babyPrice;

    private Double childPrice;

    private String remark;
}
