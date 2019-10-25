package eqlee.ctm.apply.orders.entity.Vo;

import lombok.Data;

/**
 * @Author Claire
 * @Date 2019/9/28 0028
 * @Version 1.0
 */
@Data
public class InComeRemerberVo {

    private Long Id;

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
     * 联系人
     */
    private String ContactName;

    /**
     * (0--未付款  1--已付款)
     */
    private Boolean IsPayment;

    /**
     * 总人数
     */
    private Integer AllNumber;

    /**
     * 支付类型(0--现结  1--月结  2--代付)
     */
    private Integer PayType;

    /**
     * 联系电话
     */
    private String ContactTel;


    private String CarNumber;

    /**
     * false--本公司车辆   true--外部车辆
     */
    private Boolean CarType;

}
