package eqlee.ctm.apply.orders.entity.Vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Author Claire
 * @Date 2019/9/27 0027
 * @Version 1.0
 */
@Data
public class ChoisedVo {

    /**
     *导游Id
     */
    private Long id;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 出行时间
     */
    private String outDate;

    /**
     * 联系电话
     */
    private String contactTel;

    /**
     * 导游姓名
     */
    private String guideName;

    /**
     * 线路名
     */
    private String lineName;


    /**
     * 被更换导游的Id
     */
    private String updatedId;





}
