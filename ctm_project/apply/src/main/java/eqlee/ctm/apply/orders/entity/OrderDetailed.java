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
@TableName("OrderDetailed")
public class OrderDetailed extends Model<OrderDetailed> {
    private static final long serialVersionUID = 1L;
    private Long Id;

    /**
     *订单ID
     */
    private Long OrderId;

    /**
     * 换人id
     */
    private Long OrderSubstitutId;

    /**
     * 联系人
     */
    private String ContactName;

    /**
      * 联系电话
     */
    private String ContactTel;

    /**
     * 同行代表人姓名
     */
    private String AccountName;

    /**
     * 同行公司名
     */
    private String CompanyName;

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

    /**
     * 0--正常    1--换人中   2--已完成
     */
    private Integer Statu;

    @Override
    protected Serializable pkVal() {
        return this.Id;
    }
}
