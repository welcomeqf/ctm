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
     * 出游情况（几日游）
     */
    private String TravelSituation;
}
