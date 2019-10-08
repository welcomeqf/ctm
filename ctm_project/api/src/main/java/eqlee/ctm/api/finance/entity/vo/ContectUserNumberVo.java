package eqlee.ctm.api.finance.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Data
public class ContectUserNumberVo {

    private Long Id;

    private Long NumberId;

    private String ContectUserName;

    private String ContectUserTel;

    private Integer AllNumber;

    /**
     * 未付款代收总价格
     */
    private Double AllPrice;

    private Integer BabyNumber;

    /**
     * 未付款代收老人人数
     */
    private Integer OldNumber;

    /**
     * 未付款代收小孩人数
     */
    private Integer ChildNumber;

    /**
     * 未付款代收成年人数
     */
    private Integer AdultNumber;

    private Long CreateUserId;

    private Long UpdateUserId;
}
