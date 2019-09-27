package eqlee.ctm.finance.settlement.entity.vo;

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
    private String OutDate;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 车牌号
     */
    private String CarNo;

    /**
     * 实到人数
     */
    private Integer TrueAllNumber;

    /**
     * 实到成年人数
     */
    private Integer TreeAdultNumber;

    /**
     * 实到幼儿人数
     */
    private Integer TreeBabyNumber;

    /**
     * 实到老人人数
     */
    private Integer TreeOldNumber;

    /**
     * 实到小孩人数
     */
    private Integer TreeChildNumber;

    /**
     * 未付款的联系人列表
     */
    private List<ContectUserVo> UnpaidList;

    /**
     * 未付款人数
     */
    private Integer UnpaidNumber;

    /**
     * 应收金额
     */
    private Double GaiMoney;

    /**
     * 实收金额
     */
    private Double TrueMoney;

    /**
     * 门票名
     */
    private String TicketName;

    /**
     * 门票价格
     */
    private Double TicketPrice;

    /**
     * 午餐费用
     */
    private Double LunchPrice;

    /**
     * 停车费
     */
    private Double ParkingRatePrice;

    /**
     * 租车费用
     */
    private Double RentCarPrice;

    /**
     * 导游补助
     */
    private Double GuideSubsidy;

    /**
     * 司机补助
     */
    private Double DriverSubsidy;

    /**
     * 总支出费用
     */
    private Double AllOutPrice;

}
