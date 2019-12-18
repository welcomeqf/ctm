package eqlee.ctm.finance.settlement.entity.vo;

import eqlee.ctm.finance.settlement.entity.bo.OtherBo;
import lombok.Data;

import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Data
public class FinanceVo {

    /**
     * 出发日期
     */
    private String outDate;

    /**
     * 线路名
     */
    private String lineName;

    /**
     * 车牌号
     */
    private String carNo;

    /**
     * 应到人数
     */
    private Integer allDoNumber;

    /**
     * 实到人数
     */
    private Integer trueAllNumber;

    /**
     * 实到成年人数
     */
    private Integer treeAdultNumber;

    /**
     * 实到幼儿人数
     */
    private Integer treeBabyNumber;

    /**
     * 实到老人人数
     */
    private Integer treeOldNumber;

    /**
     * 实到小孩人数
     */
    private Integer treeChildNumber;


    /**
     * 应收金额
     */
    private Double gaiMoney;

    /**
     * 其他收入
     */
    private List<OtherBo> otherInPrice;

    /**
     * 门票名
     */
    private String ticketName;

    /**
     * 门票价格
     */
    private Double ticketPrice;

    /**
     * 午餐费用
     */
    private Double lunchPrice;

    /**
     * 停车费
     */
    private Double parkingRatePrice;

    /**
     * 租车费用
     */
    private Double rentCarPrice;

    /**
     * 导游补助
     */
    private Double guideSubsidy;

    /**
     * 司机补助
     */
    private Double driverSubsidy;

    /**
     * 总支出费用
     */
    private Double allOutPrice;

    /**
     * 该团总价格
     */
    private Double allPrice;

    /**
     * 月结金额
     */
    private Double monthPrice;

    private Boolean carType;

}
