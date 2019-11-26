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

}
