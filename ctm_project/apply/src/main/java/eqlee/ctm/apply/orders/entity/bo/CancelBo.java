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
    * 订单ID
    */
   private Long orderId;

   /**
    * 取消金钱
    */
   private Double cancelPrice;
}
