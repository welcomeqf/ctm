package eqlee.ctm.resource.company.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/28
 * @Version 1.0
 */
@Data
public class CompanyQueryVo {

    private Long Id;

    /**
     * 公司简称
     */
    private String CompanyName;

    /**
     * 公司全称
     */
    private String CompanyFullName;

    /**
     * 合同开始时间
     */
    private String StartDate;

    /**
     * 合同结束时间
     */
    private String endDate;

    /**
     * 同行编号
     */
    private String CompanyNo;

    /**
     * 同行合同图片
     */
    private String CompanyPic;

    /**
     * 授信月结金额
     */
    private Double SxPrice;

    /**
     * 支付方式
     */
    private Integer PayMethod;

    /**
     * 公司状态
     */
    private Boolean Stopped;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 创建人
     */
    private Long CreateUserId;

    /**
     * 创建时间
     */
    private String CreateDate;

    /**
     * 修改人
     */
    private Long UpdateUserId;

    /**
     * 修改时间
     */
    private String UpdateDate;

    /**
     * 负责人名字
     */
    private String ChargeName;

    /**
     * 负责人电话
     */
    private String ChargeTel;

    /**
     * 财务人名字
     */
    private String FinanceName;

    /**
     * 财务人电话
     */
    private String FinanceTel;

    /**
     * 营业执照图片
     */
    private String Business;

    /**
     * 许可证
     */
    private String Licence;

    /**
     * 保险证
     */
    private String Insurance;

    /**
     * 银行卡
     */
    private String BankCard;

    /**
     * 公司地址
     */
    private String Address;

    /**
     * 0-待审核  1-通过 2-拒绝
     */
    private Integer Status;
}
