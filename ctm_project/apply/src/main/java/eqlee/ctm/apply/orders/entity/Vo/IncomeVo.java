package eqlee.ctm.apply.orders.entity.Vo;

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
     *应到人数
     */
    private Integer comeedCount;

    /**
     * 实到人数
     */
    private Integer DoNumber;

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
     * 联系人列表
     */
    private List<Map<String,Object>> ContactNames;

    /**
     * 未付人数
     */
    private Integer Unpaid;

    /**
     * 应收金额
     */
    private Double SumInCome;

    private String CarNumber;

    /**
     * false--本公司车辆   true--外部车辆
     */
    private Boolean CarType;

}
