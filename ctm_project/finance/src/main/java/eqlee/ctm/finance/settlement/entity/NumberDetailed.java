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
@TableName("NumberDetailed")
public class NumberDetailed extends Model<NumberDetailed> {

    private static final long serialVersionUID = 1L;

    private Long Id;

    private Long NumberId;

    /**
     * 联系人名字
     */
    private String ContectUserName;

    /**
     * 联系电话
     */
    private String ContectUserTel;

    /**
     * 面收未付款总人数
     */
    private Integer AllNumber;

    /**
     * 面收未付款总价格
     */
    private Double AllPrice;

    /**
     * 未付款代收幼儿人数
     */
    private Integer BabyNumber;

    /**
     * 未付款代收老人人数
     */
    private Integer OldNumber;

    /**
     * 未付款代收小孩人数
     */
    private Integer ChildNumber;

    /**
     * 未付款代收成年人数
     */
    private Integer AdultNumber;

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
