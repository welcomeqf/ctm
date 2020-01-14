package eqlee.ctm.finance.settlement.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/1/10
 * @vesion 1.0
 **/
@Data
public class OutAddBo {

   /**
    * ID
    */
   private Long id;

   /**
    * 收入ID
    */
   private Long incomeId;

   /**
    * 支出消费名字
    */
   private String outName;

   /**
    * 支出金额
    */
   private Double outPrice;

   /**
    * 消费凭证
    */
   private String picture;

   /**
    * 创建人
    */
   private Long createUserId;

   /**
    * 修改人
    */
   private Long updateUserId;
}
