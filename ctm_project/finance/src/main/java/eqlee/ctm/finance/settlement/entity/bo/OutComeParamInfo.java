package eqlee.ctm.finance.settlement.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/26
 * @vesion 1.0
 **/
@Data
public class OutComeParamInfo {

   /**
    * 支出消费名字
    */
   private String outName;

   /**
    * 支出金额
    */
   private Double outPrice;

   /**
    * 支出消费凭证
    */
   private String picture;
}
