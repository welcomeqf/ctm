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
    private LocalDateTime OutDate;
    /**
     * 同行公司名
     */
    private String CompanyName;

    /**
     * 同行代表人
     */
    private String CompanyUser;

    /**
     *联系人名字
     */
    private String ContactName;

    /**
     * 联系电话
     */
    private String ContactTel;

    /**
     * 接送地点
     */
    private String Place;

    /**
     * 区域
     */
    private String Region;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 成年人数
     */
    private Integer AdultNumber;

    /**
     * 幼儿人数
     */
    private Integer BabyNumber;

    /**
     * 老人人数
     */
    private Integer OldNumber;

    /**
     * 小孩人数
     */
    private Integer ChildNumber;

    /**
     * 支付类型(0--现结  1--月结 2--代付)默认0
     */
    private Integer PayType;
}
