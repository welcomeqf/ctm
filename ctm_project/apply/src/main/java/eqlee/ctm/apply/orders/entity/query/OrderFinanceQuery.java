package eqlee.ctm.apply.orders.entity.query;

import lombok.Data;

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
}
