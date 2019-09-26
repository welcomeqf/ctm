package eqlee.ctm.apply.entry.entity.query;


import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Author Claire
 * @Date 2019/9/17 0017
 * @Version 1.0
 */
@Data
public class Company {

    private Long Id;

    /**
     * 公司名称
     */
    private String CompanyName;

    /**
     * 合同开始时间
     */
    private LocalDateTime StartDate;

    /**
     * 合同结束时间
     */
    private LocalDateTime endDate;

    /**
     * 支付方式
     */
    private int PayMethod;

    /**
     * 公司状态
     */
    private boolean Stopped;

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
    private LocalDateTime CreateDate;

    /**
     * 修改人
     */
    private Long UpdateUserId;

    /**
     * 修改时间
     */
    private LocalDateTime UpdateDate;
}
