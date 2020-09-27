package eqlee.ctm.apply.line.entity.vo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Data
public class LineVo {

    /**
     * 线路名
     */
    private String lineName;

    /**
     * 线路简介
     */
    private String information;

    /**
     * 城市
     */
    private String city;

    /**
     * 图片路径
     */
    private String picturePath;

    /**
     * 区域
     */
    private String region;

    /**
     * 出游情况（几日游）
     */
    private Integer travelSituation;

    /**
     * 最大人数
     */
    private Integer maxNumber;

    /**
     * 最小人数
     */
    private Integer minNumber;

    /**
     * 线路样式(暂存颜色)
     */
    private String Style;
}
