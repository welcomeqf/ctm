package eqlee.ctm.apply.orders.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
public class Orders extends Model<Orders> {
    private static final long serialVersionUID = 1L;
    private Long Id;

    /**
     * 订单号
     */
    private String OrderNo;

    /**
     * 出行时间
     */
    private LocalDate OutDate;


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
     * 导游电话
     */
    private String GuideTel;

    /**
     * 车辆类别
     */
    private Boolean CarType;

    /**
     * 车牌号
     */
    private String CarNumber;

    /**
     * (0-- 未完成  1--已完成)
     */
    private Boolean IsFinash;

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


    private Integer Statu;

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }
}
