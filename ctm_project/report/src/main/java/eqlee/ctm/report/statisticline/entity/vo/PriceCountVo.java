package eqlee.ctm.report.statisticline.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/14
 * @Version 1.0
 */
@Data
public class PriceCountVo {

    /**
     * 日期（天数）
     */
    private String day;

    /**
     * 总金额
     */
    private Double allPrice;
}
