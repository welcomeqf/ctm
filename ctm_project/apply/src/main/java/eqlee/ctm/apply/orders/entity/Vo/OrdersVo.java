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

    private Long lineId;
    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系方式
     */
    private String contactTel;

    /**
     * 区域
     */
    private String region;

    /**
     * 接送地
     */
    private String place;

    /**
     * 线路名
     */
    private String lineName;

    /**
     * 出发日期
     */
    private String outDate;

}
