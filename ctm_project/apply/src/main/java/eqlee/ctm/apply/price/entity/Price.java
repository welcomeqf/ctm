package eqlee.ctm.apply.price.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/17
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Price")
public class Price extends Model<Price> {

    /**
     * ID
     */
    private Long Id;

    /**
     * 出行日期
     */
    private LocalDateTime OutDate;

    /**
     * 线路ID
     */
    private Long LineId;

    /**
     * 渠道ID
     */
    private Long ChannelId;

    /**
     * 总价格
     */
    private Double AllPrice;

    /**
     * 成年价格
     */
    private Double AdultPrice;

    /**
     * 老人价格
     */
    private Double OldPrice;

    /**
     * 幼儿价格
     */
    private Double BabyPrice;

    /**
     * 小孩价格
     */
    private Double ChildPrice;

    /**
     * 最大人数
     */
    private Integer MaxNumber;

    /**
     * 最小人数
     */
    private Integer MinNumber;

    /**
     * 是否停用(0默认表示false--不出行1--出行)
     */
    private Boolean Stopped;

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
     * 修改人
     */
    private Long UpdateUserId;

    /**
     * 修改时间
     */
    private LocalDateTime UpdateDate;
}
