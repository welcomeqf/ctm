package eqlee.ctm.finance.settlement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/12/26
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("OutCome2")
public class Outcome2 extends Model<Outcome2> {

   /**
    * ID
    */
   private Long Id;

   /**
    * 收入ID
    */
   private Long IncomeId;

   /**
    * 支出消费名字
    */
   private String OutName;

   /**
    * 支出金额
    */
   private Double OutPrice;

   /**
    * 上传消费凭证
    */
   private String Picture;

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
