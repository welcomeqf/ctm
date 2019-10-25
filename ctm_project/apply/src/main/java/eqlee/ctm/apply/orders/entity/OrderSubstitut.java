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
 * @Author qf
 * @Date 2019/10/21
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("OrderSubstitut")
public class OrderSubstitut extends Model<OrderSubstitut> {


    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long Id;

    /**
     * 发送人名字
     */
    private String GuideName;

    /**
     * 接收人Id
     */
    private Long ToGuideId;

    /**
     * 出发时间
     */
    private LocalDate OutDate;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 状态（0--换人申请  1--成功  2--失败）
     */
    private Integer Status;

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

    /**
     * 备注
     */
    private String Remark;

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }
}
