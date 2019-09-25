package eqlee.ctm.apply.orders.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Orders")
public class Orders {
    private Long Id;

    /**
     * 订单号
     */
    private String OrderNo;

    /**
     * 同行代表人姓名
     */
    private String AccountName;

    /**
     * 出行时间
     */
    private LocalDate OutDate;

    /**
     * 同行公司名
     */
    private String CompanyName;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 价格
     */
    private Double AllPrice;

    /**
     * 省
     */
    private String Province;

    /**
     * 市
     */
    private String City;

    /**
     * 区域
     */
    private String Region;

    /**
     * 导游姓名
     */
    private String GuideName;

    /**
     * 车辆类别
     */
    private Boolean CarType;

    /**
     * 车牌号
     */
    private String CarNumber;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 创建人
     */
    private Long CreateUserId;

    /**
     * 创建时间
     */
    private LocalDateTime CreateDate;

    /**
     * 更新人
     */
    private Long UpdateUserId;

    /**
     * 更新时间
     */
    private LocalDateTime UpdateDate;


}
