package eqlee.ctm.apply.line.entity.query;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/10/8
 * @Version 1.0
 */
@Data
public class LineSeacherQuery {

    private Long Id;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 线路简介
     */
    private String Information;

    /**
     * 区域
     */
    private String Region;

    /**
     * 城市
     */
    private String City;

    /**
     * 图片
     */
    private String PicturePath;

    /**
     * 出游天数（几日游）
     */
    private Integer TravelSituation;

    /**
     * 最大人数
     */
    private Integer MaxNumber;

    /**
     * 最小人数
     */
    private Integer MinNumber;

    private Boolean Stopped;
}
