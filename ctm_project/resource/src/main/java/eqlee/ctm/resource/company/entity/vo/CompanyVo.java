package eqlee.ctm.resource.company.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author Claire
 * @Date 2019/9/17 0017
 * @Version 1.0
 */
@Data
public class CompanyVo {

    private String CompanyName;

    /*
    合同开始时间
    */
    private LocalDateTime StartDate;

    /*
    合同结束时间
    */
    private LocalDateTime endDate;

    /*
    支付方式(0--默认1--现结2--月结3--代收)
    */
    private int PayMethod;
}
