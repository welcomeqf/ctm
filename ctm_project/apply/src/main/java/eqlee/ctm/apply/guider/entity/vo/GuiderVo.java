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

    private Long id;

    private String lineName;

    private String outDate;

    /**
     * 已选人数
     */
    private Integer slcCnt;

    /**
     * 最大人数
     *
     */
    private Integer allNumber;


}

