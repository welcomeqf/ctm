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

    private Double AdultPrice;

    private Double OldPrice;

    private Double BabyPrice;

    private Double ChildPrice;
}
