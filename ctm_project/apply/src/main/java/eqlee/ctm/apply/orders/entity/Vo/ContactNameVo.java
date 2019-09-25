package eqlee.ctm.apply.orders.entity.Vo;

import lombok.Data;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
@Data
public class ContactNameVo {

    private Long Id;
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

}
