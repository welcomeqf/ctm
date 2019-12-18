package eqlee.ctm.apply.orders.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/4
 * @vesion 1.0
 **/
@Data
public class OrderBo {

   /**
    * 订单表id
    */
   private Long id;

   /**
    * 订单号
    */
   private String orderNo;

   /**
    * 出行时间
    */
   private String outDate;

   /**
    * 线路名
    */
   private String lineName;

   /**
    * 区域
    */
   private String region;

   /**
    * 面收总额
    */
   private Double msPrice;

   /**
    * 成人
    */
   private Integer adultNumber;

   /**
    * 幼儿
    */
   private Integer babyNumber;

   /**
    * 老人
    */
   private Integer oldNumber;

   /**
    * 小孩
    */
   private Integer childNumber;

   /**
    * 总人数
    */
   private Integer allNumber;
}
