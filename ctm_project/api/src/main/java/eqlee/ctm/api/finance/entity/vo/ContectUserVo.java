package eqlee.ctm.api.finance.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Data
public class ContectUserVo {

    private String contectUserName;

    private String contectUserTel;

    private Integer allNumber;

    /**
     * 未付款代收总价格
     */
    private Double allPrice;

    private Integer babyNumber;

    /**
     * 未付款代收老人人数
     */
    private Integer oldNumber;

    /**
     * 未付款代收小孩人数
     */
    private Integer childNumber;

    /**
     * 未付款代收成年人数
     */
    private Integer adultNumber;

}
