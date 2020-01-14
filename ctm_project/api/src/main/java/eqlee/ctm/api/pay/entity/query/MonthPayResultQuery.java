package eqlee.ctm.api.pay.entity.query;

import lombok.Data;

/**
 * @author qf
 * @date 2020/1/7
 * @vesion 1.0
 **/
@Data
public class MonthPayResultQuery {

   /**
    * 支付时间
    */
   private String payDate;

   /**
    * 支付方式
    */
   private String payType;

   /**
    * 第三方流水号
    */
   private String thNo;

   /**
    * 转账截图
    */
   private String filePath;

   /**
    * 支付单号
    */
   private String monthNo;

   /**
    * 支付金额
    */
   private Double payMoney;

   /**
    * 支付状态（0--未支付  1--支付成功  2--支付失败）
    */
   private Integer payStatus;

}
