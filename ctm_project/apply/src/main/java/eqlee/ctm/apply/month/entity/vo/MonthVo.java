package eqlee.ctm.apply.month.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/27
 * @vesion 1.0
 **/
@Data
public class MonthVo {

   private Long id;

   /**
    * 月结单号
    */
   private String monthNo;

   /**
    * 月结详细商品信息
    */
   private String monthInfo;

   /**
    * 月结支付金额
    */
   private Double monthPrice;

   /**
    * 下单时间
    */
   private String time;

   /**
    *auto
    */
   private Boolean auto;


}
