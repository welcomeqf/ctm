package eqlee.ctm.apply.orders.entity.query;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2020/1/14
 * @vesion 1.0
 **/
@Data
public class OrderFinanceQuery {

   /**
    * 财务人名字
    */
   private String financeName;

   /**
    * 其他收入
    */
   private Double otherInPrice;

   /**
    * 支出名称
    */
   private String outName;

   /**
    * 支出金额
    */
   private Double outPrice;

   /**
    * 收入id
    */
   private Long incomeId;

   /**
    * 支出id
    */
   private Long Id;

   /**
    * 支出图片路径
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
