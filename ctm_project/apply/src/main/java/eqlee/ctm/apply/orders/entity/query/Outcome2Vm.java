package eqlee.ctm.apply.orders.entity.query;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2020/1/14
 * @vesion 1.0
 **/
@Data
public class Outcome2Vm {
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
