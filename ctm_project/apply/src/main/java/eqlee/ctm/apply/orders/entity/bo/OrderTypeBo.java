package eqlee.ctm.apply.orders.entity.bo;

import lombok.Data;

/**
 * @Author qf
 * @Date 2019/11/5
 * @Version 1.0
 */
@Data
public class OrderTypeBo {

    private Long id;

    private Double allPrice;

    private Integer adultNumber;

    private Integer babyNumber;

    private Integer oldNumber;

    private Integer childNumber;

    private String contactName;

    private String contactTel;

    private Integer allNumber;
}
