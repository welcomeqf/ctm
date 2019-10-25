package eqlee.ctm.finance.settlement.entity.query;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/29
 * @Version 1.0
 */
@Data
public class ExamineDetailedQuery {


    private Long guideId;

    /**
     * 导游名字
     */
    private String guideName;

    /**
     * 出发日期
     */
    private String outDate;

    /**
     * 线路名
     */
    private String lineName;

    /**
     * 应到人数
     */
    private Integer allDoNumber;

    /**
     * 实到人数
     */
    private Integer trueAllNumber;

    /**
     * 实到成人人数
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
     * 未付款人数
     */
    private Integer unpaidNumber;

    /**
     * 结算价(应收金额)
     */
    private Double settlementPrice;

    /**
     * 代收费用（实收金额）
     */
    private Double substitutionPrice;

    /**
     * 门票名
     */
    private String ticketName;

    /**
     * 门票费用
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
     * 车牌号
     */
    private String carNo;

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
     * 应得收入（应收金额-总支出费用）
     */
    private Double inPrice;

    /**
     * 结算金额
     */
    private Double finallyPrice;

    /**
     * 未付款所有人数
     */
    private Integer allNoNumber;

    /**
     * 未付款所有金额
     */
    private Double allNoPrice;

    /**
     * 联系人详情列表
     */
    private List<ExamineContectQuery> queryList;
}
