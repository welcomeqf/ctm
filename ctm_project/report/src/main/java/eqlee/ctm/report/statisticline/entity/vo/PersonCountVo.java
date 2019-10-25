package eqlee.ctm.report.statisticline.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/14
 * @Version 1.0
 */
@Data
public class PersonCountVo {

    /**
     * 天数（日期）
     */
    private String day;

    /**
     * 总人数
     */
    private Integer allPersonCount;
}
