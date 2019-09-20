package eqlee.ctm.apply.entry.entity.query;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author qf
 * @Date 2019/9/19
 * @Version 1.0
 */
@Data
public class ApplyQuery {

    private String LineName;

    private LocalDate OutDate;

    /**
     * 成年价格
     */
    private Double AdultPrice;

    /**
     * 老人价格
     */
    private Double OldPrice;

    /**
     * 幼儿价格
     */
    private Double BabyPrice;

    /**
     * 小孩价格
     */
    private Double ChildPrice;

    /**
     * 已报人数
     */
    private Integer AllNumber;

    /**
     * 最大人数
     */
    private Integer MaxNumber;

    private String Region;
}
