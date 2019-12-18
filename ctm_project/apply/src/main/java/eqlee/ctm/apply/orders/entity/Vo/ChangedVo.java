package eqlee.ctm.apply.orders.entity.Vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author Claire
 * @Date 2019/9/27 0027
 * @Version 1.0
 */
@Data
public class ChangedVo {


    /**
     *订单Id
     */
    private Long OrderId;

    /**
     * 联系电话
     */
    private String Tel;

    /**
     * 导游姓名
     */
    private String GuideName;

    private String Place;

    private String LineName;

    private String OutDate;

    private Integer AllNumber;

    private Integer AdultNumber;

    private Integer BabyNumber;

    private Integer OldNumber;

    private Integer ChildNumber;

    private String ContactName;

    private String ContactTel;

}
