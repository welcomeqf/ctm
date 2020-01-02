package eqlee.ctm.finance.settlement.entity.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2019/12/26
 * @vesion 1.0
 **/
@Data
public class OutComeInfoBo {

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
