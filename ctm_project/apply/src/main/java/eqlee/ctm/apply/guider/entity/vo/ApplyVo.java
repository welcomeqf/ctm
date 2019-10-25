package eqlee.ctm.apply.guider.entity.vo;

import lombok.Data;

/**
 * @Author Claire
 * @Date 2019/9/23 0023
 * @Version 1.0
 */
@Data
public class ApplyVo {

    /**
     * 报名表Id
     */
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

    /**
     * 人数
     */
    private String AllNumber;

    private String LineName;

    private String OutDate;

}
