package eqlee.ctm.apply.orders.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/18
 * @Version 1.0
 */
@Data
public class OrderContectQuery {

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
     * 总人数
     */
    private Integer AllNumber;

    /**
     * 联系电话
     */
    private String ContactTel;
}
