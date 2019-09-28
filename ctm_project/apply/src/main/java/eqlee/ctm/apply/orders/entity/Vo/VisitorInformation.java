package eqlee.ctm.apply.orders.entity.Vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author Claire
 * @Date 2019/9/26 0026
 * @Version 1.0
 */
@Data
public class VisitorInformation {
    /**
     * 联系人姓名
     */
    private String ContactName;


    /**
     * 出发日期
     */
//    private LocalDate OutDate;


    /**
     * 联系方式
     */
    private String ContactTel;


    /**
     * 接送地
     */
    private String Place;
}
