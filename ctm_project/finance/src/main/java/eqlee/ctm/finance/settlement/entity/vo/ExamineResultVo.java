package eqlee.ctm.finance.settlement.entity.vo;

import eqlee.ctm.finance.other.entity.Other;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author qf
 * @Date 2019/9/28
 * @Version 1.0
 */
@Data
public class ExamineResultVo {


    private Long id;

    private Long GuestId;
    /**
     * 导游名字
     */
    private String GuideName;

    /**
     * 出发日期
     */
    private String OutDate;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 结算价(面收金额)
     */
    private Double SettlementPrice;

    /**
     * 总收入
     */
    private Double AllInPrice;

    /**
     * 门票名
     */
    private String TicketName;

    /**
     * 门票费用
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
     * 车牌号
     */
    private String CarNo;

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

    /**
     * 结算金额
     */
    private Double FinallyPrice;

    /**
     * 毛利
     */
    private Double ProfitPrice;

    /**
     * 月结总价
     */
    private Double MonthPrice;

    /**
     * 应到人数
     */
    private Integer AllDoNumber;

    /**
     * 实到人数
     */
    private Integer TrueAllNumber;

    /**
     * 实到成人人数
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







}
