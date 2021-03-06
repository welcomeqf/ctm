package eqlee.ctm.apply.entry.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Data
public class ApplyVo {

    /**
     * 出行日期
     */
    private String outDate;
    /**
     * 同行公司名Id
     */
    private Long companyNameId;

    /**
     * 同行代表人
     */
    private String companyUser;

    /**
     *联系人名字
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactTel;

    /**
     * 接送地点
     */
    private String place;

    /**
     * 线路名
     */
    private String lineName;

    /**
     * 成年人数
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
     * 支付类型(0--现结  1--月结 2--代付)默认0
     */
    private String payType;

    private Long createUserId;

    private Long updateUserId;

    private Long companyUserId;

    private String cName;

    /**
     * 门市价
     */
    private Double marketAllPrice;

    /**
     * 面收金额
     */
    private Double msPrice;

    /**
     * 报名备注
     */
    private String applyRemark;

    /**
     * 授信月结金额
     */
    private Double sxPrice;


    /**
     * 成年价格
     */
    private Double adultPrice;

    /**
     * 老人价格
     */
    private Double oldPrice;

    /**
     * 幼儿价格
     */
    private Double babyPrice;

    /**
     * 小孩价格
     */
    private Double childPrice;

    /**
     * 正常报名 type = 0
     * 补录type= 1
     * 包团type = 2
     */
    private Integer type;

    /**
     * 1-修改 0-报名
     */
    private Integer upOrInsert;

    private Long applyId;

    /**
     * 报名单号
     */
    private String applyNo;

    /**
     * 同行代表电话
     */
    private String companyTel;

    /**
     * 包团详情
     */
    private String applyPic;

    /**
     *  身份证信息（选填）
     */
    private String icnumber;

    /**
     *  (0-待审核  1-通过  2--不通过 3-已取消) 默认0
     */
    private Integer statu;

    /**
     * 接送地
     */
    private String PlaceRegion;

    /**
     * 区域
     */
    private String PlaceAddress;


}
