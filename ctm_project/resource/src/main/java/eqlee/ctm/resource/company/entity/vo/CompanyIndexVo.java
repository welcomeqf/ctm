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
    private boolean stopped;

}
