package eqlee.ctm.finance.financeexpress.entity;

import lombok.Data;

/**
 * @Author Claire
 * @Date 2019/10/8 0008
 * @Version 1.0
 */
@Data
public class CountVo {
    private Integer month;

    private String accountName;

    private String companyName;
    //月结金额
    private Double count;

    private Boolean isPayment;
}
