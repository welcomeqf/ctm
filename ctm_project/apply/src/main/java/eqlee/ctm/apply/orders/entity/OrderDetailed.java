package eqlee.ctm.apply.orders.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author Claire
 * @Date 2019/9/24 0024
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("OrderDetailed")
public class OrderDetailed {
    private Long Id;

    /**
     *订单ID
     */
    private Long OrderId;

    /**
     * 联系人
     */
    private String ContactName;

    /**
      * 联系电话
     */
    private String ContactTel;

    /**
     * 接送地
     */
    private String Place;

    /**
     * 成人人数
     */
    private Integer AdultNumber;

    /**
     * 幼儿人数
     */
    private Integer BabyNumber;

    /**
     * 老人人数
     */
    private Integer OldNumber;

    /**
     * 小孩人数
     */
    private Integer ChildNumber;
    /**
     * 总人数
     */
     private Integer AllNumber;

    /**
     * 支付类型(0--现结  1--月结  2--代付)
     */
    private Integer PayType;

    /**
     * (0--未付款  1--已付款)
     */
    private Boolean IsPayment;

    /**
     * 价格
     */
    private Double Price;

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
