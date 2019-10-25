package eqlee.ctm.finance.settlement.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/16
 * @Version 1.0
 */
@Data
public class GuestResultQuery {

    /**
     * 出行日期
     */
    private String OutDate;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 人数
     */
    private Integer TrueAllNumber;

    /**
     * 实际收入
     */
    private Double SubstitutionPrice;

    /**
     * 支出
     */
    private Double AllOutPrice;

    /**
     * 结算价
     */
    private Double ResultPrice;

    /**
     * 审核状态
     */
    private Integer Status;
}
