package eqlee.ctm.apply.price.entity.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author qf
 * @Date 2019/10/24
 * @Version 1.0
 */
@Data
public class PriceAllUpdateVo {

    private LocalDate outDate;

    private Long lineId;

    private Double adultPrice;

    private Double oldPrice;

    private Double babyPrice;

    private Double childPrice;

    private Double marketAdultPrice;

    private Double marketOldPrice;

    private Double marketBabyPrice;

    private Double marketChildPrice;
}
