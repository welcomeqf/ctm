package eqlee.ctm.finance.settlement.entity.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author qf
 * @Date 2019/9/28
 * @Version 1.0
 */
@Data
public class ExamineResultVo {


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

    /**
     * 未付款人数
     */
    private Integer UnpaidNumber;

    /**
     * 结算价(应收金额)
     */
    private Double SettlementPrice;

    /**
     * 代收费用（实收金额）
     */
    private Double SubstitutionPrice;


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
     * 应得收入（应收金额-总支出费用）
     */
    private Double InPrice;

    /**
     * 结算金额
     */
    private Double FinallyPrice;
}
