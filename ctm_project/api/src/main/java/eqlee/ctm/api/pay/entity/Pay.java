package eqlee.ctm.api.pay.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author qf
 * @Date 2019/10/28
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("Pay")
public class Pay extends Model<Pay> {

    /**
     * id
     */
    private Long Id;

    /**
     * 报名单号
     */
    private String ApplyNo;

    /**
     * 报名流水号
     */
    private String PayApplyNumber;

    /**
     * 报名ID
     */
    private Long ApplyId;

    /**
     * 第三方流水号
     */
    private String ThirdPartyNumber;

    /**
     * 支付方式备注
     */
    private String PayMethodText;

    /**
     * 支付金额
     */
    private Double PayMoney;

    /**
     * 已退款金额
     */
    private Double RefundMoney;

    /**
     * 支付时间
     */
    private LocalDateTime PayDate;

    /**
     * 备注
     */
    private String Remark;

    /**
     * 支付状态（0--未支付  1--支付成功  2--支付失败）
     */
    private Integer PayStatu;

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
     * (0--微信  1--支付宝)
     */
    private Integer PayType;
}
