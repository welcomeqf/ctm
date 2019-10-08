package eqlee.ctm.finance.financeexpress.entity;

import lombok.Data;

/**
 * @Author Claire
 * @Date 2019/10/8 0008
 * @Version 1.0
 */
@Data
public class FinanceExpressCountVo {
    private Integer month;

    private String guideName;

    //代收金额
    private Double substitutionPrice;
}
