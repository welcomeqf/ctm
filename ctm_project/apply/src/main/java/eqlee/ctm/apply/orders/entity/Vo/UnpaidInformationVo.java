package eqlee.ctm.apply.orders.entity.Vo;

import lombok.Data;

/**
 * @Author Claire
 * @Date 2019/9/28 0028
 * @Version 1.0
 */
@Data
public class UnpaidInformationVo {
    /**
     * 联系人
     */
    private String ContactName;

    /**
     * 联系电话
     */
    private String ContactTel;

    /**
     * 总人数
     */
    private Integer AllNumber;

    /**
     * 成人人数
     */
    private Integer AdultNumber;

    /**
     * 幼儿人数
     */
    private Integer BabyNumber;

    /**
     * 老人人数
     */
    private Integer OldNumber;

    /**
     * 小孩人数
     */
    private Integer ChildNumber;

    /**
     * 总付款
     */
    private Double SumPaid;
}
