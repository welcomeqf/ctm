package eqlee.ctm.apply.month.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/12/27
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("MonthPay")
public class MonthPay extends Model<MonthPay> {

   /**
    * id
    */
   private Long Id;

   /**
    * 月结单号
    */
   private String MonthNo;

   /**
    * 支付的开始时间
    */
   private LocalDate StartDate;

   /**
    * 支付的结束时间
    */
   private LocalDate EndDate;

   /**
    * 支付的金额
    */
   private Double PayPrice;

   /**
    * 月结商品说明
    */
   private String MonthInfo;

   /**
    * 是否支付  0-未支付  1-已支付
    */
   private Boolean IsPay;

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
    * '修改时间
    */
   private LocalDateTime UpdateDate;

   /**
    * 备注
    */
   private String Remark;

   private Long KeyId;

   private String CompanyName;
}
