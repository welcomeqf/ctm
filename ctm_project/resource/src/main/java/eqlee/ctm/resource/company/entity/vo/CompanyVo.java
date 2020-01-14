package eqlee.ctm.resource.company.entity.vo;


import lombok.Data;


/**
 * @Author Claire
 * @Date 2019/9/17 0017
 * @Version 1.0
 */
@Data
public class CompanyVo {

    private Long id;

    private String companyName;

    private String companyFullName;

    private String startDate;

    private String endDate;

    private String payMethod;

    private Boolean stopped;

    private String companyNo;

    /**
     * 同行合同图片
     */
    private String companyPic;

    /**
     * 授信月结金额
     */
    private Double sxPrice;

    /**
     * 负责人名字
     */
    private String chargeName;

    /**
     * 负责人电话
     */
    private String chargeTel;

    /**
     * 财务人名字
     */
    private String financeName;

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
