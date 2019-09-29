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
     * 同行公司名
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
}
