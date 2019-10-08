package eqlee.ctm.finance.financeexpress.entity;

import lombok.Data;

/**
 * @Author Claire
 * @Date 2019/10/8 0008
 * @Version 1.0
 */
@Data
public class AllCountVo {
    private Integer month;

    private String accountName;

    private String companyName;
    //应收金额
    private Double settlementPrice;

    //实收金额
    private Double substitutionPrice;

    //结算金额
    private Double count;
}
