package eqlee.ctm.apply.month.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/27
 * @vesion 1.0
 **/
@Data
public class MonthParamVo {

   /**
    * 月结支付金额
    */
   private Double monthPrice;

   /**
    * 开始时间
    */
   private String startDate;
}
