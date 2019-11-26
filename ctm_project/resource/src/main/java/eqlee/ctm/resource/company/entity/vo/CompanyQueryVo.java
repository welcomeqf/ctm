package eqlee.ctm.resource.company.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/28
 * @Version 1.0
 */
@Data
public class CompanyQueryVo {

    private Long Id;

    private String CompanyName;

    private String StartDate;

    private String EndDate;

    private Integer PayMethod;

    private Boolean Stopped;

    /**
     * 同行编号
     */
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
