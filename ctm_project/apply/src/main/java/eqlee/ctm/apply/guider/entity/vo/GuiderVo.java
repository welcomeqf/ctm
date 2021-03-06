package eqlee.ctm.apply.guider.entity.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author Claire
 * @Date 2019/9/23 0023
 * @Version 1.0
 */
@Data
public class GuiderVo {

    private Long id;

    private String lineName;

    private String outDate;

    private Integer allNumber;

    private Integer adultNumber;

    private String contactName;

    private String contactTel;

    private String place;

    private Integer babyNumber;

    private Integer oldNumber;

    private Integer childNumber;

    /**
     * 结算价
     */
    private Double allPrice;

    /**
     * 面收金额
     */
    private Double msPrice;

    /**
     * 备注
     */
    private String applyRemark;

    /**
     * 同行
     */
    private String companyName;

    /**
     * 是否选人  true-已选  false-未选
     */
    private Boolean isSelect;

    /**
     * (0--正常报名  1-补录  2-包团)
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
     *支付方式 (0--微信  1--支付宝 2--转账上传图片)
     */
    private Integer ApplyPayType;

    /**
     * 支付流水号
     */
    private String ThirdpartyNumber;

    /**
     *支付方式 (0--现结 2--月结)
     */
    private Integer PayType;

    /**
     *  身份证信息（选填）
     */
    private String icnumber;

    /**
     *  包团信息
     */
    private String ApplyPic;

}

