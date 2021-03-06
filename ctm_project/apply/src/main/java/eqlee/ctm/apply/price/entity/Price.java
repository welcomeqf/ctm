package eqlee.ctm.apply.price.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
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

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long Id;

    /**
     * 出行日期
     */
    private LocalDate OutDate;

    /**
     * 线路ID
     */
    private Long LineId;

    /**
     * 渠道ID
     */
    private Long ChannelId;

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
     * 门市成年价格
     */
    private Double MarketAdultPrice;

    /**
     * 门市老人价格
     */
    private Double MarketOldPrice;

    /**
     * 门市幼儿价格
     */
    private Double MarketBabyPrice;

    /**
     * 门市小孩价格
     */
    private Double MarketChildPrice;

    /**
     * 是否停用(0默认表示false--使用1--不使用)
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

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }
}
