package eqlee.ctm.apply.entry.entity.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/10/10
 * @Version 1.0
 */
@Data
public class ApplySeacherVo {

    private Long id;

    /**
     * 报名单号
     */
    private String applyNo;

    /**
     * 出行日期
     */
    private String outDate;

    /**
     * 总价格
     */
    private Double allPrice;

    /**
     * 线路ID  哪条线路
     */
    private Long lineId;

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
     * 总人数
     */
    private Integer allNumber;

    /**
     * 同行公司名
     */
    private String companyName;

    /**
     * 同行代表人
     */
    private String companyUser;

    /**
     * 省（备用）
     */
    private String province;

    /**
     * 市（备用）
     */
    private String city;

    /**
     * 区域
     */
    private String region;

    /**
     * 支付类型(0--现结  1--月结 2--代付)默认0
     */
    private Integer payType;

    private Double msPrice;

    /**
     * (0默认false--未付款 1  true--已付款)
     */
    private Boolean isPayment;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否取消(0默认false--正常  1  true--取消)
     */
    private Boolean isCancel;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 修改人
     */
    private Long updateUserId;

    /**
     * 修改时间
     */
    private String updateDate;

    /**
     * (0-待审核  1-通过  2--不通过)
     */
    private Integer statu;

    private Boolean isSelect;

    /**
     * 0-- 微信支付  1--支付宝支付
     */
    private Integer payInfo;

    private String lineName;

    private String applyRemark;

    private Double adultPrice;

    private Double babyPrice;

    private Double oldPrice;


    private Double childPrice;

    private Long companyId;

    private String applyPic;

    /**
     *  身份证信息（选填）
     */
    private String icnumber;

    /**
     * 接送地
     */
    private String PlaceRegion;

    /**
     * 区域
     */
    private String PlaceAddress;

    /**
     * 0--正常报名  1--补录 2--包团
     */
    private Integer Type;
}
