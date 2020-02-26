package eqlee.ctm.apply.orders.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/2/26
 * @vesion 1.0
 **/
@Data
public class CancelBo {

   /**
    * 取消金钱
    */
   private Double cancelPrice;

   /**
    * 成人人数
    */
   private Integer adultNumber;

   /**
    * 幼儿人数
    */
   private Integer babyNumber;

   /**
    * 老人人数
    */
   private Integer oldNumber;

   /**
    * 小孩人数
    */
   private Integer childNumber;

   /**
    * 总人数
    */
   private Integer allNumber;
}
