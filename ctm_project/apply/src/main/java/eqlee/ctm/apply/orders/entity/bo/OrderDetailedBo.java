package eqlee.ctm.apply.orders.entity.bo;

import lombok.Data;


/**
 * @Author qf
 * @Date 2019/11/1
 * @Version 1.0
 */
@Data
public class OrderDetailedBo {

    private Long Id;


    private Long OrderId;

    /**
     * 联系人
     */
    private String ContactName;

    /**
     * 联系电话
     */
    private String ContactTel;

    /**
     * 同行代表人姓名
     */
    private String AccountName;

    /**
     * 同行公司名
     */
    private String CompanyName;


    /**
     * 接送地
     */
    private String Place;

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
     * 总人数
     */
    private Integer AllNumber;

    /**
     * 支付类型(0--现结  1--月结  2--代付)
     */
    private Integer PayType;

    /**
     * 价格
     */
    private Double Price;


    /**
     * 创建人
     */
    private Long CreateUserId;

    /**
     * 面收金额
     */
    private Double MsPrice;

}
