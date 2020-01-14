package eqlee.ctm.api.pay.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2019/12/27
 * @vesion 1.0
 **/
@Data
public class MonthPhonePayBo {

   /**
    * 上传凭证
    */
   private String filePath;

   /**
    * 月结单号
    */
   private String monthNo;

   /**
    * 支付金额
    */
   private Double monthPrice;

   /**
    * 开始时间
    */
   private String startDate;

   private String companyName;
}
