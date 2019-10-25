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
     * 总付款
     */
    private Double Price;
}
