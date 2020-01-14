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
     * 城市
     */
    private String city;

    /**
     * 总金额
     */
    private Double allPrice;
}
