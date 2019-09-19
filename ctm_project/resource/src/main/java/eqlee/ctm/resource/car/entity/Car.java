package eqlee.ctm.resource.car.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author Claire
 * @Date 2019/9/18 0018
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Car")
public class Car extends Model<Car> {

    private Long Id;

/**
 * 车辆编号
 */
    private String CarNo;


    /**
     *车辆名称
     */
    private String CarName;


    /**
     * 是否是公司车辆（0--是 1--否）
     */
    private Boolean IsCompany;

    /**
     * 创建人
     */
    private Long CreateUserId;

    /**
     * 创建时间
     */
    private LocalDateTime CreateDate;

    /**
     * 更新使用人
     */
    private Long UpdateUserId;

    /**
     * 更新时间
     */
    private LocalDateTime UpdateDate;

    /**
     * 备注
     */
    private String Remark;

    /**
     * (0-表示未出行  1--已出行  2--正在维修)
     */
    private Integer Statu;
}
