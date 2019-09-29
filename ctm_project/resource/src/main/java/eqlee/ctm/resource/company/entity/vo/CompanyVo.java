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

    private String stopped;

}
