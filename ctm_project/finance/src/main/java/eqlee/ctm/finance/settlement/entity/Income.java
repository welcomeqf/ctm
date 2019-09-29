package eqlee.ctm.finance.settlement.entity;

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
 * @Date 2019/9/27
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("Income")
public class Income extends Model<Income> {

    private static final long serialVersionUID = 1L;

    private Long Id;

    /**
     * 导游名字
     */
    private String GuideName;

    /**
     * 出发日期
     */
    private LocalDate OutDate;

    /**
     * 线路名
     */
    private String LineName;

    /**
     * 部门
     */
    private String Department;

    /**
     * 人数ID
     */
    private Long NumberId;

    /**
     * 收款单位
     */
    private String ReceivingUnit;

    /**
     * 结算价(应收金额)
     */
    private Double SettlementPrice;

    /**
     * 状态(0--已提交待审核1--审核成功2--审核失败)
     */
    private Integer Status;

    /**
     * 代收费用（实收金额）
     */
    private Double SubstitutionPrice;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 创建人Id
     */
    private Long CreateUserId;

    /**
     * 创建日期
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

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }
}
