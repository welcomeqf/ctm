package eqlee.ctm.apply.guider.entity.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author Claire
 * @Date 2019/9/23 0023
 * @Version 1.0
 */
@Data
public class GuiderVo {

    private Long Id;

    private String LineName;

    private String OutDate;

    private Double AdultPrice;

    private Double OldPrice;

    private Double BabyPrice;

    private Double ChildPrice;

    /**
     * 报名人数
     */
    private Integer AppliedNumber;

    /**
     * 已选人数
     *
     */
    private Integer ChooisedNumber;

    /**
     * 待选人数
     */
    private Integer WaitedPeople;
}

