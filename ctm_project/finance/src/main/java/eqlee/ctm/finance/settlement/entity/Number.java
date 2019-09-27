package eqlee.ctm.finance.settlement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/9/27
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@TableName("Number")
public class Number extends Model<Number> {

    private static final long serialVersionUID = 1L;

    private Long Id;

    /**
     * 实到人数
     */
    private Integer TrueAllNumber;

    /**
     * 实到成人人数
     */
    private Integer TreeAdultNumber;

    /**
     * 实到幼儿人数
     */
    private Integer TreeBabyNumber;

    /**
     * 实到老人人数
     */
    private Integer TreeOldNumber;

    /**
     * 实到小孩人数
     */
    private Integer TreeChildNumber;

    /**
     * 未付款人数
     */
    private Integer UnpaidNumber;

    /**
     * 创建人Id
     */
    private Long CreateUserId;

    /**
     * 创建时间
     */
    private LocalDateTime CreateDate;

    /**
     * 修改人Id
     */
    private Long UpdateUserId;

    /**
     * 修改时间
     */
    private LocalDateTime UpdateDate;

    /**
     * 备注
     */
    private String Remark;

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }

}
