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
}
