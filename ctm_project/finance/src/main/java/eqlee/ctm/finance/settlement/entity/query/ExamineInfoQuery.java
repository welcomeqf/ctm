package eqlee.ctm.finance.settlement.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/20
 * @Version 1.0
 */
@Data
public class ExamineInfoQuery {

    private Long guideId;

    /**
     * 导游名字
     */
    private String guideName;

    /**
     * 出发日期
     */
    private String outDate;

    /**
     * 线路名
     */
    private String lineName;

    /**
     * 结算金额
     */
    private Double finallyPrice;

    /**
     * 操作时间
     */
    private String updateDate;

    /**
     * 审核结果（0--审核中 1--审核通过 2--审核失败）
     */
    private Integer status;
}
