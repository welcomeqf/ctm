package eqlee.ctm.resource.company.entity.vo;

import lombok.Data;


/**
 * @Author Claire
 * @Date 2019/10/9 0009
 * @Version 1.0
 */
@Data
public class CompanyIndexVo {
    private Long id;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 合同开始时间
     */
    private String startDate;

    /**
     * 合同结束时间
     */
    private String endDate;

    /**
     * 支付方式
     */
    private Integer payMethod;

    /**
     * 公司状态
     */
    private Boolean stopped;

    private String companyNo;

    private String companyPic;

    private Double sxPrice;

    /**
     * 负责人名字
     */
    private String chargeName;

    /**
     * 财务人电话
     */
    private String financeTel;

    /**
     * 营业执照图片
     */
    private String business;

    /**
     * 许可证
     */
    private String licence;

    /**
     * 保险证
     */
    private String insurance;

    /**
     * 银行卡
     */
    private String bankCard;

    private String address;

    /**
     * 0-待审核 1-通过 2-拒绝
     */
    private Integer status;



}
