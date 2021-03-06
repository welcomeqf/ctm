package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author qf
 * @Date 2019/9/20
 * @Version 1.0
 */
@Data
public class ApplyCompanyQuery {

    private Long Id;

    private String CompanyName;

    /**
     * 报名单号
     */
    private String ApplyNo;

    /**
     * 出行日期
     */
    private String OutDate;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 总人数
     */
    private Integer AllNumber;

    /**
     *联系人名字
     */
    private String ContactName;

    /**
     * 联系电话
     */
    private String ContactTel;

    /**
     * (0-待审核  1-通过  2--不通过)
     */
    private Integer Statu;

    /**
     * true 已经取消   false--正常
     */
    private Boolean IsCancel;

    /**
     * 0--正常  1--取消中
     */
    private Integer CancelInfo;

    /**
     * 是否付款(true-已付款  false-未付款)
     */
    private Boolean IsPayment;

    /**
     * 下单时间
     */
    private String CreateDate;

    /**
     * 门市价
     */
    private Double MarketAllPrice;

    /**
     * 结算价
     */
    private Double AllPrice;

    private Integer ChildNumber;

    private Integer OldNumber;

    private Integer BabyNumber;

    private Integer AdultNumber;

    private String Place;

    private String CName;

    /**
     * 0--现结  1--月结  2--面收
     */
    private Integer PayType;

    /**
     * 0-正常报名  1-补录 2-包团
     */
    private Integer type;

    /**
     * 导游名字
     */
    private String guide;

    /**
     * 导游电话
     */
    private String tel;

    /**
     *  身份证信息（选填）
     */
    private String icnumber;
}
