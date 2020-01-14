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

    private Long id;

    private String LineName;

    private String OutDate;

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
     * 门市成年价格
     */
    private Double MarketAdultPrice;

    /**
     * 门市老人价格
     */
    private Double MarketOldPrice;

    /**
     * 门市幼儿价格
     */
    private Double MarketBabyPrice;

    /**
     * 门市小孩价格
     */
    private Double MarketChildPrice;

    /**
     * 已报人数
     */
    private Integer DoAllNumber;

    /**
     * 最大人数
     */
    private Integer MaxNumber;

    private String Region;

    private Integer TravelSituation;


    private String PicturePath;
}
