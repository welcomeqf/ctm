package eqlee.ctm.finance.settlement.entity.query;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/28
 * @Version 1.0
 */
@Data
public class ExamineResultQuery {


    private Long Id;
    /**
     * 导游名字
     */
    private String GuideName;

    /**
     * 出发日期
     */
    private String OutDate;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 导游电话
     */
    private String Tel;

    /**
     * 总支出费用
     */
    private Double AllOutPrice;

    /**
     * 总收入
     */
    private Double AllInPrice;

    /**
     * 实际收入（实收金额-总支出费用）
     */
    private Double FinallyPrice;

    private Integer Status;

    /**
     * 出发日期
     */
    private LocalDateTime UpdateDate;
}
