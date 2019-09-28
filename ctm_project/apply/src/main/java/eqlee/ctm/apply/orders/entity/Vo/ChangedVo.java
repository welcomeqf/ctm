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
     *导游Id
     */
    private Long CreateUserId;

    /**
     * 联系人
     */
    private String ContactName;

    /**
     * 出行时间
     */
    private LocalDate OutDate;

    /**
     * 联系电话
     */
    private String ContactTel;

    /**
     * 导游姓名
     */
    private String GuideName;

    /**
     * 线路名
     */
    private String LineName;



}
