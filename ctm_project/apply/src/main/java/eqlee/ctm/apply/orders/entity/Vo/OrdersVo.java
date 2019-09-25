package eqlee.ctm.apply.orders.entity.Vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author Claire
 * @Date 2019/9/23 0023
 * @Version 1.0
 */
@Data
public class OrdersVo {

    private Long LineId;
    /**
     * 联系人姓名
     */
    private String ContactName;

    /**
     * 联系方式
     */
    private String ContactTel;

    /**
     * 区域
     */
    private String Region;

    /**
     * 接送地
     */
    private String Place;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 出发日期
     */
    private String OutDate;

}
