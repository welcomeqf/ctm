package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/30
 * @Version 1.0
 */
@Data
public class ApplyMonthQuery {

    /**
     * id
     */
    private Long Id;

    /**
     * 出行日期
     */
    private String OutDate;

    /**
     * 总价
     */
    private Double AllPrice;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 总人数
     */
    private Integer AllNumber;

    /**
     * 是否付款（true--已付款  false--未付款）
     */
    private Boolean IsPayment;

    private String CompanyUser;

    private String ContactName;
}
