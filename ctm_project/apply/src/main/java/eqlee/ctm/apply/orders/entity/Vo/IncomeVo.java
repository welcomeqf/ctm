package eqlee.ctm.apply.orders.entity.Vo;

import eqlee.ctm.apply.orders.entity.bo.OrderTypeBo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author Claire
 * @Date 2019/9/28 0028
 * @Version 1.0
 */
@Data
public class IncomeVo {
    /**
     *总人数
     */
    private Integer allNumber;

    /**
     * 成人人数
     */
    private Integer adultNumber;

    /**
     * 幼儿人数
     */
    private Integer babyNumber;

    /**
     * 老人人数
     */
    private Integer oldNumber;

    /**
     * 小孩人数
     */
    private Integer childNumber;

    /**
     * 面收金额
     */
    private Double msPrice;

    private String carNumber;

    /**
     * false--本公司车辆   true--外部车辆
     */
    private Boolean carType;

    private Double allPrice;

    private Double monthPrice;

    /**
     * 状态(0--已提交待审核1--审核成功2--审核失败)
     */
    private Integer status;

}
