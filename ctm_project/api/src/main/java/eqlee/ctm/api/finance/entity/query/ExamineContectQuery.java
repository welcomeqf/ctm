package eqlee.ctm.api.finance.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/29
 * @Version 1.0
 */
@Data
public class ExamineContectQuery {

    /**
     * 联系人名字
     */
    private String ContectUserName;

    /**
     * 联系电话
     */
    private String ContectUserTel;

    /**
     * 面收未付款总人数
     */
    private Integer AllNumber;

    /**
     * 面收未付款总价格
     */
    private Double AllPrice;

    /**
     * 未付款代收幼儿人数
     */
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
}
